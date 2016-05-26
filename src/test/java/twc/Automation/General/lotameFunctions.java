package twc.Automation.General;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

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

public class lotameFunctions extends Drivers{
	
	static String excel_sheet_name = "lotame";
	static int Cap = 2;
	
	public static void validateSGFromPubadAndLotameCallWithID() throws Exception{
		
		List<String> id_results = read_ad_crwdcntrl_call();
		List<String> sg_results = readpubad_call();
		
		
		
		String lotameidresults = id_results.toString().replace("[", "").replace("]", "").replace(" ", "");
		String pubadssgresults = sg_results.toString().replace("[", "").replace("]", "");
		
		System.out.println(lotameidresults);
		System.out.println(pubadssgresults);
		String[] lotame_id_results = lotameidresults.split(",");
		String[] pubads_sg_results = pubadssgresults.split(",");
		
		for(int i=0;i<lotame_id_results.length;i++){
			
				if(pubads_sg_results[i].contains(lotame_id_results[i])){
				System.out.println("Lotame Id :::: "+lotame_id_results[i]);
				System.out.println("Pub Ad SG :::: "+pubads_sg_results[i]);
				}
				else{
					Assert.fail("Lotame Id Values Does Not Matched With PubAd SG Values");
				}
		}
	}
	
	public static List<String>  read_ad_crwdcntrl_call() throws Exception{
		
		List<String> id_res = new ArrayList<String>();
		String[][] lotamedata = read_excel_data.exceldataread(excel_sheet_name);
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		String ApiCall = sb.toString().substring(sb.toString().lastIndexOf(lotamedata[2][Cap]));
		String req1 = ApiCall.toString().substring(ApiCall.toString().indexOf(lotamedata[3][Cap]));
		
		String req = req1.toString().substring(req1.indexOf(lotamedata[4][Cap])+7,req1.indexOf(lotamedata[5][Cap]));
		String lotameValues = req.toString();
		
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(lotameValues);
		JSONObject jsonObject = (JSONObject) obj;
		
		JSONObject profileObject = (JSONObject) jsonObject.get("Profile");
		JSONObject audiencesObject = (JSONObject) profileObject.get("Audiences");
		
		JSONArray id_val = (JSONArray) audiencesObject.get("Audience");
		
		for(int i=0;i< id_val.size();i++){
			
			JSONObject filter = (JSONObject) id_val.get(i);
			if(filter.containsKey("id")){
				id_res.add(filter.get("id").toString());
			}
		}
		return id_res;
	}
	
	public static List<String> readpubad_call() throws Exception{
		
		Drivers.property();
		
		List<String> sg_res = new ArrayList<String>();
		
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
					if(decoderstring.contains("iu=/7646/app_android_us/display/bb")){
						
						String[] arrayval = decoderstring.split("&");
						
						for (String keys : arrayval) {
							
							String[] key = keys.split("=");
							/* --- Start If Key pair contains ZIP Value --- */
							if (key[0].equals("sg")) {
								sg_res.add(key[1].toString());
							}
							
							
						}
						
					}
					
				}
			}
		}
		return sg_res;
	}

}
