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
public class ASRClient {
    static {
        System.loadLibrary("azrecoasrclient");
    }
    
    private long clientPtr = 0; 
    private ASRClientConfiguration asrClientConf;
    
    public ASRClient(ASRClientConfiguration asrClientConf) {
        this.asrClientConf = asrClientConf;
        clientPtr = makeASRClient(asrClientConf);
    }
    
    private native long makeASRClient(ASRClientConfiguration asrClientConf);
    
    public int connect() {
        if(clientPtr == 0) {
            return -1;
        }
        return connect(clientPtr);
    }
    
    private native int connect(long clientPtr);
    
    public void stop() {
        stop(clientPtr);
    }
    
    private native void stop(long clientPtr);
    
    public void destroy() {
        destroy(clientPtr);
    }
    
    private native void destroy(long handlePtr);
    
    public void write(byte[] buffer, int offset, int length) {
        write(clientPtr, buffer, offset, length);
    }
    
    private native void write(long clientPtr, byte[] buffer, int offset, int length);
    
    public boolean hasResult(boolean waitUntilResultExists) {
        return hasResult(clientPtr, waitUntilResultExists);
    }
    
    private native boolean hasResult(long clientPtr, boolean waitUntilResultExists);
    
    public String getResult() {
        return getResult(clientPtr);
    }
    
    private native String getResult(long clientPtr);
    
    public boolean isOk() {
        return isOk(clientPtr);
    }
    
    private native boolean isOk(long clientPtr);
    
    public void endStream() {
        endStream(clientPtr);
    }
    
    private native void endStream(long clientPtr);
    
    public void waitForCompletion() {
        waitForCompletion(clientPtr);
    }
    
    private native void waitForCompletion(long clientPtr);
    
    public String getErrorMessage() {
        return getErrorMessage(clientPtr);
    }
    
    private native String getErrorMessage(long clientPtr);
}