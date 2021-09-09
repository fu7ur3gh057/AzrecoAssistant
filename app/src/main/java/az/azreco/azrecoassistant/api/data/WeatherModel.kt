package az.azreco.azrecoassistant.api.data

data class WeatherModel(
    val city: String,
    val temperature: String,
    val description: String,
    val image: String
)