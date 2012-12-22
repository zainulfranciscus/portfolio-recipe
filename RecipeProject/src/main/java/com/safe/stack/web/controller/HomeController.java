package com.safe.stack.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
	
	List<Recipe> list = new ArrayList<Recipe>();
	for(int i = 0; i < 10; i++)
	{
	    list.add(recipeService.findAll().get(0));
	}
	uiModel.addAttribute("recipes", list);
	//uiModel.addAttribute("recipes", recipeService.findAll());
	return RECIPE_LIST_PAGE;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showRecipe(@PathVariable("id") Long id, Model uiModel) {
	uiModel.addAttribute("recipe", recipeService.findRecipe(id));
	return RECIPE_DETAILS_PAGE;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String openLoginPage(Model uiModel, HttpServletRequest request) {

	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	if (principal != null && principal instanceof RecipeUser) {
	    request.getSession().setAttribute("RecipeUser", (RecipeUser) principal);
	}

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
	    @RequestParam(value = "file", required = false) Part file) throws IOException {

	Set<ConstraintViolation<Recipe>> recipeViolations = validator.validate(recipe);

	if (recipeViolations.size() > 0) {

	    uiModel.addAttribute("recipe_errors", recipeViolations);

	}

	List<Ingredient> ingredients = recipe.getIngredients();

	Set<ConstraintViolation<Ingredient>> ingredientViolations = new HashSet<ConstraintViolation<Ingredient>>();

	for (Ingredient ingr : ingredients) {
	    ingredientViolations = validator.validate(ingr);

	    if (ingredientViolations.size() > 0) {
		uiModel.addAttribute("ingredient_errors", ingredientViolations);
		break;
	    }
	}

	if (recipeViolations.size() > 0 || ingredientViolations.size() > 0) {
	    return RECIPE_ADD_RECIPE_PAGE;
	}

	recipeService.save(recipe);

	if (file != null) {
	    File f = new File(
		    "C:/springsource/vfabric-tc-server-developer-2.7.2.RELEASE/base-instance/wtpwebapps/RecipeProject/images/"
			    + recipe.getPicture());
	    if (f.exists()) {
		f.createNewFile();
	    }
	    OutputStream out = new FileOutputStream(f);
	    IOUtils.copy(file.getInputStream(), out);
	    out.flush();
	    out.close();
	}
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
    
    @RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "SignUp")
    public String editProfile(Model uiModel, HttpServletRequest request)
    {
	RecipeUser recipeUser = (RecipeUser)request.getSession().getAttribute("RecipeUser");
	Account acc = accountService.findByEmail(recipeUser.getUsername());
	
	uiModel.addAttribute("account", acc);
	return RECIPE_EDIT_PROFILE_PAGE;
    }

}
