package com.safe.stack.service.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.safe.stack.annotation.DataSets;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.service.RecipeService;

public class RecipeServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    RecipeService recipeService;

    @Test
    public void testSave() {

	Recipe r = new Recipe();
	r.setAuthor("caserolle lover");
	r.setAuthorLink("url");
	r.setDiet("vegan");
	r.setName("caserolle");
	r.setPicture("caserolle_pic");
	
	IngredientType ingredientType = new IngredientType();
	ingredientType.setName("Pasta");
	
	Ingredient ingr = new Ingredient();
	ingr.setAmount("250");
	ingr.setIngredientType(ingredientType);
	ingr.setMetric("g");

	
	List<Ingredient> ingredients = new ArrayList<Ingredient>();
	ingredients.add(ingr);
	
	r.setIngredients(ingredients);
	
	recipeService.save(r);

	Recipe recipeFromDB = recipeService.findRecipe(1L);

	assertNotNull(recipeFromDB);
	assertEquals("caserolle lover", recipeFromDB.getAuthor());
	assertEquals("url", recipeFromDB.getAuthorLink());
	assertEquals("vegan", recipeFromDB.getDiet());
	assertEquals("caserolle", recipeFromDB.getName());
	assertEquals("caserolle_pic", recipeFromDB.getPicture());
	
	ingredients = recipeFromDB.getIngredients();
	assertEquals(1,ingredients.size());
	
	assertEquals("250", ingredients.get(0).getAmount());
	assertEquals("Pasta", ingredients.get(0).getIngredientType().getName());
	assertEquals("g", ingredients.get(0).getMetric());

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
	assertEquals("egg", recipeFromDB.getIngredients().iterator().next().getIngredientType().getName());

	ingredients.add("rice");
	ingredients.add("potato");

	recipes = recipeService.findByIngredients(ingredients);

	assertNotNull(recipes);
	assertEquals(3, recipes.size());

	recipeFromDB = recipes.get(0);
	assertEquals("egg", recipeFromDB.getIngredients().iterator().next().getIngredientType().getName());

	recipeFromDB = recipes.get(1);
	assertEquals("rice", recipeFromDB.getIngredients().iterator().next().getIngredientType().getName());

	recipeFromDB = recipes.get(2);
	assertEquals("potato", recipeFromDB.getIngredients().iterator().next().getIngredientType().getName());

    }

}
