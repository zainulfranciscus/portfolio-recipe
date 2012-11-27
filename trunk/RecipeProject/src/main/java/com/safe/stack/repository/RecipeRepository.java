package com.safe.stack.repository;

import org.springframework.data.repository.CrudRepository;

import com.safe.stack.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
