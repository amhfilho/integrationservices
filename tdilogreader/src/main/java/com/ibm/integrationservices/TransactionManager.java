package com.ibm.integrationservices;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<TdiTransaction> transactions;

    public TransactionManager(List<TdiTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<TdiTransaction> findByCommand(String command){
        List<TdiTransaction> result = new ArrayList<>();
        for(TdiTransaction transaction: transactions){
            if(transaction.getCommand().equals(command)){
                result.add(transaction);
            }
        }
        return result;
    }

    public String statisticsReport(){
        return  String.format(
                "Total number of transactions: %s\n" +
                "Total number of CALLBACK: %s\n" +
                "Total number of CREATE_PROBLEM: %s\n" +
                "Total number of UPDATE_PROBLEM: %s\n" +
                "Total number of UPDATE_CALLBACK: %s\n",
                transactions.size(),
                findByCommand("CALLBACK").size(),
                findByCommand("CREATE_PROBLEM").size(),
                findByCommand("UPDATE_PROBLEM").size(),
                findByCommand("UPDATE_CALLBACK").size()
        );
    }

    public List<TdiTransaction> findByNetcoolEvent(String netcoolEvent){
        List<TdiTransaction> result = new ArrayList<>();
        for(TdiTransaction transaction: transactions){
            if(!transaction.getCommand().equals("CALLBACK")){
                if(transaction.getNetcoolEvent().toLowerCase().contains(netcoolEvent.toLowerCase())){
                    result.add(transaction);
                }
            }
        }
        return result;
    }


}
