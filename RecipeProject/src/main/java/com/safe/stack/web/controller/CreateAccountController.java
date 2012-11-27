package com.safe.stack.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.safe.stack.domain.Account;

@RequestMapping(method = RequestMethod.POST)
@Controller
public class CreateAccountController {

	final Logger logger = LoggerFactory
			.getLogger(CreateAccountController.class);

	@Autowired
	MessageSource messageSource;

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String createAccount(Account account, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale) {

		logger.info("updating account");
		
		redirectAttributes.addFlashAttribute("account_saved_success", "account saved");
		return "accountPage";
	}

}
