/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azreco.asr.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author toghrul
 */
public class ASRJSONResult {
    private String resultText;
    private Long startTimeMsec;
    private Long endTimeMsec;
    private Integer confidence;
    private List<RecognizedWord> words = new ArrayList<>();
    private ASRStatus status;

    public ASRJSONResult() {
    }
    
    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
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

    public List<RecognizedWord> getWords() {
        return words;
    }

    public void setWords(List<RecognizedWord> words) {
        this.words = words;
    }
    
    public void addWord(RecognizedWord word) {
        words.add(word);
    }

    public ASRStatus getStatus() {
        return status;
    }

    public void setStatus(ASRStatus status) {
        this.status = status;
    }
}
