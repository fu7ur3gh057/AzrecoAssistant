package com;

public class EnergyVad {
    public final static int SPEECH_TIMEOUT = -13;
    public final static int SERVER_CUT = -15;
    public final static int SERVER_CUT_WITH_FAILURE = -19;
    public final static int visualizeTime = 50;//ms

    public final static String TAG = "ev";
    public final static int MAX_SPEECH_LEN = 320000;
    private static final int minimalRMSE = 50;
    boolean first_frame = true;
    int headBufferBeginLength = 0;
    int totalLen = 0;
    int prev_res = 0;
    boolean triggered = true;
    float rmseMin;
    float rmseMax;
    float initialRmseMin;
    float lambda;
    float max = 0;
    int cycle_count = 0;
    /**
     * CallBacks
     */
    private Callbacks callbacks;
    private boolean haveCallbacks = false;
    private int chunk_size = 1000;
    private short[] buffer;
    private short[] headBuffer;
    private int cycle_countSize = 0;
    private int end_status;

    public EnergyVad(int headMargin, int tailmargin) {
        int headSamples = (headMargin * 16000 / 1000);
        if (headSamples < 500)
            headSamples = 500;
        buffer = new short[32000];
        headBuffer = new short[headSamples];
        this.cycle_countSize = (tailmargin * 16000 / 1000) / chunk_size + 2;
    }

    public EnergyVad(int headMargin, int tailmargin,int chunk_size) {
        int headSamples = (headMargin * 16000 / 1000);
        if (headSamples < 250)
            headSamples = 250;
        headSamples= (int) (Math.floor( headSamples/(float)chunk_size)*chunk_size);
        buffer = new short[32000];
        headBuffer = new short[headSamples];
        this.chunk_size=chunk_size;
        this.cycle_countSize = (tailmargin * 16000 / 1000) / chunk_size + 2;
    }

    /**
     * @param calls
     */
    public void setCallbacks(Callbacks calls) {
        callbacks = calls;
        haveCallbacks = true;
    }

    float calcRmse(short buf[], int offset, int count) {
        float sum_energy = 0.0f;
        for (int i = offset; i < offset + count; i++) {
            sum_energy += buf[i] * buf[i];
            float m = buf[i] > 0 ? buf[i] : -buf[i];
            max = max > m ? max : m;
        }
        return (float) Math.sqrt(sum_energy / count);
    }

