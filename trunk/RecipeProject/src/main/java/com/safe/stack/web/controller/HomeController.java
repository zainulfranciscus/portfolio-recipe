package com.safe.stack.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.safe.stack.domain.Account;
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

	// ============================================================================
	// Constants
	// ============================================================================
	private static final String LIKE = "like";
	private static final String UNLIKE = "unlike";
	protected static final String RECIPE_LIST_PAGE = "recipe/list";
	protected static final String RECIPE_LOGIN_PAGE = "recipe/login";
	protected static final String RECIPE_DETAILS_PAGE = "recipe/detail";
	protected static final String RECIPE_ADD_RECIPE_PAGE = "recipe/addRecipe";
	protected static final String RECIPE_EDIT_PROFILE_PAGE = "recipe/editProfile";
	protected static final String NUM_OF_LIKE_PAGE = "recipe/numOfLikes";

	/**
	 * This variable is intended to record a name of a file hosted in a server.
	 * The @Value annotation is used to retrieve the file name, which is stored
	 * in a properties file.
	 */
	@Value("${resources.file}")
	private String file;
	
	/**
	 * A message source that is used to display messages on a webpage
	 */
	@Autowired
	private MessageSource messageSource;


	/**
	 * An instance of RecipeService used to retrieve recipes from the database.
	 * The Autowired annotation indicates that Spring will inject an
	 * implementation of a RecipeService into this variable.
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
	 * this method is intended as an end-point of a RESTful webservice. The
	 * purpose of this method is to return recipes in the database in a json
	 * format.
	 * 
	 * URL of this method is intercepted by spring security. 
	 * 
	 * <http pattern='/json/**' create-session="stateless">
	 *	<intercept-url pattern='/json/**' access='ROLE_USER' />
	 *	<http-basic />
	 *  <custom-filter ref="digestFilter" after="BASIC_AUTH_FILTER" />
	 * </http>
	 * 
	 * Client must provide a user name and password to access this URL. The password
	 * will be hashed by a server provided nonce before sending the password over the 
	 * network
	 * 
	 * The value variable of the RequestMapping annotation records an HTTP path that this method handles. The
	 * dispatcher servlet will call this method for any HTTP path that ends with
	 * /json/allRecipes.
	 * 
	 * @return every recipe in the database in a json format. This is indeicated
	 *         by the produces variable. MediaType.Application_JSON_VALUE is
	 *         equivalent to 'application/json'
	 */
	@RequestMapping(value = "/json/allRecipes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<Recipe> findAllRecipe(@RequestParam(value = "pageNumber", required = false) int pageNumber, @RequestParam(value = "numOfDataPerPage", required = false) int numOfData) {

		return recipeService.findAll(pageNumber, numOfData);
	}
	
	/**
	 * The purpose of this method is to provide a way for user to download a
	 * file stored in a server. Access to this file is restricted to registered user of the website.
	 * Therefore, this file cannot be serve as a static resource in the server. Instead, this method
	 * need to be written for downloading the file. 
	 * 
	 * Access restriction is achieved by recording the URL of this method within a spring security XML file:
	 * 
	 * <http pattern='/getFile' create-session="stateless">
	 *	<intercept-url pattern='/getFile' access='ROLE_USER' />
	 *	<http-basic />
	 *	<custom-filter ref="digestFilter" after="BASIC_AUTH_FILTER" />
	 *  </http>
	 * 
	 * Whenever a client access this URL, Spring will ask the client to specify 
	 * a user name and password. The password
	 * will be hashed by a server provided nonce before sending the password over the 
	 * network
	 * 
	 * The value in the RequestMapping annotation records an HTTP path that this method handles. The
	 * dispatcher servlet will call this method for any HTTP path that ends with
	 * /getFile. The produces variable indicates that this method will return a
	 * binary file. MediaType.APPLICATION_OCTET_STREAM_VALUE is equivalent to
	 * "application/octet-stream", which is A MIME attachment with the content
	 * type is a binary file
	 * 
	 * @param response
	 *            is used to write the content of the binary file to an output
	 *            stream
	 * @throws IOException
	 */
	@RequestMapping(value = "/getFile", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody
	void getFile(final HttpServletResponse response) throws IOException {

		
		InputSupplier<FileInputStream> inputStreamSupplier = Files.newInputStreamSupplier(new File(file));
		
		/**
		 * This method uses Guava's ByteStream to read the file. There are 2 benefits for using ByteStream.
		 * The first benefit is that ByteStream takes care of closing the input stream. Therefore omitting
		 * try-catch-finally code. The ByteStream also reads the file in 4KB chunks, and uses a ByteProcessor
		 * to immediately write that byte into an output stream.
		 */
		ByteStreams.readBytes(inputStreamSupplier, new ByteProcessor<Byte>() {
			
			/** 
			 * The file could be very huge ( > 5 GB). Therefore, the whole file
			 * should not be loaded into the memory. Instead, for every byte read, 
			 * that byte will be written directly to the response output stream.
			 */
			public boolean processBytes(byte[] b, int off, int len) throws IOException{
				
				OutputStream os = response.getOutputStream();				
			    os.write(b, 0, len);
				return true;
				
			}
			public Byte getResult() {
				return null;
				
			}
		});
		
	}

	@Value("${resources.file}")
	private String dir;
	
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
