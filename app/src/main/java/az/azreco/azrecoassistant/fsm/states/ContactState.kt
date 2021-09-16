package az.azreco.azrecoassistant.fsm.states

import android.util.Log
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.constants.Audio
import az.azreco.azrecoassistant.constants.Commands
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.fsm.State
import az.azreco.azrecoassistant.fsm.StateParam
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.util.ContactUtil
import az.azreco.azrecoassistant.util.TextUtil

class ContactState(
    private val assistant: Assistant,
    private val contactUtil: ContactUtil
) : State() {
    private val stopCommands = Commands.formatToKeywords(Commands.stop)
    private val contactList by lazy {
        contactUtil.contactsList
    }

    private var nextState: State? = null

    override suspend fun enter(
        dialogListener: (DialogResponse) -> Unit,
        onStateChanged: suspend (StateParam?) -> Unit,
        parametr: StateParam?
    ) {
        super.enter(dialogListener, onStateChanged, parametr)
        Log.d(TAG, "enter ::: ConstactState")
        param?.also {
            if (it is StateParam.ContactStateParam) {
                nextState = it.nextState
                getContact(param = it)
            }
        }
//        param = StateParam.ContactStateParam(nextState = smsState!!)
//        nextState = (param as StateParam.ContactStateParam).nextState
//        getContact(param as StateParam.ContactStateParam)
    }

    private suspend fun getContact(param: StateParam.ContactStateParam) {
        if (param.contactName != null) filterName(name = param.contactName)
        else {
            if (param.nextState == smsState) assistant.playSync(audioFile = Audio.smsContact)
            else if (param.nextState == callState) assistant.playSync(audioFile = Audio.callContact)
            var selectedName = ""
            var totalNames = contactList.joinToString(separator = "\n") { it.name }
            totalNames = TextUtil.removeNonAzLetters(totalNames.split("\n"))
            assistant.reKeywordSpotting(
                keyWords = totalNames,
                startLambda = {},
                endLambda = {
                    uiCall.updateProcess(false)
                    when (it) {
                        in totalNames -> {
                            uiCall.updateDialog(false, it)
                            selectedName = it
                        }
                    }
                }
            )
            filterName(name = selectedName)
        }
    }

    private suspend fun filterName(name: String) {
        val foundedContacts = contactUtil.containsContactName(contactName = name)
        return when (foundedContacts.size) {
            0 -> ifContactNotFound()
            1 -> ifContactSingle(contact = foundedContacts[0])
            else -> ifContactMultiple(contactName = name, foundedContacts = foundedContacts)
        }
    }

    private suspend fun ifContactNotFound() {
        Log.v(TAG, "::ifContactNotFound::")
        assistant.speak("kontakt tapılmadı")

    }

    private suspend fun ifContactSingle(contact: PhoneContact) {
        Log.v(TAG, "::ifContactSingle::")
        val phoneNo = TextUtil.editNumberForPronounce(phoneNo = contact.phoneNumber)
        uiCall.updateDialog(isReceived = true, text = "${contact.name} ${contact.phoneNumber}")
        assistant.speak(text = "Kontaktın adı ${contact.name}. nömrəsi $phoneNo")
        onNextState(nextState = nextState!!, param = StateParam.ContactParam(contact = contact))
    }

    private suspend fun ifContactMultiple(
        contactName: String,
        foundedContacts: List<PhoneContact>
    ) {
        Log.v(TAG, "::ifContactMultiple::")
        var receiver: PhoneContact? = null
        val contactInfoText = editContactsInfoForPronounce(contactList = foundedContacts)
        uiCall.updateDialog(true, contactInfoText)
        uiCall.updateDialog(true, choiceText)
        assistant.apply {
            speak("$contactName adında ${foundedContacts.size} dənə kontakt tapıldı, $contactInfoText. $choiceText")
            reKeywordSpotting(
                keyWords = TextUtil.azNumerical(),
                startLambda = {},
                endLambda = {
                    uiCall.updateProcess(false)
                    when {
                        TextUtil.azNumerical().contains(it) -> {
                            uiCall.updateDialog(false, it)
                            receiver = foundedContacts[TextUtil.getContactByNumerical(it)]
                        }
                    }
                }
            )
        }
        receiver?.let {
            val phoneNo = TextUtil.editNumberForPronounce(it.phoneNumber)
            uiCall.updateDialog(true, "${it.name} ${it.phoneNumber}")
            assistant.speak(text = "Kontaktın adı ${it.name}, nömrəsi $phoneNo")
            onNextState(nextState = nextState, param = StateParam.ContactParam(it))
        }
    }

    override suspend fun onNextState(nextState: State?, param: StateParam?) {
        super.onNextState(nextState, param)
    }

    private fun editContactsInfoForPronounce(contactList: List<PhoneContact>): String {
        return contactList.joinToString(separator = ", ") {
            "${it.name} , nömrəsi ${TextUtil.editNumberForPronounce(it.phoneNumber)}"
        }
    }

    companion object {
        private val TAG = "ContactState"

        private const val choiceText =
            "kontaktın ardıcılıq rəqəmini, məsəl üçün birinci söyləyib seçə bilərsiniz."
    }
}