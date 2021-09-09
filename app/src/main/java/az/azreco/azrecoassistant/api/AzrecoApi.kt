package az.azreco.azrecoassistant.api

import az.azreco.azrecoassistant.api.data.TipModel
import az.azreco.azrecoassistant.api.data.NewsModel
import az.azreco.azrecoassistant.api.data.UserModel
import retrofit2.Response
import retrofit2.http.*


interface AzrecoApi {

    // USERS DATABASE
    @GET("/users")
    suspend fun signIn(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<UserModel>

    // TODO
    @POST("/users")
    suspend fun signUp(@Body user: UserModel)

    // TODO SIGN OUT
    @PUT("/users/logout")
    suspend fun signOut(): Boolean

    // NEWS
    @GET("/news/latest")
    suspend fun getNews(@Query("count") count: Int = 10): Response<String>

    @GET("news/audio")
    suspend fun getNewsAudio(): Response<List<NewsModel>>

    // TODO NOT STRING, new WeatherModel
    @GET("/weather/random")
    suspend fun getRandomWeather(): Response<String>

    // TODO
    @GET("weather")
    suspend fun getWeatherByCity(@Query("city") city: String): Response<String>

    // TODO TIP OF THE DAY
    @GET("/tips")
    suspend fun getTipOfDay(): Response<TipModel>

}