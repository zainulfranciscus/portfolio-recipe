package com.safe.stack.service.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.safe.stack.annotation.DataSets;
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.RecipeService;

public class RecipeServiceImplTest extends AbstractServiceImplTest {

	@Autowired
	RecipeService recipeService;

	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
	@Test
	public void testSave() {

		Recipe recipeFromDB = recipeService.findRecipe(1L);

		assertNotNull(recipeFromDB);
		assertEquals("caserolle lover", recipeFromDB.getAuthor());
		assertEquals("url", recipeFromDB.getAuthorLink());
		assertEquals("vegan", recipeFromDB.getDiet());
		assertEquals("caserolle", recipeFromDB.getName());
		assertEquals("caserolle_pic", recipeFromDB.getPicture());

	}

	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
	@Test
	public void testFindAll() {

		List<Recipe> recipes = recipeService.findAll();

		assertNotNull(recipes);
		assertEquals(3, recipes.size());

		Recipe recipeFromDB = recipes.get(0);		
		assertEquals("pie lover", recipeFromDB.getAuthor());
		assertEquals("url", recipeFromDB.getAuthorLink());
		assertEquals("vegan", recipeFromDB.getDiet());
		assertEquals("pie", recipeFromDB.getName());
		
		recipeFromDB = recipes.get(1);		
		assertEquals("caserolle lover", recipeFromDB.getAuthor());
		assertEquals("url", recipeFromDB.getAuthorLink());
		assertEquals("vegan", recipeFromDB.getDiet());
		assertEquals("caserolle", recipeFromDB.getName());
		
		recipeFromDB = recipes.get(2);		
		assertEquals("fries lover", recipeFromDB.getAuthor());
		assertEquals("url", recipeFromDB.getAuthorLink());
		assertEquals("vegan", recipeFromDB.getDiet());
		assertEquals("fries", recipeFromDB.getName());

	}
	
	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
	@Test
	public void testFindByIngredient() {

		List<String> ingredients = new ArrayList<String>();
		
		List<Recipe> recipes = recipeService.findByIngredients(ingredients);
		
		assertNotNull(recipes);
		assertEquals(0, recipes.size());
		
		ingredients.add("egg");
		
		recipes = recipeService.findByIngredients(ingredients);
		
		assertNotNull(recipes);
		assertEquals(1, recipes.size());

		Recipe recipeFromDB = recipes.get(0);				
		assertEquals("egg", recipeFromDB.getIngredients().iterator().next().getIngredient());

		ingredients.add("rice");
		ingredients.add("potato");
		
		recipes = recipeService.findByIngredients(ingredients);
		
		assertNotNull(recipes);
		assertEquals(3, recipes.size());

		recipeFromDB = recipes.get(0);				
		assertEquals("egg", recipeFromDB.getIngredients().iterator().next().getIngredient());
		
		recipeFromDB = recipes.get(1);				
		assertEquals("rice", recipeFromDB.getIngredients().iterator().next().getIngredient());

		
		recipeFromDB = recipes.get(2);				
		assertEquals("potato", recipeFromDB.getIngredients().iterator().next().getIngredient());


	}

}
