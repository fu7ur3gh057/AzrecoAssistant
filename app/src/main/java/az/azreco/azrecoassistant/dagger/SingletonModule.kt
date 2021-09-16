package az.azreco.azrecoassistant.dagger

import android.content.Context
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.assistant.azreco.SpeechRecognizer
import az.azreco.azrecoassistant.assistant.azreco.TextToSpeech
import az.azreco.azrecoassistant.assistant.player.AudioPlayer
import az.azreco.azrecoassistant.assistant.player.ExoPlayer
import az.azreco.azrecoassistant.fsm.SpeechVisualizer
import az.azreco.azrecoassistant.util.ContactUtil
import az.azreco.azrecoassistant.util.SmsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun provideContactsUtil(@ApplicationContext app: Context) =
        ContactUtil(context = app)

    @Singleton
    @Provides
    fun provideSmsUtil(@ApplicationContext app: Context, contactUtil: ContactUtil) =
        SmsUtil(contactUtil = contactUtil)


    @Singleton
    @Provides
    fun provideAudioPlayer(@ApplicationContext app: Context) = AudioPlayer(context = app)

    @Singleton
    @Provides
    fun provideSpeechVisualize() = SpeechVisualizer()

    @Singleton
    @Provides
    fun provideSpeechRecognizer(speechVisualizer: SpeechVisualizer) =
        SpeechRecognizer(speechVisualizer = speechVisualizer)

    @Singleton
    @Provides
    fun provideTextToSpeech() = TextToSpeech()

    @Singleton
    @Provides
    fun provideExoPlayer(@ApplicationContext app: Context) = ExoPlayer(context = app)

    @Singleton
    @Provides
    fun provideAssistant(
        speechRecognizer: SpeechRecognizer,
        textToSpeech: TextToSpeech,
        exoPlayer: ExoPlayer,
        audioPlayer: AudioPlayer
    ) = Assistant(speechRecognizer, textToSpeech, exoPlayer, audioPlayer)
}