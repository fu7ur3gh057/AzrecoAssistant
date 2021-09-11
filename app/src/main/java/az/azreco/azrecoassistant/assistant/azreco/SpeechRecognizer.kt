package az.azreco.azrecoassistant.assistant.azreco

import android.util.Log
import az.azreco.azrecoassistant.constants.Constants
import az.azreco.azrecoassistant.util.TimeUtil
import com.AndroidMic
import com.Callbacks
import com.EnergyVad
import com.azreco.asr.client.ASRClient
import com.azreco.asr.client.ASRClientConfiguration
import com.azreco.asr.client.AudioSource
import com.azreco.asr.client.ResultType
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class SpeechRecognizer : Callbacks {

    private var asrJob: Job? = null // Job for running KWS,continues and EnergyVad

    // поля для EnergyVad
    private var silenceTime = 3000 // energy vad silence time
    private var minConfidence = 55 // minimum value of kws confidence
    private var frameSizeInBytes = 0
    private var frameSizeInSamples = 0

    private var kwsClient: ASRClient? = null
    private var speechClient: ASRClient? = null
    private var androidMic: AndroidMic? = null
    private var energyVad: EnergyVad? = null
    private var closeMic = false
    private var keyWords = ""

    @Volatile
    private var isReleased = false // врубается если из вне вызвали класс release()

    suspend fun listen(silence: Int = 5, keyWords: String): SttResponse {
        this.keyWords = keyWords
        speechClient = ASRClient(getAsrConfig(isKWS = false))
        kwsClient = ASRClient(getAsrConfig(isKWS = true))
        var continuesResult = ""
        var kwsResult = ""
        silenceTime = silence * 1000
        val continuesStatus = speechClient!!.connect()
        val kwsStatus = kwsClient!!.connect()
        coroutineScope {
            asrJob = launch {
                launch { startVad() }
                val asrAnswer = async { waitingSpeechResult(status = continuesStatus) }
                val kwsAnswer = async { waitingKwsResult(status = kwsStatus) }
                continuesResult = asrAnswer.await()
                kwsResult = kwsAnswer.await()
            }
        }
        asrJob?.join()
        Log.d(TAG, "AsrResult - $continuesResult, KwsResult - $kwsResult")
        destroy()
        return SttResponse(speechResponse = continuesResult, kwsResponse = kwsResult)
    }

    suspend fun speechRecognize(silence: Int = 3): String {
        speechClient = ASRClient(getAsrConfig(isKWS = false))
        var result = ""
        silenceTime = silence * 1000
        val status = speechClient!!.connect()
        coroutineScope {
            asrJob = launch {
                launch(Dispatchers.IO) { startVad() }
                result = withContext(Dispatchers.Default) {
                    waitingSpeechResult(status = status)
                }
            }
            asrJob?.join()
            Log.v(TAG,"end kws ${TimeUtil.getCurrentTime(true)}")
        }
        Log.d(TAG, "The Result is - $result")
        destroy()
        return result
    }

    suspend fun keywordSpotting(
        silence: Int = 5,
        keyWords: String,
    ): String {
        var resultKeyword = ""
        this.keyWords = keyWords
        kwsClient = ASRClient(getAsrConfig(isKWS = true))
        val kwsStatus = kwsClient!!.connect()
        setVadParametrs(silence = silence)
        coroutineScope {
            asrJob = launch {
                Log.v(TAG,"start kws job ${TimeUtil.getCurrentTime(true)}")
                launch(Dispatchers.IO) { startVad() }
                withContext(Dispatchers.IO) {
                    resultKeyword = waitingKwsResult(status = kwsStatus)
                }
            }
            asrJob?.join()
            Log.v(TAG,"end kws job ${TimeUtil.getCurrentTime(true)}")
        }
        Log.d(TAG, "The Result is - $resultKeyword")
        destroy()
        return resultKeyword
    }

    private fun waitingSpeechResult(status: Int): String {
        val resultSb = StringBuilder()
        if (status == 0) {
            Log.d(TAG, "ASR working")
            while (speechClient?.hasResult(true) == true) {
                val jsonResult = speechClient?.result
                try {
                    val jsonObj = JSONObject(jsonResult)
                    val recogResult = jsonObj.getString("resultText")
                    resultSb.append(recogResult).append(" ")
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                }
            }
        }
        speechClient?.apply {
            waitForCompletion()
            destroy()
        }
        speechClient = null
        Log.d(TAG, "ASR stopped")
        return if (resultSb.isNotEmpty()) resultSb.toString() else ""
    }

    private fun waitingKwsResult(status: Int): String = if (status == 0) {
        var resultKws = ""
        Log.d(TAG, "KWS working")
        while (kwsClient!!.hasResult(true)) {
            Log.d(TAG, TimeUtil.getCurrentTime(seconds = true))
            val result = kwsClient!!.result
            try {
                val jsonObj = JSONObject(result)
                val list = jsonObj.getJSONArray("words")
                for (i in 0 until list.length()) {
                    val rec = list.getJSONObject(i)
                    val confindence = rec["confidence"] as Int
                    val word = rec.getString("word")
                    if (confindence > minConfidence) {
                        resultKws = word
                        closeMic = true
                    }
                    Log.e(TAG, "word - $word | conf - $confindence")
                }
            } catch (ex: JSONException) {
                ex.printStackTrace()
            }
        }
        kwsClient?.let {
            it.waitForCompletion()
            it.destroy()
        }
        kwsClient = null
        Log.d(TAG, "KWS stopped")
        resultKws
    } else {
        ""
    }


    @Throws(Exception::class)
    private fun startVad() {
        Log.d(TAG, "EnergyVad start - ${Calendar.getInstance().time}")
        androidMic = AndroidMic()
        androidMic?.let {
            it.open()
            frameSizeInSamples = FRAME_RATE * FRAME_MS / 1000
            frameSizeInBytes = 2 * frameSizeInSamples
            energyVad =
                EnergyVad(HEAD_MARGIN, silenceTime, frameSizeInBytes)
            energyVad?.setCallbacks(this)
            it.start()
            energyVad?.process()
            Log.d(TAG, "initEnergyVad end - " + Calendar.getInstance().time)
        }
    }

    override fun callbackRead(buf: ShortArray?, offset: Int, len: Int): Int {
        if (closeMic) return -1
        val currentLen = len * 2
        val bufferBytes = ByteArray(currentLen)
        var cnt = 0
        if (androidMic != null) {
            cnt = androidMic!!.read(bufferBytes, 0, currentLen)
        } else {
            return -1
        }
        if (cnt > 0) {
            val bBuffer = ByteBuffer.allocate(currentLen).apply {
                put(bufferBytes)
                order(ByteOrder.LITTLE_ENDIAN)
                rewind()
            }
            if (bBuffer.hasArray()) {
                bBuffer.asShortBuffer()[buf, offset, cnt / 2]
            }
        } else if (cnt < 0) {
            return -1
        }
        return cnt / 2
    }

    override fun callbackProcess(buf: ShortArray?, offset: Int, len: Int): Int {
        if (closeMic) return -1
        val data = ByteArray(len * 2)
        val bf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)
        bf.asShortBuffer().put(buf, offset, len)
        val leBuffer = bf.array()
        return if ((speechClient?.isOk == true || kwsClient?.isOk == true) && !closeMic) {
            kwsClient?.write(leBuffer, 0, leBuffer.size)
            speechClient?.write(leBuffer, 0, leBuffer.size)
            0
        } else {
            -1
        }
    }

    override fun callbackGetCheck(): Int {
        return when (androidMic?.isOpen) {
            true -> 1
            else -> 0
        }
    }

    override fun callbackStart() {
        if (!closeMic)
            Log.d(TAG, "callbackStart - androidMic ON ::: ${Calendar.getInstance().time}")
    }

    override fun callbackStop() {
        if (closeMic) return
        else {
            Log.d(TAG, "callbackStop - ${Calendar.getInstance().time}")
            kwsClient?.endStream()
            speechClient?.endStream()
            closeMic = true
            androidMic?.close()
        }
    }

    override fun callbackVisualize(value: Float) {}

    private fun setVadParametrs(silence: Int) {
        silenceTime = silence * 1000
    }

    private fun resetFields() {
        frameSizeInBytes = 0
        frameSizeInSamples = 0
        energyVad = null
        closeMic = false
    }

    private suspend fun destroy() {
        resetFields()
        androidMic?.let {
            it.stop()
            it.close()
        }
        kwsClient?.let {
            it.endStream()
            it.stop()
            it.destroy()
        }
        kwsClient = null
        speechClient?.let {
            it.endStream()
            it.stop()
            it.destroy()
        }
        speechClient = null
        asrJob?.cancelAndJoin()
        asrJob = null
    }

    suspend fun release() {
        resetFields()
        callbackStop()
        asrJob?.cancelAndJoin()
        asrJob = null
        Log.d(TAG, "Continues & KWS Released")
    }

    private fun getAsrConfig(isKWS: Boolean): ASRClientConfiguration {
        return ASRClientConfiguration().apply {
            host = Constants.ASR_HOST
            port = Constants.ASR_PORT
            audioSource = AudioSource.AS_REALTIME
            if (isKWS) {
                resultType = ResultType.RT_KWS_PARTIAL
                customDictionary = keyWords
            } else resultType = ResultType.RT_PARTIAL
            language = Constants.AZ_LANG
            isSSLEnabled = true
            isOpusEnabled = true
        }
    }

    companion object {
        private const val TAG = "SpeechRecognizer"
        private const val FRAME_MS = 100
        private const val FRAME_RATE = 16000
        private const val HEAD_MARGIN = 600
    }
}

// обычный дата класс который хранит в себе два результата. используется в методе listen()
data class SttResponse(val speechResponse: String, val kwsResponse: String)
