package az.azreco.azrecoassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import az.azreco.azrecoassistant.repo.network.AzrecoRepo
import az.azreco.azrecoassistant.util.ContactUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val azrecoRepo: AzrecoRepo,
    private val contactUtil: ContactUtil
) : ViewModel() {


    fun initContacts() = contactUtil.initContacts()
}