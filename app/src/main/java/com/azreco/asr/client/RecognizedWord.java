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
public class RecognizedWord {
    private String word;
    private Long startTimeMsec;
    private Long endTimeMsec;
    private Integer confidence;

    public RecognizedWord() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getStartTimeMsec() {
        return startTimeMsec;
    }

    public void setStartTimeMsec(Long startTimeMsec) {
        this.startTimeMsec = startTimeMsec;
    }

    public Long getEndTimeMsec() {
        return endTimeMsec;
    }

    public void setEndTimeMsec(Long endTimeMsec) {
        this.endTimeMsec = endTimeMsec;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }
}
