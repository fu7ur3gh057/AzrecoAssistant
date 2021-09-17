package az.azreco.azrecoassistant.dagger

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.assistant.azreco.ServiceSpeech
import az.azreco.azrecoassistant.constants.Constants.ACTION_SHOW_MAIN_ACTIVITY
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_CHANNEL_ID
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_TEXT
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_TITLE
import az.azreco.azrecoassistant.scene.SceneContainer
import az.azreco.azrecoassistant.scene.scenes.SmsScene
import az.azreco.azrecoassistant.scene.scenes.WeatherScene
import az.azreco.azrecoassistant.ui.activity.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideSceneContainer(smsScene: SmsScene, weatherScene: WeatherScene) =
        SceneContainer(smsScene = smsScene, weatherScene = weatherScene)

    @ServiceScoped
    @Provides
    fun provideSmsScene() = SmsScene()


    @ServiceScoped
    @Provides
    fun provideWeatherScene() = WeatherScene()

    @ServiceScoped
    @Provides
    fun provideServiceSpeech() = ServiceSpeech()

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent = PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity::class.java).also {
            it.action = ACTION_SHOW_MAIN_ACTIVITY
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )


    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.idrak_logo)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(NOTIFICATION_TEXT)
        .setContentIntent(pendingIntent)
}