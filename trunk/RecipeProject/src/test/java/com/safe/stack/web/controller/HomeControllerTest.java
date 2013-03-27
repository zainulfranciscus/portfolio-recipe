package com.safe.stack.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.safe.stack.domain.Account;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;
import com.safe.stack.service.jpa.AccountServiceImpl;
import com.safe.stack.service.security.RecipeUser;

/**
 * This class has a number of methods intended as unit tests for HomeController.
 * 
 * @author Zainul Franciscus
 * 
 */
public class HomeControllerTest extends AbstractControllerTest {

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#showAllRecipes(org.springframework.ui.Model)}
     */
    @Test
    public void testShowAllRecipe() {
	RecipeService recipeService = mock(RecipeService.class);
	AccountService accountService = mock(AccountService.class);

	HomeController homeController = new HomeController();

	ReflectionTestUtils.setField(homeController, "recipeService", recipeService);
	ReflectionTestUtils.setField(homeController, "accountService", accountService);

	RecipeUser user = new RecipeUser("user1", "123456", true, false, false, false, AuthorityUtils.NO_AUTHORITIES);

	when(accountService.isAnonymousUser()).thenReturn(false);
	when(accountService.getUser()).thenReturn(user);

	List<RecipeSummary> recipeList = new ArrayList<RecipeSummary>();
	recipeList.add(new RecipeSummary(1l, "Lamb Stew", "Jamie Oliver", "definitely not vegetarian", 100L, "www.jamieoliver.com", "stew_boiling.jpg"));

	when(recipeService.findRecipesWithLlikedIndicator("user1")).thenReturn(recipeList);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.showAllRecipes(uiModel);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_LIST_PAGE);

	List<Recipe> modelRecipe = (List<Recipe>) uiModel.get("recipes");

	assertEquals(1, modelRecipe.size());
    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#showRecipe(Long, org.springframework.ui.Model)}
     */
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
	assertEquals(result, HomeController.RECIPE_DETAILS_PAGE);

	assertEquals(aRecipe, uiModel.get("recipe"));

    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#searchRecipes(String, org.springframework.ui.Model)}
     */
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
	assertEquals(result, "recipe/list");

	assertEquals(recipes, uiModel.get("recipes"));

    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#showLikedRecipe(org.springframework.ui.Model)}
     */
    @Test
    public void testShowLikedRecipe() {
	AccountService accountService = mock(AccountService.class);
	HomeController homeController = new HomeController();

	String userName = "user1";
	Set<Recipe> likedRecipe = new HashSet<Recipe>();

	Account account = new Account();
	account.setUserName(userName);
	account.setLikedRecipes(likedRecipe);

	when(accountService.findByEmail(userName)).thenReturn(account);

	ReflectionTestUtils.setField(homeController, "accountService", accountService);

	RecipeUser user = new RecipeUser("user1", "123456", true, false, false, false, AuthorityUtils.NO_AUTHORITIES);
	when(accountService.getUser()).thenReturn(user);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.showLikedRecipe(uiModel);

	assertNotNull(result);
	assertEquals(HomeController.RECIPE_LIST_PAGE, result);

	assertEquals(account, uiModel.get("account"));

    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#likeARecipe(String, String, org.springframework.ui.Model)}
     */
    @Test
    public void testLikeARecipe() {

	AccountService accountService = mock(AccountService.class);
	RecipeService recipeService = mock(RecipeService.class);

	HomeController homeController = new HomeController();

	String userName = "user1";
	Long recipeId = 1l;

	Recipe recipe = new Recipe();
	recipe.setId(recipeId);

	ReflectionTestUtils.setField(homeController, "accountService", accountService);

	when(recipeService.findRecipe(recipeId)).thenReturn(recipe);

	ReflectionTestUtils.setField(homeController, "recipeService", recipeService);

	RecipeUser user = new RecipeUser("user1", "123456", true, false, false, false, AuthorityUtils.NO_AUTHORITIES);
	when(accountService.getUser()).thenReturn(user);

	ExtendedModelMap uiModel = new ExtendedModelMap();

	homeController.likeARecipe(String.valueOf(recipeId), "like", uiModel);

	assertEquals(recipe, uiModel.get("recipe"));

	Mockito.verify(accountService).likeARecipe(userName, recipeId);

    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#loginFail(org.springframework.ui.Model, Locale)}
     */
    @Test
    public void testLoginFail() {
	HomeController homeController = new HomeController();
	ExtendedModelMap uiModel = new ExtendedModelMap();

	MessageSource mockMessageSource = mock(MessageSource.class);
	when(mockMessageSource.getMessage("message_login_fail", new Object[] {}, Locale.UK)).thenReturn("Invalid user name or password");

	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);

	String result = homeController.loginFail(uiModel, Locale.UK);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_LOGIN_PAGE);
    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#signUp(String, String, org.springframework.ui.Model, HttpServletRequest)}
     */
    @Test
    public void testSignUp() {
	HomeController homeController = new HomeController();
	AccountServiceImpl mockAccountService = mock(AccountServiceImpl.class);

	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);

	when(mockValidator.validate(Mockito.isA(Account.class))).thenReturn(new HashSet<ConstraintViolation<Account>>());
	
	ReflectionTestUtils.setField(homeController, "accountService", mockAccountService);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);

