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

    @DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
    @Test
    public void testSave() {

	Account account = accountService.findByEmail("user@recipe.com");

	assertNotNull(account);
	assertEquals("user1", account.getUserName());
	assertEquals("password", account.getPassword());
	assertEquals("user@recipe.com", account.getEmail());
	assertEquals("twitter", account.getTwitter());
	assertEquals("user", account.getAuthority());
    }

    @DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
    @Test
    public void testLikedRecipe() {

	Recipe recipe = recipeService.findAll().get(0);
	accountService.likeARecipe("user@recipe.com", recipe.getId());

	Account account = accountService.findByEmail("user@recipe.com");
	Set<Recipe> recipes = account.getLikedRecipes();

	assertEquals(1, recipes.size());

	Recipe likedRecipe = recipes.iterator().next();

    }

    @DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
    @Test
    public void testUnlikeRecipe() {

	Recipe recipe = recipeService.findAll().get(0);
	accountService.likeARecipe("user@recipe.com", recipe.getId());

	Account account = accountService.findByEmail("user@recipe.com");
	Set<Recipe> recipes = account.getLikedRecipes();

	assertEquals(1, recipes.size());

	Recipe likedRecipe = recipes.iterator().next();
	accountService.unlikeARecipe("user@recipe.com", recipe.getId());

	account = accountService.findByEmail("user@recipe.com");
	recipes = account.getLikedRecipes();

	assertEquals(0, recipes.size());
    }

}
