package twc.Automation.General;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twc.Automation.Driver.Drivers;

public class bbCallFunctions extends Drivers{
	
public static void bb_call_verify() throws IOException{
		
		
	    Drivers.property();
		System.out.println("===================================================");
		System.out.println("Verification Start for BB Call");
		
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
					/* --- Start In XML Host Value Equals to  pubads.g.doubleclick.net --- */
					if(eElement.getAttribute("host").equals("pubads.g.doubleclick.net") && eElement.getAttribute("query").contains("iu=%2F7646%2Fapp_android_us%2Fdisplay%2Fbb")){
						System.out.println("BB has been verified");
						break;
					}/* --- End In XML Host Value Equals to  pubads.g.doubleclick.net --- */
					
				}/* --- End In XML Node  --- */
				
			}/* --- End In XML Node Tag Name Start With transaction  --- */
		    } catch (Exception e) {
		    	System.out.println("BB has been not generated");
		    	Assert.fail("BB has been not generated");
		    }/* --- Start Try Catch Method  --- */
		System.out.println("===================================================");
	}

}
