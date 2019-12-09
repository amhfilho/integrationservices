package com.ibm.integrationservices;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
                transaction.setNetcoolEvent(netcoolEventId);
                LocalDateTime dateTime = extractDateTime(line);
                TdiEvent inputXml = extractMessage(
                        i,
                        new SearchCondition(new SearchExpression("Input XML")),
                        new SearchCondition(new SearchExpression("</command>")));


                TdiEvent getKeysRequest = extractGetKeysRequestEvent(i, transaction);
                TdiEvent getKeysResponse = extractGetKeysResponseEvent(i, transaction);
                TdiEvent soapRequest = extractSoapRequestEvent(i, transaction);
                TdiEvent soapResponse = extractSoapResponseEvent(i, transaction);
                TdiEvent netcoolResponse = extractNetcoolResponseEvent(i, transaction);


                transaction.setRequestTime(dateTime);
                transaction.setNetcoolEvent(netcoolEventId);
                transaction.setNetcoolRequest(inputXml);
                transaction.setSoapRequest(soapRequest);
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

    private TdiEvent extractNetcoolResponseEvent(int i, TdiTransaction transaction) {
        SearchCondition start = new SearchCondition(
                new SearchExpression("<result"),
                new SearchExpression(transaction.getNetcoolEvent()),
                new SearchExpression("id=\"CALLBACK\"").not()
        );
        if(transaction.isCallback()){
            start = new SearchCondition(new SearchExpression("<result"),
                    new SearchExpression("id=\"CALLBACK\""));
        }
        return extractMessage(i,start, new SearchCondition(new SearchExpression("</result>")));
    }

    private TdiEvent extractSoapRequestEvent(int i, TdiTransaction transaction) {
        SearchCondition start = null;
        if(transaction.isCreateProblem()){
            start = new SearchCondition(
                    new SearchExpression("INFO"),
                    new SearchExpression(transaction.getNetcoolEvent()),
                    new SearchExpression("transformedCreateXML","SOAP-ENV:Fault")
            );
        }
        if(transaction.isUpdateProblem()){
            start = new SearchCondition(
                    new SearchExpression("INFO"),
                    new SearchExpression(transaction.getNetcoolEvent()),
                    new SearchExpression("Transform UpdateXML"),
                    new SearchExpression("TicketHandle", "Callback Query").not()
            );
        }
        if(transaction.isUpdateCallback()) {
            start = new SearchCondition(
                    new SearchExpression("INFO"),
                    new SearchExpression(transaction.getNetcoolEvent()),
                    new SearchExpression("transformedUpdateCallbackXML", "UpdateCallbackXML"),
                    new SearchExpression("TicketHandle", "Callback Query").not()
            );
        }
        if(transaction.isCallback()){
            start = new SearchCondition(
                    new SearchExpression("INFO"),
                    new SearchExpression("updatedCallbackXML")
            );
        }
        return extractMessage(i, start, soapEndSearchCondition);
    }

    private TdiEvent extractSoapResponseEvent(int i, TdiTransaction transaction) {
        SearchCondition start = null;
        if(transaction.isCreateProblem()){
            start = new SearchCondition(
                    new SearchExpression("CreateResponseXSL: Initial Create Response:"),
                    new SearchExpression(transaction.getNetcoolEvent(),"SOAP-ENV:Fault")
            );
        }
        if(transaction.isUpdateProblem()) {
            start = new SearchCondition(
                    new SearchExpression("UpdateResponseXSL"),
                    new SearchExpression(transaction.getNetcoolEvent(),"SOAP-ENV:Fault")
            );
        }
        if(transaction.isUpdateCallback()){
            start = new SearchCondition(
                    new SearchExpression("UpdateCallbackResponseXSL"),
                    new SearchExpression(transaction.getNetcoolEvent(),"SOAP-ENV:Fault")
            );
        }
        if(transaction.isCallback()) {
            start = new SearchCondition(
                    new SearchExpression("[Callback Query]"),
                    new SearchExpression("SOAP response:")
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
