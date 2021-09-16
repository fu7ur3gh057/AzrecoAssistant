package az.azreco.azrecoassistant.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.databinding.ActivityDialogBinding
import az.azreco.azrecoassistant.fsm.SpeechVisualizer
import az.azreco.azrecoassistant.fsm.StateService
import az.azreco.azrecoassistant.service.DialogService
import az.azreco.azrecoassistant.ui.viewmodel.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class DialogActivity : AppCompatActivity() {
    private val TAG = "DialogActivity"
    private var viewJob: Job? = null

    // UI Components
    private lateinit var binding: ActivityDialogBinding
    private var recording = false

    // LifeCycle Components
    private val viewModel: DialogViewModel by viewModels()
    private var dialogService: DialogService? = null

    @Inject
    lateinit var stateService: StateService

    @Inject
    lateinit var assistant: Assistant

    @Inject
    lateinit var speechVisualizer: SpeechVisualizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        subscribeToSpeechVizualizer()
        startServiceClickListener()
        subscribeToServiceBinder()

//        subscribeToServiceActivity()
        stateService.setContext(ctx = this)
    }

    private fun startServiceClickListener() =
        binding.dialogStop.setOnClickListener {
            Log.v(TAG, "startServiceClickListener")
            dialogService?.receive()
        }

//    private fun subscribeToServiceActivity() {
//        stateService.serviceIsActive.observe(this, {
//            if (it) binding.dialogMotionLayout.transitionToState(R.id.finish)
//            else binding.dialogMotionLayout.transitionToState(R.id.init)
//        })
//    }

    // TODO Утечка
    private fun subscribeToSpeechVizualizer() = speechVisualizer.apply {
        isActive.observe(this@DialogActivity, {
            if (it == true) {
                if (viewJob?.isActive == true) stopDrawing()
                recording = true
                lifecycleScope.launch(Dispatchers.Default) { viewJob = launch { startDrawing() } }
            } else {
                stopDrawing()
            }
        })
    }

    private fun subscribeToServiceBinder() = viewModel.binder.observe(this, {
//        dialogService = it?.service
        dialogService = when (it) {
            null -> {
                Log.v(TAG, "disconnect from service")
                null
            }
            else -> {
                Log.v(TAG, "connected to service")
                it.service
            }
        }
    })

    private suspend fun startDrawing() {
        while (recording) {
            val amplitude = speechVisualizer.maxAmplitude
            withContext(Dispatchers.Main) { binding.dialogRecordView.update(amplitude ?: 0) }
            delay(1)
        }
        stopDrawing()
        Log.d(TAG, "end of Drawing")
    }

    private fun stopDrawing() {
        recording = false
        viewJob?.cancel()
        viewJob = null
        binding.dialogRecordView.recreate()
    }


    private fun setupToolbar() = binding.toolbarDialog.let {
        setSupportActionBar(it)
        supportActionBar?.title = ""
        binding.toolbarDialog.setNavigationIcon(R.drawable.ic_back)
        it.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        viewJob?.cancel()
        super.onPause()
        dialogService?.stopCommand()
        if (viewModel.binder.value != null) unbindService(viewModel.serviceConnection)
    }

    override fun onResume() {
        super.onResume()
        bindService()
    }

    private fun bindService() {
        val serviceBindIntent = Intent(this, DialogService::class.java)
        bindService(serviceBindIntent, viewModel.serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        viewJob?.cancel()
        lifecycleScope.launch { assistant.release() }
        stateService.onDestroy()
        super.onDestroy()
    }
}