    public int process() throws InterruptedException{
        int step = 0;
        int read_size = chunk_size;
        int count = 0;
        int endOf = 0;
        int chunkStep = chunk_size / 2;
        float delta = 1.01f;
        boolean end_of_stream = false;
        long currentStartTime = System.currentTimeMillis();
        if (!haveCallbacks) {
            return -1;
        }
        callbacks.callbackStart();
        break_input:
        {
            while (true) {
                count = callbacks.callbackRead(buffer, step, read_size);
                /* end of stream /segment or error */
                if (count < 0) {
                    switch (count) {
                        case -1:
                            end_status = 0;
                            break;
                        case -2:
                            end_status = -1;
                            break;
                        case -3:
                            end_status = 0;
                            break;
                    }
                    end_of_stream = true;
                    break break_input;
                }//
                int restate = callbacks.callbackGetCheck();
                if (restate < 0) {

                    if (totalLen > 0) {

                        if (restate != SPEECH_TIMEOUT && restate != SERVER_CUT) {
                            callbacks.callbackStop();
                        }
                    } else {
                        restate = SPEECH_TIMEOUT;
                    }
                    end_status = restate;
                    break break_input;
                }//
                if (count > 0) {

                    int ii = 0;
                    int buflen = step + count;
                    for (ii = 0; ii + chunk_size <= buflen; ii += chunkStep) {
                        int nonOverlapOffset = endOf - ii;
                        float RMSE = calcRmse(buffer, ii, chunk_size);

                        if (first_frame) {
                            first_frame = false;
                            lambda = 0.8f;
                            initialRmseMin = RMSE < minimalRMSE ? minimalRMSE
                                    : RMSE;
                            rmseMin = initialRmseMin;
                            rmseMax = rmseMin / (1 - lambda);
                        }

                        rmseMax = Math.max(rmseMax, RMSE);

                        if (RMSE < rmseMin) {
                            if (RMSE < minimalRMSE)
                                rmseMin = initialRmseMin;
                            else {
                                rmseMin = RMSE;
                            }
                            delta = 1.01f;
                        } else {
                            delta = delta * 1.001f;
                        }
                        rmseMin = rmseMin * delta;
                        lambda = (rmseMax - rmseMin) / rmseMax;
                        float threshold = (1 - lambda) * rmseMax + lambda
                                * rmseMin;
                        int res = prev_res;
                        if (prev_res < 1 && RMSE > threshold) {
                            cycle_count = cycle_countSize;
                            res = 1;
                        } else if (triggered && prev_res > 0
                                && RMSE < threshold) {
                            res = -1;
                        }
                        prev_res = res;
                        if (triggered && prev_res < 0) {
                            --cycle_count;
                        }
                        if (!triggered && res > 0) {
                            triggered = true;
//                            callbacks.callbackStart();
                            int ad_process_ret = callbacks.callbackProcess(headBuffer,
                                    0, headBufferBeginLength);

                            switch (ad_process_ret) {
                                case 1:
                                    end_status = 2;
                                    break break_input;
                                case -1:
                                    end_status = -1;
                                    end_of_stream=true;
                                    break break_input;
                                case -9:
                                    end_of_stream=true;
                                    end_status = -9;
                                    break break_input;
                            }
                            totalLen += headBufferBeginLength;

                        } else if (cycle_count <= 0 && triggered) {
                            prev_res = 0;
                            triggered = false;
                            cycle_count = cycle_countSize;
                            callbacks.callbackStop();
                            end_status = 0;
                            continue ;
                        }

                        if (triggered) {

                            int ad_process_ret = callbacks.callbackProcess(buffer, ii
                                    + nonOverlapOffset, chunk_size
                                    - nonOverlapOffset);

                            switch (ad_process_ret) {
                                case 1:
                                    end_status = 2;
                                    break break_input;
                                case -1:
                                    end_of_stream = true;
                                    end_status = -1;
                                    break break_input;
                                case -9:
                                    end_of_stream = true;
                                    end_status = -9;
                                    break break_input;
                            }
                            totalLen += chunk_size - nonOverlapOffset;
                        }

                        if (!triggered) {
                            int len = chunk_size - nonOverlapOffset;
                            if (len >= headBuffer.length) {
                                System.arraycopy(buffer, count
                                                - headBuffer.length, headBuffer, 0,
                                        headBuffer.length);
                                headBufferBeginLength = headBuffer.length;
                            } else {
                                if (len + headBufferBeginLength > headBuffer.length) {
                                    int overflowSize = len + headBufferBeginLength
                                            - headBuffer.length;
                                    int shiftSize = headBufferBeginLength
                                            - overflowSize;
                                    System.arraycopy(headBuffer, overflowSize,
                                            headBuffer, 0, shiftSize);
                                    headBufferBeginLength = shiftSize;
                                }
                                System.arraycopy(buffer, nonOverlapOffset + ii,
                                        headBuffer, headBufferBeginLength, len);
                                headBufferBeginLength += len;
                            }
                        }
                        endOf = ii + chunk_size;
                    }// for loop
                    step = buflen - ii;
                    endOf = step >= chunkStep ? chunkStep : 0;
                    if (ii > 0) {
                        System.arraycopy(buffer, ii, buffer, 0, step);
                    }
                    if (currentStartTime + visualizeTime <= System.currentTimeMillis()) {
                        currentStartTime = System.currentTimeMillis();
                        callbacks.callbackVisualize(max);
                        max = 0.0f;
                    }
                }// if count >0
                else if(count==0){
//                    Thread.sleep(1);
                }
            }// while true

        }// break_input
        if (end_of_stream) {
            this.callbacks.callbackStop();
        }
        return end_status;
    }

}