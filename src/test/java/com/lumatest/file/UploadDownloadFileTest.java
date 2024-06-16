package com.lumatest.file;

import com.lumatest.utils.DeleteFileUtils;
import com.lumatest.utils.DriverUtils;
import com.lumatest.utils.ReportUtils;
import com.lumatest.utils.UnzipUtility;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class UploadDownloadFileTest {

    private WebDriver driver;
    private final String chrome = "chrome";

    final String downloadPath = "/LUMAJavaSeleniumTestNGAllure/src/test/resources/";
    final String downloadFileName = "chromedriver_win32.zip";

    @Parameters(chrome)
    @BeforeMethod()
    protected void setupDriver(@Optional(chrome) String browser, ITestResult result) {
        Reporter.log("______________________________________________________________________", true);

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
        }

        Reporter.log("RUN " + result.getMethod().getMethodName(), true);

        this.driver = DriverUtils.createDriver(browser, driver);

        if (this.driver == null) {
            System.exit(1);
        }

        Reporter.log("INFO: " + browser.toUpperCase() + " driver created.", true);
    }

    @Test(testName = "UPLOAD | Upload File",
            description = "TC-04 Verify upload file",
            groups = {"regression"})
    public void testUploadFile() throws InterruptedException {
        final String fileName = "image.jpg";
        File file = new File(downloadPath + fileName);

        driver.get("https://blueimp.github.io/jQuery-File-Upload/");

        WebElement addFile = driver.findElement(By.xpath(".//input[@type='file']"));
        addFile.sendKeys(file.getAbsolutePath());

        Thread.sleep(5000);
        String actualFileToUploadName = driver.findElement(By.xpath("//p[@class = 'name']")).getText();

        Assert.assertEquals(actualFileToUploadName, fileName);

        driver.findElement(By.xpath("//button//span[text()='Start upload']")).click();
        Thread.sleep(3000);

        String actualFileUploadedName = driver.findElement(By.xpath("//p[@class = 'name']")).getText();

        Assert.assertEquals(actualFileUploadedName, fileName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button//span[text()='Delete']")));

        Assert.assertTrue(deleteButton.isDisplayed());

        deleteButton.click();

        Thread.sleep(2000);

        DeleteFileUtils.deleteFile(downloadPath, ".jpg");
    }

    @Test(testName = "DOWNLOAD | Download File",
            description = "TC-05 Verify download file",
            groups = {"regression"})
    public void testDownloadFile() throws AWTException, InterruptedException, IOException {

        driver.get("https://chromedriver.storage.googleapis.com/index.html?path=79.0.3945.36/");
        Thread.sleep(2000);
        WebElement downloadButton = driver.findElement(By.xpath(".//a[text()='chromedriver_win32.zip']"));
        downloadButton.click();

        waitForFileDownload(downloadPath, downloadFileName);

        UnzipUtility unzipper = new UnzipUtility();
        String zipFilePath = downloadPath + downloadFileName;
        unzipper.unzip(zipFilePath, downloadPath);
        System.out.println("Successfully unzipped file: " + zipFilePath);

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(downloadPath + "image.jpg"));

        Thread.sleep(7000);
    }

    @Parameters(chrome)
    @AfterMethod(alwaysRun = true)
    protected void tearDown(@Optional(chrome) String browser, ITestResult result) throws IOException {
        Reporter.log(result.getMethod().getMethodName() + ": " + ReportUtils.getTestStatus(result),
                true);

        if (this.driver != null) {
            this.driver.quit();
            Reporter.log("INFO: " + browser.toUpperCase() + " driver closed.", true);

            this.driver = null;
        } else {
            Reporter.log("INFO: Driver is null.", true);
        }

        DeleteFileUtils.deleteFile(downloadPath, ".zip");
        DeleteFileUtils.deleteFile(downloadPath, ".exe");
    }

    public void waitForFileDownload(String downloadPath, String fileName) {
        Path downloadFilePath = Paths.get(downloadPath, fileName);

        // Wait for the .crdownload file to be created
        while (!Files.exists(downloadFilePath)) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Wait for the .crdownload file to disappear, meaning the download is complete
        while (Files.exists(downloadFilePath.resolveSibling(fileName + ".crdownload"))) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Wait for the final file to be present
        while (!Files.exists(downloadFilePath)) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
