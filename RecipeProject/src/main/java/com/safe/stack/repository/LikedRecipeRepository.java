package com.safe.stack.repository;

import org.springframework.data.repository.CrudRepository;

import com.safe.stack.domain.LikedRecipe;

/**
 * An implementation of CrudRepository interface intended to provide CRUD operation 
 * for LikedRecipe table
 * 
 * @author Zainul Franciscus
 * 
 */
public interface LikedRecipeRepository extends CrudRepository<LikedRecipe, Long> {

}
