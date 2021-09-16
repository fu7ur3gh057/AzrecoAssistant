package az.azreco.azrecoassistant.util

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SmsManager
import az.azreco.azrecoassistant.model.PhoneContact

class SmsUtil(private val contactUtil: ContactUtil) {

    fun sendSMSByNumber(phoneNo: String, msg: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("DefaultLocale")
    fun getContactByName(contactName: String): List<PhoneContact> {
        return contactUtil.contactsList.filter { i ->
            i.name.equals(
                contactName,
                ignoreCase = true
            )
        }
    }


    fun sendSMSByName(name: String, msg: String) {
        try {
            val foundedContacts =
                getContactByName(contactName = name)
            val phoneNo = foundedContacts[0].phoneNumber
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}