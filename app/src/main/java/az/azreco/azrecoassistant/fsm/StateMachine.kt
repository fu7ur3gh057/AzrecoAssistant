package az.azreco.azrecoassistant.fsm

import android.util.Log
import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.fsm.states.*
import az.azreco.azrecoassistant.model.PhoneContact
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * Finite State Machine
 */
class StateMachine(
    homeState: HomeState,
    callState: CallState,
    smsState: SmsState,
    newsState: NewsState,
    contactState: ContactState
) {
    private val TAG = "DialogFlow"

    private val stateScope = CoroutineScope(Dispatchers.Default)

    private var stateJob: Job? = null

    private var isRunning = false

    init {
        State.homeState = homeState
        State.callState = callState
        State.smsState = smsState
        State.newsState = newsState
        State.contactState = contactState
        State.currentState = State.homeState
    }

    fun start(listener: (DialogResponse) -> Unit) = stateScope.launch {
        isRunning = true
        stateJob = launch {
            try {
                runState(listener = listener)
            } catch (ex: Exception) {
                State.currentState = State.homeState
            } finally {
                Log.d(TAG, "End StateMachine")
                isRunning = false
            }
        }
    }

    private suspend fun runState(
        listener: (DialogResponse) -> Unit,
        stateParam: StateParam? = null
    ) {
        State.currentState?.enter(
            dialogListener = {
                listener(it)
            },
            onStateChanged = {
                runState(listener = listener, stateParam = it)
            },
            parametr = stateParam
        )
    }

    fun resume() {

    }

    fun pause() {
        stateJob?.cancel()
        stateJob = null
    }

    fun isRunning() = isRunning

    fun destroy() {
        stateScope.launch {
            stateJob?.cancel()
            stateJob = null
        }
        stateScope.cancel()
    }

}

// Response for DialogCallback
sealed class DialogResponse {
    class Process(val value: Boolean) : DialogResponse()
    class Action(val key: String, val value: String) : DialogResponse()
    class DialogLink(val link: String) : DialogResponse()
    class Message(val msg: DialogData.Message) : DialogResponse()
}

// Parametr that have all Scene classes
sealed class StateParam {
    class Contact(val contact: PhoneContact) : StateParam()
    class ContactName(val contactName: String, val nextState: String) : StateParam()
}