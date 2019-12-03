package com.ibm.integrationservices;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TransactionReaderTest {
    @Test
    public void shouldReturnUpdateCallbackResponse() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/acc.log");
        List<String> lines = new TdiFileReader().open(inputStream);
        assertTrue(lines.size() > 0);

        TransactionReader transactionReader = new TransactionReader(lines);
        TransactionManager transactionManager = new TransactionManager(transactionReader.readTransactions());
        List<TdiTransaction> uCallbackList = transactionManager.findByCommand("UPDATE_CALLBACK");
        for(TdiTransaction transaction: uCallbackList) {
            String text = transaction.getSoapResponse().getText();
            if (text.toLowerCase().contains("callback query")) {
                fail("contains callback query in text");
            }
        }
        assertTrue(true);
    }
}
