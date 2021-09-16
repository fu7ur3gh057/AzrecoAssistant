package az.azreco.azrecoassistant.constants

import az.azreco.azrecoassistant.R

object Audio {

    const val signalStart = R.raw.signal_start
    const val signalStop = R.raw.signal_stop
    val callContact = listOf(R.raw.call_contact_1, R.raw.call_contact_2).random()
    val smsContact = listOf(R.raw.sms_contact_1, R.raw.sms_contact_2).random()
    val smsConfirm = listOf(R.raw.sms_confirm_1, R.raw.sms_confirm_1, R.raw.sms_confirm_3).random()
    val smsSayText = R.raw.sms_say_text
    val smsSuccess = R.raw.sms_success
    const val smsText = R.raw.sms_text
    const val greeting = R.raw.greeting
    const val contactNotFound = R.raw.contact_not_found
    val repeats = listOf(R.raw.repeat_1, R.raw.repeat_2).random()
    val callCanceled = listOf(R.raw.call_cancel, R.raw.canceled_2).random()
    val smsCanceled = listOf(R.raw.sms_canceled, R.raw.canceled_2, R.raw.canceled_1).random()

}