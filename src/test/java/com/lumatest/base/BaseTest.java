package com.lumatest.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    private WebDriver driver;

    @BeforeClass
    protected void setup() {
        WebDriverManager.chromedriver().setup();

        createChromeDriver();

    }

    @AfterClass(alwaysRun = true)
    protected void tearDown() {
        if (this.driver != null) {
            getDriver().quit();
            this.driver = null;
        }
    }

    private void createChromeDriver() {
        if (this.driver == null) {
            this.driver = new ChromeDriver();
        }
    }

    public WebDriver getDriver() {
        return this.driver;
    }
}
