package az.azreco.azrecoassistant.assistant.exo

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.AssetDataSource
import com.google.android.exoplayer2.upstream.DataSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class ExoPlayer(private val context: Context) {
    private var exoPlayer: SimpleExoPlayer? = null

    private var syncEventListener: Player.EventListener? = null

    private fun initializePlayer() {
        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
        exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
            setAudioAttributes(audioAttributes, true)
        }
    }

    // при вызове в курутин скоупе, код будет дожидаться завершения прежде чем продолжить
    private suspend fun listenState() = suspendCancellableCoroutine<Boolean> { cont ->
        syncEventListener = object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == Player.STATE_ENDED) {
                    reset(this)
                    cont.resume(true)
                }
            }

        }
        exoPlayer?.addListener(syncEventListener as Player.EventListener)
    }

    private val asyncEventListener = object : Player.EventListener {
        override fun onPlaybackStateChanged(state: Int) {
            super.onPlaybackStateChanged(state)
            when (state) {
                Player.STATE_READY -> {
                }
                Player.STATE_ENDED -> {
                    Log.v(TAG, "Ended")
                    reset(this)
                }
                Player.STATE_BUFFERING -> {
                }
                Player.STATE_IDLE -> {
                }
            }
        }
    }

    private fun reset(listener: Player.EventListener) {
        exoPlayer?.let {
            it.stop()
            it.removeListener(listener)
            it.release()
        }
        exoPlayer = null
        Log.d(TAG, "EXO RESETED")
    }

    suspend fun release() = withContext(Dispatchers.Main) {
        exoPlayer?.let {
            it.pause()
            it.stop()
            it.removeListener(asyncEventListener)
            syncEventListener?.let { it1 -> it.removeListener(it1) }
            it.release()
        }
        syncEventListener = null
        exoPlayer = null
        Log.d(TAG, "ExoPlayer Released")
    }


    // playPostFile для асинхронного воспроизведения второго файла
    //
    suspend fun playSync(
        audioFile: String,
        playPostFile: Boolean,
        postFile: String = "signal_start"
    ) = withContext(Dispatchers.Main) {
        Log.d(TAG, Thread.currentThread().name)
        if (exoPlayer == null) initializePlayer()
        exoPlayer?.let {
            if (it.isPlaying) it.playWhenReady = false
            it.setMediaSource(buildAssetDataSource(fileName = audioFile))
            it.prepare()
            it.playWhenReady = true
            if (playPostFile) {
                listenState()
                playAsset(fileName = postFile)
            } else listenState()
        }
    }

    suspend fun playAsync(
        audioFile: String,
    ) = withContext(Dispatchers.Main) {
        Log.d(TAG, Thread.currentThread().name)
        if (exoPlayer == null) initializePlayer()
        exoPlayer?.let {
            if (it.isPlaying) it.playWhenReady = false
            it.setMediaSource(buildAssetDataSource(fileName = audioFile))
            it.prepare()
            it.playWhenReady = true
            it.addListener(asyncEventListener)
        }
    }

    private fun playAsset(fileName: String) {
        if (exoPlayer == null) initializePlayer()
        exoPlayer?.let {
            it.setMediaSource(buildAssetDataSource(fileName = fileName))
            it.prepare()
            it.playWhenReady = true
            it.addListener(asyncEventListener)
        }
    }

    private fun buildAssetDataSource(fileName: String): ProgressiveMediaSource {
        val dataSpec = DataSpec(Uri.parse("asset:///audio/$fileName.wav"))
        val assetDataSource = AssetDataSource(context)
        assetDataSource.open(dataSpec)
        val mediaItem = MediaItem.fromUri(assetDataSource.uri!!)
        return ProgressiveMediaSource.Factory { assetDataSource }
            .createMediaSource(mediaItem)
    }

    companion object {
        private const val TAG = "Exo"
    }
}