package com.ibm.integrationservices;

import org.junit.Before;
import org.junit.Test;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionReaderTest {
    private List<TdiTransaction> transactions;
    private TransactionManager manager;

    @Before
    public void loadTransactions() throws IOException {
        if (manager == null) {
            manager = new TransactionManager(
                        new TransactionReader(
                            new TdiFileReader()
                                    .open(getClass().getResourceAsStream("/bbd.log")))
                                    .readTransactions());
        }
    }

    @Test
    public void updateCallbackTest() {
        List<TdiTransaction> uCallbackList = manager.findByCommand("UPDATE_CALLBACK");

        for (TdiTransaction transaction : uCallbackList) {
            //Testing inputXMl
            String inputXmlText = transaction.getNetcoolRequest().getText();
            assertTrue(inputXmlText.contains("<exec name=\"UPDATE_CALLBACK\">"));

            String soapRequestText = transaction.getSoapRequest().getText();
            assertTrue(soapRequestText.contains("<soapenv:Envelope"));


            String soapResponseText = transaction.getSoapResponse().getText();
            assertFalse(soapResponseText.toLowerCase().contains("callback query"));

            String netcoolResponseText = transaction.getNetcoolResponse().getText();
            assertFalse(netcoolResponseText.toLowerCase().contains("<row>") ||
                    netcoolResponseText.toLowerCase().contains("id=\"callback\""));
        }
    }
}
