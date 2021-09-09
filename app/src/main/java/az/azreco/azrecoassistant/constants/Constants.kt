package az.azreco.azrecoassistant.constants

object Constants {
    // AZRECO
    const val ASR_HOST = "api.azreco.az"
    const val ASR_PORT = 55666
    const val AZ_LANG = "az"
    const val TTS_HOST = "api.azreco.az"
    const val TTS_PORT = 5005
    const val TTS_VOICE_ID = "325640"

    // AUDIO TRACK AND RECORD
    const val SAMPLE_RATE_HZ = 22050

    //FOREGROUND NOTIFICATION SETTING
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "SimSimAssistantService"
    const val NOTIFICATION_CHANNEL_NAME = "SimSimAssistant"
    const val NOTIFICATION_TITLE = "SimSim is working"
    const val NOTIFICATION_TEXT = "Say your command"


    // NOTIFICATIONS CODES
    const val SMS_PACK_NAME_XIOMI = "com.google.android.apps.messaging"
    const val SMS_PACK_NAME_SAMSUNG = "com.samsung.android.messaging"
    const val FACEBOOK_PACK_NAME = "com.facebook.katana"
    const val FACEBOOK_MESSENGER_PACK_NAME = "com.facebook.orca"
    const val WHATSAPP_PACK_NAME = "com.whatsapp"
    const val INSTAGRAM_PACK_NAME = "com.instagram.android"

    const val SMS_CODE = 0
    const val FACEBOOK_CODE = 1
    const val WHATSAPP_CODE = 2
    const val INSTAGRAM_CODE = 3
    const val OTHER_CODE = 4

    // API KEYS
    const val WEATHER_API_KEY = "5d965c7aecd5bd16b1904c1aecf100c9"
    const val WEATHER_BASE_URL = "https://api.openweathermap.org/"
    const val YOUTUBE_API_KEY = "AIzaSyCv45EbYPkJhcCTNjEsmhzX6GdmTHlrYAQ"

    // AZRECO REST SERVICE
    const val AZRECO_BASE_URL = "http://81.21.81.70:8090/"

    // PERMISSIONS
    const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    const val NOTIFICATION_LISTENERS_PARAMETERS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    const val ACTION_SHOW_MAIN_ACTIVITY = "SHOW_MAIN_ACTIVITY"
    const val ACTION_START_SERVICE = "START_SERVICE"

    const val RELEASE = "release"
    const val ERROR = "error"

}