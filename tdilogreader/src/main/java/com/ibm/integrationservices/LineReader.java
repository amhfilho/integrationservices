package com.ibm.integrationservices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LineReader {

    private String line;

    public LineReader(String line) {
        this.line = line;
    }

    public String readEventId(){
        return readExpressionAfter(TdiTransaction.XPATH_EXT_PROB_ID);
    }

    public boolean hasCommand(){
        return readCommand() != null;
    }

    public boolean hasCreateOrUpdate(){
        return readCommand() != null && !"CALLBACK".equals(readCommand()) && !"UPDATE_CALLBACK".equals(readCommand());
    }

    public String readCommand(){
        return readExpressionAfter(TdiTransaction.XPATH_COMMAND);
    }

    public LocalDateTime readDateTime(){
        String input = line.substring(0, 23);
        return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    }

    private String readExpressionAfter(String key){
        int index = line.indexOf(key);
        if(index == -1) return null;
        return line.substring(index + key.length()).trim();
    }
}
