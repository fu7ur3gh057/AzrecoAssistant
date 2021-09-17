package az.azreco.azrecoassistant.fsm

import androidx.lifecycle.MutableLiveData

class SpeechVisualizer {
    val isActive = MutableLiveData<Boolean>()
    var maxAmplitude: Int? = null
        get() {
            val amp = field
            maxAmplitude = null
            return amp
        }

    init {
        isActive.postValue(false)
    }
}