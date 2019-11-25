package com.ibm.integrationservices;

public class SearchCondition {
    private String firstKey;
    private String secondKey;
    private String operator;

    public SearchCondition(String firstKey, String secondKey, String operator) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.operator = operator;
    }

    public SearchCondition(String firstKey) {
        this.firstKey = firstKey;
    }

    public boolean match(String line){
        if("and".equals(operator)){
            return line.toLowerCase().contains(firstKey.toLowerCase()) &&
                    line.toLowerCase().contains(secondKey.toLowerCase());
        }
        if("or".equals(operator)){
            return line.toLowerCase().contains(firstKey.toLowerCase()) ||
                    line.toLowerCase().contains(secondKey.toLowerCase());
        }
        if("not".equals(operator)){
            return line.toLowerCase().contains(firstKey.toLowerCase()) &&
                    !line.toLowerCase().contains(secondKey.toLowerCase());
        }
        if(!firstKey.isEmpty() && secondKey==null && operator == null){
            return line.toLowerCase().contains(firstKey.toLowerCase());
        }
        return false;
    }

    public String getFirstKey() {
        return firstKey;
    }

    public String getSecondKey() {
        return secondKey;
    }

    public String getOperator() {
        return operator;
    }
}
