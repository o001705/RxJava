package com.ravik.rxhello;

import java.io.IOException;

public class RxHello {
    public RxHello() {
    }

    public static void main(String [] args) throws IOException {

        RxBinFileCopy x = new RxBinFileCopy("d:\\MediaCollection.json", "d:\\MediaCollection-copy.json");
        long start = System.currentTimeMillis();
        x.rxFileCopy();
        long finish = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (finish-start) + " Milli Seconds");
        //Thread.currentThread().join(2000);
    }
}
