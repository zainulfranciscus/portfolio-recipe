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
import com.safe.stack.domain.RecipeSummary;
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

	IngredientType ingredientType2 = new IngredientType();
	ingredientType2.setName("Beef");

	Ingredient ingr2 = new Ingredient();
	ingr2.setAmount("120");
	ingr2.setIngredientType(ingredientType2);
	ingr2.setMetric("g");

	List<Ingredient> ingredients = new ArrayList<Ingredient>();
	ingredients.add(ingr);
	ingredients.add(ingr2);

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
	assertEquals(2, ingredients.size());

	assertEquals("250", ingredients.get(0).getAmount());
	assertEquals("Pasta", ingredients.get(0).getIngredientType().getName());
	assertEquals("g", ingredients.get(0).getMetric());

	assertEquals("120", ingredients.get(1).getAmount());
	assertEquals("Beef", ingredients.get(1).getIngredientType().getName());
	assertEquals("g", ingredients.get(1).getMetric());

    }

    @DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
    @Test
    public void testFindAllIngredientType() {
	List<IngredientType> ingredientTypes = recipeService.findAllIngredientTypes();

	assertNotNull(ingredientTypes);
	assertEquals(3, ingredientTypes.size());

	assertEquals(0, ingredientTypes.get(0).getId().intValue());
	assertEquals("egg", ingredientTypes.get(0).getName());

	assertEquals(1, ingredientTypes.get(1).getId().intValue());
	assertEquals("rice", ingredientTypes.get(1).getName());

	assertEquals(2, ingredientTypes.get(2).getId().intValue());
	assertEquals("potato", ingredientTypes.get(2).getName());

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
	assertEquals("egg", recipeFromDB.getIngredients().iterator().next().getIngredientType()
		.getName());

	ingredients.add("rice");
	ingredients.add("potato");

	recipes = recipeService.findByIngredients(ingredients);

	assertNotNull(recipes);
	assertEquals(3, recipes.size());

	recipeFromDB = recipes.get(0);
	assertEquals("egg", recipeFromDB.getIngredients().iterator().next().getIngredientType()
		.getName());

	recipeFromDB = recipes.get(1);
	assertEquals("potato", recipeFromDB.getIngredients().iterator().next().getIngredientType()
		.getName());

	recipeFromDB = recipes.get(2);
	assertEquals("rice", recipeFromDB.getIngredients().iterator().next().getIngredientType()
		.getName());

    }

    @DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
    @Test
    public void testFindRecipesWithNumOfLikes() {

	List<RecipeSummary> recipeSummaryList = recipeService.findRecipesWithNumOfLikes();
	
	assertEquals(3, recipeSummaryList.size());
	assertEquals(2, recipeSummaryList.get(0).getNumOfLikes().intValue());
	assertEquals(1, recipeSummaryList.get(1).getNumOfLikes().intValue());
	assertEquals(0, recipeSummaryList.get(2).getNumOfLikes().intValue());

    }

}
