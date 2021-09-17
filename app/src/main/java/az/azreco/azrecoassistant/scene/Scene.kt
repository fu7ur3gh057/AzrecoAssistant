package az.azreco.azrecoassistant.scene

import az.azreco.azrecoassistant.constants.Commands
import az.azreco.azrecoassistant.fsm.StateParam
import az.azreco.azrecoassistant.scene.callbacks.SceneListener

abstract class Scene {
    private val stopCommands = Commands.formatToKeywords(Commands.stop)

    lateinit var listener: SceneListener

    open suspend fun onStart(listener: SceneListener, config: SceneConfig? = null) {
        this.listener = listener
    }

    open suspend fun finish() {

    }

    open suspend fun onNext(nextScene: Scene?, config: SceneConfig? = null) {
        finish()
        listener.changeScene(nextScene = nextScene, config = config)
    }

    companion object {
        var currentScene: Scene? = null
        var smsScene: Scene? = null
        var callScene: Scene? = null
        var weatherScene: Scene? = null
    }

}
