package az.azreco.azrecoassistant.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.databinding.ActivityDialogBinding
import az.azreco.azrecoassistant.fsm.DialogResponse
import az.azreco.azrecoassistant.fsm.StateService
import az.azreco.azrecoassistant.ui.viewmodel.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class DialogActivity : AppCompatActivity() {
    private val TAG = "DialogActivity"
    private lateinit var binding: ActivityDialogBinding
    private lateinit var navHostFragment: NavHostFragment
    private var recording = false
    private val viewJob: Job? = null
    private val viewModel: DialogViewModel by viewModels()

    @Inject
    lateinit var stateService: StateService

    @Inject
    lateinit var assistant: Assistant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch(Dispatchers.Default) {

        }
//        fabDialogClickListener()
    }

    private fun fabDialogClickListener() = binding.dialogFab.setOnClickListener {
        lifecycleScope.launch(Dispatchers.Default) {
            recording = true
            val job = launch { assistant.keywordSpotting(silence = 3, keyWords = "stop") }
            launch { startDrawing() }
            job.join()
            recording = false
        }
    }

    private suspend fun startDrawing() {
        stopDrawing()
        while (recording) {
            val amplitude = stateService.getMaxAmplitude()
            Log.d(TAG, "Max Amplitude: $amplitude")
            withContext(Dispatchers.Main) { binding.recordView.update(amplitude ?: 0) }
            delay(1)
        }
    }

    private fun stopDrawing() = binding.recordView.recreate()


    override fun onPause() {
        viewJob?.cancel()
        super.onPause()
    }

    override fun onDestroy() {
        stateService.onDestroy()
        viewJob?.cancel()
        super.onDestroy()
    }
}