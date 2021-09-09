package az.azreco.azrecoassistant.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.azreco.azrecoassistant.api.data.TipModel
import az.azreco.azrecoassistant.api.data.WeatherModel
import az.azreco.azrecoassistant.repo.network.AzrecoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val azrecoRepo: AzrecoRepo
) : ViewModel() {


    val tipDay = MutableLiveData<TipModel?>()

    val currentWeather = MutableLiveData<WeatherModel?>()


    init {
        tipDay.postValue(null)
        currentWeather.postValue(null)
//        getTipOfDay()
    }

    // TODO
    private fun getWeatherByCity() = viewModelScope.launch(Dispatchers.Default) {
        try {
            val response = azrecoRepo.getWeatherByCity(city = "baku")
            if (response.isSuccessful && response.code() == 200) {
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun getTipOfDay() = viewModelScope.launch(Dispatchers.Default) {
        try {
            val response = azrecoRepo.getTipOfDay()
            if (response.isSuccessful && response.code() == 200) {
                response.body()?.let { tipDay.postValue(it) }
            }
        } catch (ex: Exception) {
            tipDay.postValue(TipModel("nothing!!bad request", "", "", ""))
            ex.printStackTrace()
        }
    }

}
