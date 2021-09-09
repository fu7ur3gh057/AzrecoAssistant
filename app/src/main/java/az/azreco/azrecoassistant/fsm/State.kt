package az.azreco.azrecoassistant.fsm

/**
 * State
 */
abstract class State {

    private lateinit var dialogListener: (DialogResponse) -> Unit
    private lateinit var onStateChangedListener: suspend (StateParam?) -> Unit
    val dialogCall by lazy {
        DialogCallback(
            dialogListener = dialogListener)
    }
    private var param: StateParam? = null

    open suspend fun enter(
        dialogListener: (DialogResponse) -> Unit,
        onSceneChanged: suspend (StateParam?) -> Unit,
        parametr: StateParam? = null
    ) {
        this.dialogListener = dialogListener
        onStateChangedListener = onSceneChanged
        parametr?.let { this.param = it }
    }

    open suspend fun onNextState(nextState: State) {}

    fun getParametr(): StateParam? =
        if (param != null) {
            val result = param
            param = null
            result
        } else null


    // All Scenes should be here
    companion object {
        var currentState: State? = null
        var homeState: State? = null
        var callState: State? = null
        var smsState: State? = null
        var newsState: State? = null
    }
}
