package com.ibm.integrationservices;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public void startFromClasspath(String file) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(file);
        List<String> lines = new TdiFileReader().open(inputStream);

        List<String> transactions = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if(line.contains("XPath command:")){ //start of a new request
                // Parsing the command
                TdiTransaction transaction = new TdiTransaction(extractCommand(line));
                // Searching for Netcool request
                int j = i;
                boolean netcoolRequestFound = false;
                while(!netcoolRequestFound){
                    String netcoolSearchLine = lines.get(j++);
                    if(netcoolSearchLine.contains("Input XML")){

                    }
                }

            }
        }
                //new TransactionReader(lines).findAllTransactions();

        System.out.println("Writing transactions...");
        Files.write(Paths.get("c:/temp/logs/phhs.txt"),transactions);
        System.out.println("Done");
    }

    public String extractCommand(String input){
        int keyIndex = input.indexOf("XPath command:");
        int commandIndex = keyIndex + "XPath command:".length();
        return input.substring(commandIndex).trim();
    }

    public static void main(String[] args) {
        try {
            new Application().startFromClasspath("/phhs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
