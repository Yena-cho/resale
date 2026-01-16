package com.damoa;

import com.damoa.process.ApplyProcessPay;

@Deprecated
public class DamoaPay {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        ApplyProcessPay ap = new ApplyProcessPay();
        ap.process();

    }
}
