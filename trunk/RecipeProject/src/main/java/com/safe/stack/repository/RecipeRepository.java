package com.safe.stack.repository;

import org.springframework.data.repository.CrudRepository;

import com.safe.stack.domain.Recipe;

/**
 * An implementation of CrudRepository intended to provide CRUD operation 
 * for the Recipe table.
 * 
 * @author Zainul Franciscus
 *
 */
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
