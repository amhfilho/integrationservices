package com.ibm.integrationservices;

import java.util.ArrayList;
import java.util.List;

public class TransactionStatistics {
    private List<TdiTransaction> transactions;

    public TransactionStatistics(List<TdiTransaction> transactions) {
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


}
