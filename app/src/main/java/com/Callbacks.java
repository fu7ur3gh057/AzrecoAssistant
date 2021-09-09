package com;

/**
 * JavaAdinnet Callbacks for using outside of class
 *
 * @author abdurrauf
 */
public interface Callbacks {

    /**
     * @param buf
     * @param offset
     * @param len
     * @return
     */
    int callbackRead(short[] buf, int offset, int len);

    /**
     * @param buf
     * @param offset
     * @param len
     * @return
     */
    int callbackProcess(short[] buf, int offset, int len);

    /**
     * Use this to break
     *
     * @return return command
     */
    int callbackGetCheck();

    /**
     * speech started
     */
    void callbackStart();

    /**
     * speech stopped
     */
    void callbackStop();

    void callbackVisualize(float value);


}