package az.azreco.azrecoassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.azreco.azrecoassistant.repo.network.AzrecoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val azrecoRepo: AzrecoRepo
) : ViewModel() {


    fun signIn(email: String, password: String) = viewModelScope.launch(IO) {
        val response = azrecoRepo.signIn(email, password)
    }

}