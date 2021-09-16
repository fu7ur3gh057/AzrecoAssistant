package az.azreco.azrecoassistant.fsm.states

import android.util.Log
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.constants.Audio
import az.azreco.azrecoassistant.constants.Commands
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.fsm.State
import az.azreco.azrecoassistant.fsm.StateParam
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.model.SmsModel
import az.azreco.azrecoassistant.util.SmsUtil
import kotlinx.coroutines.delay

class SmsState(
    private val assistant: Assistant,
    private val smsUtil: SmsUtil
) : State() {

    private val TAG = "SmsState"
    private val stopCommands = Commands.formatToKeywords(Commands.stop)
    private var phoneContact: PhoneContact? = null

    override suspend fun enter(
        dialogListener: (DialogResponse) -> Unit,
        onStateChanged: suspend (StateParam?) -> Unit,
        parametr: StateParam?
    ) {
        super.enter(dialogListener, onStateChanged, parametr)
        Log.d(TAG, "enter")
        if (param is StateParam.ContactParam) {
            phoneContact = (param as StateParam.ContactParam).contact
            listenMessageText()
        }
    }

    private suspend fun listenMessageText() = assistant.apply {
        playSync(audioFile = Audio.smsSayText)
        uiCall.updateDialog(true, "signaldan sonra SMS-ın mətni oxuyun")
        uiCall.updateProcess(true)
        playAsync(audioFile = Audio.signalStart)
        val text = speechRecognize(silence = 5)
        uiCall.updateProcess(false)
        uiCall.updateDialog(false, text)
        checkMessageText(text = text)
    }

    private suspend fun checkMessageText(text: String) {
        if (text.isEmpty()) textIsEmpty()
        else textIsSuccess(text = text)
    }

    private suspend fun textIsEmpty() = assistant.apply {
        val speechResponse =
            "esemesin mətni boşdu. dəyişmək üçün dəyiş, ləğv etmək üçün ləğv et deyin"
        speak(text = speechResponse)
        uiCall.updateDialog(true, speechResponse)
        reKeywordSpotting(
            "ləğv et\ndəyiş\n$stopCommands",
            startLambda = {},
            endLambda = {
                uiCall.updateProcess(false)
                when (it) {
                    in stopCommands -> {
                        uiCall.updateDialog(false, it)
                        playAsync(audioFile = Audio.smsCanceled)
                    }
                    "dəyiş" -> {
                        uiCall.updateDialog(false, it)
                        listenMessageText()
                    }
                }
            }
        )
    }

    // if text isn't empty
    private suspend fun textIsSuccess(text: String) = assistant.apply {
        playAsync(audioFile = Audio.smsText)
        uiCall.updateDialog(true, "SMS-ın mətni : $text")
        speak(text = text, voiceId = "325651")
        delay(1000)
        playSync(audioFile = Audio.smsConfirm)
        reKeywordSpotting(
            keyWords = "göndər\nləğv et\ndəyiş\nyox\n${stopCommands}",
            startLambda = { uiCall.updateProcess(true) },
            endLambda = {
                uiCall.updateProcess(false)
                when (it) {
                    "dəyiş" -> {
                        uiCall.updateDialog(false, it)
                        listenMessageText()
                    }
                    "ləğv et", "yox", in stopCommands -> {
                        uiCall.updateDialog(false, it)
                        playAsync(audioFile = Audio.smsCanceled)
                    }
                    "göndər" -> {
                        uiCall.updateDialog(false, it)
                        sendMessage(SmsModel(phoneContact = phoneContact!!, msg = text))
                        playAsync(audioFile = Audio.smsSuccess)
                    }
                }
            }
        )
    }

    private fun sendMessage(smsModel: SmsModel) {
        Log.d(TAG, "sendMessage")
        smsUtil.sendSMSByNumber(
            phoneNo = smsModel.phoneContact.phoneNumber,
            msg = smsModel.msg
        )
    }

    override suspend fun onNextState(nextState: State?, param: StateParam?) {
        super.onNextState(nextState, param)
    }


    companion object {
        private val TAG = "SmsState"

    }
}