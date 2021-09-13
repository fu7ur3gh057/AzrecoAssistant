package az.azreco.azrecoassistant.fsm.states

import android.util.Log
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.fsm.State
import az.azreco.azrecoassistant.fsm.StateParam
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.util.ContactsUtil

class ContactState(
    private val assistant: Assistant,
    private val contactsUtil: ContactsUtil
) : State() {

    private val TAG = "ContactState"

    private val contactList by lazy {
        contactsUtil.contactsList
    }

    override suspend fun enter(
        dialogListener: (DialogResponse) -> Unit,
        onStateChanged: suspend (StateParam?) -> Unit,
        parametr: StateParam?
    ) {
        super.enter(dialogListener, onStateChanged, parametr)
        Log.d(TAG, "enter ::: ConstactState")
        when (this.getParametr()) {
            is StateParam.ContactName -> {

            }
            else -> {

            }
        }
    }

    override suspend fun onNextState(nextState: State) {
        super.onNextState(nextState)
    }

    private fun fromListToString(contactList: List<PhoneContact>): String {
        return contactList.joinToString(separator = ", ") {
            "${it.name} , nömrəsi ${contactsUtil.numberForTTS(it.phoneNumber)}"
        }
    }

}