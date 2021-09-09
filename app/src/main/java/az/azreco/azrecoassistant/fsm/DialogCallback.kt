package az.azreco.azrecoassistant.fsm

// class utility for ease callback dialogListener and sceneChangedListener for every Scene Classes
class DialogCallback(
    dialogListener: (DialogResponse) -> Unit,
) {
    private var listener: (DialogResponse) -> Unit = dialogListener

//    fun updateDialog(isReceived: Boolean, text: String) =
//        listener(
//            DialogResponse.DialogMessage(DialogData.Message(isReceived = isReceived, text = text))
//        )

    fun updateDialog(link: String) =
        listener(DialogResponse.DialogLink(link = link))

    fun updateAction(key: String, value: String) = listener(DialogResponse.Action(key, value))

    fun updateProcess(isSynthesizing: Boolean) =
        listener(DialogResponse.Process(value = isSynthesizing))

}