package az.azreco.azrecoassistant.dagger

import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.fsm.StateMachine
import az.azreco.azrecoassistant.fsm.states.*
import az.azreco.azrecoassistant.util.ContactsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideStateMachine(
        homeState: HomeState,
        callState: CallState,
        smsState: SmsState,
        newsState: NewsState,
        contactState: ContactState
    ) = StateMachine(homeState, callState, smsState, newsState, contactState)

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

    @ViewModelScoped
    @Provides
    fun provideContactState(assistant: Assistant, contactsUtil: ContactsUtil) =
        ContactState(assistant = assistant, contactsUtil = contactsUtil)

}