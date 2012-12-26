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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.safe.stack.domain.Account;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;
import com.safe.stack.service.security.RecipeUser;
import com.safe.stack.web.form.Message;

@RequestMapping("/")
@Controller
public class HomeController {

    private static final String LIKE = "like";
    private static final String UNLIKE = "unlike";
    protected static final String RECIPE_LIST_PAGE = "recipe/list";
    protected static final String RECIPE_LOGIN_PAGE = "recipe/login";
    protected static final String RECIPE_DETAILS_PAGE = "recipe/detail";
    protected static final String RECIPE_ADD_RECIPE_PAGE = "recipe/addRecipe";
    protected static final String RECIPE_EDIT_PROFILE_PAGE = "recipe/editProfile";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private AuthenticationManager authManager;

    @RequestMapping(method = RequestMethod.GET)
    public String showAllRecipes(Model uiModel) {

	uiModel.addAttribute("recipes", recipeService.findAll());

	return RECIPE_LIST_PAGE;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showRecipe(@PathVariable("id") Long id, Model uiModel) {

	uiModel.addAttribute("recipe", recipeService.findRecipe(id));
	return RECIPE_DETAILS_PAGE;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String openLoginPage(Model uiModel, HttpServletRequest request) {

	

	String referrer = request.getHeader("Referer");
	
	request.getSession().setAttribute("url_prior_login", referrer);
	
	return RECIPE_LOGIN_PAGE;
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/addRecipe", method = RequestMethod.GET)
    public String showAddRecipe(Model uiModel) {

	List<IngredientType> ingredientTypes = recipeService.findAllIngredientTypes();
	uiModel.addAttribute("ingredientTypes", ingredientTypes);
	uiModel.addAttribute("recipe", new Recipe());
	return RECIPE_ADD_RECIPE_PAGE;
    }

    @RequestMapping(value = "/saveRecipe", method = RequestMethod.POST)
    public String saveRecipe(Recipe recipe, Model uiModel,
	    @RequestParam(value = "recipePicture", required = false) Part recipePicture,
	    @RequestParam(value = "thumbnail", required = false) Part thumbnail) throws IOException {

	Set<ConstraintViolation<Recipe>> recipeViolations = validator.validate(recipe);

	if (recipeViolations.size() > 0) {

	    uiModel.addAttribute("recipe_errors", recipeViolations);

	}

	List<Ingredient> ingredients = recipe.getIngredients();

	Set<ConstraintViolation<Ingredient>> ingredientViolations = new HashSet<ConstraintViolation<Ingredient>>();

	Set<ConstraintViolation<IngredientType>> ingredientTypeViolations = new HashSet<ConstraintViolation<IngredientType>>();

	Set<String> ingredientNames = new HashSet<String>();

	for (Ingredient ingr : ingredients) {

	    ingredientNames.add(ingr.getIngredientType().getName());

	    if (ingredientViolations.size() == 0) {
		ingredientViolations = validator.validate(ingr);
	    }

	    IngredientType ingrType = ingr.getIngredientType();

	    if (ingredientTypeViolations.size() == 0) {
		ingredientTypeViolations = validator.validate(ingrType);
	    }

	}

	if (ingredientNames.size() > 0 && ingredientNames.size() < ingredients.size()) {
	    ConstraintViolation<IngredientType> violation = new ConstraintViolationImpl<IngredientType>(
		    "{validation.ingredient.duplicate.message}",
		    "A recipe can't have duplicate ingredients", IngredientType.class,
		    new IngredientType(), new Object(), new Object(), null, null,
		    ElementType.ANNOTATION_TYPE);
	    ingredientTypeViolations.add(violation);
	}

	if (ingredientViolations.size() > 0) {
	    uiModel.addAttribute("ingredient_errors", ingredientViolations);

	}

	if (ingredientTypeViolations.size() > 0) {
	    uiModel.addAttribute("ingredientType_errors", ingredientTypeViolations);

	}

	if (recipeViolations.size() > 0 || ingredientViolations.size() > 0
		|| ingredientTypeViolations.size() > 0) {
	    return RECIPE_ADD_RECIPE_PAGE;
	}

	String imgFileName = "";

	if (recipePicture != null) {

	    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss");
	    imgFileName = sdf.format(Calendar.getInstance().getTime()) + ".png";

	    File imgFile = new File("C:/source/Pictures/" + imgFileName);
	    imgFile.createNewFile();

	    OutputStream out = new FileOutputStream(imgFile);
	    IOUtils.copy(recipePicture.getInputStream(), out);
	    out.flush();
	    out.close();

	    File thumbnailFile = new File("C:/source/Pictures/thumb" + imgFileName);
	    thumbnailFile.createNewFile();

	    out = new FileOutputStream(thumbnailFile);
	    IOUtils.copy(thumbnail.getInputStream(), out);
	    out.flush();
	    out.close();

	}

	recipe.setPicture(imgFileName);
	recipeService.save(recipe);

	return RECIPE_LIST_PAGE;
    }

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

    @RequestMapping(value = "/searchLikedRecipe", method = RequestMethod.POST)
    public String showLikedRecipe(@RequestParam("userName") String userName, Model uiModel) {
	Account account = accountService.findByEmail(userName);
	uiModel.addAttribute("account", account);
	return "account";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/likeARecipe", method = RequestMethod.POST)
    public String likeARecipe(@RequestParam("recipeId") String recipeId,
	    @RequestParam("operation") String operation) {

	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	String userName = ((RecipeUser) principal).getUsername();

	if (operation.equalsIgnoreCase(LIKE)) {

	    accountService.likeARecipe(userName, Long.parseLong(recipeId));

	} else if (operation.equalsIgnoreCase(UNLIKE)) {

	    accountService.unlikeARecipe(userName, Long.parseLong(recipeId));

	}

	return RECIPE_LIST_PAGE;

    }

    @RequestMapping("/redirectLogin")
    public String redirectLogin(HttpServletRequest request, HttpServletResponse response) {
	SavedRequest savedRequest = 
		    new HttpSessionRequestCache().getRequest(request, response);
	String url = savedRequest.getRedirectUrl();
	
	return url;
    }

    @RequestMapping("/loginfail")
    public String loginFail(Model uiModel, Locale locale) {
	uiModel.addAttribute(
		"login_failed",
		new Message("error", messageSource.getMessage("message_login_fail",
			new Object[] {}, locale)));
	return RECIPE_LOGIN_PAGE;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST, params = "SignUp")
    public String signUp(@RequestParam("j_username") String userName,
	    @RequestParam("j_password") String password, Model uiModel, Locale locale,
	    HttpServletRequest request) {

	Account acc = new Account();
	acc.setEmail(userName);
	acc.setPassword(password);

	Set<ConstraintViolation<Account>> violations = validator.validate(acc);

	if (violations.size() > 0) {

	    uiModel.addAttribute("valdation_errors", violations);
	    return RECIPE_LOGIN_PAGE;

	}

	accountService.save(acc);

	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
		userName, password);

	Authentication auth = authManager.authenticate(token);
	SecurityContextHolder.getContext().setAuthentication(auth);
	request.getSession().setAttribute(
		HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
		SecurityContextHolder.getContext());

	return RECIPE_LIST_PAGE;

    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.GET)
    public String editProfile(Model uiModel, HttpServletRequest request) {
	RecipeUser recipeUser = (RecipeUser) request.getSession().getAttribute("RecipeUser");
	
	Account acc = accountService.findByEmail(recipeUser.getUsername());

	uiModel.addAttribute("account", acc);
	return RECIPE_EDIT_PROFILE_PAGE;
    }

    @RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
    public String saveProfile(Account account, Model uiModel,
	    @RequestParam(value = "file", required = false) Part file) {

	Set<ConstraintViolation<Account>> violations = validator.validate(account);

	if (violations.size() > 0) {

	    uiModel.addAttribute("account_errors", violations);
	    return RECIPE_EDIT_PROFILE_PAGE;

	}

	accountService.save(account);

	return RECIPE_EDIT_PROFILE_PAGE;
    }

}
