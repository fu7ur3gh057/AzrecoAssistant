package az.azreco.azrecoassistant.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.constants.Audio
import az.azreco.azrecoassistant.scene.SceneContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class DialogService : Service() {

    private val TAG = "DialogService"
    private var serviceScope: CoroutineScope? = null
    private var serviceJob: Job? = null
    private var binder = DialogBinder()
    private var isActive = false

    @Inject
    lateinit var sceneContainer: SceneContainer

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate ${getThreadName()}")
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.v(TAG, "onBind ${getThreadName()}")
        serviceScope = CoroutineScope(Dispatchers.Default)
        return binder
    }

    fun receive() {
        if (!isActive) startCommand()
        else stopCommand()
    }

    fun startCommand() = serviceScope?.launch {
        serviceJob = launch {
            sceneContainer.rrr {
                Log.v(TAG, "-- $it")
            }
        }
    }


    fun stopCommand() = serviceScope?.launch {
        destroyJob()
    }


    private fun destroyJob() {
        serviceJob?.cancel()
        serviceJob = null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        destroyJob()
        serviceScope?.cancel()
        serviceScope = null
        Log.v(TAG, "onUnbind ${getThreadName()}")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy ${getThreadName()}")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.v(TAG, "onTaskRemoved ${getThreadName()}")
        stopSelf()
    }

    inner class DialogBinder : Binder() {
        val service: DialogService
            get() = this@DialogService
    }

    private fun getThreadName() = Thread.currentThread().name
}