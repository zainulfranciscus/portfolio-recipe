package com.safe.stack.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.safe.stack.domain.Account;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;
import com.safe.stack.service.security.RecipeUser;
import com.safe.stack.web.form.Message;

/**
 * A controller intended to redirect user to a web page.
 * 
 * @author Zainul Franciscus
 * 
 */
@RequestMapping("/")
@Controller
public class HomeController {

	//============================================================================
	// Constants
	//============================================================================
	private static final String LIKE = "like";
	private static final String UNLIKE = "unlike";
	protected static final String RECIPE_LIST_PAGE = "recipe/list";
	protected static final String RECIPE_LOGIN_PAGE = "recipe/login";
	protected static final String RECIPE_DETAILS_PAGE = "recipe/detail";
	protected static final String RECIPE_ADD_RECIPE_PAGE = "recipe/addRecipe";
	protected static final String RECIPE_EDIT_PROFILE_PAGE = "recipe/editProfile";
	protected static final String NUM_OF_LIKE_PAGE = "recipe/numOfLikes";

	/**
	 * A message source that is used to display messages on a webpage
	 */
	@Autowired
	private MessageSource messageSource;

	/**
	 * An instance of RecipeService used to retrieve recipes from the database
	 */
	@Autowired
	private RecipeService recipeService;

	/**
	 * An instance of AccountService used to perform user-related operations
	 */
	@Autowired
	private AccountService accountService;

	/**
	 * An instance of LocalValidatorFactoryBean used to validate user input
	 */
	@Autowired
	private LocalValidatorFactoryBean validator;


