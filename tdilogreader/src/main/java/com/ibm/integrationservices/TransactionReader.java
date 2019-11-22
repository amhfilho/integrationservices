package com.ibm.integrationservices;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Reads all transactions from logfile
 */
public class TransactionReader {
    private List<String> lines;

    public TransactionReader(List<String> lines) {
        this.lines = lines;
    }

    private Map<Integer, String> findAllWebServiceRequests(){
        return findAllRequests(Arrays.asList("SOAP request:"),
                Arrays.asList("</soapenv:Envelope>","</SOAP-ENV:Envelope>"));
    }

    private Map<Integer, String> findAllWebServiceResponses(){
        return findAllRequests(Arrays.asList("SOAP request:"),
                Arrays.asList("</soapenv:Envelope>","</SOAP-ENV:Envelope>"));
    }

    private Map<Integer,String> findAllNetcoolRequests(){
        return findAllRequests(Arrays.asList("Input XML"), Arrays.asList("</command>"));
    }

    private Map<Integer,String> findAllRequests(List<String> startKey, List<String> endKey){
        Map<Integer,String> requestMap = new HashMap<>();
        boolean requestFound = false;
        String request = "";
        int lineNumber = 0;
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if(contaisAnyKey(line, startKey)){
                lineNumber = i;
                request = line;
                requestFound = true;
                if(contaisAnyKey(line, endKey)){
                    requestFound = false;
                    requestMap.put(lineNumber,request);
                    request = "";
                }
            }
            else if(requestFound){
                request += line;
                if(contaisAnyKey(line, endKey)){
                    requestFound = false;
                    requestMap.put(lineNumber,request);
                    request = "";
                }

            }
        }
        return requestMap;
    }

    private boolean contaisAnyKey(String line, List<String> startKey) {
        for(String key: startKey){
            if(line.contains(key)){
                return true;
            }
        }
        return false;
    }

    public List<String> findAllTransactions(){
        //Instant startNetcool = Instant.now();
        Map<Integer,String> netcool = findAllNetcoolRequests();
        //Instant endNetcool = Instant.now();
        //System.out.println("Run netcool in " + Duration.between(startNetcool, endNetcool).toMillis());

        //Instant startWs = Instant.now();
        Map<Integer,String> wsRequests = findAllWebServiceRequests();
        //Instant endWs = Instant.now();
        //System.out.println("Run ws request in " + Duration.between(startWs, endWs).toMillis());

        //Instant startWsR = Instant.now();
        Map<Integer,String> wsResponses = findAllWebServiceResponses();

        //Instant endWsR = Instant.now();
        //System.out.println("Run ws response in " + Duration.between(startWsR,endWsR).toMillis());

        Map<Integer,String> all = new HashMap<>();
        all.putAll(netcool);
        all.putAll(wsRequests);
        all.putAll(wsResponses);

        return sortedTransactions(all);
    }

    private List<String> sortedTransactions(Map<Integer,String> transactions){
        List<Integer> keyList = new ArrayList<>(transactions.keySet());
        Collections.sort(keyList);
        List<String> sortedTransactions = new ArrayList<>();
        for(Integer key:keyList){
            sortedTransactions.add(transactions.get(key));
        }
        return sortedTransactions;
    }
}
