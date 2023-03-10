package com.ravik.rxhello;

import reactor.core.publisher.Flux;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class RxFileCopy {
    public RxFileCopy() throws IOException {
    }
// private methods to handle checked exceptions

    private void close(Closeable closeable){
        try {
            closeable.close();
            System.out.println("Closed the resource");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void write(BufferedWriter bw, String string){
        try {
            bw.write(string);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void rxFileCopy( String ipFile, String opFile) throws IOException {
        // input file
        Path ipPath = Paths.get(ipFile);

        Flux<String> stringFlux = Flux.using(
                () -> Files.lines(ipPath),
                Flux::fromStream,
                Stream::close
        );

        // output file
        Path opPath = Paths.get(opFile);
        BufferedWriter bw = Files.newBufferedWriter(opPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        stringFlux
                .subscribe(s -> write(bw, s),
                        (e) -> close(bw),  // close file if error / oncomplete
                        () -> close(bw)
                );
    }
}
