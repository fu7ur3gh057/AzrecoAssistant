package az.azreco.azrecoassistant.ui.fragment.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.databinding.FragmentChatBinding
import az.azreco.azrecoassistant.databinding.FragmentSignInBinding
import az.azreco.azrecoassistant.ui.viewmodel.DialogViewModel
import az.azreco.azrecoassistant.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG = "SignInFragment"

    private lateinit var binding: FragmentSignInBinding

    private val sharedViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)

        loginClickListener()
    }

    private fun loginClickListener() = binding.loginBtn.setOnClickListener {
        if (filterLoginData()) {
            // viewModel signIn()
        }
    }

    private fun filterLoginData(): Boolean {
        binding.apply {
            val emailText = loginEmailEt.text
            val passText = loginPasswordEt.text
            if (emailText.length < 5 || !emailText.contains("@")) {
                showText("Email is Empty")
                return false
            } else if (passText.length < 3) {
                showText("Password is empty")
                return false
            } else {
                showText("DONE!")
                return true
            }
        }
    }

    private fun forgorPasswordClickListener() = binding.loginForgotPassTv.setOnClickListener {

    }

    private fun signUpClickListener() = binding.loginSignUpTv.setOnClickListener {

    }

    private fun showText(text: String) = Toast.makeText(
        requireContext(), text, Toast.LENGTH_SHORT
    ).show()


}