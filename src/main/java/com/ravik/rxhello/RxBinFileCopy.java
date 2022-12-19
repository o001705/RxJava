package com.ravik.rxhello;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RxBinFileCopy {
    private String inFile;
    private String outFile;

    public RxBinFileCopy(String inFile, String outFile) throws FileNotFoundException {
        this.inFile = inFile;
        this.outFile = outFile;
    }

    public void rxFileCopy() throws IOException {
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);

       Flowable<byte[]> source = new FlowableCreate<byte[]>(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<byte[]> emitter) throws Throwable {
                try {
                    while (fis.available() > 0) {
                        emitter.onNext(fis.readNBytes(50000));
                    }
                    fis.close();
                    System.out.println("File Read Complete.");
                } catch (Throwable t) {
                    fis.close();
                    System.out.println("Closed with Error..");
                }
            }
        }, BackpressureStrategy.BUFFER).observeOn(Schedulers.computation());

        source.observeOn(Schedulers.io())
                .subscribe(e -> {
                    try {
                        fos.write(e);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                },
                ex -> {
                    fos.close();
                    System.out.println("Error: Closing output stream" + Thread.currentThread().getName());
                },
                () -> {
                       fos.close();
                       System.out.println("Completed: Closing output stream" + Thread.currentThread().getName());
                    }
               );

    }
}
