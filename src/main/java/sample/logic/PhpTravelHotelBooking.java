package sample.logic;

import sample.helper.Base;

public class PhpTravelHotelBooking extends Base{
	
	public void checkHotelDetails()
	{
		
		click(locator("hotelLink"));
		click(locator("Rendezvous-Hotels"));		
	}

	public void bookHotel()
	{
		click(locator("SuperiorBookButton"));
		click(locator("confirmBooking"));
		click(locator("payOnArrivalButton"));		
	}
}
