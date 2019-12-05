package com.ibm.integrationservices;

import java.util.ArrayList;
import java.util.List;

public class SearchCondition {

    private SearchExpression[] expressions;

    public SearchCondition(SearchExpression... expressions){
        this.expressions = expressions;
    }

//    public boolean match(String line){
//        boolean firstKeyMatch = false;
//        boolean secondKeyMatch = false;
//
//        for(String key: firstKey){
//            if(line.toLowerCase().contains(key.toLowerCase())){
//                firstKeyMatch = true;
//                break;
//            }
//        }
//
//        if(secondKey != null) {
//            for (String key : secondKey) {
//                if (line.toLowerCase().contains(key.toLowerCase())) {
//                    secondKeyMatch = true;
//                    break;
//                }
//            }
//        }
//
//        if("and".equals(operator)){
//            return (firstKeyMatch && secondKeyMatch) && matchEventId(line);
//        }
//        if("or".equals(operator)){
//            return (firstKeyMatch || secondKeyMatch) && matchEventId(line);
//        }
//        if("not".equals(operator)){
//            return (firstKeyMatch && !secondKeyMatch) && matchEventId(line);
//        }
//        if(!firstKey.isEmpty() && secondKey==null && operator == null){
//            return firstKeyMatch && matchEventId(line);
//        }
//        return false;
//    }
//
//    private boolean matchEventId(String line){
//        if(netcoolEventId == null || line.contains("SOAP-ENV:Fault")) return true;
//        String parts[] = line.split("\\s+");
//        return parts[4].toLowerCase().contains(netcoolEventId.toLowerCase());
//    }
//
//    public List<String> getFirstKey() {
//        return firstKey;
//    }
//
//    public List<String> getSecondKey() {
//        return secondKey;
//    }
//
//    public String getOperator() {
//        return operator;
//    }

    public boolean isValid(String source) {
        for(SearchExpression expression: expressions){
            if(!expression.match(source)){
                return false;
            }
        }
        return true;
    }
}
