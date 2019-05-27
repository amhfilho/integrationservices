package com.ibm.integrationservices;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        String eventId = "1985415";

        String file = "C:\\Users\\AntonioMarioHenrique\\Desktop\\temp\\chubb.prd.2018-12-04.log";
        String command = "";
        StringBuilder inputXml = new StringBuilder();
        StringBuilder transformedCreateXML = new StringBuilder();

        System.out.println("opening file...");
        List<String> lines = TdiFileReader.open(file).lines();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("XPath command") &&
                    lines.get(i).contains(eventId)) {

                System.out.println(String.format("Line %s: %s" , i,lines.get(i)));
                if(lines.get(i).contains("CREATE_PROBLEM")){
                    command = "CREATE";
                }
                else if(lines.get(i).contains("UPDATE_PROBLEM")){
                    command = "UPDATE";
                }
                else if(lines.get(i).contains("UPDATE_CALLBACK")){
                    command = "UPDATE_CALLBACK";
                }

                boolean xmlFound = false;

                while(!xmlFound){
                    String line = lines.get(i++);
                    if(line.contains("Input XML") && line.contains(eventId)){
                        boolean endXml = false;
                        while(!endXml){

                            inputXml.append(line);
                            if(line.contains("</command>")){
                                endXml = true;
                            }
                            line = lines.get(i++);
                        }
                        xmlFound = true;
                    }

                }

                boolean transformedCreateXmlFound = false;
                while(!transformedCreateXmlFound) {
                    String line = lines.get(i++);
                    if (line.contains("transformedCreateXML") && line.contains(eventId)) {
                        transformedCreateXmlFound = true;
                        boolean createXMLFound = false;
                        while (!createXMLFound) {
                            transformedCreateXML.append(line);
                            if (line.contains("</soapenv:Envelope>")) {
                                createXMLFound = true;
                            }
                            line = lines.get(i++);
                        }
                    }
                }


            }
        }

        System.out.println(command);
        String inputStringXml = inputXml.toString();
        int start = inputStringXml.indexOf("<command");
        String xml = inputStringXml.substring(start);
        System.out.println(xml);

        String createXml = transformedCreateXML.toString();
        start = createXml.indexOf("<?xml");
        createXml = createXml.substring(start);
        System.out.println(createXml);
    }
}
