package az.azreco.azrecoassistant.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeUtil {

    fun getCurrentDate(): String = SimpleDateFormat("yyyy-MM-dd").format(Date())

    fun getCurrentTime(seconds: Boolean = true): String = if (seconds) {
        getTimeDateInfo(pattern = "HH:mm:ss")
    } else {
        getTimeDateInfo(pattern = "HH:mm")
    }

    //public method. Used by HomeFragment homeGreetingTv
    fun greetingMessage(): String = getPartOfDay(
        hour = getTimeDateInfo(pattern = "HH").toInt()
    )

    private fun getPartOfDay(hour: Int): String = when (hour) {
        in 19..24 -> "Axşamınız xeyir" // evening
        in 0..6 -> "Gecəniz xeyrə" // night
        in 7..11 -> "Sabahınız xeyir" // morning
        else -> "Hər vaxtınız xeyir" // afternoon
    }

    private fun getTimeDateInfo(pattern: String) = SimpleDateFormat(pattern).format(Date())
}