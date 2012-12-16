package com.safe.stack.test.ui;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.thoughtworks.selenium.SeleneseTestBase;

public class UIRecipeListTest extends SeleneseTestBase {

    @Before
    public void setup() {

	WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File(
		"C:\\Program Files (x86)\\Mozilla Firefox 3.6\\firefox.exe")), new FirefoxProfile());

	String baseUrl = "http://localhost:8080/RecipeProject/";
	selenium = new WebDriverBackedSelenium(driver, baseUrl);
    }

    @Test
    public void testShowRecipes() {
	assertTrue(selenium.isTextPresent("search"));
	assertTrue(selenium.isElementPresent("searchText"));

//	selenium.waitForPageToLoad("90000");
//	selenium.type("searchText", "salt");
    }

    @Test
    public void testSearch() {
	selenium.type("searchText", "salt");
	verifyTrue(selenium.isTextPresent("No Recipe found with these Ingredients"));

    }

    @After
    public void tearDown() throws Exception {
	selenium.stop();

    }

}
