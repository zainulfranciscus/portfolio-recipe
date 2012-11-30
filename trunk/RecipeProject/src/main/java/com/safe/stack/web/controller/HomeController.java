package com.safe.stack.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.safe.stack.service.RecipeService;

@RequestMapping("/")
@Controller
public class HomeController {
	
	@Autowired
	private RecipeService recipeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showAllRecipes(Model uiModel)
	{
		uiModel.addAttribute("recipes", recipeService.findAll());
		return "list";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String showRecipe(@PathVariable("id") Long id, Model uiModel)
	{
		uiModel.addAttribute("recipe", recipeService.findRecipe(id));
		return "recipe";
	}

}
