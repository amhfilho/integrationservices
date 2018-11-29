package com.ibm.integrationservices;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        String file = "C:\\Users\\AntonioMarioHenrique\\Desktop\\temp\\phhs.26.11";

        System.out.println(String.format("Opening file: %s",file));
        List<String> lines = new ArrayList<>();
        long start = System.currentTimeMillis();
        try(BufferedReader br = new BufferedReader(new FileReader(new File(file)))){
            String line = "";
            while((line = br.readLine()) != null){
                lines.add(line);
            }
        }
        long elapsed = (System.currentTimeMillis()-start)/1000;
        System.out.println(String.format("File loaded successfully in %d seconds, %d lines",elapsed,lines.size()));

        String extProbId = "";
    }
}
