package com;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import com.azreco.tts.client.TTSClient;

/**
 * Created by abdurrauf on 5/30/17.
 */
public class AndroidMic {


    private static final String TAG = "mic";
    private AudioRecord recorder = null;
    private boolean state = false;


    public int read(byte[] b, int off, int len) {
        if (state) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return recorder.read(b, off, len, AudioRecord.READ_NON_BLOCKING);
            } else {
                return recorder.read(b, off, len);
            }
        }
        return -1;
    }


    public void start() throws Exception {
        if (recorder != null
                && recorder.getState() == AudioRecord.STATE_INITIALIZED
                && recorder.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED && state == false) {
            this.state = true;
            recorder.startRecording();
        } else {
            throw new Exception("error in start");
        }
    }


    public synchronized void stop() {
        if (!state) return;

        if (recorder != null
                && recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            recorder.stop();

        }// if not open

        state = false;
    }


    public void open() throws Exception {

        int tmpBufferSize = AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (tmpBufferSize != AudioRecord.ERROR_BAD_VALUE) {
            recorder = new AudioRecord(
                    MediaRecorder.AudioSource.VOICE_RECOGNITION, 16000,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, tmpBufferSize * 15);
        }
        if (recorder != null
                && recorder.getState() == AudioRecord.STATE_INITIALIZED) {
            //Log.i(TAG, "success in audio creating ");
        } else {
            //Log.i(TAG, "failure in audio creating ");
            throw new Exception("Mic error");
        }

    }


    public void close() {
        stop();
        if (recorder != null)
            recorder.release();
        recorder = null;

    }


    public boolean isOpen() {
        return state;
    }



}
