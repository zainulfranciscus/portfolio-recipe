package com.safe.stack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.safe.stack.domain.Recipe;
import com.safe.stack.domain.RecipeSummary;

/**
 * An implementation of CrudRepository intended to provide CRUD operation for
 * the Recipe table.
 * 
 * @author Zainul Franciscus
 * 
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	@Query(value = "select new com.safe.stack.domain.RecipeSummary(r.id, r.name, r.author, r.diet, "
			+ "(select count(*) from LikedRecipe l where r.id = l.recipeId) as numOfLikes, r.authorLink, r.picture) " 
			+ "from Recipe r")
	public List<RecipeSummary> findNumOfLikePerRecipe();
	
	@Query(value="select new com.safe.stack.domain.RecipeSummary(r.id, r.name, r.author, r.diet, "
			+ "(select count(*) from LikedRecipe l where r.id = l.recipeId) as numOfLikes, r.authorLink, r.picture,"
			+ "(select count(*) from LikedRecipe l where r.id = l.recipeId and l.email =?1) as likedByUser) " + "from Recipe r")
	public List<RecipeSummary> countRecipesAUserLikes(String userName);

}
