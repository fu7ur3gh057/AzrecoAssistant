package az.azreco.azrecoassistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_CHANNEL_ID
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_TITLE
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AzRecoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_TITLE,
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}