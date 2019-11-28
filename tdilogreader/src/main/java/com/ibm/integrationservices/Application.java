package com.ibm.integrationservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        if(args.length < 1){
            usage();
            System.exit(0);
        }

        try {
            FileInputStream inputStream = new FileInputStream(new File(args[0]));
            List<String> lines = new TdiFileReader().open(inputStream);

            System.out.println("Reading log file " + args[0]);
            System.out.println("Total lines: " + lines.size() + ". Please wait...");

            TransactionReader reader = new TransactionReader(lines);
            List<TdiTransaction> transactions = reader.readTransactions();

            String command = "";
            while(!command.equalsIgnoreCase("exit")){
                System.out.print("enter command>");
                Scanner scanner = new Scanner(System.in);
                command = scanner.nextLine().toLowerCase();

                switch(command){
                    case "stats":
                        System.out.println(new TransactionStatistics(transactions).statisticsReport());
                        break;
                    default:
                        usage();
                        break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void usage(){
        System.err.println("Usage: tdilogreader <file>");
    }
}
