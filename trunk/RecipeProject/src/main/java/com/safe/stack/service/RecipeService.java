package com.safe.stack.service;

import java.util.List;

import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;

public interface RecipeService {

    void save(Recipe recipe);

    Recipe findRecipe(Long id);

    List<Recipe> findAll();

    List<Recipe> findByIngredients(List<String> ingredients);

    List<IngredientType> findAllIngredientTypes();

}
