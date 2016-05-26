package twc.Automation.ReadDataFromFile;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twc.Automation.Driver.Drivers;

public class verify_call_from_xml_file extends Drivers{
	
	public static boolean Verify_Call_From_XML_Data_File(String host, String Value) throws Exception{
		
		Drivers.property();
		
		boolean result = false;
		try {
			
			File fXmlFile = new File(properties.getProperty("xmlFilePath"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("transaction");
			/* --- Start In XML Node Tag Name Start With transaction  --- */
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				Node nNode = nList.item(temp);
				/* --- Start In XML Node  --- */
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					System.out.println(eElement);
					/* --- Start In XML Host Value Equals to  pubads.g.doubleclick.net --- */
					if(eElement.getAttribute(host).equals(Value)){
						result = true;
					}
					else{
						result = false;
						
					}
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("Unable To Verify The Call From XML File");
		}
		return result;
		
		
	}
}
