package az.azreco.azrecoassistant.util

import android.annotation.SuppressLint
import android.app.Notification
import android.os.Build
import android.os.Bundle
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import az.azreco.azrecoassistant.constants.Constants.FACEBOOK_CODE
import az.azreco.azrecoassistant.constants.Constants.FACEBOOK_MESSENGER_PACK_NAME
import az.azreco.azrecoassistant.constants.Constants.FACEBOOK_PACK_NAME
import az.azreco.azrecoassistant.constants.Constants.INSTAGRAM_CODE
import az.azreco.azrecoassistant.constants.Constants.INSTAGRAM_PACK_NAME
import az.azreco.azrecoassistant.constants.Constants.OTHER_CODE
import az.azreco.azrecoassistant.constants.Constants.SMS_CODE
import az.azreco.azrecoassistant.constants.Constants.SMS_PACK_NAME_SAMSUNG
import az.azreco.azrecoassistant.constants.Constants.SMS_PACK_NAME_XIOMI
import az.azreco.azrecoassistant.constants.Constants.WHATSAPP_CODE
import az.azreco.azrecoassistant.constants.Constants.WHATSAPP_PACK_NAME
import az.azreco.azrecoassistant.model.Notif

object NotificationUtil {

    fun matchNotificationCode(sbn: StatusBarNotification): Int {
        return when (sbn.packageName) {
            SMS_PACK_NAME_XIOMI -> SMS_CODE
            SMS_PACK_NAME_SAMSUNG -> SMS_CODE
            FACEBOOK_PACK_NAME -> FACEBOOK_CODE
            FACEBOOK_MESSENGER_PACK_NAME -> FACEBOOK_CODE
            WHATSAPP_PACK_NAME -> WHATSAPP_CODE
            INSTAGRAM_PACK_NAME -> INSTAGRAM_CODE
            else -> OTHER_CODE
        }
    }

    private fun getSubText(extras: Bundle): String {
        var subText = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val b = extras[Notification.EXTRA_MESSAGES] as Array<*>?
            if (b != null) {
                for (tmp in b) {
                    val msgBundle = tmp as Bundle
                    subText = msgBundle.getString("text").toString()
                }
            }
        }
        return subText
    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getDataFromNotification(sbn: StatusBarNotification): Notif {
        val newNotification = sbn.notification
        val extras = newNotification.extras
        val title = extras.getCharSequence("android.title").toString()
        val text = extras.getCharSequence("android.text").toString()
        val subText = getSubText(extras = extras)
        return Notif(
            title = title,
            text = text,
            subText = subText,
            packageName = sbn.packageName,
            key = sbn.key
        )
    }
}