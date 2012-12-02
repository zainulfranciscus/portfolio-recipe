package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "Ingredient")
public class Ingredient {

	private Long ingredientId;	
	private int amount;
	private String metric;
	private String ingredient;
	private int version;
	private Recipe recipe;
	

	
	/**
	 * @return the recipe
	 */
	@ManyToOne
	@JoinColumn(name = "recipeId")
	public Recipe getRecipe() {
		return recipe;
	}

	/**
	 * @param recipe the recipe to set
	 */
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ingredientId")
	public Long getIngredientId() {
		return ingredientId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setIngredientId(Long id) {
		this.ingredientId = id;
	}

	/**
	 * @return the amount
	 */
	@Column(name = "amount")
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the metric
	 */
	@Column(name = "metric")
	public String getMetric() {
		return metric;
	}

	/**
	 * @param metric
	 *            the metric to set
	 */
	public void setMetric(String metric) {
		this.metric = metric;
	}

	/**
	 * @return the version
	 */
	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the ingredient
	 */
	@Column(name = "ingredient")
	public String getIngredient() {
		return ingredient;
	}

	/**
	 * @param ingredient
	 *            the ingredient to set
	 */
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

}