	/**
	 * Directs user to the recipe list page.
	 * 
	 * @param uiModel
	 *            used as a holder for a list of recipe objects
	 * @return the name of the recipe list view.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showAllRecipes(Model uiModel) {

		if (accountService.isAnonymousUser()) {
			uiModel.addAttribute("recipes", recipeService.findRecipesWithNumOfLikes());
		} else {
			String userName = accountService.getUser().getUsername();
			uiModel.addAttribute("recipes", recipeService.findRecipesWithLlikedIndicator(userName));
		}

		return RECIPE_LIST_PAGE;
	}

	/**
	 * this method is intended as an end-point of a RESTful webservice. The purpose
	 * of this method is to return recipes in the database in a json format.
	 * 
	 * @return every recipe in the database in a json format
	 */
	@RequestMapping(value = "/json/allRecipes", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Recipe> findAllRecipe() {
		return recipeService.findAll();
	}

	/**
	 * @param id
	 *            of a recipe
	 * @param uiModel
	 *            used as a holder of a recipe object
	 * @return user to the page that describes the detail of a recipe
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String showRecipe(@PathVariable("id") Long id, Model uiModel) {

		uiModel.addAttribute("recipe", recipeService.findRecipe(id));
		return RECIPE_DETAILS_PAGE;
	}

	/**
	 * @param request
	 *            an instance of HttpServletRequest
	 * @return user to a page where they click the login button
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String openLoginPage(HttpServletRequest request, Model uiModel) {

		String referrer = request.getHeader("Referer");

		if (StringUtils.isEmpty(referrer)) {
			referrer = "/login";
		}

		request.getSession().setAttribute("url_prior_login", referrer);
		uiModel.addAttribute("account", new Account());
		return RECIPE_LOGIN_PAGE;
	}

	/**
	 * Directs a user who has an admin role to the add recipe page.
	 * 
	 * @param uiModel
	 *            a holder of ingredient types objects and a recipe object
	 * @return
	 */
	@PreAuthorize("hasRole('admin')")
	@RequestMapping(value = "/addRecipe", method = RequestMethod.GET)
	public String showAddRecipe(Model uiModel) {

		List<IngredientType> ingredientTypes = recipeService.findAllIngredientTypes();
		uiModel.addAttribute("ingredientTypes", ingredientTypes);
		uiModel.addAttribute("recipe", new Recipe());
		return RECIPE_ADD_RECIPE_PAGE;
	}

	
	/**
	 * 
	 * @param ingredient
	 *            that a user looks for
	 * @param uiModel
	 *            is used as a holder of recipes
	 * @return user to the recipe list page
	 */
	@RequestMapping(value = "/searchRecipeByIngredient", method = RequestMethod.POST)
	public String searchRecipes(@RequestParam("ingredient") String ingredient, Model uiModel) {
		String[] tokens = ingredient.trim().split("\\s+");
		List<String> ingredients = new ArrayList<String>();

		for (String t : tokens) {
			ingredients.add(t);
		}

		uiModel.addAttribute("recipes", recipeService.findByIngredients(ingredients));
		return RECIPE_LIST_PAGE;
	}

	/**
	 * show a list of recipes that a user likes
	 * 
	 * @param uiModel
	 *            a holder for an account, recipes and a boolean.
	 * @return user to the recipe list page
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/searchLikedRecipe", method = RequestMethod.GET)
	public String showLikedRecipe(Model uiModel) {

		String userName = accountService.getUser().getUsername();

		Account account = accountService.findByEmail(userName);
		uiModel.addAttribute("account", account);
		uiModel.addAttribute("recipes", account.getLikedRecipes());
		uiModel.addAttribute("show_liked_recipes", new Boolean(true));
		return RECIPE_LIST_PAGE;
	}

	/**
	 * set a recipe like indicator to true or false depending on the provided
	 * operation
	 * 
	 * @param recipeId
	 *            an id of a recipe
	 * @param operation
	 *            either like or unlike
	 * @param uiModel
	 *            is used as a holder for a recipe object
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/likeARecipe", method = RequestMethod.POST)
	public String likeARecipe(@RequestParam("recipeId") String recipeId, @RequestParam("operation") String operation, Model uiModel) {

		String userName = accountService.getUser().getUsername();

		if (operation.equalsIgnoreCase(LIKE)) {

			accountService.likeARecipe(userName, Long.parseLong(recipeId));

		} else if (operation.equalsIgnoreCase(UNLIKE)) {

			accountService.unlikeARecipe(userName, Long.parseLong(recipeId));

		}

		uiModel.addAttribute("recipe", recipeService.findRecipe(Long.parseLong(recipeId)));

		return NUM_OF_LIKE_PAGE;

	}

	/**
	 * @param uiModel
	 *            a holder for a Message
	 * @param locale
	 * @return user to a page that display a message that tells the user that
	 *         his login failed.
	 */
	@RequestMapping("/loginfail")
	public String loginFail(Model uiModel, Locale locale, Account acc) {
		uiModel.addAttribute("login_failed", new Message("error", messageSource.getMessage("message_login_fail", new Object[] {}, locale)));
		uiModel.addAttribute("account", acc);
		return RECIPE_LOGIN_PAGE;
	}

	/**
	 * Create a new user account if the provided user name and password is
	 * valid.
	 * 
	 * @param userName
	 *            for a user
	 * @param password
	 *            for a user
	 * @param uiModel
	 *            is used as a holder for validation errors
	 * @param request
	 *            used to store the user into the session
	 * @return user to the edit profile page.
	 */
	@RequestMapping(value = "/signUp", method = RequestMethod.POST, params = "SignUp")
	public String signUp(@Valid Account acc, BindingResult bindingResult, Model uiModel, HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("account", acc);
			return RECIPE_LOGIN_PAGE;
		}

		accountService.signUpAUser(acc.getEmail(), acc.getPassword());
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		request.getSession().setAttribute("RecipeUser", accountService.getUser());

		return "redirect:/editProfile";

	}

	/**
	 * @param uiModel
	 *            is used as a holder for an account object
	 * @param request
	 *            an instance of HttpServletRequest
	 * @return user to the edit profile page
	 */
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public String editProfile(Model uiModel, HttpServletRequest request) {
		RecipeUser recipeUser = (RecipeUser) request.getSession().getAttribute("RecipeUser");

		Account acc = accountService.findByEmail(recipeUser.getUsername());

		uiModel.addAttribute("account", acc);
		return RECIPE_EDIT_PROFILE_PAGE;
	}

	/**
	 * @param account
	 *            of a user
	 * @param uiModel
	 *            is used as a holder for an account object
	 * @param profilePicture
	 * @return user to the edit profile page
	 */
	@RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
	public String saveProfile(Account account, Model uiModel, @RequestParam(value = "file", required = false) Part profilePicture) {

		Set<ConstraintViolation<Account>> violations = validator.validate(account);

		if (violations.size() > 0) {

			uiModel.addAttribute("account_errors", violations);
			return RECIPE_EDIT_PROFILE_PAGE;

		}

		account = accountService.save(account);
		uiModel.addAttribute("account", account);

		return RECIPE_EDIT_PROFILE_PAGE;
	}

}
