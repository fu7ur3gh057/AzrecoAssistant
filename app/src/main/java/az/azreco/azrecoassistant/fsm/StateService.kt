package az.azreco.azrecoassistant.fsm

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.assistant.Assistant
import kotlinx.coroutines.*

class StateService(
    private val stateMachine: StateMachine,
    private val assistant: Assistant,
) {
    private val TAG = "StateService"
    private val stateScope = CoroutineScope(Dispatchers.Default)
    private var stateJob: Job? = null
    var serviceIsActive = MutableLiveData<Boolean>()
    private var context: Context? = null

    private var copyMessageList = mutableListOf<Any>()

    // Live Data
    val messageList = MutableLiveData<List<Any>>()
    val isProcessing = MutableLiveData<Boolean>()
    val lastQuestion = MutableLiveData<DialogData.Message?>()
    val action = MutableLiveData<DialogResponse.Action?>()

    init {
        initLiveData()
    }

    fun setContext(ctx: Context) {
        context = ctx
    }

    private fun observeVizualProgress() {

    }

//    fun getMaxAmplitude() = speechVisualize.maxAmplitude

    fun startCommand() = stateScope.launch {
        if (serviceIsActive.value == false) {
            serviceIsActive.postValue(true)
            stateJob = launch {
                try {
                    stateMachine.start { handleResponse(response = it) }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    serviceIsActive.postValue(false)
                }
            }
        }
    }


    private fun handleResponse(response: DialogResponse) = when (response) {
        is DialogResponse.Action -> handleAction(response)
        is DialogResponse.Message -> handleDialogMessage(response)
        is DialogResponse.DialogLink -> handleLink(response)
        is DialogResponse.Process -> handleProcess(response)
    }

    private fun handleAction(response: DialogResponse.Action) = action.postValue(response)

    private fun handleProcess(response: DialogResponse.Process) =
        isProcessing.postValue(response.value)

    private fun handleDialogMessage(response: DialogResponse.Message) {
        if (response.msg.isReceived) lastQuestion.postValue(response.msg)
        val message =
            DialogData.Message(isReceived = response.msg.isReceived, text = response.msg.text)
        copyMessageList.add(message)
        messageList.postValue(copyMessageList)
    }

    private fun handleLink(response: DialogResponse.DialogLink) {
        copyMessageList.add(DialogData.WebLink(link = response.link))
        messageList.postValue(copyMessageList)
    }

    fun onDestroy() {
        stateMachine.destroy()
        stateJob?.cancel()
        stateJob = null
        initLiveData()
        context = null
        Log.v(TAG, "onDestroy")
    }

    private fun initLiveData() {
        messageList.postValue(emptyList())
        isProcessing.postValue(false)
        lastQuestion.postValue(null)
        action.postValue(null)
        serviceIsActive.postValue(false)
    }

}