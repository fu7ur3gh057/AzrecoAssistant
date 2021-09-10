package az.azreco.azrecoassistant.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.databinding.ActivityLoginBinding
import az.azreco.azrecoassistant.databinding.ActivityMainBinding
import az.azreco.azrecoassistant.ui.viewmodel.LoginViewModel
import az.azreco.azrecoassistant.util.TimeUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding

    private lateinit var navHostFragment: NavHostFragment

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun initNavHostFragment() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.loginNavHostFragment) as NavHostFragment
    }

}