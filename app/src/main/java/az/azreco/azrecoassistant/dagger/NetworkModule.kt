package az.azreco.azrecoassistant.dagger

import az.azreco.azrecoassistant.api.AzrecoApi
import az.azreco.azrecoassistant.constants.Constants.AZRECO_BASE_URL
import az.azreco.azrecoassistant.repo.network.AzrecoRepo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @ViewModelScoped
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().setLenient().create()

    @ViewModelScoped
    @Provides
    fun provideAzrecoApi(gson: Gson): AzrecoApi = Retrofit.Builder()
        .baseUrl(AZRECO_BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(ScalarsConverterFactory.create()) //important
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AzrecoApi::class.java)

    @ViewModelScoped
    @Provides
    fun provideAzrecoRepo(azrecoApi: AzrecoApi) = AzrecoRepo(azrecoApi = azrecoApi)
}