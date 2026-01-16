package com.damoa;

import com.damoa.process.ApplyProcessRcp;

@Deprecated
public class DamoaRcp {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        ApplyProcessRcp ap = new ApplyProcessRcp();
        ap.process();

    }
}
