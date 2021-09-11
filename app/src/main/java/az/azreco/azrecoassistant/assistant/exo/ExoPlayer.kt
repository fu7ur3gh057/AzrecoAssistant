package az.azreco.azrecoassistant.assistant.exo

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExoPlayer(private val context: Context) {
    private var exoPlayer: SimpleExoPlayer? = null

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

    private val playerEventListener = object : Player.EventListener {
        override fun onPlaybackStateChanged(state: Int) {
            super.onPlaybackStateChanged(state)
            when (state) {
                Player.STATE_READY -> {
                }
                Player.STATE_ENDED -> {
                    Log.v(TAG, "Ended")
                    reset()
                }
                Player.STATE_BUFFERING -> {
                }
                Player.STATE_IDLE -> {
                }
            }
        }
    }

    suspend fun playAsync(
        audioFile: Int,
    ) = withContext(Dispatchers.Main) {
        Log.d(TAG, Thread.currentThread().name)
        if (exoPlayer == null) initializePlayer()
        exoPlayer?.let {
            if (it.isPlaying) it.playWhenReady = false
            buildAssetDataSource(fileName = audioFile)?.let { dataSource ->
                it.setMediaSource(dataSource)
                it.prepare()
                it.playWhenReady = true
                it.addListener(playerEventListener)
            }
        }
    }

    private fun reset() {
        exoPlayer?.let {
            it.stop()
            it.removeListener(playerEventListener)
            it.release()
        }
        exoPlayer = null
        Log.d(TAG, "EXO RESETED")
    }

    suspend fun release() = withContext(Dispatchers.Main) {
        exoPlayer?.let {
            it.pause()
            it.stop()
            it.removeListener(playerEventListener)
            it.release()
        }
        exoPlayer = null
        Log.d(TAG, "ExoPlayer Released")
    }

    private fun buildAssetDataSource(fileName: Int): ProgressiveMediaSource? = try {
        val dataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(fileName))
        val rawDataSource = RawResourceDataSource(context)
        rawDataSource.open(dataSpec)
        val mediaItem = MediaItem.fromUri(rawDataSource.uri!!)
        ProgressiveMediaSource.Factory { rawDataSource }
            .createMediaSource(mediaItem)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    companion object {
        private const val TAG = "Exo"
    }
}