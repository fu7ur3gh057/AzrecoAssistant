package az.azreco.azrecoassistant.dagger

import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.assistant.azreco.SpeechVisualize
import az.azreco.azrecoassistant.fsm.StateMachine
import az.azreco.azrecoassistant.fsm.StateService
import az.azreco.azrecoassistant.fsm.states.*
import az.azreco.azrecoassistant.util.ContactsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {


    @ActivityScoped
    @Provides
    fun provideStateService(
        stateMachine: StateMachine, assistant: Assistant,
        speechVisualize: SpeechVisualize
    ) = StateService(
        stateMachine = stateMachine,
        assistant = assistant,
        speechVisualize = speechVisualize
    )

    @ActivityScoped
    @Provides
    fun provideStateMachine(
        homeState: HomeState,
        callState: CallState,
        smsState: SmsState,
        newsState: NewsState,
        contactState: ContactState
    ) = StateMachine(homeState, callState, smsState, newsState, contactState)

    @ActivityScoped
    @Provides
    fun provideHomeState() = HomeState()

    @ActivityScoped
    @Provides
    fun provideCallState() = CallState()

    @ActivityScoped
    @Provides
    fun provideSmsState() = SmsState()

    @ActivityScoped
    @Provides
    fun provideNewsState() = NewsState()

    @ActivityScoped
    @Provides
    fun provideContactState(assistant: Assistant, contactsUtil: ContactsUtil) =
        ContactState(
            assistant = assistant,
            contactsUtil = contactsUtil
        )


}