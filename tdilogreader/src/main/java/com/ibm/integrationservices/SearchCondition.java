package com.ibm.integrationservices;

public class SearchCondition {

    private SearchExpression[] expressions;

    public SearchCondition(SearchExpression... expressions){
        this.expressions = expressions;
    }

    public boolean isValid(String source) {
        for(SearchExpression expression: expressions){
            if(!expression.match(source)){
                return false;
            }
        }
        return true;
    }
}
