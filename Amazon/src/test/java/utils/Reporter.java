
package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Reporter {
    private static Properties prop;
    private static ExtentReports extentReport;

    public static ExtentReports generateExtentReport() {
        return generateExtentReport(null);
    }

    public static ExtentReports generateExtentReport(String reportName) {
        if (extentReport == null) {
            extentReport = createExtentReport(reportName);
        }
        return extentReport;
    }

    private static ExtentReports createExtentReport(String reportName) {
        ExtentReports extentReport = new ExtentReports();

        // Load properties from browser.properties file
        String filepath = System.getProperty("user.dir") + "/config/browser.properties";
        try {
            FileInputStream file = new FileInputStream(filepath);
            prop = new Properties();
            prop.load(file);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        // Get the current timestamp for the report name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata"); // IST timezone
        dateFormat.setTimeZone(istTimeZone);
        String timestamp = dateFormat.format(new Date());

        // Define the report file path with the timestamp and provided report name
        String reportFilePath = System.getProperty("user.dir") + "/reports/";
        if (reportName == null || reportName.isEmpty()) {
            reportName = "Amazon test Report";
        }
        reportFilePath += reportName + "_" + timestamp + ".html";

        File extentReportFile = new File(reportFilePath);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentReportFile);

        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName("Amazon test Report");
        sparkReporter.config().setDocumentTitle("Amazon test Automation Report");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss");

        extentReport.attachReporter(sparkReporter);

        extentReport.setSystemInfo("Application URL", prop.getProperty("url"));
        extentReport.setSystemInfo("Browser Name", prop.getProperty("browserName"));
        extentReport.setSystemInfo("Email", prop.getProperty("validEmail"));
        extentReport.setSystemInfo("Password", prop.getProperty("validPassword"));
        extentReport.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReport.setSystemInfo("Username", System.getProperty("user.name"));
        extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));

        return extentReport;
    }

    public static String captureScreenshotAsBase64(WebDriver driver, String screenshotName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata"); // IST timezone
        dateFormat.setTimeZone(istTimeZone);
        String timestamp = dateFormat.format(new Date());

        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
        byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);

        String base64Screenshot = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(screenshotBytes);
            base64Screenshot = Base64.getEncoder().encodeToString(baos.toByteArray());

            // Save the screenshot to a file for reference
            saveScreenshotToFile(screenshotBytes, screenshotName + "_" + timestamp + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64Screenshot;
    }

    private static void saveScreenshotToFile(byte[] screenshotBytes, String fileName) {
        try {
            String screenshotsDirPath = System.getProperty("user.dir") + "/reports/errorScreenshots/";
            File screenshotsDir = new File(screenshotsDirPath);
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }

            String destinationScreenshotPath = screenshotsDirPath + fileName;
            FileOutputStream outputStream = new FileOutputStream(destinationScreenshotPath);
            outputStream.write(screenshotBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
