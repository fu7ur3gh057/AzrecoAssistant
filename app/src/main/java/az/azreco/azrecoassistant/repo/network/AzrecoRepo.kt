package az.azreco.azrecoassistant.repo.network

import az.azreco.azrecoassistant.api.AzrecoApi
import az.azreco.azrecoassistant.api.data.UserModel
import javax.inject.Inject


class AzrecoRepo @Inject constructor(private val azrecoApi: AzrecoApi) {

    // users db
    suspend fun signIn(username: String, password: String) =
        azrecoApi.signIn(username = username, password = password)

    suspend fun signUp(user: UserModel) = azrecoApi.signUp(user = user)

    // TODO
    suspend fun signOut() = azrecoApi.signOut()

    // News
    suspend fun getNews(count: Int) = azrecoApi.getNews(count = count)

    suspend fun getAudioNews() = azrecoApi.getNewsAudio()

    // Weather
    suspend fun getWeatherByCity(city: String) = azrecoApi.getWeatherByCity(city = city)

    suspend fun getRandomWeather() = azrecoApi.getRandomWeather()

    //TODO
    suspend fun getTipOfDay() = azrecoApi.getTipOfDay()

}