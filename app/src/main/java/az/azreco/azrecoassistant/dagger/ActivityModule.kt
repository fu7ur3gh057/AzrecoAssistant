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
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

}