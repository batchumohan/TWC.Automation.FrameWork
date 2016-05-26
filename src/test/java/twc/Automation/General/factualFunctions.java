package twc.Automation.General;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;

public class factualFunctions extends Drivers{
	
		
		public static void validateFGEOFAUDFromPubadAndLocationTriggerCalls() throws Exception{
			
			Map<String, String> location_results = readlocation_wfxTriggers();
			Map<String, String> pubads_results = readpubad_call();
			
			System.out.println(location_results.get("fgeo"));
			System.out.println(pubads_results.get("pubad_fgeo"));
			
			System.out.println(location_results.get("faud"));
			System.out.println(pubads_results.get("pubad_faud"));
			
			String locationfgeoresults = location_results.get("fgeo").toString().replace("[", "").replace("]", "");
			String pubadsfgeoresults = pubads_results.get("pubad_fgeo").toString().replace("[", "").replace("]", "");
			
			
			String locationfaudresults = location_results.get("faud").toString().replace("[", "").replace("]", "");
			String pubadsfaudresults = pubads_results.get("pubad_faud").toString().replace("[", "").replace("]", "");
			
			
			String[] pubads_fgeoresults = pubadsfgeoresults.split(",");
			
			for(int i=0;i<pubads_fgeoresults.length;i++){
				
				if(!pubadsfgeoresults.equals("nl")){
					if(locationfgeoresults.contains(pubads_fgeoresults[i])){
						System.out.println("Matched With "+pubadsfgeoresults+" :::: " + pubads_fgeoresults[i]);
					}
					else{
						System.out.println("Not Matched :::: "+pubads_fgeoresults[i]);
						Assert.fail();
					}
				}
				else{
					System.out.println("FGEO is Getting nl value from pubads call");
				}
			}
			
			String[] pubads_faudresults = pubadsfaudresults.split(",");
			
			for(int i=0;i<pubads_faudresults.length;i++){
				if(!pubadsfaudresults.equals("nl")){
					if(locationfaudresults.contains(pubads_faudresults[i])){
						System.out.println("Matched With "+pubadsfaudresults+":::: " + pubads_faudresults[i]);
					}
					else{
						System.out.println("Not Matched :::: "+pubads_faudresults[i]);
						Assert.fail();
					}
				}
				else{
					System.out.println("FAUD is Getting nl value from pubads call");
				}
			}
			
			
			
		}
		
		//Read Location.wfxtriggers API
		public static Map<String, String> readlocation_wfxTriggers() throws Exception{
			
			String req =null;
			int Cap =1;
			
			Map<String , String> fgeo_faud_res = new HashMap<String, String>();
			
			List<String> fgeo_res = new ArrayList<String>();
			List<String> faud_res = new ArrayList<String>();
			
			String[][] locationdata = read_excel_data.exceldataread("location");
			
			read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
			String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
			
			String ApiCall = sb.toString().substring(sb.toString().lastIndexOf(locationdata[2][Cap]));
			String req1 = ApiCall.toString().substring(ApiCall.toString().indexOf(locationdata[3][Cap]));
			//System.out.println("Req1 data is "+req1.toString());
			req = req1.toString().substring(req1.indexOf(locationdata[4][Cap])+7,req1.indexOf(locationdata[5][Cap]));
			String factualValues = req.toString();
		
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(factualValues);
			JSONObject jsonObject = (JSONObject) obj;
			
			JSONArray fgeoval = (JSONArray) jsonObject.get("proximity");
			for(int i=0;i< fgeoval.size();i++){
				
				JSONObject filter = (JSONObject) fgeoval.get(i);
				if(filter.containsKey("index")){
					fgeo_res.add(filter.get("index").toString());
				}
			}
			
			JSONArray faudval = (JSONArray) jsonObject.get("set");
			for(int i=0;i< faudval.size();i++){
				
				JSONObject filter = (JSONObject) faudval.get(i);
				if(filter.containsKey("group")){
					faud_res.add(filter.get("group").toString());
				}
			}
			
			fgeo_faud_res.put("fgeo", fgeo_res.toString());
			fgeo_faud_res.put("faud", faud_res.toString());
			
			return fgeo_faud_res;
		}
		
		public static Map<String, String> readpubad_call() throws Exception{
			
			Drivers.property();
			Map<String , String> fgeo_faud_res = new HashMap<String, String>();
			
			List<String> fgeo_res = new ArrayList<String>();
			List<String> faud_res = new ArrayList<String>();
			
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
						decoderstring = decoderstring.substring(16);
						
						//System.out.println(decoderstring);
						if(decoderstring.contains("iu=/7646/app_android_us/display/bb")){
							
							String[] arrayval = decoderstring.split("&");
							
							for (String keys : arrayval) {
								
								String[] key = keys.split("=");
								/* --- Start If Key pair contains ZIP Value --- */
								if (key[0].equals("fgeo")) {
									fgeo_res.add(key[1].toString());
								}
								
								if (key[0].equals("faud")) {
									faud_res.add(key[1].toString());
								}
							}
							
						}
						
					}
				}
			}
			
			fgeo_faud_res.put("pubad_fgeo", fgeo_res.toString());
			fgeo_faud_res.put("pubad_faud", faud_res.toString());
			
			return fgeo_faud_res;
		}

}
