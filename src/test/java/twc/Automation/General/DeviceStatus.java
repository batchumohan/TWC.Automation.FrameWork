package twc.Automation.General;

import twc.Automation.ReadDataFromFile.read_excel_data;

public class DeviceStatus {
	
	int Cap = 0;
	
	public int Device_Status(){
		
		try {
			String[][] devicedata = read_excel_data.exceldataread("Device");
			if(devicedata[1][1].equals("Android")){
				Cap = Cap+2;
			}
			else {
				Cap = 1;
			}
		} catch (Exception e) {
			System.out.println("Unable To Read The Device Tab Data");
		}
		return Cap;
	}
}