	ExtendedModelMap uiModel = new ExtendedModelMap();

	Account acc = new Account();
	acc.setUserName("user1");
	acc.setPassword("password");
	
	String result = homeController.signUp(acc, new DataBinder(acc).getBindingResult(), uiModel, new MockHttpServletRequest());
	Mockito.doCallRealMethod().when(mockAccountService).signUpAUser(eq("user1"), eq("password"));
	

	assertNotNull(result);
	assertNull(uiModel.get("message"));
	assertEquals("redirect:/editProfile", result);
    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#signUp(String, String, org.springframework.ui.Model, HttpServletRequest)}
     * when a user enters an invalid login credentials
     */
    @Test
    public void testInvalidSignUp() {

	HomeController homeController = new HomeController();
	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);
	MessageSource mockMessageSource = mock(MessageSource.class);
	AccountService mockAccService = mock(AccountService.class);
	
	when(mockMessageSource.getMessage("{validation.email.invalid.format}", new Object[] {}, Locale.UK)).thenReturn("This is not a valid email address");

	ConstraintViolation<Account> violation = new ConstraintViolationImpl<Account>("{validation.email.invalid.format}", "This is not a valid email address", Account.class, new Account(),
		new Object(), new Object(), null, null, ElementType.ANNOTATION_TYPE);

	Set<ConstraintViolation<Account>> violations = new HashSet<ConstraintViolation<Account>>();
	violations.add(violation);

