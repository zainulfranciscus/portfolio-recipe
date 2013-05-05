package com.safe.stack.service.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.read.biff.BiffException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.safe.stack.annotation.DataSets;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;
import com.safe.stack.repository.RecipeRepository;
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

		List<Recipe> recipes = recipeService.findAll(0);

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
		assertEquals("potato", recipeFromDB.getIngredients().iterator().next().getIngredientType().getName());

		recipeFromDB = recipes.get(2);
		assertEquals("rice", recipeFromDB.getIngredients().iterator().next().getIngredientType().getName());

	}

	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
	@Test
	public void testFindRecipesWithNumOfLikes() {

		List<RecipeSummary> recipeSummaryList = recipeService.findRecipesWithNumOfLikes();

		assertEquals(3, recipeSummaryList.size());

		RecipeSummary rs1 = recipeSummaryList.get(0);
		assertEquals(1, rs1.getNumberOfLikes().intValue());
		assertEquals("pie lover", rs1.getAuthor());
		assertEquals("url", rs1.getAuthorLink());
		assertEquals("vegan", rs1.getDiet());
		assertEquals(0, rs1.getId().intValue());
		assertEquals("pie", rs1.getName());
		assertEquals("pie_pic", rs1.getPicture());
		assertEquals(0, rs1.getLikedByAUser().intValue());

		RecipeSummary rs2 = recipeSummaryList.get(1);
		assertEquals(1, rs2.getNumberOfLikes().intValue());
		assertEquals("caserolle lover", rs2.getAuthor());
		assertEquals("url", rs2.getAuthorLink());
		assertEquals("vegan", rs2.getDiet());
		assertEquals(1, rs2.getId().intValue());
		assertEquals(0, rs2.getLikedByAUser().intValue());
		assertEquals("caserolle", rs2.getName());
		assertEquals("caserolle_pic", rs2.getPicture());

		RecipeSummary rs3 = recipeSummaryList.get(2);
		assertEquals(0, rs3.getNumberOfLikes().intValue());
		assertEquals("fries lover", rs3.getAuthor());
		assertEquals("url", rs3.getAuthorLink());
		assertEquals("vegan", rs3.getDiet());
		assertEquals(2, rs3.getId().intValue());
		assertEquals(0, rs3.getLikedByAUser().intValue());
		assertEquals("fries", rs3.getName());
		assertEquals("fries_pic", rs3.getPicture());

	}

	@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
	@Test
	public void testFindRecipesWithLikedInd() {

		List<RecipeSummary> recipeSummaryList = recipeService.findRecipesWithLlikedIndicator("user@recipe.com");

		assertEquals(3, recipeSummaryList.size());

		RecipeSummary rs1 = recipeSummaryList.get(0);
		assertEquals(1, rs1.getNumberOfLikes().intValue());
		assertEquals("pie lover", rs1.getAuthor());
		assertEquals("url", rs1.getAuthorLink());
		assertEquals("vegan", rs1.getDiet());
		assertEquals(0, rs1.getId().intValue());
		assertEquals("pie", rs1.getName());
		assertEquals("pie_pic", rs1.getPicture());
		assertEquals(1, rs1.getLikedByAUser().intValue());

		RecipeSummary rs2 = recipeSummaryList.get(1);
		assertEquals(1, rs2.getNumberOfLikes().intValue());
		assertEquals("caserolle lover", rs2.getAuthor());
		assertEquals("url", rs2.getAuthorLink());
		assertEquals("vegan", rs2.getDiet());
		assertEquals(1, rs2.getId().intValue());
		assertEquals(0, rs2.getLikedByAUser().intValue());
		assertEquals("caserolle", rs2.getName());
		assertEquals("caserolle_pic", rs2.getPicture());

		RecipeSummary rs3 = recipeSummaryList.get(2);
		assertEquals(0, rs3.getNumberOfLikes().intValue());
		assertEquals("fries lover", rs3.getAuthor());
		assertEquals("url", rs3.getAuthorLink());
		assertEquals("vegan", rs3.getDiet());
		assertEquals(2, rs3.getId().intValue());
		assertEquals(0, rs3.getLikedByAUser().intValue());
		assertEquals("fries", rs3.getName());
		assertEquals("fries_pic", rs3.getPicture());

	}

	@Test
	public void testImportData() throws IOException, BiffException {

		Recipe r = new Recipe();
		r.setName("x");

		List<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(r);

		RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);

		when(mockRecipeRepository.save(recipes)).thenReturn(recipes);

		RecipeServiceImpl recipeServiceImpl = new RecipeServiceImpl();
		ReflectionTestUtils.setField(recipeServiceImpl, "recipeRepository", mockRecipeRepository);

		Iterable<Recipe> savedRecipes = recipeService.importData();
		Iterator<Recipe> savedRecipesIt = savedRecipes.iterator();

		Recipe r1 = savedRecipesIt.next();

		assertEquals("recipe1", r1.getName());
		assertEquals("seriouseat.com", r1.getAuthor());
		assertEquals("vegan", r1.getDiet());
		assertEquals("http://seriouseat.com", r1.getAuthorLink());
		assertEquals("pic1.jpg", r1.getPicture());

		List<Ingredient> ingrList1 = r1.getIngredients();
		assertNotNull(ingrList1);
		assertEquals(1, ingrList1.size());

		Ingredient i1 = ingrList1.get(0);
		assertEquals("cardamom", i1.getIngredientType().getName());
		assertEquals("10", i1.getAmount());
		assertEquals("g", i1.getMetric());

		Recipe r2 = savedRecipesIt.next();

		assertEquals("recipe2", r2.getName());
		assertEquals("martha stewart", r2.getAuthor());
		assertEquals("", r2.getDiet());
		assertEquals("http://marthastewart.com", r2.getAuthorLink());
		assertEquals("pic2.jpg", r2.getPicture());

		List<Ingredient> ingrList2 = r2.getIngredients();
		assertNotNull(ingrList2);
		assertEquals(3, ingrList2.size());

		Ingredient i2 = ingrList2.get(0);
		assertEquals("chocolate", i2.getIngredientType().getName());
		assertEquals("250", i2.getAmount());
		assertEquals("g", i2.getMetric());

		Ingredient i3 = ingrList2.get(1);
		assertEquals("butter", i3.getIngredientType().getName());
		assertEquals("4", i3.getAmount());
		assertEquals("tbsp", i3.getMetric());

		Ingredient i4 = ingrList2.get(2);
		assertEquals("flour", i4.getIngredientType().getName());
		assertEquals("3", i4.getAmount());
		assertEquals("cups", i4.getMetric());

	}

}
