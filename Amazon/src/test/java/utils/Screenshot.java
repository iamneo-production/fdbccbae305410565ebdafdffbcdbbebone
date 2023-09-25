package utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Screenshot {
    public static String getScreenShot(WebDriver driver, String fileName) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date();
        
        String directoryPath = System.getProperty("user.dir") + "/screenshot/";
        File directory = new File(directoryPath);
        
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destination = directoryPath + fileName + "-" + dateFormat.format(date) + ".png";
        File target = new File(destination);
        FileUtils.copyFile(scrFile, target);
        return destination;
    }
}
