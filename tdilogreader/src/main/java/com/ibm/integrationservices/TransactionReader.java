package com.ibm.integrationservices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        return findAllRequests("SOAP request:", "</soapenv:Envelope>");
    }

    private Map<Integer, String> findAllWebServiceResponses(){
        return findAllRequests("SOAP response:", "</soapenv:Envelope>");
    }

    private Map<Integer,String> findAllNetcoolRequests(){
        return findAllRequests("Input XML", "</command>");
    }

    private Map<Integer,String> findAllRequests(String startKey, String endKey){
        Map<Integer,String> requestMap = new HashMap<>();
        boolean requestFound = false;
        String request = "";
        int lineNumber = 0;
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if(line.contains(startKey)){
                lineNumber = i;
                request = line;
                requestFound = true;
                if(line.contains(endKey)){
                    requestFound = false;
                    requestMap.put(lineNumber,request);
                    request = "";
                }
            }
            else if(requestFound){
                request += line;
                if(line.contains(endKey)){
                    requestFound = false;
                    requestMap.put(lineNumber,request);
                    request = "";
                }
            }
        }
        return requestMap;
    }

    public List<String> findAllTransactions(){
        Map<Integer,String> netcool = findAllNetcoolRequests();
        Map<Integer,String> wsRequests = findAllWebServiceRequests();
        Map<Integer,String> wsResponses = findAllWebServiceResponses();
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

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
        List<String> transactions =
            new TransactionReader(TdiFileReader.open("C:\\temp\\chubb.prd.2018-12-04.log")
                .lines())
                .findAllTransactions();

        Files.write(Paths.get("c:/temp/chubb.txt"),transactions);

        for (String t:transactions){
            String command = "CALLBACK";
            if(t.contains("InputXML") || t.contains("SOAP")){
                if(t.contains("CREATE_PROBLEM")){
                    command = "CREATE_PROBLEM";
                }
                if(t.contains("UPDATE_PROBLEM")){
                    command = "UPDATE_PROBLEM";
                }
                if(t.contains("UPDATE_CALLBACK")){
                    command = "UPDATE_CALLBACK";
                }
            }
            LineExtractor extractor = new LineExtractor(t);
            TdiTransaction transaction =
                    new TdiTransaction(command, extractor.extractDate())
                            .payload(extractor.extractXml());
        }

        long time = System.currentTimeMillis() - start;
        System.out.println(String.format("Run in %s milliseconds",time));

    }
}
