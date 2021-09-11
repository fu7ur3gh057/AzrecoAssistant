package az.azreco.azrecoassistant.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.assistant.Assistant
import az.azreco.azrecoassistant.databinding.ActivityLoginBinding
import az.azreco.azrecoassistant.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding

    private lateinit var navHostFragment: NavHostFragment

    @Inject
    lateinit var assistant: Assistant

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch(Dispatchers.IO) {
//            val stt = SpeechToText()
//            stt.keywordSpotting(silence = 4, keyWords = "stop")
        }
    }

    private fun initNavHostFragment() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.loginNavHostFragment) as NavHostFragment
    }

}