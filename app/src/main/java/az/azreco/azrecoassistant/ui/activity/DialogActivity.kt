package az.azreco.azrecoassistant.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.assistant.audioplayer.AudioPlayer
import az.azreco.azrecoassistant.assistant.azreco.TextToSpeech
import az.azreco.azrecoassistant.databinding.ActivityDialogBinding
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.fsm.StateMachine
import az.azreco.azrecoassistant.ui.viewmodel.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DialogActivity : AppCompatActivity() {
    private val TAG = "DialogActivity"

    private lateinit var binding: ActivityDialogBinding

    private val viewModel: DialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        viewModel.lol()
        val player = AudioPlayer(context = this)
        lifecycleScope.launch(Dispatchers.IO) {
            val tts = TextToSpeech()
            val l = tts.synthesizeMultiple(listOf("salam", "bir iki"))
            Log.d(TAG, "list size ${l.size}")
            for (i in l) {
                tts.speakByteStream(i.baos)
            }
        }
        Log.d(TAG, "end of onCreate")
    }

    // State Machine Methods
/*
    private fun handleStateMachineResponse() = stateMachine.start {
        when (it) {
            is DialogResponse.Action -> handleAction(response = it)
            is DialogResponse.Process -> handleProcess(response = it)
            is DialogResponse.Message -> handleDialogMessage(response = it)
            else -> Log.d(TAG, "Wrong Dialog Response")
        }
    }
*/

    private fun handleAction(response: DialogResponse.Action) {

    }

    private fun handleProcess(response: DialogResponse.Process) {

    }

    private fun handleDialogMessage(response: DialogResponse.Message) {

    }

    private fun handleLink() {

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}