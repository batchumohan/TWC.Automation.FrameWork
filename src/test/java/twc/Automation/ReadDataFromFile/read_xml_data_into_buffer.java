package twc.Automation.ReadDataFromFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import twc.Automation.Driver.Drivers;

public class read_xml_data_into_buffer extends Drivers {
	
	public String read_xml_file_into_buffer_string() throws Exception{
		
		Drivers.property();
		StringBuilder sb=null;
		
		try {
			File xmlFile = new File(properties.getProperty("xmlFilePath")); 
			Reader fileReader = new FileReader(xmlFile); 
			BufferedReader bufReader = new BufferedReader(fileReader); 
			sb = new StringBuilder(); 
			String line = bufReader.readLine(); 
			while( (line=bufReader.readLine()) != null)
			{ 
				sb.append(line).append("\n"); 
			} 
			bufReader.close();
		} catch (Exception e) {
			
		}
		return sb.toString();
		
	}
}
