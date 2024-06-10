package com.lumatest.base;

import com.lumatest.utils.DriverUtils;
import com.lumatest.utils.ReportUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;


public abstract class BaseTest {
    private WebDriver driver;

    @BeforeSuite
    protected void setupWebDriverManager() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.chromiumdriver().setup();
//        WebDriverManager.edgedriver().setup();
//        WebDriverManager.firefoxdriver().setup();

    }

    @Parameters("browser")
    @BeforeMethod
    protected void setupDriver(@Optional("chrome") String browser, ITestResult result) {
        Reporter.log("---------------------------------------------------------", true);
        this.driver = DriverUtils.createDriver(browser, this.driver);
        Reporter.log("RUN " + result.getMethod().getMethodName(), true);

        if (getDriver() == null) {
            Reporter.log("Error: Unknown parameter 'browser' - " + browser + ". ", true);

            System.exit(1);
        }

        Reporter.log("INFO: " + browser.toUpperCase() + " driver created.", true);
    }

    @Parameters("browser")
    @AfterMethod(alwaysRun = true)
    protected void tearDown(@Optional("chrome") String browser, ITestResult result) {
        Reporter.log(result.getMethod().getMethodName() + ": " + ReportUtils.getTestStatus(result),
                true);

        if (getDriver() != null) {

            getDriver().quit();
            Reporter.log("INFO: " + browser.toUpperCase() + " driver closed.", true);

            this.driver = null;
        } else {
            Reporter.log("INFO: Driver is null.", true);
        }
    }

    public WebDriver getDriver() {

        return this.driver;
    }
}
