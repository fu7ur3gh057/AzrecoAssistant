package az.azreco.azrecoassistant.dagger

import android.content.Context
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.assistant.azreco.SpeechRecognizer
import az.azreco.azrecoassistant.assistant.azreco.TextToSpeech
import az.azreco.azrecoassistant.assistant.exo.ExoPlayer
import az.azreco.azrecoassistant.fsm.StateMachine
import az.azreco.azrecoassistant.fsm.states.CallState
import az.azreco.azrecoassistant.fsm.states.HomeState
import az.azreco.azrecoassistant.fsm.states.NewsState
import az.azreco.azrecoassistant.fsm.states.SmsState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideSpeechRecognizer() = SpeechRecognizer()

    @ViewModelScoped
    @Provides
    fun provideTextToSpeech() = TextToSpeech()

    @ViewModelScoped
    @Provides
    fun provideExoPlayer(@ApplicationContext app: Context) = ExoPlayer(context = app)

    @ViewModelScoped
    @Provides
    fun provideAssistant(
        speechRecognizer: SpeechRecognizer,
        textToSpeech: TextToSpeech,
        exoPlayer: ExoPlayer
    ) = Assistant(speechRecognizer, textToSpeech, exoPlayer)

    @ViewModelScoped
    @Provides
    fun provideStateMachine(
        homeState: HomeState,
        callState: CallState,
        smsState: SmsState,
        newsState: NewsState
    ) = StateMachine(
        homeState = homeState,
        callState = callState,
        smsState = smsState,
        newsState = newsState
    )

    @ViewModelScoped
    @Provides
    fun provideHomeState() = HomeState()

    @ViewModelScoped
    @Provides
    fun provideCallState() = CallState()

    @ViewModelScoped
    @Provides
    fun provideSmsState() = SmsState()

    @ViewModelScoped
    @Provides
    fun provideNewsState() = NewsState()
}