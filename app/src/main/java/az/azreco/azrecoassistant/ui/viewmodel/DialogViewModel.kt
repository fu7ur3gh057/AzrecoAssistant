package az.azreco.azrecoassistant.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.assistant.Assistant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val assistant: Assistant
) : ViewModel() {

    private val TAG = "DialogViewModel"

    private val copyMessage = mutableListOf<DialogData.Message>()

    val messageList = MutableLiveData<List<DialogData.Message>>()

    val isSynthesizing = MutableLiveData<Boolean>()
    val lastQuestion = MutableLiveData<DialogData.Message>()

}