package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.EventHandler;
import io.github.bonigarcia.wdm.WebDriverManager;


public class Base {
    public static WebDriver driver;
    public final static int IMPLICIT_WAIT_TIME = 10;
    public final static int PAGE_LOAD_TIME = 10;
    public static Properties prop;
    public static java.util.logging.Logger log = LoggerHandler.log;
    public static WebElement element = null;
   // public static WebDriverWait exwait = null;
    ExcelReader file = new ExcelReader();

    public Base() {
        String filepath = System.getProperty("user.dir") + "/config/browser.properties";
        try {
            FileInputStream file = new FileInputStream(filepath);
            prop = new Properties();
            prop.load(file);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static WebDriver openBrowser() throws MalformedURLException {
        String browsername = prop.getProperty("browsername");
        if (browsername.equalsIgnoreCase("chrome")) {
            DesiredCapabilities dc=new DesiredCapabilities();
            dc.setBrowserName("chrome");
			driver = new RemoteWebDriver(new URL("http://34.85.201.58:4450/"), dc);
            // WebDriverManager.chromedriver().setup();
            // driver = new ChromeDriver();
        } else if (browsername.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browsername.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browsername.equalsIgnoreCase("ie")) {
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();
        } else if (browsername.equalsIgnoreCase("safari")) {
            // SafariDriver does not require setup, and it works only on macOS.
            driver = new SafariDriver();
        }
        driver.manage().window().maximize();
        log.info("Browser Launched");
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIME, TimeUnit.SECONDS);
        driver.get(prop.getProperty("url"));
        log.info("Browser Loaded the URL");

       WebDriverListener listener = new EventHandler();
       driver = new EventFiringDecorator<>(listener).decorate(driver);
        return driver;
    }
    public static void ClickOnElement(By element) {
		
	//exwait = new WebDriverWait(driver, 60);
	//	WebElement webelement = exwait.until(ExpectedConditions.visibilityOf(element));
    //element.click();
    	String elementName= driver.findElement(element).getText();
        log.info("Clicked on "+elementName);
    	driver.findElement(element).click();
    	
	}
    public static void sendKeys(By element, String data) {
		
    	driver.findElement(element).sendKeys(data);
    	System.out.println("Enter Data");
	}
    public String ReadData(String SheetName, int RowNum, String ColumnName) throws InvalidFormatException, IOException {
    	List<Map<String, String>> testData = file.getData("./testdata/Testdata.xlsx", SheetName);
    	String Data=testData.get(RowNum).get(ColumnName);
		log.info("Data Picked from SheetName: "+SheetName+", RowNumber: "+RowNum+"and ColumnName: "+ColumnName+": " +Data);
		log.warning(Data);
		return Data;
    }
    public static String getText(By element) {
    	String text = driver.findElement(element).getText();
		return text;
    	
    }
    public void jsClick(By locator) {
    	WebElement element = driver.findElement(locator);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10)); 
		WebElement webelement = wait.until(ExpectedConditions.visibilityOf(element));
		//WebElement webelement = wait.until(ExpectedConditions.elementToBeClickable(element));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: lightskyblue; border: 2px solid red;');", element);
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		js.executeScript("arguments[0].click();", element);
		String elementName= driver.findElement(locator).getText();
    	log.info("Clicked on "+elementName);
	}
    
    public void javascriptScroll(By locator) {
    	WebElement element = driver.findElement(locator);
    	WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10)); 
		WebElement webelement = wait.until(ExpectedConditions.visibilityOf(element));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: lightskyblue; border: 2px solid red;');", element);
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		js.executeScript("arguments[0].scrollIntoView();", element);
	}
    
    public static void switchToNewWindow() throws Throwable {
		// killChromeSession();
		try {
			Set<String> windowNames = driver.getWindowHandles();
			for (String windowName : windowNames) {
				if (windowName != null) {
					driver.switchTo().window(windowName);
					//System.out.println("switched to new window");
				} else {
					throw new Exception("New window could not be retrived");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

