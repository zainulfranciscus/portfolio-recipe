package com.safe.stack.service.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.safe.stack.annotation.DataSets;
import com.safe.stack.domain.Account;
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;

public class AccountServiceImplTest extends AbstractServiceImplTest {

	@Autowired
	AccountService accountService;

	@Autowired
	RecipeService recipeService;

	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/accountTestData.xls")
	@Test
	public void testSave() {

		Account account = accountService.findByUserName("My Company");

		assertNotNull(account);
		assertEquals("My Company", account.getUserName());
		assertEquals("password", account.getPassword());
		assertEquals("mycompany@company", account.getEmail());
		assertEquals("twitter", account.getTwitter());
	}

	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/LikedRecipeTestData.xls")
	@Test
	public void testLikedRecipe() {
		
		Recipe recipe = recipeService.findAll().get(0);
		accountService.saveRecipe("user 1", recipe.getId());
		
		Account account = accountService.findByUserName("user 1");
		Set<Recipe> recipes = account.getLikedRecipes();
		
		assertEquals(1,recipes.size());
		
		Recipe likedRecipe = recipes.iterator().next();
		

	}

}
