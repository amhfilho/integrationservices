package com.ibm.integrationservices;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Reads all transactions from logfile
 */
public class TransactionReader {
    private List<String> lines;

    public TransactionReader(List<String> lines) {
        this.lines = lines;
    }
    private SearchCondition soapEndSearchCondition =
            new SearchCondition(new SearchExpression("</SOAP-ENV:Envelope>", "</soapenv:Envelope>"));

    public List<TdiTransaction> readTransactions() throws IOException {
        List<TdiTransaction> transactions = new ArrayList<>();
        int linesTotal = this.lines.size();
        int linesProcessed = 0;
        for (int i = 0; i < linesTotal; i++) {
            String line = this.lines.get(i);
            if (line.contains("XPath command:")) { //start of a new request
                TdiTransaction transaction = new TdiTransaction(i, extractCommand(line));
                String netcoolEventId = extractEventId(line, transaction.getCommand());
                LocalDateTime dateTime = extractDateTime(line);
                TdiEvent inputXml = extractMessage(
                        i,
                        new SearchCondition(new SearchExpression("Input XML")),
                        new SearchCondition(new SearchExpression("</command>")));


                TdiEvent getKeysRequest = extractGetKeysRequestEvent(i, transaction);
                TdiEvent getKeysResponse = extractGetKeysResponseEvent(i, transaction);
                //TdiEvent soapRequest = extractSoapRequestEvent(i, transaction);
                TdiEvent soapResponse = extractSoapResponseEvent(i, transaction);


                SearchCondition requestSearchCondition = null;
                SearchCondition responseSearchCondition = null;
                SearchCondition netcoolResponseSearchCondition = null;

                if(!transaction.getCommand().equals("CALLBACK")) {
                    requestSearchCondition = new SearchCondition(
                            new SearchExpression("soap request:", "UpdateCallbackXML"),
                            new SearchExpression("TicketHandle", "Callback Query").not());
                    responseSearchCondition = new SearchCondition(
                            new SearchExpression("soap response:", "UpdateCallback Response:","SOAP-ENV:Fault"),
                            new SearchExpression("TicketHandle", "Callback Query").not());
                    netcoolResponseSearchCondition = new SearchCondition(
                            new SearchExpression("<result"),
                            new SearchExpression("id=\"CALLBACK\"").not()
                    );

                } else {
                    requestSearchCondition = new SearchCondition(
                            new SearchExpression("soap request:", "UpdateCallbackXML"),
                            new SearchExpression("TicketHandle").not());
                    responseSearchCondition = new SearchCondition(
                            new SearchExpression("soap response:","SOAP-ENV:Fault"),
                            new SearchExpression("TicketHandle").not());
                    netcoolResponseSearchCondition = new SearchCondition(
                            new SearchExpression("<result"));
                }



//                TdiEvent soapRequest = extractMessage(
//                        i,
//                        requestSearchCondition,
//                        soapEndSearchCondition);
//                TdiEvent soapResponse = extractMessage(
//                        i,
//                        responseSearchCondition,
//                        soapEndSearchCondition);
                TdiEvent netcoolResponse = extractMessage(
                        i,
                        netcoolResponseSearchCondition,
                        new SearchCondition(new SearchExpression("</result>")));

                transaction.setRequestTime(dateTime);
                transaction.setNetcoolEvent(netcoolEventId);
                transaction.setNetcoolRequest(inputXml);
                //transaction.setSoapRequest(soapRequest);
                transaction.setSoapResponse(soapResponse);
                transaction.setNetcoolResponse(netcoolResponse);
                transaction.setGetKeysRequest(getKeysRequest);
                transaction.setGetKeysResponse(getKeysResponse);

                transactions.add(transaction);
            }

            linesProcessed++;
            System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            System.out.print("Completed: " + ((100*linesProcessed)/linesTotal) + "%");
        }
        System.out.println();
        return transactions;
    }

    private TdiEvent extractSoapResponseEvent(int i, TdiTransaction transaction) {
        SearchCondition start = null;
        if(transaction.getCommand().equals("CREATE_PROBLEM")){
            start = new SearchCondition(
                    new SearchExpression("INFO"),
                    new SearchExpression(transaction.getNetcoolEvent()),
                    new SearchExpression("transformedCreateXML")
            );
        }
        return extractMessage(i, start, soapEndSearchCondition);
    }


    private LocalDateTime extractDateTime(String line) {
        String strings[] = line.split("\\s+");
        return LocalDateTime.parse(strings[0] + " " + strings[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    }

    private String extractEventId(String line, String command) {
        if (!command.equals("CALLBACK")) {
            String[] strings = line.split("\\s+");
            String tmp = strings[4].replaceAll("\'", "");
            int index = tmp.indexOf("::") + 2;
            return (index > -1 ? tmp.substring(index) : tmp);
        }
        return "";
    }

    public String extractCommand(String input) {
        int keyIndex = input.indexOf("XPath command:");
        int commandIndex = keyIndex + "XPath command:".length();
        return input.substring(commandIndex).trim();
    }

    private TdiEvent extractGetKeysRequestEvent(int cursor, TdiTransaction transaction){
        if(transaction.isUpdateOperation()){
            return extractGetKeysEvent(cursor, "request");
        }
        return null;
    }

    private TdiEvent extractGetKeysResponseEvent(int cursor, TdiTransaction transaction){
        if(transaction.isUpdateOperation()){
            return extractGetKeysEvent(cursor, "response");
        }
        return null;
    }

    private TdiEvent extractGetKeysEvent(int cursor, String requestOrResponse){
            return extractMessage(
                    cursor,
                    new SearchCondition(
                            new SearchExpression("TicketHandle"),
                            new SearchExpression("soap "+ requestOrResponse)),
                    soapEndSearchCondition
            );
    }

    private TdiEvent extractMessage(int cursor, SearchCondition start, SearchCondition end) {
        boolean finishPart = false;
        String part = "";
        int aux = cursor + 1;
        boolean partFound = false;
        int lineFound = 0;
        while (!finishPart && aux < lines.size()) {
            String actualLine = lines.get(aux);
            if (start.isValid(actualLine)) {
                partFound = true;
                lineFound = aux;
                part += actualLine;
                if (end.isValid(part)) {
                    break;
                }
            } else if (partFound) {
                part += lines.get(aux);
                if (end.isValid(lines.get(aux))) {
                    finishPart = true;
                    partFound = false;
                }
            }
            aux++;
        }
        return TdiEvent.createFromText(null, lineFound, part);
    }
}
