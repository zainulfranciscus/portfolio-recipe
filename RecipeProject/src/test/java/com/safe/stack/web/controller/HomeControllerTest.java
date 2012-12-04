package com.safe.stack.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.safe.stack.domain.Account;
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;
import com.safe.stack.web.form.Message;

public class HomeControllerTest extends AbstractControllerTest {

    @Test
    public void testShowAllRecipe() {
	RecipeService recipeService = mock(RecipeService.class);
	HomeController homeController = new HomeController();

	List<Recipe> recipeList = new ArrayList<Recipe>();
	recipeList.add(new Recipe());

	when(recipeService.findAll()).thenReturn(recipeList);
	ReflectionTestUtils.setField(homeController, "recipeService", recipeService);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.showAllRecipes(uiModel);

	assertNotNull(result);
	assertEquals(result, "recipe/list");

	List<Recipe> modelRecipe = (List<Recipe>) uiModel.get("recipes");

	assertEquals(1, modelRecipe.size());
    }

    @Test
    public void testShowRecipe() {
	RecipeService recipeService = mock(RecipeService.class);
	HomeController homeController = new HomeController();

	Long id = 1l;
	Recipe aRecipe = new Recipe();
	when(recipeService.findRecipe(id)).thenReturn(aRecipe);

	ReflectionTestUtils.setField(homeController, "recipeService", recipeService);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.showRecipe(1l, uiModel);

	assertNotNull(result);
	assertEquals(result, "recipe");

	assertEquals(aRecipe, uiModel.get("recipe"));

    }

    @Test
    public void testSearchRecipes() {

	String ingredient = "spinach beef";

	List<String> ingredients = new ArrayList<String>();
	ingredients.add("spinach");
	ingredients.add("beef");

	Recipe spinachBeefRecipe = new Recipe();

	List<Recipe> recipes = new ArrayList<Recipe>();
	recipes.add(spinachBeefRecipe);

	RecipeService recipeService = mock(RecipeService.class);
	when(recipeService.findByIngredients(ingredients)).thenReturn(recipes);

	HomeController homeController = new HomeController();
	ReflectionTestUtils.setField(homeController, "recipeService", recipeService);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.searchRecipes(ingredient, uiModel);

	assertNotNull(result);
	assertEquals(result, "list");

	assertEquals(recipes, uiModel.get("recipes"));

    }

    @Test
    public void testShowLikedRecipe() {
	AccountService accountService = mock(AccountService.class);
	HomeController homeController = new HomeController();

	String userName = "user1";
	Set<Recipe> likedRecipe = new HashSet<Recipe>();

	Account account = new Account();
	account.setUserName(userName);
	account.setLikedRecipes(likedRecipe);

	when(accountService.findByUserName(userName)).thenReturn(account);

	ReflectionTestUtils.setField(homeController, "accountService", accountService);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.showLikedRecipe(userName, uiModel);

	assertNotNull(result);
	assertEquals(result, "account");

	assertEquals(account, uiModel.get("account"));

    }

    @Test
    public void testLikeARecipe() {
	AccountService accountService = mock(AccountService.class);
	HomeController homeController = new HomeController();

	String userName = "user1";
	Long recipeId = 1l;

	Recipe recipe = new Recipe();
	recipe.setId(recipeId);

	ReflectionTestUtils.setField(homeController, "accountService", accountService);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.likeARecipe(userName, "1");

	Mockito.verify(accountService).likeARecipe(userName, recipeId);

	assertNotNull(result);
	assertEquals(result, "recipe");

    }

    @Test
    public void testLoginFail() {
	HomeController homeController = new HomeController();
	ExtendedModelMap uiModel = new ExtendedModelMap();

	MessageSource mockMessageSource = mock(MessageSource.class);
	when(mockMessageSource.getMessage("message_login_fail", new Object[] {}, Locale.UK))
		.thenReturn("Invalid user name or password");

	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);

	String result = homeController.loginFail(uiModel, Locale.UK);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_LOGIN_PAGE);
    }

    @Test
    public void testSignUp() {
	HomeController homeController = new HomeController();
	AccountService mockAccountService = mock(AccountService.class);
	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);

	when(mockValidator.validate(Mockito.isA(Account.class))).thenReturn(
		new HashSet<ConstraintViolation<Account>>());

	ReflectionTestUtils.setField(homeController, "accountService", mockAccountService);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);

	ExtendedModelMap uiModel = new ExtendedModelMap();

	String result = homeController.signUp("user1", "password", uiModel, Locale.UK);
	Mockito.verify(mockAccountService).save(Mockito.isA(Account.class));

	assertNotNull(result);
	assertNull(uiModel.get("message"));
	assertEquals(result, HomeController.RECIPE_LIST_PAGE);
    }

    @Test
    public void testInvalidSignUp() {

	HomeController homeController = new HomeController();
	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);
	MessageSource mockMessageSource = mock(MessageSource.class);

	when(
		mockMessageSource.getMessage("{validation.email.invalid.format}", new Object[] {},
			Locale.UK)).thenReturn("This is not a valid email address");

	ConstraintViolation<Account> violation = new ConstraintViolationImpl<Account>(
		"{validation.email.invalid.format}", "This is not a valid email address",
		Account.class, new Account(), new Object(), new Object(), null, null,
		ElementType.ANNOTATION_TYPE);

	Set<ConstraintViolation<Account>> violations = new HashSet<ConstraintViolation<Account>>();
	violations.add(violation);

	when(mockValidator.validate(Mockito.isA(Account.class))).thenReturn(violations);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);
	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);

	ExtendedModelMap uiModel = new ExtendedModelMap();

	String result = homeController.signUp("user1", "password", uiModel, Locale.UK);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_LOGIN_PAGE);

	Set<ConstraintViolation<Account>> errors = (Set<ConstraintViolation<Account>>) uiModel
		.get("valdation_errors");

	assertNotNull(errors);
	assertEquals("This is not a valid email address", errors.iterator().next().getMessage());

    }
}
