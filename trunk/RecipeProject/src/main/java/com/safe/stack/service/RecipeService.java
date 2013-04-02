package com.safe.stack.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import com.safe.stack.domain.IngredientType;
import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;

public interface RecipeService {

    void save(Recipe recipe);

    Recipe findRecipe(Long id);

    List<Recipe> findAll();

    List<Recipe> findByIngredients(List<String> ingredients);

    List<IngredientType> findAllIngredientTypes();
    
    List<RecipeSummary> findRecipesWithNumOfLikes();
    
    List<RecipeSummary> findRecipesWithLlikedIndicator(String userName);
    
    Iterable<Recipe> importData();

}
