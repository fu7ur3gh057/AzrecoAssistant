package az.azreco.azrecoassistant.ui.fragment.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.adapter.DialogAdapter
import az.azreco.azrecoassistant.databinding.FragmentChatBinding
import az.azreco.azrecoassistant.fsm.StateService
import az.azreco.azrecoassistant.ui.viewmodel.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {
    private lateinit var binding: FragmentChatBinding
    private val sharedViewModel by viewModels<DialogViewModel>()
    private lateinit var dialogAdapter: DialogAdapter

    @Inject
    lateinit var stateService: StateService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        initRecycler()
        subscribeToObservers()
    }

    private fun subscribeToObservers() = binding.apply {
        stateService.messageList.observe(viewLifecycleOwner, {
            dialogAdapter.differ.submitList(it)
            dialogAdapter.notifyDataSetChanged()
            if (it.size > 2) dialogRecycler.smoothScrollToPosition(it.size - 1)
        })
    }


    private fun initRecycler() = binding.apply {
        val linearManager = LinearLayoutManager(requireContext())
        dialogAdapter = DialogAdapter()
        dialogRecycler.let {
            it.setHasFixedSize(true)
            it.layoutManager = linearManager
            it.adapter = dialogAdapter
        }
    }
}