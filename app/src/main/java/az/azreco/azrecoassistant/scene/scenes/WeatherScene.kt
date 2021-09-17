package az.azreco.azrecoassistant.scene.scenes

import android.util.Log
import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.scene.Scene
import az.azreco.azrecoassistant.scene.SceneConfig
import az.azreco.azrecoassistant.scene.callbacks.SceneListener
import kotlinx.coroutines.delay

class WeatherScene : Scene() {

    private val TAG = "WeatherScene"

    override suspend fun onStart(listener: SceneListener, config: SceneConfig?) {
        super.onStart(listener, config)
        Log.v(TAG, "onStart")
        listener.onMessageReceived(data = DialogData.Message(false, "salam"))
        delay(3000)
        onNext(
            nextScene = weatherScene,
            config = SceneConfig.ContactConfig(PhoneContact("lol", "1234"))
        )
    }

    override suspend fun onNext(nextScene: Scene?, config: SceneConfig?) {
        Log.v(TAG, "onNext")
        super.onNext(nextScene, config)
    }


}