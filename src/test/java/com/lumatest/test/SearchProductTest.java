package com.lumatest.test;

import com.lumatest.base.BaseTest;
import com.lumatest.data.TestData;
import com.lumatest.model.HomePage;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchProductTest extends BaseTest {

    @Test(description = "TC-04 Search Product Test",
            groups = {"Smoke", "Regression"})
    public void testSearchProduct() {
        Allure.step("Open Base URL.");
        getDriver().get(TestData.BASE_URL);

        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct(TestData.DRIVEN_BACKPACK_PRODUCT_NAME);

        final String searchProductName = homePage.getRelatedSearchTerms();

        Allure.step(
                "Verify actual '" + TestData.RELATED_SEARCH_TERMS+ "' related to '" + TestData.DRIVEN_BACKPACK_PRODUCT_NAME + "'"
        );
        Assert.assertEquals(searchProductName, TestData.RELATED_SEARCH_TERMS);
    }
}
