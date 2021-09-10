package az.azreco.azrecoassistant.assistant.audioplayer

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import az.azreco.azrecoassistant.assistant.azreco.AudioTrackKit
import az.azreco.azrecoassistant.constants.Constants.SAMPLE_RATE_HZ
import az.azreco.azrecoassistant.util.Ext.destroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class AudioPlayer(private val context: Context) {

    private var audioTrack: AudioTrack? = null

    @Throws(Exception::class)
    suspend fun play(fileName: Int) {
        withContext(Dispatchers.Default) {
            val audioKit = initAudioTrack()
            audioTrack = audioKit.audioTrack.also { it.play() }
            var i: Int
            val buffer = ByteArray(audioKit.bufferSize)
            val b = buffer.copyOfRange(44, buffer.size)
            val inputStream = context.resources.openRawResource(fileName)
            audioTrack?.play()
            audioTrack?.playbackHeadPosition = 100
            try {
                while (inputStream.read(buffer).also { i = it } != -1)
                    audioTrack?.write(buffer, 0, i)
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                inputStream.close()
                audioTrack?.release()
            }
        }
    }

    @Throws(IOException::class)
    private fun readAllBytes(inputStream: InputStream): ByteArrayOutputStream {
        val out = ByteArrayOutputStream()
        copyAllBytes(inputStream, out)
        return out
    }

    @Throws(IOException::class)
    private fun copyAllBytes(inputStream: InputStream, outputStream: OutputStream): Int {
        var byteCount = 0
        val buffer = ByteArray(4096)
        while (true) {
            val read = inputStream.read(buffer)
            if (read == -1) break
            outputStream.write(buffer, 0, read)
            byteCount += read
        }
        return byteCount
    }


    // Init AudioTrack
    private fun initAudioTrack(): AudioTrackKit {
        val track: AudioTrack
        var bufferSize = AudioTrack.getMinBufferSize(
            SAMPLE_RATE_HZ,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        bufferSize =
            if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
                SAMPLE_RATE_HZ * 1 * 2 * 5
            } else bufferSize * 5

        track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            SAMPLE_RATE_HZ,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
        return AudioTrackKit(bufferSize = bufferSize, audioTrack = track)
    }

    fun release() {
        audioTrack.destroy()
        audioTrack = null
    }
}