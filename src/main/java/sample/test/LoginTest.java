package sample.test;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sample.helper.ExcelReader;
import sample.logic.PhpTravelsLogin;


public class LoginTest extends PhpTravelsLogin {
	ExcelReader read = new ExcelReader();
	Logger log = Logger.getLogger(LoginTest.class);
	
  @Test(description = "login Test", dataProvider = "login")
  public void loginTest(String url,String username , String password , int i) throws InterruptedException {
	  openUrl(url);
	  login(username, password);
	  log.info("Entered user credentials");
	  if (i == 0) {

			waitForElementVisible(locator("alertMessage"));
			log.info("Verifying error message");
			try {
				Assert.assertEquals(element(locator("alertMessage")).getText(), "Invalid Email or Password");
			}

			catch (AssertionError e) {
				Reporter.log("Text doesn't macthed -" + e.getMessage());
				throw e;
			}
		}

		else if (i == 2) {
			waitForTitle("My Account");
			log.info("User logined successfully");
			Assert.assertEquals(title(), "My Account");

		}
  }
  
  
  @DataProvider(name = "login")
	public Object[][] dataProviderLogin() throws IOException {

		read.testDataFile(File);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 1, 0), "sd", "sd", 0 },
				{ read.readFromColumn("Base", 1, 0), read.readFromColumn("Base", 1, 1),
						read.readFromColumn("Base", 1, 2), 2 } };
		read.closeFile();
		return data;
	}
}
