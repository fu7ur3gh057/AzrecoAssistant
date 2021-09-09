package az.azreco.azrecoassistant.util

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.telephony.SmsManager
import az.azreco.azrecoassistant.model.PhoneContact
import az.azreco.azrecoassistant.model.SmsModel

class SmsUtil(private val context: Context, private val contactsUtility: ContactsUtil) {

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
        return contactsUtility.contactsList.filter { i ->
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