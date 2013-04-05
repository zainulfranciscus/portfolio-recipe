package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * This class is intended to map LikedRecipe table to a Java Class
 * 
 * @author Zainul Franciscus
 *
 */
@Entity
@Table (name = "LikedRecipe")
@NamedQueries({ @NamedQuery(name = "LikedRecipe.unlikeARecipe", query = "delete from LikedRecipe where recipeId = :recipeId and email = :email") })
public class LikedRecipe {
	
	/**
	 * An id of a LikedRecipe data
	 */
	private Long id;
	/**
	 * A recipe id
	 */
	private Long recipeId;
	/**
	 * Email address of a user.
	 */
	private String email;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue (strategy = IDENTITY)
	@Column (name = "id")
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the recipeId
	 */
	@Column (name = "recipeId")
	public Long getRecipeId() {
		return recipeId;
	}
	/**
	 * @param recipeId the recipeId to set
	 */
	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
	}
	/**
	 * @return the email
	 */
	@Column (name = "email")
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String name) {
		this.email = name;
	}
	
	

}
