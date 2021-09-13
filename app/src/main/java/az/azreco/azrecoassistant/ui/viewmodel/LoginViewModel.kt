package az.azreco.azrecoassistant.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.repo.network.AzrecoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val azrecoRepo: AzrecoRepo,
    private val assistant: Assistant
) : ViewModel() {

    private val TAG = "LoginViewModel"

    fun signIn(email: String, password: String) = viewModelScope.launch(IO) {
        val response = azrecoRepo.signIn(email, password)
    }

    init {
        Log.d(TAG, "INIT VM")
    }
}