	when(mockValidator.validate(Mockito.isA(Account.class))).thenReturn(violations);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);
	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);
	ReflectionTestUtils.setField(homeController, "accountService", mockAccService);
	
	ExtendedModelMap uiModel = new ExtendedModelMap();

	Account acc = new Account();
	acc.setUserName("user1");
	acc.setPassword("password");
	
	BindingResult bindingResult = new DataBinder(acc).getBindingResult();
	bindingResult.reject("valdation_errors");
	
	String result = homeController.signUp(acc, bindingResult, uiModel, new MockHttpServletRequest());

	assertNotNull(result);
	assertEquals(HomeController.RECIPE_LOGIN_PAGE, result);
	assertEquals(acc,(Account) uiModel.get("account"));
	

    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#saveRecipe(Recipe, org.springframework.ui.Model, javax.servlet.http.Part, javax.servlet.http.Part)}
     */
    @Test
    public void testSaveRecipe() throws IOException {
	HomeController homeController = new HomeController();
	RecipeService mockRecipeService = mock(RecipeService.class);
	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);

	when(mockValidator.validate(Mockito.isA(Recipe.class))).thenReturn(new HashSet<ConstraintViolation<Recipe>>());
	when(mockValidator.validate(Mockito.isA(Ingredient.class))).thenReturn(new HashSet<ConstraintViolation<Ingredient>>());

	Recipe recipe = new Recipe();

	ReflectionTestUtils.setField(homeController, "recipeService", mockRecipeService);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.saveRecipe(recipe, uiModel, null, null);

	Mockito.verify(mockRecipeService).save(recipe);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_LIST_PAGE);
    }

    /**
     * A test for
     * {@link com.safe.stack.web.controller.HomeController#saveRecipe(Recipe, org.springframework.ui.Model, javax.servlet.http.Part, javax.servlet.http.Part)}
     * when user records duplicate ingredients
     */
    @Test
    public void testSavingDuplicateIngredient() throws IOException {
	HomeController homeController = new HomeController();

	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);
	MessageSource mockMessageSource = mock(MessageSource.class);

	Recipe recipe = new Recipe();
	recipe.setAuthor("me");
	recipe.setName("fried rice");

	IngredientType type1 = new IngredientType();
	type1.setName("salt");

	IngredientType type2 = new IngredientType();
	type2.setName("salt");

	Ingredient ingr1 = new Ingredient();
	ingr1.setIngredientType(type1);

	Ingredient ingr2 = new Ingredient();
	ingr2.setIngredientType(type2);

	List<Ingredient> ingredients = new ArrayList<Ingredient>();
	ingredients.add(ingr1);
	ingredients.add(ingr2);

	recipe.setIngredients(ingredients);

	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.saveRecipe(recipe, uiModel, null, null);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_ADD_RECIPE_PAGE);

	Set<ConstraintViolation<Ingredient>> errors = (Set<ConstraintViolation<Ingredient>>) uiModel.get("ingredientType_errors");

	assertNotNull(errors);
	assertEquals("A recipe can't have duplicate ingredients", errors.iterator().next().getMessage());
    }

    @Test
    public void testSaveInvalidRecipe() throws IOException {
	HomeController homeController = new HomeController();

	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);
	MessageSource mockMessageSource = mock(MessageSource.class);

	when(mockMessageSource.getMessage("{validation.ingredient.NotEmpty.message}", new Object[] {}, Locale.UK)).thenReturn("Please specify an ingredient");

	ConstraintViolation<Ingredient> violation = new ConstraintViolationImpl<Ingredient>("{validation.ingredient.NotEmpty.message}", "Please specify an ingredient", Ingredient.class,
		new Ingredient(), new Object(), new Object(), null, null, ElementType.ANNOTATION_TYPE);

	Set<ConstraintViolation<Ingredient>> violations = new HashSet<ConstraintViolation<Ingredient>>();
	violations.add(violation);

	when(mockValidator.validate(Mockito.isA(Ingredient.class))).thenReturn(violations);

	Recipe recipe = new Recipe();
	recipe.setAuthor("me");
	recipe.setName("fried rice");

	IngredientType ingrType = new IngredientType();
	ingrType.setName("salt");

	Ingredient ingr = new Ingredient();
	ingr.setAmount("5");
	ingr.setIngredientType(ingrType);

	List<Ingredient> ingredients = new ArrayList<Ingredient>();
	ingredients.add(ingr);

	recipe.setIngredients(ingredients);

	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.saveRecipe(recipe, uiModel, null, null);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_ADD_RECIPE_PAGE);

	Set<ConstraintViolation<Ingredient>> errors = (Set<ConstraintViolation<Ingredient>>) uiModel.get("ingredient_errors");

	assertNotNull(errors);
	assertEquals("Please specify an ingredient", errors.iterator().next().getMessage());
    }

    @Test
    public void testEditProfile() {
	ExtendedModelMap uiModel = new ExtendedModelMap();
	HttpServletRequest request = new MockHttpServletRequest();
	RecipeUser recipeUser = new RecipeUser("a@email.com", "password", true, true, true, true, new ArrayList<GrantedAuthority>());
	AccountService mockAccountService = mock(AccountService.class);

	Account userAccount = new Account();
	userAccount.setEmail("a@email.com");
	when(mockAccountService.findByEmail("a@email.com")).thenReturn(userAccount);
	request.getSession().setAttribute("RecipeUser", recipeUser);

	HomeController homeController = new HomeController();

	ReflectionTestUtils.setField(homeController, "accountService", mockAccountService);

	String result = homeController.editProfile(uiModel, request);

	Account acc = (Account) uiModel.get("account");
	assertEquals("a@email.com", acc.getEmail());
	assertEquals(HomeController.RECIPE_EDIT_PROFILE_PAGE, result);
    }

    @Test
    public void testSaveInvalidProfile() {
	LocalValidatorFactoryBean mockValidator = mock(LocalValidatorFactoryBean.class);
	MessageSource mockMessageSource = mock(MessageSource.class);

	when(mockMessageSource.getMessage("{validation.password.invalid.format}", new Object[] {}, Locale.UK)).thenReturn(
		"A password must be at least 6 characters with at least a number and a capital letter");

	ConstraintViolation<Account> violation = new ConstraintViolationImpl<Account>("{validation.password.invalid.format}",
		"A password must be at least 6 characters with at least a number and a capital letter", Account.class, new Account(), new Object(), new Object(), null, null,
		ElementType.ANNOTATION_TYPE);

	Set<ConstraintViolation<Account>> violations = new HashSet<ConstraintViolation<Account>>();
	violations.add(violation);

	when(mockValidator.validate(Mockito.isA(Account.class))).thenReturn(violations);

	Account account = new Account();
	account.setEmail("email@email.com");
	account.setPassword("invalidpassword");
	account.setUserName("userName");

	HomeController homeController = new HomeController();

	ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);
	ReflectionTestUtils.setField(homeController, "validator", mockValidator);

	ExtendedModelMap uiModel = new ExtendedModelMap();
	String result = homeController.saveProfile(account, uiModel, null);

	assertNotNull(result);
	assertEquals(result, HomeController.RECIPE_EDIT_PROFILE_PAGE);

	Set<ConstraintViolation<Account>> errors = (Set<ConstraintViolation<Account>>) uiModel.get("account_errors");

	assertNotNull(errors);
	assertEquals("A password must be at least 6 characters with at least a number and a capital letter", errors.iterator().next().getMessage());

    }
}
