package sample.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class Browser {
	 WebDriver driver = null;
	static Logger log;

	public static HashMap<String, WebDriver> config = new HashMap<String, WebDriver>();
	public  static ThreadLocal<Map<String, WebDriver>> webdriver= new ThreadLocal<Map<String, WebDriver>>();
	public static String File, screenshotPath, imagepath, importfilepath, OutputPath, home;
	public static String chromedriver, iedriver, chromebinary, locator;
	static Properties prop;
	public String str;
	static ExcelReader reader ;
	public static HashMap<String, ArrayList<String>> appName = new HashMap<String, ArrayList<String>>();

	/**
	 *  File , chromedriver.exe ,
	 * iedriver.exe location are mentioned on config.properties file.
	 */
	@BeforeSuite
	public void setup() throws IOException {
		log = Logger.getLogger(Browser.class);
		prop = new Properties();
		prop.setProperty("ws.home", "${basedir}");
		FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "\\Data\\config.properties");
		prop.load(ip);
		home = prop.getProperty("home");
		File = home + prop.getProperty("File");
		chromedriver = home + prop.getProperty("chromedriver");
		screenshotPath = home + prop.getProperty("screenshotPath");
		locator = home + prop.getProperty("LocatorFile");
		OutputPath = home + prop.getProperty("OutputPath");
	}

	/**
	 * setDriver() reads the browser name from xml file and activate the
	 * browser mentioned on test data sheet 
	 * @param browser
	 * @throws IOException
	 */
	@BeforeTest
	@Parameters("browser")
	public void browserSetup(String browser) throws IOException {
		reader = new ExcelReader();
		reader.testDataFile(File);
		try {
			str = browser;
			if (str.equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
				config.put(str, driver);
				webdriver.set(config);
				driver.manage().window().maximize();
				System.out.println("firefox driver selected");				
				log.info("Firefox browser started");
			}
			if (str.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", chromedriver);
				driver = new ChromeDriver();
				config.put(str, driver);
				webdriver.set(config);
				driver.manage().window().maximize();
				System.out.println("Chrome driver selected");
				log.info("Firefox browser started");
			}
			if (str.equalsIgnoreCase("iexplorer")) {
				System.setProperty("webdriver.ie.driver", iedriver);
				DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
				caps.setCapability("ignoreZoomSetting", true);
				// Delete Browser Cache since IE does not open a clean profile
				// unlike Chrome & FireFox
				try {
					Runtime.getRuntime().exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");
				} catch (IOException e) {
					e.printStackTrace();
				}
				driver = new InternetExplorerDriver(caps);
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
				config.put(str, driver);
				

			}
			if (str.toLowerCase().equals("safari")) {
				driver = new SafariDriver();
				config.put(str, driver);
				System.out.println("safari driver selected");
				driver.manage().window().maximize();				
				System.out.println("safari");
			}

			if (str.toLowerCase().equals("grid")) {
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setBrowserName("firefox");
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			}
			
			reader.testDataFile(locator);
			ArrayList<String> keyList = reader.dataProviderByRow("appname", "LocatorKey");
			for (int i = 0; i < keyList.size(); i++) {
				appName.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i + 1, "appname"));
			}
		}

		catch (NullPointerException e) {
			e.printStackTrace();
			log.info("Unable to start the browser");
		}

	}



	@AfterSuite

	public void driverClose() throws IOException {
		Set<String> set = Browser.config.keySet();
	 	for(String s:set)
	 	{
		config.get(s).close();
	 	}
	}

	@AfterSuite
	public void sendMail() throws IOException {
		reader.testDataFile(File);
		final String mailto = reader.readFromColumn("Base", 1, 3);
		final String psswrd = reader.readFromColumn("Base", 1, 4);
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailto, psswrd);
			}
		});
		try {

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailto));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailto));
			message.setSubject("Test Report");

			// create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// fill message
			messageBodyPart.setText("Please Check Attachment for test report");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			String fileAttachment = OutputPath;
			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(fileAttachment);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileAttachment);
			multipart.addBodyPart(messageBodyPart);
			// Put parts in message
			message.setContent(multipart);
			// Send the message
			Transport.send(message);
			System.out.println("message sent....");
			reader.closeFile();
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}


}
