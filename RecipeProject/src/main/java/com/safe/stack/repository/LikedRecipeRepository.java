package com.safe.stack.repository;

import org.springframework.data.repository.CrudRepository;

import com.safe.stack.domain.LikedRecipe;

public interface LikedRecipeRepository extends
		CrudRepository<LikedRecipe, Long> {

}
