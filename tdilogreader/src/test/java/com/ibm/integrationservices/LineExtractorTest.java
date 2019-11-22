package com.ibm.integrationservices;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class LineExtractorTest {
    @Test
    public void extractInputXmlTest() throws IOException, URISyntaxException {
        String source = loadFile("inputxml.txt");
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><command xmlns:java=\"java\" name=\"multi\"><userID>ENVAUTO</userID><password>w1ldR0se</password><URL>https://170.225.28.217/tdi/servicenow/anthem/prod</URL><exec name=\"CREATE_PROBLEM\"><option name=\"EXT_PROB_ID\"> <value>US980P0WLPP:81084512:wlp</value> </option><option name=\"ITEM\"> <value>WLP:5:DNC_AGP_5AKL7:CONTROL-M:CTM002:DNC_AGP_5AKL7:CTM002</value> </option><option name=\"PR_TICKET\"> <value/> </option><option name=\"account_id\"> <value>wlp</value> </option><option name=\"appl_id\"> <value>CTM002</value> </option><option name=\"category\"> <value>Workload</value> </option><option name=\"ci\"> <value/> </option><option name=\"classification\"> <value/> </option><option name=\"date\"> <value>2019-11-04 02:00:52</value> </option><option name=\"default_group\"> <value>LEXINGTON DSM BATCH</value> </option><option name=\"description\"> <value>Job: DNC_AGP OrderId: 5akl7 System: CTM002 Folder: EHUB_BDS host: bdpr2r4e1pr.wellpoint.com reports: Ended not OK&#13;&#13;TicketGroup: CONSUMER HUB SUPPORT&#13;Date: 2019-11-04 02:00:52\tSeverity: Minor&#13;InstanceId: DNC_AGP_5akl7&#13;InstanceSituation: Ended not OK&#13;ComponentType: Workload&#13;Component: Control-M&#13;SubComponent: CTM002&#13;ApplId: CTM002&#13;MsgId: Source: Control-M&#13;Node: DNC_AGP_5akl7&#13;Manager: CTRL-M: va10plvctm309.wellpoint.com 30.230.128.162&#13;Agent: mttrapd probe on va10puvtos001&#13;AlertKey: DNC_AGP_5akl7&#13;AlertGroup: CTM002&#13;EventKey: US980P0WLPP:81084512:wlp&#13;</value> </option><option name=\"event_class\"> <value>Control-M</value> </option><option name=\"fqhostname\"> <value/> </option><option name=\"hostname\"> <value>DNC_AGP_5akl7</value> </option><option name=\"nco_agent\"> <value>mttrapd probe on va10puvtos001</value> </option><option name=\"nco_alert_group\"> <value>CTM002</value> </option><option name=\"nco_alert_key\"> <value>DNC_AGP_5akl7</value> </option><option name=\"nco_appl_id\"> <value>CTM002</value> </option><option name=\"nco_component\"> <value>Control-M</value> </option><option name=\"nco_component_type\"> <value>Workload</value> </option><option name=\"nco_instance_id\"> <value>DNC_AGP_5akl7</value> </option><option name=\"nco_instance_situation\"> <value>Ended not OK</value> </option><option name=\"nco_instance_value\"> <value/> </option><option name=\"nco_manager\"> <value>CTRL-M: va10plvctm309.wellpoint.com 30.230.128.162</value> </option><option name=\"nco_node\"> <value>DNC_AGP_5akl7</value> </option><option name=\"nco_origin\"> <value/> </option><option name=\"nco_originating_event_id\"> <value>US980P0WLPP:81084512:wlp</value> </option><option name=\"nco_resource_id\"> <value/> </option><option name=\"nco_severity\"> <value>Minor</value> </option><option name=\"nco_src\"> <value>CTRL-M</value> </option><option name=\"nco_sub_component\"> <value>CTM002</value> </option><option name=\"nco_support_group\"> <value>CONSUMER HUB SUPPORT</value> </option><option name=\"origin\"> <value>_</value> </option><option name=\"originating_event_id\"> <value>US980P0WLPP:81084512:wlp</value> </option><option name=\"owner_group\"> <value>CONSUMER HUB SUPPORT</value> </option><option name=\"resource_id\"> <value>DNC_AGP_5akl7</value> </option><option name=\"severity\"> <value>Minor</value> </option><option name=\"source\"> <value/> </option><option name=\"summary\"> <value>DNC_AGP_5akl7: Minor: Job: DNC_AGP OrderId: 5akl7 System: CTM002 Folder: EHUB_BDS host: bdpr2r4e1pr.wellpoint.com reports: Ended not OK Time: 2019-11-04 02:00:52</value> </option><option name=\"support_group\"> <value>CONSUMER HUB SUPPORT</value> </option><option name=\"tec_id\"> <value>va10puvtis001</value> </option><option name=\"ticket_severity\"> <value>5</value> </option><option name=\"ticket_system\"> <value>SN_WLP_PROD_TDI</value> </option></exec></command>";
        assertEquals(expectedResult,new LineExtractor(source).extractXml());
    }

    @Test
    public void extractInputXmlTest2() throws IOException, URISyntaxException {
        String source = loadFile("inputxml2.txt");
        String expectedResult = "<command name=\"http\">\t\t<userID>ENVAUTO</userID>\t\t<password>w1ldR0se</password>\t\t<URL>https://170.225.28.217/tdi/servicenow/chubb/prod</URL>\t\t<exec name=\"CALLBACK\">\t\t\t<option name=\"tec_id\">\t\t\t\t<value preserveCase=\"true\">usrd0ni0p0chbxa</value>\t\t\t</option>\t\t\t<option name=\"ticket_system\">\t\t\t\t<value>SN_CHUBB_PROD_TDI</value>\t\t\t</option>\t\t</exec>\t</command>";
        assertEquals(expectedResult,new LineExtractor(source).extractXml());
    }

    @Test
    public void extractSoapRequestTest() throws IOException, URISyntaxException {
        String source = loadFile("soaprequest.txt");
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:Format=\"java.text.SimpleDateFormat\" xmlns:Date=\"java.util.Date\" xmlns:java=\"java\" xmlns:SQLExit=\"com.ibm.sql.SQLExit\" xmlns:u=\"http://www.service-now.com/u_anthem_envision_integration\"><soapenv:Header/><soapenv:Body><u:getRecords><u:name>CONSUMER HUB SUPPORT</u:name><u:active>1</u:active></u:getRecords></soapenv:Body></soapenv:Envelope>";
        assertEquals(expectedResult, new LineExtractor(source).extractXml());
    }

    @Test
    public void extractDateTest() throws IOException, URISyntaxException {
        String source = loadFile("dateinput.txt");
        LocalDateTime date = new LineExtractor(source).extractDate();
        assertEquals(2019, date.getYear());
        assertEquals(11, date.getMonthValue());
        assertEquals(4, date.getDayOfMonth());
        assertEquals(0, date.getHour());
        assertEquals(5, date.getMinute());
        assertEquals(36, date.getSecond());
    }

    @Test
    public void commandExtractTest(){
        Application application = new Application();
        String input = "2019-11-22 00:01:33,140 INFO  - :: XPath command: CALLBACK";
        String command = application.extractCommand(input);
        assertEquals("CALLBACK", command);
    }



    private String loadFile(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
        byte[] fileBytes = Files.readAllBytes(path);
        return new String(fileBytes);
    }
}
