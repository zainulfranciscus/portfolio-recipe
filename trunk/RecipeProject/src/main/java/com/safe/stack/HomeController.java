package com.safe.stack;


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.safe.stack.domain.Account;

/**
 * Handles requests for the application home page.
 */
@RequestMapping("/")
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@RequestMapping(method = RequestMethod.POST)
	public String signUp(Account acc, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale) {

		Account account = (Account) uiModel.asMap().get("account");
		return "home";

	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String showSignUpForm(Model model)
	{
		Account account = new Account();
		model.addAttribute("account", account);
		return "home";
	}

	@RequestMapping(value= "/recipe", method = RequestMethod.GET)
	public String showRecipe(Model model)
	{
		
		return "";
		
	}
}
