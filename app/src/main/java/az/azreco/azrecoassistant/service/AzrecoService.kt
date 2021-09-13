package az.azreco.azrecoassistant.service

import android.annotation.SuppressLint
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_ID
import az.azreco.azrecoassistant.constants.Constants.SMS_CODE
import az.azreco.azrecoassistant.assistant.azreco.ServiceSpeech
import az.azreco.azrecoassistant.model.Notif
import az.azreco.azrecoassistant.util.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AzrecoService : NotificationListenerService() {
    private val TAG = "AzrecoService"
    private val notificationQueue: Queue<Notif> = LinkedList(listOf())

    @Volatile
    private var ttsIsActive = false

    private val serviceScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var textToSpeech: ServiceSpeech

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.v(TAG, "onListenerConnected")
        runForeground()
    }


    private fun runForeground() {
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        Log.v(TAG, "onNotificationPosted")
        serviceScope.launch { handleNotification(sbn = sbn) }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    suspend fun handleNotification(sbn: StatusBarNotification) {
        val notifCode = NotificationUtil.matchNotificationCode(sbn = sbn)
        val notification = NotificationUtil.getDataFromNotification(sbn = sbn)
        Log.v(TAG, "new Notification")
        when (notifCode) {
            SMS_CODE -> {
                notificationQueue.add(notification)
                if (!ttsIsActive) soundMessage()
            }
        }
    }

    @SuppressLint("NewApi")
    private suspend fun soundMessage() {
        ttsIsActive = true
        while (notificationQueue.size != 0) {
            notificationQueue.poll()?.let { it ->
                val title = "${it.title} esemes göndərdi. Esemesin mətni."
                val text = it.text
                textToSpeech.apply {
                    val titleBaos = synthesizeMultiple(texts = listOf(title))[0].baos
                    val textBaos =
                        synthesizeMultiple(texts = listOf(text), voiceId = "325651")[0].baos
                    speakByteStream(baos = titleBaos)
                    speakByteStream(baos = textBaos)
                }
            }
        }
        ttsIsActive = false
    }


    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        stopForeground(true)
        Log.v(TAG, "onListenerDisconnected")
        serviceScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
        serviceScope.cancel()
    }


}