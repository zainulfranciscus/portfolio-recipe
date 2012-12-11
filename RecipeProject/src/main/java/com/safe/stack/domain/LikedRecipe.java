package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table (name = "LikedRecipe")
@NamedQueries({ @NamedQuery(name = "LikedRecipe.unlikeARecipe", query = "delete from LikedRecipe where recipeId = :recipeId and email = :email") })
public class LikedRecipe {
	
	private Long id;
	private Long recipeId;
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
