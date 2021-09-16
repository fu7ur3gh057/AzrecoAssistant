package az.azreco.azrecoassistant.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.constants.Audio
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class DialogService : Service() {

    private val TAG = "DialogService"
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var serviceJob: Job? = null
    private var binder = DialogBinder()
    private var isActive = false

    @Inject
    lateinit var assistant: Assistant

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate ${getThreadName()}")

    }

    override fun onBind(intent: Intent?): IBinder {
        Log.v(TAG, "onBind ${getThreadName()}")
        return binder
    }

    fun receive() {
        if (!isActive) startCommand()
        else stopCommand()
    }

    fun startCommand() = serviceScope.launch {
        this@DialogService.isActive = true
        Log.v(TAG, "startCommand ${getThreadName()}")
        serviceJob = launch {
            assistant.playAsync(Audio.callContact)
            repeat(1000) {
                Log.v(TAG, "$it time, | ${getThreadName()}")
                delay(5000)
            }
        }
    }


    fun stopCommand() = serviceScope.launch {
        this@DialogService.isActive = false
        Log.v(TAG, "stopCommand ${getThreadName()}")
        assistant.playAsync(Audio.signalStop)
        destroyJob()
    }


    private fun destroyJob() {
        serviceJob?.cancel()
        serviceJob = null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        destroyJob()
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