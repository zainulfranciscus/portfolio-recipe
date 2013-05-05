package com.safe.stack.service.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.safe.stack.domain.ExcelSpreadSheet;
import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;
import com.safe.stack.repository.RecipeRepository;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.RecipeService;
import com.safe.stack.service.security.RecipeUser;

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
	
	@Autowired
	private AccountService accountService;

	/**
	 * An SQL used to search recipes based on a set of ingredients
	 */
	private static final String FIND_RECIPE_BY_INGREDIENT_SQL = "select distinct(r) from Recipe as r join r.ingredients as i join i.ingredientType as t where ";


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

	/**
	 * Defines the maximum number of data that can be retrieved from the
	 * recipe table.
	 */
	@Value("${numofdata.perpage}")
	private int recipeDataLimit;

	/**
	 * This method uses the findAll() method method of
	 * PagingAndSortingRepository to retrieve recipes stored in the database.
	 * Without using PagingAndSortingRepository, this method need to use a query
	 * such as: select r from Recipe r. PagingAndSortingRepository takes care of
	 * this for us.
	 * 
	 * PagingAndSortingRepository also provides a mechanism to get only an x
	 * number of data at a time. This method uses that mechanism to ensure that
	 * a client does not retrieve every recipes from the database, because there
	 * could be millions of recipe in the database.
	 * 
	 * 
	 * @param pageNumber
	 *            1 page of data contains an X number of recipes. If there are
	 *            100 recipes in the database there will be 10 pages of data.
	 * 
	 * @return a list of recipes
	 */
	public List<Recipe> findAll(int pageNumber) {

		
		PageRequest pageRequest = pageRequest(pageNumber);
		return Lists.newArrayList(recipeRepository.findAll(pageRequest).iterator());

	}

	/**
	 * A PageRequest object defines a start index used by
	 * PagingAndSortingRepository to retrieve data from the database.
	 * 
	 * This method ensures that the max num of data retrieved is not more than
	 * the specified in the recipeDataLimit variable
	 * 
	 * @param pageNumber
	 *            records the start index of the data
	 * @param numOfDataPerPage
	 *            defines the amount of data to be retrieved.
	 * @return
	 */
	private PageRequest pageRequest(int pageNumber) {

		//A page starts at page number. 
		if (pageNumber < 0) {
			pageNumber = 0;
		}

		/**
		 * A registered user can only get an X number of recipes at a time. 
		 * This code ensure that a request from user does not exceed the data limit.
		 */
		//int dataAllowance = accountService.getUser().dataAllowance();
		
		return new PageRequest(pageNumber, 5);

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
