package az.azreco.azrecoassistant.util

import android.media.AudioTrack
import com.azreco.tts.client.TTSClient
import java.io.ByteArrayOutputStream

object Ext {
    // TTSClient
    fun TTSClient?.clear() {
        this?.waitForCompletion()
        this?.reset()
    }

    fun TTSClient?.annihilate() {
        this?.waitForCompletion()
        this?.reset()
        this?.destroy()
    }

    // AudioTrack
    fun AudioTrack?.writeBytes(bufferSize: Int, baos: ByteArrayOutputStream) = this?.apply {
        play()
        val buffer = ByteArray(bufferSize)
        baos.toByteArray().also {
            if (it.size > bufferSize) {
                val numBlocks: Int = it.size / bufferSize
                val remainBytes: Int = it.size % bufferSize
                for (j in 0 until numBlocks) {
                    System.arraycopy(it, j * bufferSize, buffer, 0, bufferSize)
                    write(buffer, 0, bufferSize)
                }
                if (remainBytes > 0) {
                    System.arraycopy(it, numBlocks * bufferSize, buffer, 0, remainBytes)
                    write(buffer, 0, remainBytes)
                }
            } else {
                System.arraycopy(it, 0, buffer, 0, it.size)
                write(buffer, 0, it.size)
            }
        }
    }

    fun AudioTrack?.writeBytes(bufferSize: Int, ttsClient: TTSClient?) = this?.apply {
        play()
        val buffer = ByteArray(bufferSize)
        while (ttsClient?.isOk == true) {
            val result = ttsClient.read()
            if (result == null || result.isEmpty()) break
            if (result.size > bufferSize) {
                val numBlocks: Int = result.size / bufferSize
                val remainBytes: Int = result.size % bufferSize
                for (j in 0 until numBlocks) {
                    System.arraycopy(result, j * bufferSize, buffer, 0, bufferSize)
                    write(buffer, 0, bufferSize)
                }
                if (remainBytes > 0) {
                    System.arraycopy(result, numBlocks * bufferSize, buffer, 0, remainBytes)
                    write(buffer, 0, remainBytes)
                }
            } else {
                System.arraycopy(result, 0, buffer, 0, result.size)
                write(buffer, 0, result.size)
            }
        }
    }


    fun AudioTrack?.destroy() {
        this?.stop()
        this?.release()
    }
}