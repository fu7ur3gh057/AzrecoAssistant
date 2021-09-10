package az.azreco.azrecoassistant.adapter

class DialogAdapter {





}

sealed class DialogData {
    class Message(val isReceived: Boolean, val text: String)
    class WebLink(val link: String)
}