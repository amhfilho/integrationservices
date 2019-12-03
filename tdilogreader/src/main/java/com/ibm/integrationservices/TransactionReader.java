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

    private String currentCommand;

    public List<TdiTransaction> readTransactions() throws IOException {
        List<TdiTransaction> transactions = new ArrayList<>();
        int linesTotal = this.lines.size();
        int linesProcessed = 0;
        for (int i = 0; i < linesTotal; i++) {
            String line = this.lines.get(i);
            if (line.contains("XPath command:")) { //start of a new request
                TdiTransaction transaction = new TdiTransaction(i, extractCommand(line));
                currentCommand = transaction.getCommand();
                String netcoolEventId = extractEventId(line, transaction.getCommand());
                LocalDateTime dateTime = extractDateTime(line);
                TdiEvent inputXml = extractMessage(
                        lines,
                        i,
                        new SearchCondition(Arrays.asList("Input XML")),
                        Arrays.asList("</command>"));
                TdiEvent getKeysRequest = null;
                TdiEvent getKeysResponse = null;
                if (transaction.getCommand().equals("UPDATE_CALLBACK") || transaction.getCommand().equals("UPDATE_PROBLEM")) {
                    getKeysRequest = extractMessage(
                            lines,
                            i,
                            new SearchCondition(
                                    Arrays.asList("TicketHandle"),
                                    Arrays.asList("soap request:"),
                                    "and"),
                            Arrays.asList("</SOAP-ENV:Envelope>", "</soapenv:Envelope>"));
                    getKeysResponse = extractMessage(
                            lines,
                            i,
                            new SearchCondition(
                                    Arrays.asList("TicketHandle"),
                                    Arrays.asList("soap response:"),
                                    "and"),
                            Arrays.asList("</SOAP-ENV:Envelope>", "</soapenv:Envelope>"));
                }

                SearchCondition requestSearchCondition = new SearchCondition(
                        null,
                        Arrays.asList("soap request:", "UpdateCallbackXML"),
                        Arrays.asList("TicketHandle"),
                        "not");
                SearchCondition responseSearchCondition = new SearchCondition(
                        null,
                        Arrays.asList("soap response:", "UpdateCallback Response:", "SOAP-ENV:Fault"),
                        Arrays.asList("TicketHandle"),
                        "not");

                TdiEvent soapRequest = extractMessage(
                        lines,
                        i,
                        requestSearchCondition,
                        Arrays.asList("</SOAP-ENV:Envelope>", "</soapenv:Envelope>"));
                TdiEvent soapResponse = extractMessage(
                        lines,
                        i,
                        responseSearchCondition,
                        Arrays.asList("</SOAP-ENV:Envelope>", "</soapenv:Envelope>"));
                TdiEvent netcoolResponse = extractMessage(
                        lines,
                        i,
                        new SearchCondition(Arrays.asList("<result")),
                        Arrays.asList("</result>"));


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

    private LocalDateTime extractDateTime(String line) {
        String strings[] = line.split(" ");
        return LocalDateTime.parse(strings[0] + " " + strings[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    }

    private String extractEventId(String line, String command) {
        if (!command.equals("CALLBACK")) {
            String[] strings = line.split(" ");
            return strings[5].replaceAll("\'", "").replaceAll("::", "");
        }
        return "";
    }

    public String extractCommand(String input) {
        int keyIndex = input.indexOf("XPath command:");
        int commandIndex = keyIndex + "XPath command:".length();
        return input.substring(commandIndex).trim();
    }

    private boolean containsAnyKey(String line, List<String> startKey) {
        for (String key : startKey) {
            if (line.toLowerCase().contains(key.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private TdiEvent extractMessage(List<String> lines, int cursor, SearchCondition startSearch, List<String> endKey) {
        boolean finishPart = false;
        String part = "";
        int aux = cursor + 1;
        boolean partFound = false;
        int lineFound = 0;
        while (!finishPart && aux < lines.size()) {
            String actualLine = lines.get(aux);
            if (startSearch.match(actualLine)) {
                partFound = true;
                lineFound = aux;
                part += actualLine;
                if (containsAnyKey(part, endKey)) {
                    break;
                }
            } else if (partFound) {
                part += lines.get(aux);
                if (containsAnyKey(lines.get(aux), endKey)) {
                    finishPart = true;
                    partFound = false;
                }
            }
            aux++;
        }
        return TdiEvent.createFromText(null, lineFound, part);
    }
}
