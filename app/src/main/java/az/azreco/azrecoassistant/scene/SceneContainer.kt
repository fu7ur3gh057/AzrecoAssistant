package az.azreco.azrecoassistant.scene

import android.util.Log
import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.scene.callbacks.SceneListener
import az.azreco.azrecoassistant.scene.scenes.SmsScene
import az.azreco.azrecoassistant.scene.scenes.WeatherScene
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class SceneContainer(smsScene: SmsScene, weatherScene: WeatherScene) : SceneListener {
    private val TAG = "SceneContainer"
    private var isRunning = false
    private lateinit var serviceCallback: (DialogData.Message) -> Unit

//    fun initServiceCallbacks(serviceCallback: (DialogData.Message) -> Unit) {
//        this.serviceCallback = serviceCallback
//    }

    init {
        Scene.smsScene = smsScene
        Scene.weatherScene = weatherScene
        Scene.currentScene = smsScene
    }

    suspend fun run(serviceCallback: (DialogData.Message) -> Unit): Any = if (!isRunning) {
        isRunning = true
        this.serviceCallback = serviceCallback
        Scene.currentScene?.onStart(listener = this)
        Log.v(TAG, "end of Run")
    } else {
        Log.v(TAG, "Scene is already run")
    }

    suspend fun rrr(serviceCallback: (DialogData.Message) -> Unit) = try {
        if (!isRunning) {
            isRunning = true
            this.serviceCallback = serviceCallback
            coroutineScope { Scene.currentScene?.onStart(listener = this@SceneContainer) }
            Log.v(TAG, "end of Run")
        } else {
            Log.v(TAG, "Scene is already run")
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    } finally {
        isRunning = false
    }

    override suspend fun changeScene(nextScene: Scene?, config: SceneConfig?) {
        Log.v(TAG, "changeScene || $nextScene")
        nextScene?.onStart(listener = this, config = config)
    }

    override fun onMessageReceived(data: DialogData.Message) {
        Log.v(TAG, "onMessageReceived :: $data")
        serviceCallback(data)
    }

    override fun onWebLinkReceived(link: DialogData.WebLink) {

    }

    override fun onProgressUpdated() {

    }

    override fun onActionReceived() {

    }

}

sealed class SceneConfig {
    class ContactSceneConfig(val contactName: String? = null, val nextScene: Scene?) :
        SceneConfig()

    class ContactConfig(val contact: PhoneContact) : SceneConfig()
}