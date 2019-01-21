package com.ibm.integrationservices;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionSearch {

    private List<String> lines;

    public TransactionSearch(List<String> lines){
        this.lines = lines;
    }

    public TdiTransaction findFirst(String netcoolEvent) {
        String eventId = null;
        String command = null;
        for(String line:this.lines){
            LineReader reader = new LineReader(line);
            if(reader.hasCreateOrUpdate()){
                command = reader.readCommand();
                System.out.println(line);
                continue;
            }
            if(command != null){
                eventId = reader.readEventId();
                System.out.println(line);
            }
            if(command != null && eventId != null) return new TdiTransaction(command,eventId,reader.readDateTime());
        }
        return null;
    }


}
