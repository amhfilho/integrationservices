package com.ibm.integrationservices;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionReaderTest {
    private static TransactionManager manager;

    @BeforeClass
    public static void loadTransactions() throws IOException {
        if (manager == null) {
            System.out.println("Loading transactions...");
            manager = new TransactionManager(
                        new TransactionReader(
                            new TdiFileReader()
                                    .open(TransactionReaderTest.class.getResourceAsStream("/bbd.log")))
                                    .readTransactions());
            System.out.println("Done");
        }
    }

    @Test
    public void testEventId() {
        String eventId = "USDL0P0BBDP:3335781:BOD";
        List<TdiTransaction> transactions = manager.findByNetcoolEvent(eventId);
        assertTrue(transactions.size() > 0);
    }

    @Test
    public void createProblemTest(){
        eventsTest("CREATE_PROBLEM");
    }

    @Test
    public void updateCallbackTest() {
        eventsTest("UPDATE_CALLBACK");
    }

    @Test
    public void updateProblemTest() {
        eventsTest("UPDATE_PROBLEM");
    }

    @Test
    public void callbackTest() {
        List<TdiTransaction> callbackList = manager.findByCommand("CALLBACK");

        for(TdiTransaction transaction : callbackList) {
            String inputXmlText = transaction.getNetcoolRequest().getText();
            assertTrue(inputXmlText.contains(String.format("<exec name=\"%s\">","CALLBACK")));

            String soapRequestText = transaction.getSoapRequest().getText();
            assertTrue(soapRequestText.contains("<soapenv:Envelope"));

            String netcoolResponseText = transaction.getNetcoolResponse().getText();
            assertTrue(netcoolResponseText.toLowerCase().contains("id=\"callback\""));

        }
    }

    public void eventsTest(String command){
        List<TdiTransaction> uCallbackList = manager.findByCommand(command);

        for (TdiTransaction transaction : uCallbackList) {
            //Testing inputXMl
            String inputXmlText = transaction.getNetcoolRequest().getText();
            assertTrue(inputXmlText.contains(String.format("<exec name=\"%s\">",command)));

            String soapRequestText = transaction.getSoapRequest().getText();
            assertTrue(soapRequestText.contains("<soapenv:Envelope"));
            assertTrue(soapRequestText.contains(transaction.getNetcoolEvent()));


            String soapResponseText = transaction.getSoapResponse().getText();
            assertFalse(soapResponseText.toLowerCase().contains("callback query"));
            assertTrue(soapResponseText.contains(transaction.getNetcoolEvent()) ||
                              soapResponseText.toLowerCase().contains("SOAP-ENV:Fault"));

            String netcoolResponseText = transaction.getNetcoolResponse().getText();
            assertFalse(netcoolResponseText.toLowerCase().contains("<row>") ||
                    netcoolResponseText.toLowerCase().contains("id=\"callback\""));
            assertTrue(netcoolResponseText.contains(transaction.getNetcoolEvent()));
        }
    }
}
