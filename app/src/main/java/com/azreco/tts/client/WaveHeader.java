/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azreco.tts.client;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author toghrul
 */
public class WaveHeader {
    private String chunkId = "RIFF";
    private int chunkSize = 0;
    private String format = "WAVE";
    private String subchunk1Id = "fmt ";
    private int subchunk1Size = 16;
    private short audioFormat = 1;
    private short numChannels = 1;
    private int sampleRate = 23000;
    private int byteRate = 0;
    private short blockAlign = 0;
    private short bitsPerSample = 16;
    private String subchunk2Id = "data";
    private int subchunk2Size = 0;

    public WaveHeader() {
        byteRate = (sampleRate * numChannels * bitsPerSample / 8);
        blockAlign = (short)(numChannels * bitsPerSample / 8);
    }

    public void setBitsPerSample(short bitsPerSample) {
        this.bitsPerSample = bitsPerSample;
        byteRate = (sampleRate * numChannels * bitsPerSample / 8);
        blockAlign = (short)(numChannels * bitsPerSample / 8);
    }

    public short getBitsPerSample() {
        return bitsPerSample;
    }

    public void setNumChannels(short numChannels) {
        this.numChannels = numChannels;
        byteRate = (sampleRate * numChannels * bitsPerSample / 8);
        blockAlign = (short)(numChannels * bitsPerSample / 8);
    }

    public short getNumChannels() {
        return numChannels;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        byteRate = (sampleRate * numChannels * bitsPerSample / 8);
    }

    public int getSampleRate() {
        return sampleRate;
    }
    
    public byte[] getHeaderBytes(int numSamples) {
        subchunk2Size = (numSamples * numChannels * bitsPerSample / 8);
        chunkSize = (36 + subchunk2Size);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(44);
        try {
            baos.write(chunkId.getBytes());
            baos.write(chunkSize & 0xff);
            baos.write((chunkSize >> 8) & 0xff);
            baos.write((chunkSize >> 16) & 0xff);
            baos.write((chunkSize >> 24) & 0xff);
            baos.write(format.getBytes());
            baos.write(subchunk1Id.getBytes());
            baos.write(subchunk1Size & 0xff);
            baos.write((subchunk1Size >> 8) & 0xff);
            baos.write((subchunk1Size >> 16) & 0xff);
            baos.write((subchunk1Size >> 24) & 0xff);
            baos.write(audioFormat & 0xff);
            baos.write((audioFormat >> 8) & 0xff);
            baos.write(numChannels & 0xff);
            baos.write((numChannels >> 8) & 0xff);
            baos.write(sampleRate & 0xff);
            baos.write((sampleRate >> 8) & 0xff);
            baos.write((sampleRate >> 16) & 0xff);
            baos.write((sampleRate >> 24) & 0xff);
            baos.write(byteRate & 0xff);
            baos.write((byteRate >> 8) & 0xff);
            baos.write((byteRate >> 16) & 0xff);
            baos.write((byteRate >> 24) & 0xff);
            baos.write(blockAlign & 0xff);
            baos.write((blockAlign >> 8) & 0xff);
            baos.write(bitsPerSample & 0xff);
            baos.write((bitsPerSample >> 8) & 0xff);
            baos.write(subchunk2Id.getBytes());
            baos.write(subchunk2Size & 0xff);
            baos.write((subchunk2Size >> 8) & 0xff);
            baos.write((subchunk2Size >> 16) & 0xff);
            baos.write((subchunk2Size >> 24) & 0xff);
            byte[] headerBytes = baos.toByteArray();
            baos.close();
            return headerBytes;
        }
        catch(Exception ex) {
        }
        return null;
    }
}
