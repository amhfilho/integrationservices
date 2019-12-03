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

            TransactionManager manager = new TransactionManager(transactions);
            System.out.println(manager.statisticsReport());

            String command = "";
            while(!command.equalsIgnoreCase("exit")){
                System.out.print("enter command>");
                Scanner scanner = new Scanner(System.in);
                command = scanner.nextLine().trim().toLowerCase();
                String[] token = command.split("\\s+");

                switch(token[0]){
                    case "stats":
                        System.out.println(manager.statisticsReport());
                        break;
                    case "callback":
                        manager.findByCommand("CALLBACK").forEach(System.out::println);
                        break;
                    case "create":
                        manager.findByCommand("CREATE_PROBLEM").forEach(System.out::println);
                        break;
                    case "update":
                        manager.findByCommand("UPDATE_PROBLEM").forEach(System.out::println);
                        break;
                    case "ucallback":
                        manager.findByCommand("UPDATE_CALLBACK").forEach(System.out::println);
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

    private static void usage(){
        System.err.println("Usage: tdilogreader <file>");
    }

    private static void help() {
        System.out.println("Help of available commands: \n" +
                "\tstats                      Displays totals of transactions by type\n" +
                "\tcallback                   List of all CALLBACK transactions\n" +
                "\tcreate                     List of all CREATE transactions\n" +
                "\tupdate                     List of all UPDATE_PROBLEM transactions\n" +
                "\tucallback                  List of all UPDATE_CALLBACK transactions\n" +
                "\tevent <netcool event id>   List of all transactions with given event id\n" +
                "\texit                       Exits program");
    }
}
