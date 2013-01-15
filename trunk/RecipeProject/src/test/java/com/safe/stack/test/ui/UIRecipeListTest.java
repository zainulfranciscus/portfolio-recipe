package com.safe.stack.test.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.SeleneseTestBase;

@Ignore
public class UIRecipeListTest extends SeleneseTestBase {

    private WebDriver driver;

    @Before
    public void setup() {

	driver = new FirefoxDriver();
	driver.get("http://localhost:8080/RecipeProject/");
    }

    @Test
    public void testSignIn() {
	
	selenium.waitForPageToLoad("10000000");
	assertTrue(selenium.isElementPresent("id=signin"));
	
	WebElement signinButton = driver.findElement(new By.ById("signin"));
	signinButton.click();
	
	
	
	
    }

    @After
    public void tearDown() throws Exception {
	driver.quit();

    }

}
