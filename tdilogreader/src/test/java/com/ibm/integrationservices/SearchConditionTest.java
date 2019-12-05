package com.ibm.integrationservices;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchConditionTest {

    @Test
    public void containsSoapResponseOrSOAPENVFaultAndNotContainsTicketHandleAndNotContainsCallbackQuery(){
        String validSource1 = "blabla soap response: bla bla";
        String validSource2 = "blabla SOAP-ENV:Fault";
        String validSource3 = "blablablaSOAP-ENV:Faultsoap response:blablabla";
        String invalidSource1 = "blabla soap response: Callback Query";
        String invalidSource2 = "blabla soap response: Ticket Handle";
        String invalidSource3 = "blabla soap response: Callback Query Ticket Handle";
        SearchExpression e1 = new SearchExpression("soap response:", "SOAP-ENV:Fault");
        SearchExpression e2 = new SearchExpression("Ticket Handle").not();
        SearchExpression e3 = new SearchExpression("Callback Query").not();
        SearchCondition searchCondition = new SearchCondition(e1,e2,e3);
        assertTrue(searchCondition.isValid(validSource1));
        assertTrue(searchCondition.isValid(validSource2));
        assertTrue(searchCondition.isValid(validSource3));
        assertFalse(searchCondition.isValid(invalidSource1));
        assertFalse(searchCondition.isValid(invalidSource2));
        assertFalse(searchCondition.isValid(invalidSource3));


    }
}
