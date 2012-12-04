package com.safe.stack.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;
import com.safe.stack.web.form.Message;

@RequestMapping("/")
@Controller
public class HomeController {

    protected static final String RECIPE_LIST_PAGE = "recipe/list";
    protected static final String RECIPE_LOGIN_PAGE = "recipe/login";
    protected static final String RECIPE_DETAILS_PAGE = "recipe/detail";
    protected static final String RECIPE_ADD_RECIPE_PAGE = "recipe/addRecipe";

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
    public String openLoginPage(Model uiModel) {
	return RECIPE_LOGIN_PAGE;
    }
    
    @RequestMapping(value = "/addRecipe", method = RequestMethod.GET)
    public String showAddRecipe(Model uiModel)
    {
	uiModel.addAttribute("recipe", new Recipe());
	return RECIPE_ADD_RECIPE_PAGE;
    }
    

    @RequestMapping(value = "/saveRecipe", method = RequestMethod.POST)
    public String saveRecipe(Recipe recipe, Model uiModel)
    {
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
	return "list";
    }

    @RequestMapping(value = "/searchLikedRecipe", method = RequestMethod.POST)
    public String showLikedRecipe(@RequestParam("userName") String userName, Model uiModel) {
	Account account = accountService.findByUserName(userName);
	uiModel.addAttribute("account", account);
	return "account";
    }

    @RequestMapping(value = "/likeARecipe", method = RequestMethod.POST)
    public String likeARecipe(@RequestParam("userName") String userName,
	    @RequestParam("recipeId") String recipeId) {
	accountService.likeARecipe(userName, Long.parseLong(recipeId));
	return "recipe";
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

}
