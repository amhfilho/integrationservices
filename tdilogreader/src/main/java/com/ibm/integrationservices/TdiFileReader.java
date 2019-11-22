package com.ibm.integrationservices;

import java.io.*;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TdiFileReader {

    public List<String> open(InputStream inputStream) throws IOException {
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String line = "";
            while((line = br.readLine()) != null){
                lines.add(line);
            }
        }
        return lines;
    }

}
