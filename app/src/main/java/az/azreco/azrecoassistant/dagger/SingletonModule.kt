package az.azreco.azrecoassistant.dagger

import android.content.Context
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.assistant.player.AudioPlayer
import az.azreco.azrecoassistant.assistant.azreco.SpeechRecognizer
import az.azreco.azrecoassistant.assistant.azreco.SpeechVisualize
import az.azreco.azrecoassistant.assistant.azreco.TextToSpeech
import az.azreco.azrecoassistant.assistant.player.ExoPlayer
import az.azreco.azrecoassistant.util.ContactsUtil
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
        ContactsUtil(context = app)

    @Singleton
    @Provides
    fun provideSmsUtil(@ApplicationContext app: Context, contactsUtility: ContactsUtil) =
        SmsUtil(context = app, contactsUtility = contactsUtility)


    @Singleton
    @Provides
    fun provideAudioPlayer(@ApplicationContext app: Context) = AudioPlayer(context = app)

    @Singleton
    @Provides
    fun provideSpeechVisualize() = SpeechVisualize()

    @Singleton
    @Provides
    fun provideSpeechRecognizer(speechVisualize: SpeechVisualize) =
        SpeechRecognizer(speechVisualize = speechVisualize)

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