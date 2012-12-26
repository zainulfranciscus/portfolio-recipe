package com.safe.stack.service.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
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

    private static final String FIND_RECIPE_BY_INGREDIENT_SQL = "select r from Recipe as r join r.ingredients as i join i.ingredientType as t where ";

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
    public Recipe findRecipe(Long id) {
	return recipeRepository.findOne(id);
    }

    public List<Recipe> findAll() {
	return (List<Recipe>) entityManager.createNamedQuery("Recipe.findAll", Recipe.class)
		.getResultList();
    }
    
    public List<IngredientType> findAllIngredientTypes()
    {
	return (List<IngredientType>) entityManager.createNamedQuery("IngredientType.findAll", IngredientType.class)
		.getResultList();
    }

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

}
