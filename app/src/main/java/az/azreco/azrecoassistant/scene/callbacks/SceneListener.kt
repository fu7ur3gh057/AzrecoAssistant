package az.azreco.azrecoassistant.scene.callbacks

import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.scene.Scene
import az.azreco.azrecoassistant.scene.SceneConfig

interface SceneListener {

    suspend fun changeScene(nextScene: Scene?, config: SceneConfig? = null)

    fun onMessageReceived(received: Boolean, text: String)

    fun onWebLinkReceived(link: DialogData.WebLink)

    fun onProgressUpdated(isSynthesizing: Boolean)

    fun onActionReceived()

}