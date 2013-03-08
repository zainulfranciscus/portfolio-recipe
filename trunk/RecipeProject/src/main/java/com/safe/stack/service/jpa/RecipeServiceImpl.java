package com.safe.stack.service.jpa;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safe.stack.domain.Ingredient;
import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;
import com.safe.stack.repository.RecipeRepository;
import com.safe.stack.service.RecipeService;

@Service("recipeService")
@Repository
@Transactional
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_RECIPE_BY_INGREDIENT_SQL = "select distinct(r) from Recipe as r join r.ingredients as i join i.ingredientType as t where ";

    private static final String NATIVEQUERY_RECIPES_WITH_NUM_OF_LIKES = "select new com.safe.stack.domain.RecipeSummary(r.id, r.name, r.author, r.diet, "
	    + "(select count(*) from LikedRecipe l where r.id = l.recipeId) as numOfLikes, r.authorLink, r.picture) "
	    + "from Recipe r";

    private static final String NATIVEQUERY_RECIPES_WITH_LIKED_INDICATOR = "select new com.safe.stack.domain.RecipeSummary(r.id, r.name, r.author, r.diet, "
	    + "(select count(*) from LikedRecipe l where r.id = l.recipeId) as numOfLikes, r.authorLink, r.picture,"
	    + "(select count(*) from LikedRecipe l where r.id = l.recipeId and l.email =:arg0) as likedByUser) "
	    + "from Recipe r";

    private static final int NAME_COL = 0;

    private static final int AUTHOR_COL = 1;

    private static final int DIET_COL = 2;

    private static final int AUTHOR_LINK_COL = 3;

    private static final int PICTURE_COL = 4;

    private static final int INGREDIENT_COL = 5;

    private static final int AMOUNT_COL = 6;

    private static final int METRIC_COL = 7;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.safe.stack.service.RecipeService#save(com.safe.stack.domain.Recipe)
     */
    public void save(Recipe recipe) {
	entityManager.merge(recipe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.safe.stack.service.RecipeService#findRecipe(java.lang.Long)
     */
    @Transactional(readOnly=true)
    public Recipe findRecipe(Long id) {
	return recipeRepository.findOne(id);
    }

    @Transactional(readOnly=true)
    public List<Recipe> findAll() {
	return (List<Recipe>) entityManager.createNamedQuery("Recipe.findAll", Recipe.class)
		.getResultList();
    }

    @Transactional(readOnly=true)
    public List<IngredientType> findAllIngredientTypes() {
	return (List<IngredientType>) entityManager.createNamedQuery("IngredientType.findAll",
		IngredientType.class).getResultList();
    }

    @Transactional(readOnly=true)
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
     * (non-Javadoc)
     * 
     * @see com.safe.stack.service.RecipeService#findRecipesWithNumOfLikes()
     */
    @Override
    @Transactional(readOnly=true)
    public List<RecipeSummary> findRecipesWithNumOfLikes() {

	return entityManager.createQuery(NATIVEQUERY_RECIPES_WITH_NUM_OF_LIKES).getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.safe.stack.service.RecipeService#findRecipesWithLlikedIndicator(java
     * .lang.String)
     */
    @Override
    @Transactional(readOnly=true)
    public List<RecipeSummary> findRecipesWithLlikedIndicator(String userName) {
	Query q = entityManager.createQuery(NATIVEQUERY_RECIPES_WITH_LIKED_INDICATOR);
	q.setParameter("arg0", userName);
	return q.getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.safe.stack.service.RecipeService#importData()
     */
    @Override
    public Iterable<Recipe> importData(File excel) throws BiffException, IOException {
	
	Workbook workbook = Workbook.getWorkbook(excel);
	Sheet sheet = workbook.getSheet(0);
	int numOfRow = sheet.getRows();

	List<IngredientType> ingredientTypes = findAllIngredientTypes();
	List<Recipe> recipeList = new ArrayList<Recipe>();

	for (int i = 1; i < numOfRow; i++) {

	    String recipeName = sheet.getCell(NAME_COL, i).getContents();
	    Recipe r = new Recipe();

	    if (!StringUtils.isEmpty(recipeName)) {
		String authorName = sheet.getCell(AUTHOR_COL, i).getContents().trim();
		String recipePicture = sheet.getCell(PICTURE_COL, i).getContents().trim();
		String authorURL = sheet.getCell(AUTHOR_LINK_COL, i).getContents().trim();
		String diet = sheet.getCell(DIET_COL, i).getContents().trim();

		r.setAuthor(authorName);
		r.setAuthorLink(authorURL);
		r.setDiet(diet);

		r.setName(recipeName);
		r.setPicture(recipePicture);

	    } else {
		r = recipeList.get(recipeList.size() - 1);
	    }

	    String ingredientName = sheet.getCell(INGREDIENT_COL, i).getContents().trim();
	    String ingredientAmt = sheet.getCell(AMOUNT_COL, i).getContents().trim();
	    String ingredientMetric = sheet.getCell(METRIC_COL, i).getContents().trim();

	    Ingredient ingr = new Ingredient();
	    ingr.setAmount(ingredientAmt);
	    ingr.setMetric(ingredientMetric);

	    IngredientType t = new IngredientType();
	    t.setName(ingredientName);

	    for (IngredientType type : ingredientTypes) {
		if (type.getName().equalsIgnoreCase(ingredientName))		    
		{
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
