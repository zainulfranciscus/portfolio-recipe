package com.safe.stack.service.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safe.stack.domain.ExcelSpreadSheet;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;
import com.safe.stack.repository.RecipeRepository;
import com.safe.stack.service.RecipeService;

/**
 * A service class responsible for retrieving information about a recipe from
 * the database
 * 
 * @author Zainul Franciscus
 * 
 */
@Service("recipeService")
@Repository
@Transactional
public class RecipeServiceImpl implements RecipeService {

	/**
	 * Used to save recipe to the database
	 */
	@Autowired
	private RecipeRepository recipeRepository;

	/**
	 * Used to retrieve recipe from the database
	 */
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * used for importing recipes into a database from an excel spreadsheet.
	 */
	@Autowired
	private ExcelSpreadSheet excelSpreadSheet;

	/**
	 * An SQL used to search recipes based on a set of ingredients
	 */
	private static final String FIND_RECIPE_BY_INGREDIENT_SQL = "select distinct(r) from Recipe as r join r.ingredients as i join i.ingredientType as t where ";


	/**
	 * Retrieve a list of recipes, where each recipe has information whether a logged in user
	 * has liked that recipe.
	 * This SQL is intended to produce a list of RecipeSummary Objects.
	 */
	private static final String NATIVEQUERY_RECIPES_WITH_LIKED_INDICATOR = "select new com.safe.stack.domain.RecipeSummary(r.id, r.name, r.author, r.diet, "
			+ "(select count(*) from LikedRecipe l where r.id = l.recipeId) as numOfLikes, r.authorLink, r.picture,"
			+ "(select count(*) from LikedRecipe l where r.id = l.recipeId and l.email =:arg0) as likedByUser) " + "from Recipe r";


	/*
	 * Save the supplied recipe object into the database
	 * 
	 * @see
	 * com.safe.stack.service.RecipeService#save(com.safe.stack.domain.Recipe)
	 */
	public void save(Recipe recipe) {
		entityManager.merge(recipe);
	}

	
	/* Find a recipe with the supplied recipe id.
	 * @see com.safe.stack.service.RecipeService#findRecipe(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	public Recipe findRecipe(Long id) {
		return recipeRepository.findOne(id);
	}

	/* Retrieve all recipes in the database
	 * @see com.safe.stack.service.RecipeService#findAll()
	 */
	@Transactional(readOnly = true)
	public List<Recipe> findAll() {
		List<Recipe> recipes = new ArrayList<Recipe>();
		CollectionUtils.addAll(recipes, recipeRepository.findAll().iterator());
		return recipes;
	}

	/* Retrieve every ingredients stored in the database.
	 * @see com.safe.stack.service.RecipeService#findAllIngredientTypes()
	 */
	@Transactional(readOnly = true)
	public List<IngredientType> findAllIngredientTypes() {
		return (List<IngredientType>) entityManager.createNamedQuery("IngredientType.findAll", IngredientType.class).getResultList();
	}

	/* Retrieve a list of recipes based on the supplied ingredients
	 * @see com.safe.stack.service.RecipeService#findByIngredients(java.util.List)
	 */
	@Transactional(readOnly = true)
	public List<Recipe> findByIngredients(List<String> ingredients) {

		if (ingredients.size() == 0) {
			return new ArrayList<Recipe>();
		}

		StringBuilder builder = new StringBuilder();
		builder.append(FIND_RECIPE_BY_INGREDIENT_SQL);

		for (int i = 0; i < ingredients.size(); i++) {
			if (i == 0) {
				builder.append("t.name LIKE :arg0");
				continue;
			}

			builder.append(" or t.name LIKE :arg").append(i);
		}

		Query query = entityManager.createQuery(builder.toString());
		for (int i = 0; i < ingredients.size(); i++) {
			query.setParameter("arg" + i, "%" + ingredients.get(i) + "%");
		}
		return query.getResultList();
	}

	/*
	 * Retrieve a list of RecipeSummary objects where each object stores the number of 
	 * user who like each of that recipe.
	 * 
	 * @see com.safe.stack.service.RecipeService#findRecipesWithNumOfLikes()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RecipeSummary> findRecipesWithNumOfLikes() {

		return recipeRepository.findNumOfLikePerRecipe();
	}

	/*
	 * Return a list of RecipeSummary objects that have information whether
	 * a user has liked each summary
	 * 
	 * @see
	 * com.safe.stack.service.RecipeService#findRecipesWithLlikedIndicator(java
	 * .lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RecipeSummary> findRecipesWithLlikedIndicator(String userName) {
		return recipeRepository.countRecipesAUserLikes(userName);
	}

	/*
	 * Create recipes based on the information recorded in the provided excel spreadsheet.
	 * 
	 * @see com.safe.stack.service.RecipeService#importData()
	 */
	@Override
	public Iterable<Recipe> importData() {

		List<IngredientType> ingredientTypes = findAllIngredientTypes();
		List<Recipe> recipeList = new ArrayList<Recipe>();

		for (int rowNumber = 1; rowNumber < excelSpreadSheet.getNumOfRow(); rowNumber++) {

			String recipeName = excelSpreadSheet.getRecipeName(rowNumber);
			Recipe r = new Recipe();

			if (!StringUtils.isEmpty(recipeName)) {
				String authorName = excelSpreadSheet.getAuthorName(rowNumber);
				String recipePicture = excelSpreadSheet.getPicture(rowNumber);
				String authorURL = excelSpreadSheet.getAuthor(rowNumber);
				String diet = excelSpreadSheet.getDiet(rowNumber);

				r.setAuthor(authorName);
				r.setAuthorLink(authorURL);
				r.setDiet(diet);

				r.setName(recipeName);
				r.setPicture(recipePicture);

			} else {
				r = recipeList.get(recipeList.size() - 1);
			}

			String ingredientName = excelSpreadSheet.getIngredientName(rowNumber);
			String ingredientAmt = excelSpreadSheet.getIngredientAmount(rowNumber);
			String ingredientMetric = excelSpreadSheet.getIngredientMetric(rowNumber);

			Ingredient ingr = new Ingredient();
			ingr.setAmount(ingredientAmt);
			ingr.setMetric(ingredientMetric);

			IngredientType t = new IngredientType();
			t.setName(ingredientName);

			for (IngredientType type : ingredientTypes) {
				if (type.getName().equalsIgnoreCase(ingredientName)) {
					t = type;
					break;
				}
			}

			ingr.setIngredientType(t);

			r.getIngredients().add(ingr);

			if (!StringUtils.isEmpty(recipeName)) {
				recipeList.add(r);
			}

		}

		return recipeRepository.save(recipeList);

	}

}
