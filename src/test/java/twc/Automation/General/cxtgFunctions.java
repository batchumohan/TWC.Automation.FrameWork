package twc.Automation.General;

import io.appium.java_client.MobileElement;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;
import junit.framework.ComparisonFailure;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;

public class cxtgFunctions extends Drivers{
	
	public static void verifySavedAddressList() throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
			/* --- Start For Android Device --- */
			if(Cap == 2){
			String[][] addressdata = read_excel_data.exceldataread("CheckAddress");
			
			WebDriverWait wait4 = new WebDriverWait(Ad, 10);
			wait4.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));
			
			//Root Location Element
			Ad.findElementById(addressdata[4][Cap]).click();
			
			WebDriverWait wait5 = new WebDriverWait(Ad, 20);
			wait5.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[6][Cap])));
			
			//List Location Element
			@SuppressWarnings("unchecked")
			List<MobileElement> loclist = Ad.findElements(By.id(addressdata[6][Cap]));
			
			int loc_size = loclist.size() -1;
			
			String loc_length = Integer.toString(loc_size);
			
			System.out.println("Total Saved Address List :::::" + loc_length);
			
			Thread.sleep(2000);
			
			System.out.println("Start Select Address List");
			
			String firsteleXpath = addressdata[5][Cap];
			String[] parts = firsteleXpath.split("Count");
			/* --- Start For Loop For Location Click --- */
			for(int i=2;i<= loclist.size();i++){
				
				String element = null;
				
				try {
					
					element = parts[0]+i+parts[1];
							   
					MobileElement ele = (MobileElement) Ad.findElementByXPath(element);
					System.out.println("For This Location ====>"+ele.getText());
					
					WebDriverWait wait9 = new WebDriverWait(Ad, 20);
					wait9.until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
					
					Ad.findElementByXPath(element).click();
					
					WebDriverWait wait10 = new WebDriverWait(Ad, 20);
					wait10.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));
					
					Ad.findElementById(addressdata[4][Cap]).click();
				} catch (Exception e) {
					
					System.out.println(element+" is not found in the location list");
				}
			}/* --- End For Loop For Location Click --- */
			
			Thread.sleep(10000);
			
			WebDriverWait wait12 = new WebDriverWait(Ad, 10);
			wait12.until(ExpectedConditions.presenceOfElementLocated(By.xpath(parts[0]+1+parts[1])));
			
			Ad.findElementByXPath(parts[0]+1+parts[1]).click();
		}/* --- End For Android Device --- */
		System.out.println("End Select Address List");
	}
	
	/* --- Start CXTG Validation For Both PubAds as well as Triggers Response  --- */
	public static void validateCXTGResultsFromPubadAndTriggerCalls() throws Exception{
		
		System.out.println("Start Verify CXTG Results From PubAds And Trigger Calls");
		HashMap<String, String> pubad_res = pub_ads_validate();
		Iterator<String> keySetIterator = pubad_res.keySet().iterator(); 
		List<String> cxtg_not_match = new ArrayList<String>();
		
		/* --- Start While  --- */
		boolean isExceptionOccered = false;
		
		while(keySetIterator.hasNext()){ 
			
			String key = keySetIterator.next();
			
			Map<String, String> wfxtrigger_res = wfxtriggers_validate(key);
			System.out.println("For Location  : " + key);
			System.out.println("PubAd CXTG Values : " + pubad_res.get(key));
			System.out.println("Trigger CXTG Values : " + wfxtrigger_res.get("cxtg"));
			System.out.println("===================================================");
			// Assert CXTG Values from Pubads and WTX Trigger
			try {
				
				Assert.assertEquals(pubad_res.get(key),wfxtrigger_res.get("cxtg"));
			} catch (ComparisonFailure e) {
				System.out.println(key + " Doesn't Match");
				cxtg_not_match.add(key);
				isExceptionOccered= true;
			}
			
		}/* --- End While  --- */ 
		
		if(isExceptionOccered){
			System.out.println(cxtg_not_match);
			Assert.fail(cxtg_not_match + " are not matched");
		}
		
		System.out.println("End Verify CXTG Results From PubAds And Trigger Calls");
		
	}/* --- End CXTG Validation For Both PubAds as well as Triggers Response  --- */
	
	/* --- Start Getting the PubAds CXTG Values based on ZIP Code  --- */
	public static HashMap<String, String> pub_ads_validate() throws Exception{
		
		Drivers.property();
		String zip = null;
		String cxtg_val = null;
		
		HashMap<String, String> zcs_array_list = new HashMap<String, String>();
		/* --- Start Try Cache Method  --- */
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
					if(eElement.getAttribute("host").equals("pubads.g.doubleclick.net")){
						
						String request = eElement.getElementsByTagName("first-line").item(0).getTextContent();
						String decoderstring = URLDecoder.decode(request, "UTF-8");
						
						String decoderstring_sub = decoderstring.substring(16);
						//System.out.println(decoderstring_sub);
						if(decoderstring_sub.contains("iu=/7646/app_android_us/display/bb") && decoderstring_sub.contains("cxtg")){
						
						String[] arrayval = decoderstring_sub.split("&");
						/* --- Start For Loop for Split With & Symbol --- */
						for (String keys : arrayval) {
							
							String[] key = keys.split("=");
								/* --- Start If Key pair contains ZIP Value --- */
								if (key[0].equals("zip")) {
									zip = key[1];
								}
								/* --- End If Key pair contains ZIP Value --- */
								
								/* --- Start If cxtg ---*/
								if (key[0].equals("cxtg")) {
										cxtg_val = key[1];
										
										/* --- Start If cxtg not equals to nl --- */
										if(!cxtg_val.equals("nl")){
											zcs_array_list.put(zip, cxtg_val);
										}/* --- End If cxtg not equals to nl --- */
										
								}/* --- End If cxtg ---*/
								
							}/* --- End For Loop for Split With & Symbol --- */
						
						}/* --- End In XML Host Value Equals to  pubads.g.doubleclick.net --- */
						
					}/* --- End In XML Host Value Equals to  pubads.g.doubleclick.net --- */
					
				}/* --- End In XML Node  --- */
				
			}/* --- End In XML Node Tag Name Start With transaction  --- */
		    } catch (Exception e) {
		    	System.out.println("Pub Ad Calls are not generated");
		    	Assert.fail("Pug Ad Class are not matched. May be need to set up the wifi address");
		    }/* --- Start Try Catch Method  --- */
		
		return zcs_array_list;
	}/* --- End Getting the PubAds CXTG Values based on ZIP Code  --- */
	
	/* --- Start Getting the CXTG Values From triggers.wfxtriggers.com based on ZIP Code  --- */
	public static Map<String, String> wfxtriggers_validate(String zipValFrompubAd) throws Exception{
		
		String zip = null;
		String cxtg_val = null;
		Map<String , String> wfxtriggers_values = new HashMap<String, String>();
		String wxtgValues="";
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String buffer_data = xml_data_into_buffer.read_xml_file_into_buffer_string();
		/* --- Start JSON Parser for wfxtg Values --- */
		try {
			wxtgValues = buffer_data.substring(buffer_data.lastIndexOf("{\"wfxtg\":{\"current\":"));
			wxtgValues = wxtgValues.substring(wxtgValues.indexOf("{\"wfxtg\":{\"current\":"), wxtgValues.indexOf("}]}]}}")+6);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(wxtgValues);
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject wfxtgval = (JSONObject) jsonObject.get("wfxtg");
			JSONArray scatterSegsVal = (JSONArray) wfxtgval.get("scatterSegs"); 
			/* --- Start For Loop Main JSON Parser --- */
			for(int i=0;i< scatterSegsVal.size();i++){
				
				JSONObject zcsVal = (JSONObject) scatterSegsVal.get(i);
				/* --- Start Key Pair Contains ZCS --- */
				if(zcsVal.containsKey("zcs")){
					JSONArray jsonArray = (JSONArray) zcsVal.get("zcs");
					/* --- Start ZCS contains multipul ZIP Values --- */
					for(int j=0;j< jsonArray.size();j++){
						JSONObject zipval = (JSONObject) jsonArray.get(j);
						/* --- Start Key Pair Contains ZIP --- */
						if(zipval.containsKey("zip")){
							zip = zipval.get("zip").toString();
							/* --- Start ZIP Equals to Pubads ZIP --- */
							if(zipValFrompubAd.equals(zip)){
								cxtg_val = zipval.get("cxtg").toString();
								break;
							}/* --- Start ZIP Equals to Pubads ZIP --- */
							
						}/* --- End Key Pair Contains ZIP --- */
						
					}/* --- End ZCS contains multipul ZIP Values --- */
					
				}/* --- End Key Pair Contains ZCS --- */

			}/* --- End For Loop Main JSON Parser --- */
			/* --- End JSON Parser for wfxtg Values --- */
			wfxtriggers_values.put("cxtg", cxtg_val.substring(1, cxtg_val.length()-1));
		} catch (Exception e) {
			System.out.println(wxtgValues + " triggers.wfxtriggers.com call has not generated.");
//			break;
		}
		return wfxtriggers_values;
	}/* --- End Getting the CXTG Values From triggers.wfxtriggers.com based on ZIP Code  --- */
}
