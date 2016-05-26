package twc.Automation.HandleWithCharles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;

public class CharlesFunctions extends Drivers{
	
	
	public static void charlesOpen() throws Exception{
		
		String[] openCharles_str ={"/bin/bash", "-c", "open -a charles"};
		Runtime.getRuntime().exec(openCharles_str);	
		Thread.sleep(5000);
	}
	
	public static void openCharlesBrowser() throws IOException, Exception{
		
		Drivers.property();
		
		String[][] charlesdata = read_excel_data.exceldataread("Charlesdeatils");
		
		String downloadPath = properties.getProperty("downloadPath");
		
		File index = new File(downloadPath);
		
		if (!index.exists()) {
			System.out.println("Specified folder is not exist and creating the same folder now");
		    index.mkdir();
		} else {
			System.out.println("Specified folder is exist and deleting the same folder");
			FileUtils.cleanDirectory(index);
			System.out.println("Deleted all the files in the specified folder");
		}
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", downloadPath);
		profile.setPreference("browser.helperApps.neverAsk.openFile","text/xml,text/csv,application/x-msexcel,application/octet-stream,application/vnd.android.package-archive,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/apk");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/xml,text/csv,application/x-msexcel,application/octet-stream,application/vnd.android.package-archive,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/apk");
		
		driver = new FirefoxDriver(profile);
		Thread.sleep(2000);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(40,TimeUnit.SECONDS);
		
		//app_download_from_hockeyapp();
		
		driver.get("http://mohantestengg:123456@control.charles");
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[1][0])).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[2][0])).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[3][0])).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[4][0])).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[5][0])).click();
	}
	
	public static void StopExportSessionXMLFile() throws Exception{
		
		Drivers.property();
		
		String[][] charlesdata = read_excel_data.exceldataread("Charlesdeatils");
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[6][0])).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[3][0])).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(charlesdata[1][0])).click();
		Thread.sleep(1000);
		System.out.println("Exporting The Session Data Into XML File");
		driver.findElement(By.linkText(charlesdata[7][0])).click();
		Thread.sleep(2000);
		String xml_file_name = null;
		
		/* --- Start XML File Location and Saved into Properties File After Downloading  --- */
		String session_downloadPath = properties.getProperty("downloadPath");
		//CXTG_Validate listfiles = new CXTG_Validate();
		List<String> get_xml_file_name = listFiles(properties.getProperty("downloadPath"));
		
		for(int i=0;i<get_xml_file_name.size();i++){
				if(get_xml_file_name.get(i).contains(".xml")){
						xml_file_name = get_xml_file_name.get(i);
				}
				else
				{
					@SuppressWarnings("unused")
					String apk_file_name = get_xml_file_name.get(i);
				}
		}
				
		String concat_session_file_path = session_downloadPath.concat(xml_file_name);
				
		System.out.println("XML File Name : "+concat_session_file_path);
		properties.setProperty("xmlFilePath", concat_session_file_path);
		FileOutputStream xml_override = new FileOutputStream(properties.getProperty("dataFilePath"));		
		properties.store(xml_override, "override the session xml file");
		/* --- End XML File Location and Saved into Properties File After Downloading  --- */
		Thread.sleep(2000);
		driver.close();
	}
	
	public static void app_download_from_hockeyapp() throws InterruptedException, IOException{
		
		driver.get("https://rink.hockeyapp.net/users/sign_in");
		driver.findElement(By.id("user_email")).sendKeys("kvelappan@weather.com");
		driver.findElement(By.id("user_password")).sendKeys("300interstate");
		driver.findElement(By.name("commit")).click();
		
		Thread.sleep(2000);
		String Apps = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/ul/li[2]/a")).getText();
		System.out.println("Apps text :: "+Apps);
		driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/ul/li[2]/a")).click();
		Thread.sleep(2000);
		String platforms = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/ul/li[2]/ul/li[3]/a/span")).getText();
		System.out.println("platforms text :: "+platforms);
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/ul/li[2]/ul/li[3]/a/span")).click();
		System.out.println("Selected Android in the Platforms Dropdown");
		Thread.sleep(1000);
		String expectedToClick= properties.getProperty("BuildToDownload");
		System.out.println("expectedToClick is : " + expectedToClick);
		
		if (expectedToClick.contains("Alpha")) {
			downloadApp_AndroidFlagshipDev(driver);
		} else if (expectedToClick.contains("Beta")) {
			downloadApp_TheWeatherChannelFlagship(driver);
		}
	}
	
	public static void downloadApp_AndroidFlagshipDev(WebDriver driver) throws InterruptedException, IOException{
		
		Drivers.property();
		Thread.sleep(1000);
		String flag = driver.findElement(By.xpath("//table[@id='apps']/tbody/tr[2]/td[2]/div")).getText();
		System.out.println("Flag :: " + flag);
		driver.findElement(By.xpath("//table[@id='apps']/tbody/tr[2]/td[2]/div")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.linkText("Private Page")).click();
		
		String  version  = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[1]/div/div[3]/div[7]/h3")).getText();
		System.out.println("Version of the build is :: "+version); 
		String ver = version.substring(version.indexOf("(")+1, version.indexOf(")"));
		System.out.println("Present Build version : "+ver);
		String Old_Build = properties.getProperty("AndroidFlagship_AlphaBuild");
		System.out.println("Old_Build version : "+Old_Build);
		
		if (!ver.equals(Old_Build)){
			
			System.out.println("New build exists. So, download");
			properties.setProperty("AndroidFlagship_AlphaBuild", ver);		
			FileOutputStream fos =  new FileOutputStream(properties.getProperty("dataFilePath"));
			properties.store(fos, "Build Information to download the latest apk and run the scripts");
			fos.close();
			
			driver.findElement(By.linkText("Download")).click();
			Thread.sleep(2000);
			driver.navigate().to(properties.getProperty("downloadPath"));
			Thread.sleep(50000);
			driver.navigate().refresh();
			Thread.sleep(2000);
			set_app_path();
		} else{
			System.out.println("No need to Download the Alpha - Build, becoz Old Build only exists.");
		}
		

	}
	
	public static void downloadApp_TheWeatherChannelFlagship(WebDriver driver) throws InterruptedException, IOException{
			
		Drivers.property();
		Thread.sleep(1000);
		
		String flag = driver.findElement(By.xpath("//table[@id='apps']/tbody/tr[4]/td[2]/div")).getText();
		System.out.println(" Flag :: " + flag);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//table[@id='apps']/tbody/tr[4]/td[2]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("Private Page")).click();
		
		String  version  = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[1]/div/div[3]/div[6]/h3")).getText();
		System.out.println(" Version of the build is :: "+version); 
		String ver = version.substring(version.indexOf("(")+1, version.indexOf(")"));
		
		System.out.println("Present Build version : "+ver);
		
		String Old_Build = properties.getProperty("AndroidFlagship_BetaBuild");
		System.out.println("Old_Build version : "+Old_Build);
			
			if (!ver.equals(Old_Build)){
				
				System.out.println("New build exists. So, download");
				properties.setProperty("AndroidFlagship_BetaBuild", ver);
				FileOutputStream fos =  new FileOutputStream(properties.getProperty("dataFilePath"));
				properties.store(fos, "Build Information to download the latest apk and run the scripts");
				fos.close();
				driver.findElement(By.linkText("Download")).click();
				Thread.sleep(2000);
				driver.navigate().to(properties.getProperty("downloadPath"));
				Thread.sleep(50000);
				driver.navigate().refresh();
				Thread.sleep(2000);
				set_app_path();
			} else{
				System.out.println("No need to Download the Beta - Build, becoz Old Build only exists.");
			}	
	}
	
	public static void set_app_path() throws IOException{
		
		Drivers.property();
		@SuppressWarnings("unused")
		String xml_file_name = null;
		String apk_file_name = null;
		String apk_downloadPath = properties.getProperty("apkPath");
		
		File index = new File(apk_downloadPath);
		
		if (!index.exists()) {
			System.out.println("Specified folder is not exist and creating the same folder now");
		    index.mkdir();
		} else {
			System.out.println("Specified folder is exist and deleting the same folder");
			FileUtils.cleanDirectory(index);
			System.out.println("Deleted all the files in the specified folder");
		}
		
		
		List<String> get_xml_file_name = listFiles(properties.getProperty("downloadPath"));
		
		for(int i=0;i<get_xml_file_name.size();i++){
			if(get_xml_file_name.get(i).contains(".xml")){
				xml_file_name = get_xml_file_name.get(i);
			}
			else
			{
				apk_file_name = get_xml_file_name.get(i);
				break;
			}
		}
		
		String concat_apk_file_path = apk_downloadPath.concat(apk_file_name);
		
		System.out.println("APK File Name : "+concat_apk_file_path);
		properties.setProperty("appPath", concat_apk_file_path);
		FileOutputStream apk_override = new FileOutputStream(properties.getProperty("dataFilePath"));		
		properties.store(apk_override, "override the apk file");
		
		File sourceFile = new File(properties.getProperty("downloadPath").concat(apk_file_name));
		File destinationDir = new File(apk_downloadPath);

		FileUtils.moveFileToDirectory(sourceFile, destinationDir, true);
	}
	/* --- Start Get File Names from Folder  --- */
	public static List<String> listFiles(String directoryName){
	
	//String file_name = null;
	List<String> filelist = new ArrayList<String>();
    File directory = new File(directoryName);
    //get all the files from a directory
    File[] fList = directory.listFiles();
    for (File file : fList){
        if (file.isFile()){
        	filelist.add(file.getName());
        	//file_name = file.getName();
        	//break;
        }
    }
    return filelist;
	}/* --- End Get File Names from Folder  --- */

}
