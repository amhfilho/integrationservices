package com.ibm.integrationservices;

import java.io.IOException;
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

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
        System.out.println("Reading log file...");
        List<String> transactions =
            new TransactionReader(TdiFileReader.open("C:\\temp\\logs\\acc.log")
                .lines())
                .findAllTransactions();

        System.out.println("Writing transactions...");
        Files.write(Paths.get("c:/temp/logs/acc.txt"),transactions);

        /*
        List<TdiTransaction> tList = new ArrayList<>();

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
            tList.add(transaction);
        }

        tList.forEach(x->System.out.println(x.getRequestTime() + " - " + x.getCommand() + " - " + x.getPayload()));
*/
        long time = System.currentTimeMillis() - start;
        System.out.println(String.format("Run in %s milliseconds",time));

    }
}
