package az.azreco.azrecoassistant.scene.scenes

import az.azreco.azrecoassistant.scene.Scene
import az.azreco.azrecoassistant.scene.SceneConfig
import az.azreco.azrecoassistant.scene.callbacks.SceneListener

class HomeScene: Scene() {

    private val TAG = "HomeScene"


    override suspend fun onStart(listener: SceneListener, config: SceneConfig?) {
        super.onStart(listener, config)
    }

    override suspend fun onNext(nextScene: Scene?, config: SceneConfig?) {
        super.onNext(nextScene, config)
    }

}