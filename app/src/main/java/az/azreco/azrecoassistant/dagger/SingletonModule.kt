package az.azreco.azrecoassistant.dagger

import android.content.Context
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
}