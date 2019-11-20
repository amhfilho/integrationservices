package com.ibm.integrationservices;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

public class LargeFileHandler {
    public static void main(String[] args) {
        String fileName = "c:/Users/Mult-e/Downloads/itcont.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))){
            int lines = 0;
            String line = "";
            Instant start = Instant.now();

            while((line = reader.readLine()) != null){
                lines++;
            }
            System.out.println("This file contains " + lines + " lines");
            System.out.println("Run in " + Duration.between(start,Instant.now()).getSeconds() + " seconds");
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
