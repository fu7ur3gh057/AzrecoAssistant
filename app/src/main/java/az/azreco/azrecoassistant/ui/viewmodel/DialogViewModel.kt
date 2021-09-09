package az.azreco.azrecoassistant.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
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


    fun lol() = viewModelScope.launch(Dispatchers.IO) {
//        val response = assistant.listenKeyword(keyWords = "salam\nsagol",
//            startListen = {
//                Log.d(TAG, "start Listening!!")
//            },
//            endListen = {
//                Log.d(TAG, "end Listening !!")
//            })
//
//        Log.d(TAG, "RESULT IS - $response")
        assistant.playExoSync("repeat_1", true)
        Log.d(TAG, "RESULT IS")
        assistant.playExoSync("repeat_2", true)
        Log.d(TAG, "RESULT IS")
        assistant.playExoAsync("repeat_1")
        Log.d(TAG, "RESULT IS")
    }

}