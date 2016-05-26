package twc.Automation.HandleWithAppium;

import io.appium.java_client.android.AndroidDriver;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.remote.DesiredCapabilities;


import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.General.DeviceStatus;

public class AppiumFunctions extends Drivers{
	
	public static void killADB() throws IOException{
		String[] command ={"/usr/bin/killall","-KILL","adb"};
        Runtime.getRuntime().exec(command); 
        
        String[] command1 ={"/usr/bin/killall","-KILL","-9 adb"}; 
        Runtime.getRuntime().exec(command1);
	}
	
	public static void UnInstallApp() throws IOException{
		
		Drivers.property();
		
		System.out.println("Uninstall the APP and Installing");	
		String[] uninstall ={"/bin/bash", "-c",  properties.getProperty("adbPath")+" uninstall com.weather.Weather"};
		Runtime.getRuntime().exec(uninstall);
		System.out.println("Uninstall completed");
	}
	
	public static void AppiumServerStop() throws InterruptedException{
		
		String[] command ={"/usr/bin/killall","-KILL","node"};  
        
        try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("Appium Server Not Yet Killed At This Time");
		}  
        Thread.sleep(5000);
	}
	
	public static void clearTWCLogs() throws InterruptedException, IOException{
		
		Drivers.property();
		System.out.println("Clear Logcat Logs for TWC App");	
		String[] clearLogcatdata ={"/bin/bash", "-c",  properties.getProperty("adbPath")+" logcat -c"};
		Runtime.getRuntime().exec(clearLogcatdata);	
		Thread.sleep(4000);
	}
	
	public static void AppiumServerStart() throws InterruptedException{
		
		CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");
		command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js", false);
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument("4723");	
		command.addArgument("--no-reset", false);
		command.addArgument("--log-level", false);
		command.addArgument("error");
		
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			Thread.sleep(5000);
		} catch (ExecuteException e) {
			System.out.println("Appium Server Not Yet Started");
		} catch (IOException e) {
			System.out.println("Appium Server Not Yet Started");
		}
	}
	
	public static void ReLaunchApp() throws InterruptedException, IOException{
		
		clearTWCLogs();
		
		Drivers.property();
		String adbPath = properties.getProperty("adbPath");
		
		String[] str ={"/bin/bash", "-c", adbPath+" adb shell pm disable com.weather.Weather"};
		Runtime.getRuntime().exec(str);
		Thread.sleep(2000);
		
		String[] str1 ={"/bin/bash", "-c", adbPath+" adb shell pm enable com.weather.Weather"};
		Runtime.getRuntime().exec(str1);
		
		Ad.closeApp();
		Ad.launchApp();
	}
	
	@SuppressWarnings("rawtypes")
	public static void LaunchApp() throws InterruptedException, IOException{
			
			Drivers.property();
			
			try {
				killADB();
			} catch (IOException e1) {
				System.out.println("Unable To Kill The ADB");
			}
			
//			try {
//				UnInstallApp();
//			} catch (IOException e1) {
//				System.out.println("Unable To Un Install The APP");
//			}
			
			
			AppiumServerStop();
			AppiumServerStart();
			
			DeviceStatus device_status = new DeviceStatus();
			int Cap = device_status.Device_Status();
			
			
			try {
				
				String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
				DesiredCapabilities capabilities = new DesiredCapabilities();
				
				/* --- Start Android Device Capabilities --- */
				if(Cap == 2){
				capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
				capabilities.setCapability(capabilitydata[2][0], capabilitydata[2][Cap]); 
				capabilities.setCapability(capabilitydata[3][0], capabilitydata[3][Cap]);
				capabilities.setCapability(capabilitydata[7][0], capabilitydata[7][Cap]); 
				capabilities.setCapability(capabilitydata[10][0],capabilitydata[10][Cap]);
				capabilities.setCapability(capabilitydata[12][0],capabilitydata[12][Cap]);
				capabilities.setCapability(capabilitydata[13][0],capabilitydata[13][Cap]);
				capabilities.setCapability(capabilitydata[14][0],capabilitydata[14][Cap]);
				Ad = new AndroidDriver(new URL(capabilitydata[15][Cap]), capabilities);
				}
				/* ---End Android Device Capabilities --- */
				Ad.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				System.out.println("Capabilities have launched");
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println("Unable To Launch The Appium Capabilities");
			}
	}
}
