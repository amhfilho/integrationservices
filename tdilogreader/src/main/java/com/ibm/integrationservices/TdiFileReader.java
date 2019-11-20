package com.ibm.integrationservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TdiFileReader {

    private List<String> lines;

    private TdiFileReader(List<String> lines){
        this.lines = lines;
    }

    public static TdiFileReader open(String filePath) throws IOException {
        Instant start = Instant.now();
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))){
            String line = "";
            while((line = br.readLine()) != null){
                lines.add(line);
            }
        }
        Instant end = Instant.now();
        System.out.println("File opened in " + Duration.between(start, end).toMillis() + " milliseconds");
        return new TdiFileReader(lines);
    }

    public List<String> lines(){
        return Collections.unmodifiableList(lines);
    }
    
}
