package com.safe.stack.service;

import java.util.List;

import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;

/**
 * An interface that provides method that can be used to save a recipes or find
 * information related to a recipe. For example: finding a recipe based on a a
 * list of ingredients
 * 
 * @author Zainul Franciscus
 * 
 */
public interface RecipeService {

	/**
	 * Persist the provided recipe object into a database.
	 * 
	 * @param recipe 
	 */
	void save(Recipe recipe);

	/**
	 * Find a recipe based on a recipe id.
	 * @param id of a recipe
	 * @return a recipe object
	 */
	Recipe findRecipe(Long id);

	/**
	 * @return a list of recipes
	 */
	List<Recipe> findAll(int pageNumber, int numOfDataPerPage);

	/**
	 * @param ingredients
	 * @return a list of recipe with ingredients that match the provided
	 * list of ingredients.
	 */
	List<Recipe> findByIngredients(List<String> ingredients);

	/**
	 * @return a list of IngredientType
	 */
	List<IngredientType> findAllIngredientTypes();

	/**
	 * Find recipes with information on the number of users
	 * who have liked each recipe.
	 * 
	 * @return a list of recipe summary of objects
	 */
	List<RecipeSummary> findRecipesWithNumOfLikes();

	/**
	 * Find recipes with information whether a user, who has the provided user name, 
	 * has like each recipe.
	 * 
	 * @param userName of a user
	 * @return a list of RecipeSummary objects
	 */
	List<RecipeSummary> findRecipesWithLlikedIndicator(String userName);

	/**
	 * @return a list of recipes that has been inserted into the database
	 */
	Iterable<Recipe> importData();

}
