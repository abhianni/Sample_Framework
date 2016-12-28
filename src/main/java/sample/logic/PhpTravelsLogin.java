package sample.logic;

import sample.helper.Base;

public class PhpTravelsLogin extends Base {

	public void login(String username, String password) throws InterruptedException {

		type(locator("userName"), username);
		type(locator("password"), password);
		click(locator("loginButton"));

	}

}
