package az.azreco.azrecoassistant.ui.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.api.data.TipModel
import az.azreco.azrecoassistant.databinding.FragmentHomeBinding
import az.azreco.azrecoassistant.fsm.states.NewsState
import az.azreco.azrecoassistant.ui.viewmodel.HomeViewModel
import az.azreco.azrecoassistant.util.TimeUtil
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        subscribeToObservers()
        setupTimeTitle()
    }

    private fun subscribeToObservers() {
        viewModel.tipDay.observe(viewLifecycleOwner, {
            it?.let { setupTipCardView(tipModel = it) }
        })
    }


    // TIP CARD VIEW METHODS
    private fun setupTipCardView(tipModel: TipModel) = binding.homeTipCv.apply {
        Glide.with(this@HomeFragment).load(tipModel.imageUrl).centerCrop().into(tipIv)
        tipTitleTv.text = tipModel.title
        tipTextTv.text = tipModel.text

    }

    private fun tipTryBtnClickListener() = binding.homeTipCv.tipTryBtn.setOnClickListener {

    }

    private fun tipOkayBtnClickListener() = binding.homeTipCv.tipOkayBtn.setOnClickListener {

    }

    private fun setupTimeTitle() {
        binding.homeGreetingTv.text = TimeUtil.greetingMessage()
    }

}