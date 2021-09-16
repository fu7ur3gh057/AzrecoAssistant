package az.azreco.azrecoassistant.fsm

import android.util.Log

/**
 * State
 */
abstract class State {

    private lateinit var dialogListener: (DialogResponse) -> Unit
    lateinit var onStateChanged: suspend (StateParam?) -> Unit
    val uiCall by lazy {
        DialogCallback(
            dialogListener = dialogListener
        )
    }
    var param: StateParam? = null
//        get() {
//            val response = field
//            field = null
//            return response
//        }

    open suspend fun enter(
        dialogListener: (DialogResponse) -> Unit,
        onStateChanged: suspend (StateParam?) -> Unit,
        parametr: StateParam? = null
    ) {
        this.dialogListener = dialogListener
        this.onStateChanged = onStateChanged
        param = parametr
    }

    open suspend fun onNextState(nextState: State?, param: StateParam? = null) {
        currentState = nextState
        onStateChanged(param)
    }

//    fun getParametr(): StateParam? =
//        if (param != null) {
//            val result = param
//            param = null
//            result
//        } else null


    // All Scenes should be here
    companion object {
        var currentState: State? = null
        var homeState: State? = null
        var callState: State? = null
        var smsState: State? = null
        var newsState: State? = null
        var contactState: State? = null
    }
}
