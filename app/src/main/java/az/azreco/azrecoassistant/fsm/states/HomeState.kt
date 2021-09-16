package az.azreco.azrecoassistant.fsm.states

import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.constants.Audio
import az.azreco.azrecoassistant.constants.Commands
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.fsm.State
import az.azreco.azrecoassistant.fsm.StateParam
import az.azreco.azrecoassistant.util.ContactUtil
import az.azreco.azrecoassistant.util.TextUtil

typealias ContactStateParam = StateParam.ContactStateParam

class HomeState(
    private val assistant: Assistant,
    private val contactUtil: ContactUtil
) : State() {

    private var smsKeywords = ""
    private var callKeywords = ""

    override suspend fun enter(
        dialogListener: (DialogResponse) -> Unit,
        onStateChanged: suspend (StateParam?) -> Unit,
        parametr: StateParam?
    ) {
        super.enter(dialogListener, onStateChanged, parametr)
        val contactNames = contactUtil.contactsList.map { it.name }
        smsKeywords = TextUtil.getSmsKeywords(contactsNames = contactNames)
        callKeywords = TextUtil.getCallKeywords(contactsNames = contactNames)
        uiCall.updateDialog(true, "Buyurun, sizə necə kömək edə bilərəm")
        assistant.apply {
            playSync(audioFile = Audio.greeting)
            playAsync(audioFile = Audio.signalStart)
            reKeywordSpotting(
                keyWords = "${Commands.formatAllToKeywords()}$smsKeywords$callKeywords",
                limit = 3,
                startLambda = { uiCall.updateProcess(true) },
                endLambda = {
                    uiCall.updateProcess(false)
                    handleKeyword(keyword = it)
                }
            )
        }
    }

    private suspend fun handleKeyword(keyword: String) = assistant.apply {
        uiCall.apply {
            updateDialog(false, keyword)
            updateProcess(false)
        }
        when (keyword) {
            in Commands.stop -> playSync(audioFile = Audio.signalStop)
            in Commands.sendSms -> {
                val parametr = ContactStateParam(nextState = smsState)
                onNextState(nextState = contactState, param = parametr)
            }
            in smsKeywords -> {
                val name = TextUtil.removeSmsSuffix(name = keyword)
                val parametr = ContactStateParam(contactName = name, nextState = smsState)
                onNextState(nextState = contactState, parametr)
            }
            in Commands.phoneCall -> {
                val parametr = ContactStateParam(nextState = callState)
                onNextState(nextState = contactState, param = parametr)
            }
            in callKeywords -> {
                val name = TextUtil.removeCallSuffix(name = keyword)
                val parametr = ContactStateParam(contactName = name, nextState = callState)
                onNextState(nextState = contactState, parametr)
            }
            in Commands.news -> onNextState(newsState)
            in Commands.sendWhatsapp -> assistant.speak("vatsap hələki hazır deyil")
            in Commands.youtube -> assistant.speak("yutub hələki hazır deyil")
            in Commands.google -> assistant.speak("gugl hələki hazır deyil")
            in Commands.weather -> assistant.speak("hava hələki hazır deyil")
        }
    }


    override suspend fun onNextState(nextState: State?, param: StateParam?) {
        super.onNextState(nextState, param)

    }


    companion object {
        private val TAG = "HomeState"

    }
}