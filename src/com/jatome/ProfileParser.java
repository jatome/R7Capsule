/**
 * Author: JATOME (José A. Tomé)
 */

package com.jatome;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.HashMap;

public class ProfileParser {
    //class fields
    private File xmlfile;
    private String profileID;
    private MyLogger log = new MyLogger();

    //Constructor
    public ProfileParser(File f, String pID){
        xmlfile = f;
        profileID = pID;
    }

    public  HashMap<String,String> readXML(){

        HashMap<String,String> hmProfile = new HashMap<String,String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlProfile = builder.parse(xmlfile);
            xmlProfile.getDocumentElement().normalize();

            ///System.out.println("root --> " + xmlProfile.getDocumentElement().getNodeName()); //debe ser profileR7Capsule

            NodeList nlist = xmlProfile.getElementsByTagName("profile");

            int profileIndex = 0;
            for (int i = 0; i < nlist.getLength(); i++){
                Node nNode = nlist.item(i);
                Element nElement = (Element) nNode;
                if (nElement.getAttribute("id").equals(profileID)) {
                    hmProfile.put("logFileEnable", nElement.getElementsByTagName("logFileEnable").item(0).getTextContent());
                    hmProfile.put("logFilePath", nElement.getElementsByTagName("logFilePath").item(0).getTextContent());
                    hmProfile.put("compressEnable", nElement.getElementsByTagName("compressEnable").item(0).getTextContent());
                    hmProfile.put("compressSource", nElement.getElementsByTagName("compressSource").item(0).getTextContent());
                    hmProfile.put("compressTarget", nElement.getElementsByTagName("compressTarget").item(0).getTextContent());
                    hmProfile.put("compressCmdLine", nElement.getElementsByTagName("compressCmdLine").item(0).getTextContent());
                    hmProfile.put("transferEnable", nElement.getElementsByTagName("transferEnable").item(profileIndex).getTextContent());
                    hmProfile.put("transferCmdLine", nElement.getElementsByTagName("transferCmdLine").item(profileIndex).getTextContent());
                    break;
                }
            }


            /*for(String i:hmProfile.keySet()) {
                System.out.println(i + " -- > " + hmProfile.get(i));
            }*/
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hmProfile;
    }

}
