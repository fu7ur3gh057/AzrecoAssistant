package az.azreco.azrecoassistant.ui.viewmodel

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.adapter.DialogData
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.service.DialogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val assistant: Assistant
) : ViewModel() {
    private val TAG = "DialogViewModel"
    val binder = MutableLiveData<DialogService.DialogBinder?>()

    init {
        binder.postValue(null)
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            Log.d(TAG, "onServiceConnected")
            binder.postValue(iBinder as DialogService.DialogBinder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            binder.postValue(null)
        }
    }


}