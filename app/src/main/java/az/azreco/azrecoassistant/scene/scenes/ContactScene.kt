package az.azreco.azrecoassistant.scene.scenes

import android.util.Log
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.constants.Audio
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.scene.Scene
import az.azreco.azrecoassistant.scene.SceneConfig
import az.azreco.azrecoassistant.scene.callbacks.SceneListener
import az.azreco.azrecoassistant.util.ContactUtil
import az.azreco.azrecoassistant.util.TextUtil

class ContactScene(
    private val assistant: Assistant,
    private val contactUtil: ContactUtil
) : Scene() {

    private val TAG = "ContactState"
    private var nextScene: Scene? = null
    private val contactList by lazy {
        contactUtil.contactsList
    }

    override suspend fun onStart(listener: SceneListener, config: SceneConfig?) {
        super.onStart(listener, config)
        Log.d(TAG, "enter ::: ConstactState")
        config?.also {
            if (it is SceneConfig.ContactSceneConfig) {
                nextScene = it.nextScene
                getContactName(config = it)
            }
        }
    }

    private suspend fun getContactName(config: SceneConfig.ContactSceneConfig) =
        if (config.contactName != null) {
            filterName(name = config.contactName)
        } else {
            if (config.nextScene == smsScene) assistant.playSync(audioFile = Audio.smsContact)
            else if (config.nextScene == callScene) assistant.playSync(audioFile = Audio.callContact)
            var selectedName = ""
            var totalNames = contactList.joinToString(separator = "\n") { it.name }
            totalNames = TextUtil.removeNonAzLetters(totalNames.split("\n"))
            assistant.reKeywordSpotting(
                keyWords = totalNames,
                startLambda = {},
                endLambda = {
                    listener.onProgressUpdated(false)
                    when (it) {
                        in totalNames -> {
                            listener.onMessageReceived(received = false, text = it)
                            selectedName = it
                        }
                    }
                }
            )
            filterName(name = selectedName)
        }

    private suspend fun filterName(name: String) {

    }

    private fun ifContactSingle() {

    }

    private fun ifContactMany() {

    }

    private fun ifContactNotFound() {

    }

    override suspend fun onNext(nextScene: Scene?, config: SceneConfig?) {
        super.onNext(nextScene, config)
    }

    private fun editContactsInfoForPronounce(contactList: List<PhoneContact>): String {
        return contactList.joinToString(separator = ", ") {
            "${it.name} , nömrəsi ${TextUtil.editNumberForPronounce(it.phoneNumber)}"
        }
    }

    companion object {
        private const val choiceText =
            "kontaktın ardıcılıq rəqəmini, məsəl üçün birinci söyləyib seçə bilərsiniz."
    }

}