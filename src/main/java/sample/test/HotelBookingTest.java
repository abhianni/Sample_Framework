package sample.test;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import sample.logic.PhpTravelHotelBooking;
@Parameters("browser")
public class HotelBookingTest extends PhpTravelHotelBooking{	
  @Test(parameters="browser")
  public void checkHotelDetailsTest(String browser) throws IOException {
	  setStr(browser);
	  System.out.println(config.keySet());
	  checkHotelDetails();
	 Assert.assertEquals("Rendezvous Hotels", getText(locator("Rendezvous-HotelsVerify")));
  }
  
  @Test(parameters="browser")
  public void bookHotelTest(String browser)
  {
	  setStr(browser);
	  bookHotel();
	  Assert.assertEquals("Are you sure you want to pay at arrival?", alerttext());
	  alertAccept();
	  Assert.assertTrue(assertElementPresent(locator("bookingConfirmation")));
  }
}
