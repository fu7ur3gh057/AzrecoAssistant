/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azreco.tts.client;

import com.azreco.asr.client.AudioSource;
import com.azreco.asr.client.ResultType;

/**
 *
 * @author toghrul
 */
public class TTSClientConfiguration {
    // sizeof connection header is missing. must be 20
    public static final int CONNECTION_HEADER_ERROR_CODE = -100000;
    // number of connections exceedes value of max connection option of dispatcher
    public static final int MAX_CONNECTION_ERROR_CODE = -100002;
    // size of sent block is missing. default size on dispatcher side is 4096. if you want to change the block size you also have to change block size option in the configuration file of dispatcher. this size is max size of block.
    public static final int BLOCK_SIZE_ERROR_CODE = -100004;
    //credential JSON is empty.
    public static final int EMPTY_CREDENTIAL_ERROR_CODE = -100008;
    //invalid credential json
    public static final int INVALID_CREDENTIAL_ERROR_CODE = -100009;
    //balance is finished. Unable to continue.
    public static final int BALANCE_FINISHED_ERROR_CODE = -100010;
    //authentication process failed.
    public static final int AUTH_FAILED_ERROR_CODE = -100011;
    //after successful connection the client side virifies SSL certificate of server side. If verification failed returns this error code
    public static final int SSL_VERIFICATION_ERROR_CODE = -1000012;
    //if server cannot determine that this connection is for asr, kws or tts, returns this error code
    public static final int CLIENT_TYPE_ERROR_CODE = -100014;
    //returns this error code when TTS process failed
    public static final int TTS_FAILED_ERROR_CODE = -100015;
    //returns this error code when TTS service is not available
    public static final int SERVICE_TEMPORARY_UNAVAILABLE = -100017;
    // if sent TTS identifier is invalid returns this error code
    public static final int INVALID_TTS_ID_ERROR_CODE = -100018;
    
    private String host = "localhost";
    private int port = 0;
    private String language = null;
    private String ttsId = null;
    private String credential = null;
    private String caCertificate = null;
    private boolean sslEnabled = true;
    
    public
    TTSClientConfiguration() {
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getTTSId() {
        return ttsId;
    }

    public void setTTSId(String ttsId) {
        this.ttsId = ttsId;
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

    public boolean isSSLEnabled() {
        return sslEnabled;
    }

    public void setSSLEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }
}
