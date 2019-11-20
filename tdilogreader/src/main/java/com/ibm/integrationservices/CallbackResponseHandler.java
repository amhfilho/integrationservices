package com.ibm.integrationservices;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class CallbackResponseHandler {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        File fXmlFile = new File("c:/Users/mult-e/Desktop/phhs.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        if (doc.hasChildNodes()) {
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int count = 0; count < nodeList.getLength(); count++) {
                Node tempNode = nodeList.item(count);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE &&
                    !tempNode.getNodeName().equals("number") &&
                    !tempNode.getNodeName().equals("correlation_id")) {

                    System.out.print(tempNode.getNodeName());
                    System.out.print(",");
                }
            }


        }

    }
}
