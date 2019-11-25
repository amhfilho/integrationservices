package com.ibm.integrationservices;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {
    public void startFromClasspath(String file) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(file);
        List<String> lines = new TdiFileReader().open(inputStream);

        System.out.println("Lines: " + lines.size());

        List<String> transactions = new ArrayList<>();

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:/temp/logs/anixter.txt")));

        for(int i = 0; i < lines.size(); i++){
            //System.out.println("line: " + i);
            String line = lines.get(i);
            if(line.contains("XPath command:")){ //start of a new request
                TdiTransaction transaction = new TdiTransaction(i, extractCommand(line));
                String netcoolEventId = extractEventId(line, transaction.getCommand());
                LocalDateTime dateTime = extractDateTime(line);
                String inputXml = extractMessage(
                        lines,
                        i,
                        new SearchCondition("Input XML"),
                        Arrays.asList("</command>"));
                String getKeysRequest = extractMessage(
                        lines,
                        i,
                        new SearchCondition("TicketHandle","soap request:","and"),
                        Arrays.asList("</SOAP-ENV:Envelope>","</soapenv:Envelope>"));
                String getKeysResponse = extractMessage(
                        lines,
                        i,
                        new SearchCondition("TicketHandle","soap response:","and"),
                        Arrays.asList("</SOAP-ENV:Envelope>","</soapenv:Envelope>"));

                SearchCondition requestSearchCondition = new SearchCondition("UpdateCallbackXML");
                SearchCondition responseSearchCondition = new SearchCondition("UpdateCallbackXML");

                if(getKeysRequest.isEmpty()){
                    requestSearchCondition = new SearchCondition("soap request:", "TicketHandle", "not");
                    responseSearchCondition = new SearchCondition("soap response:", "TicketHandle", "not");
                }

                String soapRequest = extractMessage(
                        lines,
                        i,
                        requestSearchCondition,
                        Arrays.asList("</SOAP-ENV:Envelope>","</soapenv:Envelope>"));
                String soapResponse = extractMessage(
                        lines,
                        i,
                        responseSearchCondition,
                        Arrays.asList("</SOAP-ENV:Envelope>","</soapenv:Envelope>"));
                String netcoolResponse = extractMessage(
                        lines,
                        i,
                        new SearchCondition("<result"),
                        Arrays.asList("</result>"));


                transaction.setRequestTime(dateTime);
                transaction.setNetcoolEvent(netcoolEventId);
                transaction.setNetcoolRequest(inputXml);
                transaction.setSoapRequest(soapRequest);
                transaction.setSoapResponse(soapResponse);
                transaction.setNetcoolResponse(netcoolResponse);
                transaction.setGetKeysRequest(getKeysRequest);
                transaction.setGetKeysResponse(getKeysResponse);

                writer.write(transaction.toString());
                writer.newLine();
            }
        }
        System.out.println("Writing transactions...");
        writer.flush();
        writer.close();

        System.out.println("Done");
    }

    private LocalDateTime extractDateTime(String line) {
        String strings[] = line.split(" ");
        return LocalDateTime.parse(strings[0]+ " " + strings[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    }

    private String extractEventId(String line, String command) {
        if(!command.equals("CALLBACK")){
            String[] strings = line.split(" ");
            return strings[5].replaceAll("\'","").replaceAll("::","");
        }
        return "";

    }

    private String extractMessage(List<String> lines, int cursor, SearchCondition startSearch, List<String> endKey){
        boolean finishPart = false;
        String part = "";
        int aux = cursor+1;
        boolean partFound = false;
        while(!finishPart && aux < lines.size() && !lines.get(aux).toLowerCase().contains("xpath command:")){
            String actualLine = lines.get(aux);
            if(startSearch.match(actualLine)){
                partFound = true;
                part+=actualLine;
            }
            else if(partFound){
                part += lines.get(aux);
                if(containsAnyKey(lines.get(aux),endKey)){
                    finishPart = true;
                    partFound = false;
                }
            }
            aux++;
        }
        return part;
    }


    public String extractCommand(String input){
        int keyIndex = input.indexOf("XPath command:");
        int commandIndex = keyIndex + "XPath command:".length();
        return input.substring(commandIndex).trim();
    }

    private boolean containsAnyKey(String line, List<String> startKey) {
        for(String key: startKey){
            if(line.toLowerCase().contains(key.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Instant start = Instant.now();
            new Application().startFromClasspath("/anixter.log");
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            System.out.println("Time elapsed: " + timeElapsed.getSeconds() + " seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
