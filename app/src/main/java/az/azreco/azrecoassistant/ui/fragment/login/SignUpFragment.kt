package az.azreco.azrecoassistant.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.databinding.FragmentSignUpBinding
import az.azreco.azrecoassistant.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val TAG = "SignUpFragment"

    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        signUpClickListener()
        redirectToSignIn()
    }

    private fun signUpClickListener() = binding.signUpBtn.setOnClickListener {
        val intent = Intent(requireContext(), MainActivity::class.java)
        requireContext().startActivity(intent)
    }

    private fun redirectToSignIn() = binding.signUpRedirectTv.setOnClickListener {
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }
}