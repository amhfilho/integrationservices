package com.ibm.integrationservices;

public class SearchExpression {
    private String[] query;
    private String condition;

    public SearchExpression(String... query) {
        this.query = query;
        condition = "CONTAINS";
    }

    public SearchExpression not(){
        condition = "NOT_CONTAINS";
        return this;
    }

    public boolean match(String source){
        boolean match = false;
        for(String entry:query){
            if(source.toLowerCase().contains(entry.toLowerCase())){
                match = true;
                break;
            }
        }
        if(condition.equals("NOT_CONTAINS")){
            return !match;
        }
        return match;
    }
}
