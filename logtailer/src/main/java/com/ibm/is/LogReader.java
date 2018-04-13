package com.ibm.is;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogReader {
    private LogReader(){}

    public static LogReader build(){
        return new LogReader();
    }

    public List<String> readLines(final String fileName) throws IOException {
        return readLines(new BufferedReader(new FileReader(new File(fileName))));
    }

    public List<String> readLines(final InputStream inputStream) throws  IOException{
        return readLines(new BufferedReader(new InputStreamReader(inputStream)));
    }

    private List<String> readLines(final BufferedReader bufferedReader) throws IOException{
        final List<String> lines = new ArrayList<>();
        String line;
        while((line = bufferedReader.readLine()) != null){
            lines.add(line);
        }
        bufferedReader.close();
        return lines;
    }
}
