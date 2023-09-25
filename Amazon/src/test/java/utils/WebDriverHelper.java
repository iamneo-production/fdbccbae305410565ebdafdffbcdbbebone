package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverHelper extends Base {
    private WebDriver driver;

    public WebDriverHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void openPage(String url)throws Exception  {
        try {
             driver.get(url);
        } catch (Exception e){
              e.printStackTrace();
            throw new Exception("Error in " + e.getMessage());
        }
       
    }

    public void clickElement(By locator)throws Exception {
        try {
             WebElement element = driver.findElement(locator);
        element.click();
        } catch (Exception e) {
             e.printStackTrace();
            throw new Exception("Error in " + e.getMessage());
        }
       
    }

    public void fillForm(By locator, String text)throws Exception {
        try {
             WebElement element = driver.findElement(locator);
            element.sendKeys(text);
        } catch (Exception e) {
             e.printStackTrace();
            throw new Exception("Error in " + e.getMessage());
        }
       
    }
    
//    public void jsClick(By locator) {
//    	WebElement element = driver.findElement(locator);
//		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10)); 
//		WebElement webelement = wait.until(ExpectedConditions.visibilityOf(element));
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("arguments[0].setAttribute('style', 'background: lightskyblue; border: 2px solid red;');", webelement);
//		//waitFor(2000);
//		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", webelement, "");
//		js.executeScript("arguments[0].click();", webelement);
//	}
    

}
