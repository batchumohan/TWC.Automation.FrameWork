package twc.Automation.SmokeTestCases;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import twc.Automation.Driver.Drivers;
import twc.Automation.General.cxtgFunctions;
import twc.Automation.General.loginModule;
import twc.Automation.HandleWithApp.AppFunctions;
import twc.Automation.HandleWithAppium.AppiumFunctions;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.General.Functions;

public class somkeTestCases extends Drivers{
	
	/* Factual Call Test Case */
	@Test(priority=0)
	public void Factual_Test_Case_Using_Charles() throws Exception{
		CharlesFunctions.openCharlesBrowser();
		AppiumFunctions.LaunchApp();
		CharlesFunctions.StopExportSessionXMLFile();
		Functions.validate_API_Call_With_PubAds_Call("location");
	}
	/* Pull To Refresh Test Case */
	@Test(priority=1)
	public void PulltoRefresh_Test_Case_Using_Charles() throws Exception{
		CharlesFunctions.openCharlesBrowser();
		AppiumFunctions.LaunchApp();
		AppFunctions.Pull_To_Refresh();
		CharlesFunctions.StopExportSessionXMLFile();
		Functions.bb_call_validation("pulltorefresh");
	}
	/* Lotame Call Test Case */
	@Test(priority=2)
	public void Lotame_Test_Case_Using_Charles() throws Exception{
		CharlesFunctions.openCharlesBrowser();
		AppiumFunctions.LaunchApp();
		CharlesFunctions.StopExportSessionXMLFile();
		Functions.validate_API_Call_With_PubAds_Call("lotame");
	}
	/* Hourly Module Extended Page Ad Test Case */
	@Test(priority=3)
	public void Verify_Ad_Present_On_HourlyExtended_page() throws Exception {
		AppiumFunctions.LaunchApp();
		AppFunctions.verify_adpresent_onextendedHourly_page();
	}
	/* Daily Module Extended Page Ad Test Case */
	@Test(priority=4)
	public void Verify_Ad_Present_On_DailyExtended_page() throws Exception {
		AppiumFunctions.LaunchApp();
		AppFunctions.verify_adpresent_onextendedTenday_page();
	}
	/* RADAR And MAPS Module Extended Page Ad Test Case */
	@Test(priority=5)
	public void Verify_Ad_Present_On_MapsExtended_page() throws Exception {
		AppiumFunctions.LaunchApp();
		AppFunctions.verify_adpresent_onextendedMap_page();
	}
	/* News Module Extended Page Ad Test Case */
	@Test(priority=6)
	public void Verify_Ad_Present_On_NewsExtended_page() throws Exception {
		AppiumFunctions.LaunchApp();
		AppFunctions.verify_adpresent_onextendedNews_page();
	}
	/* Clean Launch  Test Case */
	@Test(priority=7)
	public void Clean_App_Launch() throws Exception{
		AppiumFunctions.UnInstallApp();
		CharlesFunctions.openCharlesBrowser();
		AppiumFunctions.LaunchApp();
		AppFunctions.CleanLaunch_launch();
		CharlesFunctions.StopExportSessionXMLFile();
		Functions.clean_App_Launch("clt");
	}
	/* CXTG  Values Test Case */
	@Test(priority=8)
	public void Verify_CXTG_Values() throws Exception{
		CharlesFunctions.openCharlesBrowser();
		AppiumFunctions.LaunchApp();
		loginModule.login();
		cxtgFunctions.verifySavedAddressList();
		CharlesFunctions.StopExportSessionXMLFile();
		cxtgFunctions.validateCXTGResultsFromPubadAndTriggerCalls();
	}
	/* Test Mode BB Call Test Case */
	@Test(priority=9)
	public void Verify_TestMode_BBCall() throws Exception{
		CharlesFunctions.openCharlesBrowser();
		AppiumFunctions.LaunchApp();
		AppFunctions.Change_to_Test_Mode();
		CharlesFunctions.StopExportSessionXMLFile();
		Functions.verify_bb_call_in_Test_Mode("testmode");
	}
	
	@BeforeTest
	public void Before_Test() throws Exception{
		CharlesFunctions.charlesOpen();
		AppiumFunctions.UnInstallApp();
	}

}
