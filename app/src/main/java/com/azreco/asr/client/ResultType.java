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
public enum ResultType {
    RT_FULL(0),
    RT_PARTIAL(1),
    RT_PARTIAL_WITH_WORDS(2),
    RT_KWS_FULL(3),
    RT_KWS_PARTIAL(4);
    
    private int value;

    private ResultType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
