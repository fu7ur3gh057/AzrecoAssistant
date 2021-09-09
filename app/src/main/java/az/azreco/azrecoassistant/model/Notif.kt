package az.azreco.azrecoassistant.model

import java.io.Serializable

data class Notif(
    val title: String,
    val text: String,
    val subText: String,
    val packageName: String,
    val key: String,
) : Serializable