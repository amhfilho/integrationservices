package com.ibm.integrationservices;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TdiEventTest {
    @Test
    public void testCreation(){
        String text = "2019-11-25 14:00:04,530 INFO  - ::CATR0S0ITDP:67189800:acc ifUpdateCallback Transform " +
                "UpdateCallbackResponseXSL: Initial UpdateCallback Response: <?xml version='1.0' encoding='UTF-8'?>" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<SOAP-ENV:Body><insertResponse xmlns=\"http://www.service-now.com/incident\"><sys_id>" +
                "f450a8831b800cd0f3c4866ecc4bcb99</sys_id><table>incident</table><display_name>number</display_name>" +
                "<display_value>INC0736520</display_value><status>error</status><error_message>Operation against file " +
                "'incident' was aborted by Business Rule 'Transform synchronously^4d4f378a1b55c8105c080f23cc4bcb9b'. " +
                "Business Rule Stack:Transform synchronously,DL - Enfore security on closed incidents</error_message>" +
                "</insertResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        TdiEvent event = TdiEvent.createFromText(null, 1, text);
        String expectedPayload = "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:SOAP-ENV=" +
                "\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><SOAP-ENV:Body><insertResponse " +
                "xmlns=\"http://www.service-now.com/incident\"><sys_id>f450a8831b800cd0f3c4866ecc4bcb99</sys_id><table>" +
                "incident</table><display_name>number</display_name><display_value>INC0736520</display_value><status" +
                ">error</status><error_message>Operation against file 'incident' was aborted by Business Rule " +
                "'Transform synchronously^4d4f378a1b55c8105c080f23cc4bcb9b'. Business Rule Stack:Transform synchronously" +
                ",DL - Enfore security on closed incidents</error_message></insertResponse></SOAP-ENV:Body>" +
                "</SOAP-ENV:Envelope>";
        assertEquals(expectedPayload, event.getMessagePayload());

        LocalDateTime expectedDateTime = LocalDateTime.of(2019, 11, 25,14, 00, 04);
        assertEquals(expectedDateTime.getYear(), event.getDateTime().getYear());
        assertEquals(expectedDateTime.getMonth(), event.getDateTime().getMonth());
        assertEquals(expectedDateTime.getDayOfMonth(), event.getDateTime().getDayOfMonth());
        assertEquals(expectedDateTime.getHour(), event.getDateTime().getHour());
        assertEquals(expectedDateTime.getMinute(), event.getDateTime().getMinute());
        assertEquals(expectedDateTime.getSecond(), event.getDateTime().getSecond());

    }
}
