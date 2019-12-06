package com.ibm.integrationservices;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static String[] token;

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

            TransactionManager manager = new TransactionManager(transactions);
            System.out.println(manager.statisticsReport());

            String command = "";
            while(!command.equalsIgnoreCase("exit")){
                System.out.print("enter command>");
                Scanner scanner = new Scanner(System.in);
                command = scanner.nextLine().trim().toLowerCase();
                token = command.split("\\s+");

                switch(token[0]){
                    case "stats":
                        System.out.println(manager.statisticsReport());
                        break;
                    case "callback":
                        processCommand(manager, "CALLBACK");
                        break;
                    case "create":
                        processCommand(manager, "CREATE_PROBLEM");
                        break;
                    case "update":
                        processCommand(manager, "UPDATE_PROBLEM");
                        break;
                    case "ucallback":
                        processCommand(manager, "UPDATE_CALLBACK");
                        break;
                    case "event":
                        if(token.length < 2) {
                            System.err.println("Invalid command. Usage: event <netcool event id>");
                            break;
                        }
                        List<TdiTransaction> transactionList = manager.findByNetcoolEvent(token[1]);
                        if(transactionList.size() == 0){
                            System.out.println("Not found");
                        }
                        transactionList.forEach(System.out::println);
                        break;
                    case "help":
                        help();
                        break;
                    case "exit":
                        System.exit(0);
                    default:
                        System.out.println("Invalid command");
                        help();
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processCommand(TransactionManager manager, String command){
        if(token.length == 1) {
            manager.findByCommand(command).forEach(System.out::println);
        } else if(token.length == 2) {
            String fileName = token[1];
            writeFile(manager.findByCommand(command), fileName);
        } else {
            System.out.println("Invalid command. Type help for a list of available commmands");
        }
    }

    private static void writeFile(List<TdiTransaction> transactions, String fileName){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)))){
            for(TdiTransaction t: transactions){
                writer.write(t.toString());
                writer.newLine();
            }
            writer.flush();
            System.out.println("File saved successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void usage(){
        System.err.println("Usage: tdilogreader <file>");
    }

    private static void help() {
        System.out.println("Help of available commands: \n" +
                "\tstats                      Displays totals of transactions by type\n" +
                "\tcallback  [<file>]         List of all CALLBACK transactions. Saves content to a file if required \n" +
                "\tcreate    [<file>]         List of all CREATE transactions. Saves content to a file if required\n" +
                "\tupdate    [<file>]         List of all UPDATE_PROBLEM transactions. Saves content to a file if required\n" +
                "\tucallback [<file>]         List of all UPDATE_CALLBACK transactions. Saves content to a file if required\n" +
                "\tevent <netcool event id>   List of all transactions with given event id\n" +
                "\texit                       Exit app");
    }
}
