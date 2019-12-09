package com.ibm.integrationservices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TdiEvent {
    private Integer lineNumber;
    private LocalDateTime dateTime;
    private String text;
    private String messagePayload;
    private TdiTransaction transaction;

    public static TdiEvent createFromText(TdiTransaction transaction, Integer lineNumber, String text){
        TdiEvent event = new TdiEvent();
        event.transaction = transaction;
        event.lineNumber = lineNumber;
        event.text = text;
        event.dateTime = parseDateTime(text);
        event.messagePayload = messagePayload(text);
        return event;
    }

    @Override
    public String toString() {
        return "\t{\n" +
                "\t\tlineNumber=" + lineNumber + "\n" +
                "\t\tdateTime=" + dateTime + "\n" +
                //"\t\ttext=" + text + "\n" +
                "\t\tmessagePayload=" + messagePayload + "\n" +
                "\t}";
    }

    private static LocalDateTime parseDateTime(String text){
        if(text.isEmpty()) return null;
        String strings[] = text.split("\\s+");
        return LocalDateTime.parse(strings[0] + " " + strings[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    }

    private static String messagePayload(String text){
        if(text != null && !text.isEmpty()){
            int begin = text.indexOf("<");
            int end = text.lastIndexOf(">");
            return text.substring(begin,end+1);
        }
        return "";
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getText() {
        return text;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public TdiTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(TdiTransaction transaction) {
        this.transaction = transaction;
    }
}
