/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azreco.tts.client;

/**
 *
 * @author toghrul
 */
public class TTSClient {

    static {
        System.loadLibrary("azrecottsclient");
    }
    
    private long clientPtr = 0;
    private TTSClientConfiguration ttsClientConf;
    
    public TTSClient(TTSClientConfiguration ttsClientConf) {
        this.ttsClientConf = ttsClientConf;
        clientPtr = makeTTSClient(ttsClientConf);
    }
    
    private native long makeTTSClient(TTSClientConfiguration ttsClientConf);
    
    public int synthesize(String text) {
        if(clientPtr == 0) {
            return -1;
        }
        return TTSClient.this.synthesize(clientPtr, text);
    }
    
    private native int synthesize(long clientPtr, String text);
    
    public void reset() {
        reset(clientPtr);
    }
    
    private native void reset(long clientPtr);
    
    public void stop() {
        stop(clientPtr);
    }
    
    private native void stop(long clientPtr);
    
    public void destroy() {
        destroy(clientPtr);
        clientPtr = 0;
    }
    
    private native void destroy(long clientPtr);
    
    public boolean isOk() {
        return isOk(clientPtr);
    }
    
    private native boolean isOk(long clientPtr);
    
    public void waitForCompletion() {
        waitForCompletion(clientPtr);
    }
    
    private native void waitForCompletion(long clientPtr);
    
    public byte[] read() {
        if(clientPtr == 0) {
            return null;
        }
        return read(clientPtr);
    }
    
    private native byte[] read(long clientPtr);
}
