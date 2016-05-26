package twc.Automation.General;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;

public class loginModule extends Drivers {
	
	public static String login_Status() throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String loginStatus=null;
		
		String[][] logindata = read_excel_data.exceldataread("Login");
		
		WebDriverWait wait = new WebDriverWait(Ad, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logindata[1][Cap])));
		
		WebElement menuIcon = Ad.findElementByXPath(logindata[1][Cap]);
		menuIcon.click();
		
		
		WebDriverWait wait1 = new WebDriverWait(Ad, 30);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logindata[2][Cap])));
		loginStatus = Ad.findElementByXPath(logindata[2][Cap]).getText();
		
		if(loginStatus.equals(logindata[9][Cap])){
			loginStatus = "Yes";
			
				if(Cap==2){
					WebDriverWait wait6 = new WebDriverWait(Ad, 30);
					wait6.until(ExpectedConditions.presenceOfElementLocated(By.id(logindata[10][Cap])));
					Ad.findElementById(logindata[10][Cap]).click();
				}
			System.out.println("User Already LoggedIn");
		}
		else
		{
			loginStatus = "No";
			System.out.println("User Need To Login");
		}
		return loginStatus;
	}
	
	public static void login() throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String loginStatus = login_Status();
		
		String[][] logindata = read_excel_data.exceldataread("Login");
		if(loginStatus.equals("No")){
			
			if(Cap == 2){
				System.out.println("Click On Login Link");
				WebDriverWait wait1 = new WebDriverWait(Ad, 30);
				wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logindata[2][Cap])));
				Ad.findElementByXPath(logindata[2][Cap]).click();
				
				System.out.println("Enter Email ID");
				WebDriverWait wait2 = new WebDriverWait(Ad, 30);
				wait2.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logindata[3][Cap])));
				Ad.findElementByXPath(logindata[3][Cap]).sendKeys(logindata[4][Cap]);
				
				System.out.println("Enter Password");
				WebDriverWait wait3 = new WebDriverWait(Ad, 30);
				wait3.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logindata[5][Cap])));
				Ad.findElementByXPath(logindata[5][Cap]).sendKeys(logindata[6][Cap]);
				
				Ad.navigate().back();
				
				System.out.println("Click On Log In Button");
				WebDriverWait wait4 = new WebDriverWait(Ad, 30);
				wait4.until(ExpectedConditions.presenceOfElementLocated(By.id(logindata[7][Cap])));
				Ad.findElementById(logindata[7][Cap]).click();
				
				WebDriverWait wait5 = new WebDriverWait(Ad, 40);
				wait5.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logindata[1][Cap])));
				System.out.println("Login Done Successfully");
				
				String adbPath = properties.getProperty("adbPath");
				
				String[] str ={"/bin/bash", "-c", adbPath+" adb shell pm disable com.weather.Weather"};
				Runtime.getRuntime().exec(str);
				Thread.sleep(2000);
				
				String[] str1 ={"/bin/bash", "-c", adbPath+" adb shell pm enable com.weather.Weather"};
				Runtime.getRuntime().exec(str1);
				
				Ad.closeApp();
				Ad.launchApp();
			}
		}
		
	}

}
