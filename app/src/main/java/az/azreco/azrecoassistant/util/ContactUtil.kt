package az.azreco.azrecoassistant.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import az.azreco.azrecoassistant.model.PhoneContact

// обычный класс утилита для работы с контактами телефона
class ContactUtil(private val context: Context) {

    private val TAG = "ContactUtil"

    var contactsList = listOf<PhoneContact>()
        get() = field
        private set

    fun initContacts() {
        contactsList = getContactList()
        Log.d(TAG, "initContacts, contacts size - ${contactsList.size}")
    }

    fun containsContactName(contactName: String): List<PhoneContact> {
        return contactsList.filter { i -> i.name.contains(contactName, ignoreCase = true) }
    }

    @SuppressLint("DefaultLocale")
    fun getContactByName(contactName: String, contactList: List<PhoneContact>): List<PhoneContact> {
        return contactList.filter { i -> i.name.equals(contactName, ignoreCase = true) }
    }

    fun getContactByNumber(
        contactNumber: String,
        contactList: List<PhoneContact>
    ): List<PhoneContact> {
        return contactList.filter { i -> i.phoneNumber == contactNumber }
    }


    private fun getContactList(): List<PhoneContact> {
        val contactList = mutableListOf<PhoneContact>()
        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cursor?.count ?: 0) > 0) {
            while (cursor != null && cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCursor = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf<String>(id),
                        null
                    )
                    while (pCursor?.moveToNext() == true) {
                        val phoneNo = pCursor.getString(
                            pCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        ).replace("-", "").replace(" ", "")
                        contactList.add(PhoneContact(name = name, phoneNumber = phoneNo))
                    }
                    pCursor?.close()
                }
            }
            cursor?.close()
        }
        return contactList.distinct()
    }
}