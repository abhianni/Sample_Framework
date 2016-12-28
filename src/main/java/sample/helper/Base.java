package sample.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Base extends Browser {
	FileInputStream fi;
	FileOutputStream fo;

	HSSFSheet s;
	HSSFWorkbook w;
	ArrayList<String> value;

	public static String parent, child;


	/**
	 * opens Desired url
	 * @param url
	 */
	public void openUrl(String url) {
		
		config.get(str).get(url);
	}

	/**
	 * Types data on specified locater 
	 * @param locator
	 * @param data
	 */
	public void type(By locator, String data) // To type data on the specified
												// locator
												// Created by - Abhishek Bisht
	{

		waitForElementVisible(locator);
		element(locator).clear();
		element(locator).sendKeys(data);

	}
	
	/**
	 * return focused window title
	 * @return String
	 */
	public String title() {
		String title = config.get(str).getTitle();
		return title;
	}

	/**
	 * Click on specified locater
	 * @param locator
	 */
	public void click(By locator) // To click on a locator
	{
		waitForElementVisible(locator);
		element(locator).click(); // Created by - Abhishek Bisht

	}
	
	/**
	 * Use to browse hover a element and clicks
	 * @param locator
	 */

	public void mouseOver(By locator) // For mouse hover functionality which use
										// to click or to navigate to sub menu

	{
		if (!(str.toLowerCase().equals("safari"))) {
			// Created by - Abhishek Bisht
			Actions actions = new Actions(config.get(str));
			actions.moveToElement(element(locator)).build().perform();
			actions.click();
		}
	}

	/**
	 * Use to browse hover a element
	 * @param locator
	 */

	public void mouseoverWithoutClick(By locator) {
		// config.get(str).findElement(locator).
		Actions actions = new Actions(config.get(str));
		actions.moveToElement(element(locator)).build().perform();
	}

	/**
	 * Select String value on dropdown 
	 * @param locator
	 * @param text
	 */
	public void dropdown(By locator, String text) // To select an option from
													// drop down
	{
		try {
			// waitForElementVisible(locator);
			Select drop = new Select(element(locator));
			drop.selectByVisibleText(text);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	/**
	 * Select index value on dropdown 
	 * @param locator
	 * @param int
	 */
	public void dropdown(By locator, int i) {
		waitForElementVisible(locator);
		Select drop = new Select(element(locator));
		drop.selectByIndex(i);
	}

	/**
	 * Verifies a option on dropdown and return an element
	 * @param locator
	 * @param String
	 */
	public WebElement dropdownVerify(By locator, String option) {
		WebElement element = null;
		Select drop = new Select(element(locator));
		List<WebElement> options = drop.getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(option)) {
				element = options.get(i);
			}
		}

		return element;

	}
	/**
	 * Wait for page to load till title appears
	 * @param title
	 */

	public void waitForTitle(String title) {
		WebDriverWait wait = new WebDriverWait(config.get(str), 50);
		wait.until(ExpectedConditions.titleIs(title));
	}

	/**
	 * Wait for element to be visible
	 * @param title
	 */
	public void waitForElementVisible(By locator) // Waits for an element to be
													// clickable
	{
		try {
			WebDriverWait wait = new WebDriverWait(config.get(str), 10);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (TimeoutException e) {
			throw new TimeoutException("Error message:  " + e.getMessage());
		}

	}

	/**
	 * Wait for alert to appear
	 */
	public void waitforAlert() {
		WebDriverWait wait = new WebDriverWait(config.get(str), 30);
		wait.until(ExpectedConditions.alertIsPresent());
	}

	/**
	 * returns webelement 
	 * @param String
	 * @return webelement
	 */
	public By locator(String orKey) {
		value = appName.get(orKey);
		return returnElement(value, orKey);
	}

	/**
	 * Take input from locator and after return element after traversing through whole locater excel
	 * @param ArrayList
	 * @param String
	 * @return WebElement
	 */
	public By returnElement(ArrayList<String> value, String orKey) {
		String[] str = value.get(0).split("%");
		if (str[0].equals("1")) {
			return By.id(str[1]);
		} else if (str[0].equals("2")) {
			return By.xpath(str[1]);
		} else if (str[0].equals("3")) {
			return By.linkText(str[1]);
		} else if (str[0].equals("4")) {
			return By.cssSelector(str[1]);
		} else if (str[0].equals("5")) {
			return By.className(str[1]);
		} else if (str[0].equals("6")) {
			return By.name(str[1]);
		} else if (str[0].equals("7")) {
			return By.tagName(str[1]);
		} else {
			throw new NoSuchElementException("No Such Element Exist: " + orKey);
		}
	}

	/**
	 * Switch to newly opened window
	 * @param sec
	 * @throws InterruptedException
	 */
	public void switchWindow(int sec) throws InterruptedException 
	{
		try {
			Thread.sleep(sec);
			parent = config.get(str).getWindowHandle();
			Set<String> str1 = config.get(str).getWindowHandles();
			Iterator<String> it = str1.iterator();
			while (it.hasNext()) {
				child = it.next();
				// System.out.println(child);
				config.get(str).switchTo().window(child);
			}
			if (!(str.toLowerCase().equals("safari"))) {
				config.get(str).manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();

		}

	}

	/**
	 * Reload focused window
	 */
	public void reloadPage() {
		String s = config.get(str).getCurrentUrl();
		config.get(str).get(s);

	}

	/**
	 * Switch back to parent window
	 * @param int
	 * @throws InterruptedException
	 */
	public void switchParent(int sec) throws InterruptedException 
	{
		try {
			Thread.sleep(sec);
			config.get(str).switchTo().window(parent);
			if (!(str.toLowerCase().equals("safari"))) {
				config.get(str).manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();

		}

	}

	/**
	 * returns url of focussed window
	 * @return String
	 */
	public String getUrl() {
		String url = config.get(str).getCurrentUrl();
		return url;
	}

	/**
	 * Switch to nested frame
	 * @param id
	 */
	public void switchnestedFrame(String id) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = config.get(str).findElements(By.tagName("iframe"));

		for (int i = 0; i < elements.size(); i++)

		{
			WebElement el = elements.get(i);

			if (el.getAttribute("id").equals(id)) {

				config.get(str).switchTo().frame(elements.get(i));

			} else if (el.getAttribute("name").equals(id)) {
				config.get(str).switchTo().frame(elements.get(i));
			}
		}

	}

	/**
	 * Switch to frame with tagname iframe
	 */
	public void switchtoFrame() {
		WebElement iFrame = config.get(str).findElement(By.tagName("iframe"));
		config.get(str).switchTo().frame(iFrame);
	}

	public void switchtoInnerFrame(String s) {
		config.get(str).switchTo().frame(s);
	}

	public void switchfromFrame() {
		config.get(str).switchTo().defaultContent();
	}

	/**
	 * Accept alert and close it
	 */
	public void alertAccept() // Accepts alert present on page
	{
		Alert alert = config.get(str).switchTo().alert();
		alert.accept();
	}

	/**
	 * Rejects alert and close it
	 * @throws IOException
	 */
	public void alertReject() throws IOException // Rejects alert present on
													// page
	{
		Alert alert = config.get(str).switchTo().alert();
		alert.dismiss();
	}

	/**
	 * returns text present in alert
	 * @return text
	 */
	public String alerttext() {
		Alert alert = config.get(str).switchTo().alert();
		return alert.getText(); // Returns text present on alert
	}

	public void ImplicitWait(int i) // waits for a locator to be present
	{

		config.get(str).manage().timeouts().pageLoadTimeout(i, TimeUnit.SECONDS);
	}

	/**
	 * Returns text present in element
	 * @param locator
	 * @return String
	 * @throws IOException
	 */
	public String getText(By locator) throws IOException 
	{
		try {
			assertElementPresent(locator);
			String message = config.get(str).findElement(locator).getText();
			return message;
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	public boolean assertElementPresent(By locator) // verifies that locator is
													// present
	{
		Assert.assertNotNull(locator, "Element Is present");
		return true;
	}

	/**
	 * Waits for text present on specified locater
	 * @param locator
	 * @param text
	 */
	public void waitForText(By locator, String text) 
	{
		WebDriverWait wait = new WebDriverWait(config.get(str), 50);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
		System.out.println(config.get(str).findElement(locator).getText());
	}

	public void ifElementPresent(By locator) // Waits till an element is visible
	{
		WebDriverWait wait = new WebDriverWait(config.get(str), 25);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	}
/**
 * Waits for element to get invisible
 * @param locator
 */
	public void waitForElementtoInvisible(By locator) 
	{
		WebDriverWait wait = new WebDriverWait(config.get(str), 50);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForTextToInvisible(By locator, String text) {
		WebDriverWait wait = new WebDriverWait(config.get(str), 90);
		wait.until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
	}

	public WebElement element(By locator) // returns Webelement
	{
		WebElement element;
		try {
			// waitForElementVisible(locator);
			element = config.get(str).findElement(locator);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("No Such Element present  " + e.getLocalizedMessage());
		}

		return element;
	}
	/**
	 * Returns list of elements 
	 * @param locator
	 * @return String
	 */
	public List<WebElement> elements(By locator) // Returns list of webelements
													// for the specified locator
	{
		List<WebElement> elements = config.get(str).findElements(locator);

		return elements;
	}
/**
 * returns element present in table format in html page identified with tag type and searcheable element
 * @param locator
 * @param String
 * @param String
 * @param String
 * @return WebElement
 */
	public WebElement elementInTable(By locator, String tag1, String tag2, String searchitem) 
	{

		WebElement element = element(locator);
		WebElement elementfound = null;
		List<WebElement> list = element.findElements(By.tagName(tag1));
		Iterator<WebElement> it = list.iterator();
		while (it.hasNext()) {
			WebElement tr = it.next();
			List<WebElement> tds = tr.findElements(By.tagName(tag2));
			Iterator<WebElement> i = tds.iterator();
			while (i.hasNext()) {
				WebElement td = i.next();
				String LabelText = td.getText();

				if (LabelText.equals(searchitem)) {
					elementfound = td;
					break;
				}

			}
		}
		return elementfound;
	}

}
