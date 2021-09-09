package az.azreco.azrecoassistant.assistant.azreco

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import az.azreco.azrecoassistant.constants.Constants.AZ_LANG
import az.azreco.azrecoassistant.constants.Constants.TTS_HOST
import az.azreco.azrecoassistant.constants.Constants.TTS_PORT
import az.azreco.azrecoassistant.constants.Constants.TTS_VOICE_ID
import az.azreco.azrecoassistant.util.Ext.annihilate
import az.azreco.azrecoassistant.util.Ext.clear
import az.azreco.azrecoassistant.util.Ext.destroy
import az.azreco.azrecoassistant.util.Ext.writeBytes
import com.azreco.tts.client.TTSClient
import com.azreco.tts.client.TTSClientConfiguration
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class TextToSpeech {

    private val TAG = "TextToSpeech"
    private var ttsClient: TTSClient? = null
    private var audioTrack: AudioTrack? = null
    private var voiceId = ""
    private var ttsJob: Job? = null

    // SINGLE TEXT SYNTHESIS
    @RequiresApi(Build.VERSION_CODES.N)
    fun speak(text: String, voiceId: String? = null) {
        setupVoiceId(voiceId = voiceId)
        ttsClient = TTSClient(getConfiguration()).also { it.synthesize(text) }
        initAudioTrack().let {
            val bufferSize = it.bufferSize
            audioTrack = it.audioTrack.apply {
                writeBytes(bufferSize = bufferSize, ttsClient = ttsClient)
            }
            reset()
        }
    }

    // salamg

    // передает байты клиента в audioTrack
//    private fun writeBytes(bufferSize: Int) {
//        val buffer = ByteArray(bufferSize)
//        while (ttsClient?.isOk == true) {
//            val result = ttsClient?.read()
//            if (result == null || result.isEmpty()) break
//            if (result.size > bufferSize) {
//                val numBlocks: Int = result.size / bufferSize
//                val remainBytes: Int = result.size % bufferSize
//                for (j in 0 until numBlocks) {
//                    System.arraycopy(result, j * bufferSize, buffer, 0, bufferSize)
//                    audioTrack?.write(buffer, 0, bufferSize)
//                }
//                if (remainBytes > 0) {
//                    System.arraycopy(result, numBlocks * bufferSize, buffer, 0, remainBytes)
//                    audioTrack?.write(buffer, 0, remainBytes)
//                }
//            } else {
//                System.arraycopy(result, 0, buffer, 0, result.size)
//                audioTrack?.write(buffer, 0, result.size)
//            }
//        }
//    }

    // MULTIPLE TEXT SYNTHESIS
    @RequiresApi(Build.VERSION_CODES.N)
    fun speakByteStream(baos: ByteArrayOutputStream) {
        initAudioTrack().let {
            audioTrack = it.audioTrack.apply {
                play()
                Log.d("TextToSpeech", "start track")
                writeBytes(bufferSize = it.bufferSize, baos = baos)
            }
            audioTrack?.destroy()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun synthesizeMultiple(
        texts: List<String>,
        voiceId: String? = null
    ): List<TextToSpeechModel> {
        val listOfTTS = mutableListOf<TextToSpeechModel>()
        setupVoiceId(voiceId = voiceId)
        withContext(Dispatchers.IO) {
            ttsJob = launch {
                texts.forEach { text ->
                    launch {
                        listOfTTS.add(TextToSpeechModel(text = text, baos = saveToBAOS(text)))
                    }
                }
            }
        }
        return orderByText(texts = texts, ttsList = listOfTTS)
    }

    // Save String to ByteArrayOutputStream
    private fun saveToBAOS(text: String): ByteArrayOutputStream {
        val baos = ByteArrayOutputStream()
        var counter = 1
        Log.d(TAG, "Baos workin")
        while (baos.size() == 0) {
            if (counter >= 3) break
            val tts = TTSClient(getConfiguration())
            tts.let {
                val status = it.synthesize(text)
                Log.e(TAG, "error - Status is $status")
                Log.e(TAG, "tts is ok - ${it.isOk}")
                while (it.isOk) {
                    val result = it.read()
                    if (result == null || result.isEmpty()) break
                    else baos.write(result)
                }
                it.annihilate()
            }
            // обычный счетчик
            counter += 1
            if (baos.size() == 0) {
                Log.d(TAG, "ttsSucces is ${tts.isOk}")
                Log.d(TAG, "Baos size is 0, i'll try $counter time")
            } else break
        }
        Log.d("TTS", "baos completed - size is ${baos.size()}")
        return baos
    }

    // запись байтов
//    private fun writeBytes(bufferSize: Int, track: AudioTrack, baos: ByteArrayOutputStream) {
//        val buffer = ByteArray(bufferSize)
//        baos.toByteArray().also {
//            if (it.size > bufferSize) {
//                val numBlocks: Int = it.size / bufferSize
//                val remainBytes: Int = it.size % bufferSize
//                for (j in 0 until numBlocks) {
//                    System.arraycopy(it, j * bufferSize, buffer, 0, bufferSize)
//                    track.write(buffer, 0, bufferSize)
//                }
//                if (remainBytes > 0) {
//                    System.arraycopy(it, numBlocks * bufferSize, buffer, 0, remainBytes)
//                    track.write(buffer, 0, remainBytes)
//                }
//            } else {
//                System.arraycopy(it, 0, buffer, 0, it.size)
//                track.write(buffer, 0, it.size)
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun orderByText(
        texts: List<String>,
        ttsList: List<TextToSpeechModel>
    ): List<TextToSpeechModel> {
        val resultList = mutableListOf<TextToSpeechModel>()
        texts.forEach { text ->
            ttsList.forEach { if (text == it.text) resultList.add(it) }
        }
        return resultList
    }

    // Init AudioTrack
    private fun initAudioTrack(): AudioTrackKit {
        val track: AudioTrack
        var bufferSize = AudioTrack.getMinBufferSize(
            22050,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        bufferSize =
            if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
                22050 * 1 * 2 * 5
            } else bufferSize * 5

        track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            22050,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
        return AudioTrackKit(bufferSize = bufferSize, audioTrack = track)
    }

    // default voiceId = "325640"
    private fun setupVoiceId(voiceId: String?): Unit = when (voiceId) {
        null -> this.voiceId = TTS_VOICE_ID
        else -> this.voiceId = voiceId
    }

    private fun getConfiguration(): TTSClientConfiguration = TTSClientConfiguration().apply {
        host = TTS_HOST
        port = TTS_PORT
        language = AZ_LANG
        ttsId = if (voiceId.isEmpty()) TTS_VOICE_ID else voiceId
        isSSLEnabled = true
    }

    // RESETS ttsClient
    private fun reset() {
        ttsClient?.clear()
        audioTrack?.destroy()
        audioTrack = null
    }

    // RELEASING ttsClient, ttsJob
    suspend fun release() {
        ttsClient?.annihilate()
        audioTrack?.destroy()
        audioTrack = null
        ttsClient = null
        ttsJob?.cancelAndJoin()
        Log.d("TTS", "TTS Released")
    }
}

data class AudioTrackKit(val bufferSize: Int, val audioTrack: AudioTrack)
data class TextToSpeechModel(val text: String, val baos: ByteArrayOutputStream)