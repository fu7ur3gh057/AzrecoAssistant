package az.azreco.azrecoassistant.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
    }


}