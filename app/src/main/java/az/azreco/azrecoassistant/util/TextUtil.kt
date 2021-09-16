package az.azreco.azrecoassistant.util

import android.util.Log
import java.util.*

object TextUtil {

    fun getSmsKeywords(contactsNames: List<String>): String {
        return addSuffix(
            names = contactsNames,
            firstSuffix = "a esemes göndər\n",
            secondSuffix = "ya esemes göndər\n"
        )
    }

    fun removeSmsSuffix(name: String): String {
        return removeSuffix(
            str = name,
            firstSuffix = "a esemes göndər",
            secondSuffix = "ya esemes göndər"
        )
    }

    // fuad,vova -> fuada zəng elə\n,vovaya zəng elə\n
    fun getCallKeywords(contactsNames: List<String>): String {
        return addSuffix(
            names = contactsNames,
            firstSuffix = "a zəng elə\n",
            secondSuffix = "ya zəng elə\n"
        )
    }

    fun removeCallSuffix(name: String): String {
        return removeSuffix(
            str = name,
            firstSuffix = "a zəng elə",
            secondSuffix = "ya zəng elə"
        )
    }


    fun azNumerical(): String {
        return "birinci\nikinci\nüçüncü\ndördüncü\nbeşinci\naltıncı\nyeddinci\nsəkkizinci\ndoqquzuncu\nonuncu\n"
    }

    fun getContactByNumerical(numerical: String): Int {
        return when (numerical) {
            "birinci" -> 0
            "ikinci" -> 1
            "üçüncü" -> 2
            "dördüncü" -> 3
            "beşinci" -> 4
            "altıncı" -> 5
            "yeddinci" -> 6
            "səkkizinci" -> 7
            "doqquzuncu" -> 8
            "onuncu" -> 9
            else -> 0
        }
    }

    // нужно изменить метод, использовать regex
    // цель метода - изменить номер в строку для правильного чтения TTS
    //  из +994505553355 в + 994 50 555 33 55. так же с 0 вместо +994
    fun editNumberForPronounce(phoneNo: String): String {
        var phoneNumber = phoneNo
        if (phoneNumber.startsWith("+994")) {
            val countryCode = "0"
            phoneNumber = phoneNumber.replace("+994", "")
            val operatorCode = phoneNumber.take(2)
            phoneNumber = phoneNumber.removeRange(0, 2)
            val firstNumber = phoneNumber.take(3)
            phoneNumber = phoneNumber.removeRange(0, 3)
            val secondNumber = phoneNumber.take(2)
            phoneNumber = phoneNumber.removeRange(0, 2)
            val thirdNumber = phoneNumber.take(2)
            return "$countryCode-$operatorCode-$firstNumber-$secondNumber-$thirdNumber"
        } else {
            val operatorCode = phoneNumber.take(3)
            phoneNumber = phoneNumber.removeRange(0, 3)
            val firstNumber = phoneNumber.take(3)
            phoneNumber = phoneNumber.removeRange(0, 3)
            val secondNumber = phoneNumber.take(2)
            phoneNumber = phoneNumber.removeRange(0, 2)
            val thirdNumber = phoneNumber.take(2)
            return "$operatorCode-$firstNumber-$secondNumber-$thirdNumber"
        }
    }


    fun removeNonAzLetters(names: List<String>): String {
        var counter = 0
        var result = ""
        for (n in names) {
            val l = n.lowercase(Locale.getDefault())
            var lol = true
            for (i in l.indices) {
                if (!az_letters.contains(l[i])) {
                    lol = false
                    break
                }
            }
            if (lol) {
                result += l + "\n"
                counter += 1
            }
        }
        return result
    }

    fun containsNotAzSymbols(text: String): Boolean {
        var trigger = false
        text.lowercase(Locale.ROOT).forEach { if (!az_letters.contains(it)) trigger = true }
        return trigger
    }


    //"a esemes göndər\n"
    // "ya esemes göndər\n"
    fun removeNonAzWithSuffix(
        names: List<String>,
        firstSuffix: String,
        secondSuffix: String
    ): String {
        var result = ""
        for (n in names) {
            val l = n.lowercase(Locale.getDefault())
            var lol = true
            for (i in l.indices) {
                if (!az_letters.contains(l[i])) {
                    lol = false
                    break
                }
            }
            if (lol) {
                val last = l.last()
                if (consonants.contains(last)) result += l + firstSuffix
                else if (vowels.contains(last)) result += l + secondSuffix
            }
        }
        return result
    }


    //ya esemes göndər
    //a esemes göndər
    fun removeSuffix(str: String, firstSuffix: String, secondSuffix: String): String {
        return if (str.contains(firstSuffix)) {
            str.replace(firstSuffix, "")
        } else {
            str.replace(secondSuffix, "")
        }
    }

    //add to end of string  "a esemes göndər\n" or "ya esemes göndər\n"
    private fun addSuffix(
        names: List<String>,
        firstSuffix: String,
        secondSuffix: String
    ): String {
        var result = ""
        for (n in names) {
            val l = n.toLowerCase(Locale.getDefault())
            var trigger = true
            for (i in l.indices) {
                if (!az_letters.contains(l[i])) {
                    trigger = false
                    break
                }
            }
            if (trigger) {
                val last = l.last()
                if (consonants.contains(last)) result += l + firstSuffix
                else if (vowels.contains(last)) result += l + secondSuffix
            }
        }
        Log.d("LOLKA", result)
        return result
    }

    private val vowels = listOf(
        'ü',
        'e',
        'y',
        'i',
        'o',
        'ı',
        'a',
        'ə'
    )


    private val consonants = listOf(
        'q',
        'r',
        't',
        'p',
        's',
        'd',
        'f',
        'g',
        'h',
        'j',
        'k',
        'l',
        'z',
        'x',
        'c',
        'v',
        'b',
        'n',
        'm'
    )


    private val az_letters = listOf(
        'ş',
        'ç',
        'm',
        'n',
        'b',
        'v',
        'c',
        'x',
        'z',
        'ə',
        'ı',
        'l',
        'k',
        'j',
        'h',
        'g',
        'f',
        'd',
        's',
        'a',
        'ğ',
        'ö',
        'p',
        'o',
        'i',
        'u',
        'y',
        't',
        'r',
        'e',
        'ü',
        'q',
        ' '
    )
}