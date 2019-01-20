package com.ibm.integrationservices;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        String file = "C:\\Users\\AntonioMarioHenrique\\Desktop\\temp\\chubb.prd.2018-12-04.log";

        System.out.println("opening file...");
        long start = System.currentTimeMillis();
        TdiFileReader reader = TdiFileReader.open(file);

        long time = System.currentTimeMillis()-start;
        //reader.lines().forEach(n -> System.out.println(n));

        System.out.println(String.format("opened in %d milliseconds",time));
        
    }
}
