package com.lumatest.model;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends TopMenu {

    @FindBy(id = "search")
    private WebElement searchInput;

    @FindBy(xpath = "//div//dl//dt[text()='Related search terms']")
    private WebElement searchResultTerms;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Search Product By Name")
    public void searchProduct(String text) {
        searchInput.sendKeys(text);
        searchInput.sendKeys(Keys.ENTER);
    }

    @Step("Collect Actual Product Name Text.")
    public String getRelatedSearchTerms() {
        return searchResultTerms.getText();
    }
}
