package com.ibm.integrationservices;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LineReaderTest {

    @Test
    public void shouldHaveCommand(){
        String line = "2018-12-04 06:11:46,566 INFO  [AssemblyLines/ProcessAlertWS.AlertHTTPPost.2595] - CHB::USRD0P0CHBP:1985415:chb XPath command: CREATE_PROBLEM";
        LineReader reader = new LineReader(line);
        assertTrue(reader.hasCommand());
    }

    @Test
    public void shouldNotHaveCommand(){
        String line = "TEST LINE";
        assertFalse(new LineReader(line).hasCommand());
    }

    @Test
    public void shouldReturnCommand(){
        String line = "2018-12-04 06:11:46,566 INFO  [AssemblyLines/ProcessAlertWS.AlertHTTPPost.2595] - CHB::USRD0P0CHBP:1985415:chb XPath command: CREATE_PROBLEM";
        LineReader reader = new LineReader(line);
        String command = reader.readCommand();
        assertEquals("CREATE_PROBLEM", command);
    }


    @Test
    public void shouldReturnEventId(){
        String line = "2018-12-04 06:11:46,566 INFO  [AssemblyLines/ProcessAlertWS.AlertHTTPPost.2595] - CHB::USRD0P0CHBP:1985415:chb ifMoreData initialize: XPath EXT_PROB_ID: USRD0P0CHBP:1985415:chb";
        LineReader reader = new LineReader(line);
        String eventId = reader.readEventId();
        assertEquals("USRD0P0CHBP:1985415:chb",eventId);
    }

    @Test
    public void shouldReturnDateTime(){
        String line = "2018-12-04 06:11:46,566 INFO  [AssemblyLines/ProcessAlertWS.AlertHTTPPost.2595] - CHB::USRD0P0CHBP:1985415:chb XPath command: CREATE_PROBLEM";
        LocalDateTime dateTime = new LineReader(line).readDateTime();
        assertEquals(2018, dateTime.getYear());
    }
}
