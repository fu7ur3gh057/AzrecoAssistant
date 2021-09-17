package az.azreco.azrecoassistant.dagger

import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.fsm.StateMachine
import az.azreco.azrecoassistant.fsm.StateService
import az.azreco.azrecoassistant.fsm.states.*
import az.azreco.azrecoassistant.util.ContactUtil
import az.azreco.azrecoassistant.util.SmsUtil
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
    ) = StateService(
        stateMachine = stateMachine,
        assistant = assistant,
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
    fun provideHomeState(assistant: Assistant, contactUtil: ContactUtil) = HomeState(
        assistant = assistant,
        contactUtil = contactUtil
    )

    @ActivityScoped
    @Provides
    fun provideCallState() = CallState()

    @ActivityScoped
    @Provides
    fun provideSmsState(assistant: Assistant, smsUtil: SmsUtil) =
        SmsState(assistant = assistant, smsUtil = smsUtil)

    @ActivityScoped
    @Provides
    fun provideNewsState() = NewsState()

    @ActivityScoped
    @Provides
    fun provideContactState(assistant: Assistant, contactUtil: ContactUtil) =
        ContactState(
            assistant = assistant,
            contactUtil = contactUtil
        )


}