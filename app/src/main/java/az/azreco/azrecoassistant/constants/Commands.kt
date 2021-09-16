package az.azreco.azrecoassistant.constants


object Commands {

    fun formatToKeywords(kwList: List<String>): String {
        return kwList.joinToString { it + "\n" }.replace(", ", "")
    }

    fun formatAllToKeywords(): String {
        val list = listOf(
            stop, sendSms, phoneCall,
            news, sendWhatsapp, youtube, google, weather
        )
        return list.joinToString { formatToKeywords(it) }.replace(", ", "")
    }

    val stop = listOf("sim sim stop", "sim sim dəyan", "stop")
    val positive = listOf("hə", "bəli", "yes", "da")
    val negative = listOf("yox", "xeyir", "no", "net")
    val sendSms = listOf("esemes göndər", "esemes", "esemes at", "mesaj göndər", "mesajlar")
    val phoneCall = listOf("zəng elə", "zəng et")
    val news = listOf("xəbər", "xəbərlər", "son xəbərlər", "yeni xəbərlər")
    val sendWhatsapp = listOf("vatsap", "vatsap göndər")
    val youtube = listOf("yutub", "yutub video")
    val google = listOf("gugıl")
    val weather = listOf("hava", "temperatur", "bu gün hava necədir", "neçə dərəcədir")
    val calculator = listOf("")
    val translator = listOf("")
    val alert = listOf("")
    val finance = listOf("") // какой курс валют
}