package az.azreco.azrecoassistant.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.databinding.FragmentSignInBinding
import az.azreco.azrecoassistant.ui.activity.MainActivity
import az.azreco.azrecoassistant.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG = "SignInFragment"

    private lateinit var binding: FragmentSignInBinding

    private val sharedViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        signInClickListener()
        redirectToSignUp()
    }

    private fun signInClickListener() = binding.signInBtn.setOnClickListener {
        if (filterLoginData()) {
            // TODO viewModel signIn()
            val intent = Intent(requireContext(), MainActivity::class.java)
            requireContext().startActivity(intent)
        }
    }

    private fun filterLoginData(): Boolean {
        binding.apply {
            val emailText = signInEmailEt.text
            val passText = signInPasswordEt.text
            return if (emailText.length < 5 || !emailText.contains("@")) {
                showText("Email is Empty")
                false
            } else if (passText.length < 3) {
                showText("Password is empty")
                false
            } else {
                showText("DONE!")
                true
            }
        }
    }

    private fun forgorPasswordClickListener() = binding.signInForgotPassTv.setOnClickListener {

    }

    private fun redirectToSignUp() = binding.signInRedirectTv.setOnClickListener {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun showText(text: String) = Toast.makeText(
        requireContext(), text, Toast.LENGTH_SHORT
    ).show()


}