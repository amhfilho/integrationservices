package com.ibm.integrationservices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LineExtractor {
    private String line;

    public LineExtractor(String line) {
        this.line = line;
    }

    public String extractXml(){
        if(line != null ){
            int index = -1;
            if((index = line.indexOf("<?xml")) > -1 ||
                    (index = line.indexOf("<command")) > -1){
                int last = line.lastIndexOf('>');
                return line.substring(index,last+1);
            }
        }
        return "";
    }

    public LocalDateTime extractDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
        String input = this.line.substring(0, 23);
        return LocalDateTime.parse(input, formatter);
    }

}
