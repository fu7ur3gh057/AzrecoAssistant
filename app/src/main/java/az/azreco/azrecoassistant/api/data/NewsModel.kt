package az.azreco.azrecoassistant.api.data


// модель для @Get запросов AzrecoApi который возвращает json с такими полями
data class NewsModel(
    val title: String,
    val link: String,
    val bytes: String
)