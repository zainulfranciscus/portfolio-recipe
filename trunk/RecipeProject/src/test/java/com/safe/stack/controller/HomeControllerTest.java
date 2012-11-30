package com.safe.stack.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import com.safe.stack.domain.Recipe;
import com.safe.stack.service.RecipeService;
import com.safe.stack.web.controller.HomeController;

public class HomeControllerTest extends AbstractControllerTest {

	@Test
	public void testShowAllRecipe() {
		RecipeService recipeService = mock(RecipeService.class);
		HomeController homeController = new HomeController();

		List<Recipe> recipeList = new ArrayList<Recipe>();
		recipeList.add(new Recipe());

		when(recipeService.findAll()).thenReturn(recipeList);
		ReflectionTestUtils.setField(homeController, "recipeService",
				recipeService);

		ExtendedModelMap uiModel = new ExtendedModelMap();
		String result = homeController.showAllRecipes(uiModel);

		assertNotNull(result);
		assertEquals(result, "list");

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

		ReflectionTestUtils.setField(homeController, "recipeService",
				recipeService);

		ExtendedModelMap uiModel = new ExtendedModelMap();
		String result = homeController.showRecipe(1l, uiModel);

		assertNotNull(result);
		assertEquals(result, "recipe");

		assertEquals(aRecipe, uiModel.get("recipe"));

	}

}
