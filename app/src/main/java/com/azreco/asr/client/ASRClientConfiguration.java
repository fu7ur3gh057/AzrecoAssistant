/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azreco.asr.client;

/**
 *
 * @author toghrul
 */
public class ASRClientConfiguration {
    // sizeof connection header is missing. must be 20
    public static final int CONNECTION_HEADER_ERROR_CODE = -100000;
    // language of speech recognition is missing. You can use "tr"
    public static final int RECOGNITION_LANG_ERROR_CODE = -100001;
    // number of connections exceedes value of max connection option of dispatcher
    public static final int MAX_CONNECTION_ERROR_CODE = -100002;
    // input type is missing. input type must be of [IT_FILE,IT_AUDIO,IT_RAW]
    public static final int INPUT_TYPE_ERROR_CODE = -100003;
    // size of sent block is missing. default size on dispatcher side is 4096. if you want to change the block size you also have to change block size option in the configuration file of dispatcher. this size is max size of block.
    public static final int BLOCK_SIZE_ERROR_CODE = -100004;
    //returns when type of result of FULL or PARTIAL is incorrect(FULL=0, PARTIAL=1)
    public static final int RESULT_TYPE_ERROR_CODE = -100005;
    //if you connect for partial result this is returned when error occurs while recognitioning
    public static final int PARTIAL_RESULT_ERROR_CODE = -100006;
    //credential JSON is empty.
    public static final int EMPTY_CREDENTIAL_ERROR_CODE = -100008;
    //invalid credential json
    public static final int INVALID_CREDENTIAL_ERROR_CODE = -100009;
    //balance is finished. Unable to continue.
    public static final int BALANCE_FINISHED_ERROR_CODE = -1000010;
    //authentication process failed.
    public static final int AUTH_FAILED_ERROR_CODE = -1000011;
    //after successful connection the client side virifies SSL certificate of server side. If verification failed returns this error code
    public static final int SSL_VERIFICATION_ERROR_CODE = -1000012;
    
    private String host = "localhost";
    private int port = 0;
    private AudioSource audioSource = AudioSource.AS_REALTIME;
    private ResultType resultType = ResultType.RT_PARTIAL;
    private boolean opusEnabled = true;
    private String language = "en";
    private String credential = null;
    private String caCertificate = null;
    private String customDictionary = null;
    private boolean sslEnabled = true;

    public ASRClientConfiguration() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public AudioSource getAudioSource() {
        return audioSource;
    }

    public void setAudioSource(AudioSource audioSource) {
        this.audioSource = audioSource;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCredential() {
        return credential;
    }

    public String getCACertificate() {
        return caCertificate;
    }

    public void setCACertificate(String caCert) {
        this.caCertificate = caCert;
    }
    
    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getCustomDictionary() {
        return customDictionary;
    }

    public void setCustomDictionary(String customDictionary) {
        this.customDictionary = customDictionary;
    }
    
    public boolean isOpusEnabled() {
        return opusEnabled;
    }

    public void setOpusEnabled(boolean opusEnabled) {
        this.opusEnabled = opusEnabled;
    }

    public boolean isSSLEnabled() {
        return sslEnabled;
    }

    public void setSSLEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }
}
