package com.ibm.integrationservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reads requests from Netcool inside logfile
 */
public class NetcoolReader {
    private List<String> lines;

    public NetcoolReader(List<String> lines) {
        this.lines = lines;
    }

    public Map<Integer, String> findAllWebServiceRequests(){
        return findAllRequests("SOAP request:", "</soapenv:Envelope>");
    }

    public Map<Integer, String> findAllWebServiceResponses(){
        return findAllRequests("SOAP response:", "</soapenv:Envelope>");
    }

    public Map<Integer,String> findAllNetcoolRequests(){
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

    public Map<Integer,String> findAllTransactions(){
        Map<Integer,String> netcool = findAllNetcoolRequests();
        Map<Integer,String> wsRequests = findAllWebServiceRequests();
        Map<Integer,String> wsResponses = findAllWebServiceResponses();
        Map<Integer,String> all = new HashMap<>();
        all.putAll(netcool);
        all.putAll(wsRequests);
        all.putAll(wsResponses);
        return all;
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        new NetcoolReader(TdiFileReader.open("C:\\Users\\AntonioMarioHenrique\\Desktop\\temp\\chubb.prd.2018-12-04.log")
                .lines())
                .findAllTransactions()
                .entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        long time = System.currentTimeMillis() - start;
        System.out.println(String.format("Run in %s milliseconds",time));

    }
}
