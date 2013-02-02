package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Ingredient")
public class Ingredient {

    private Long ingredientId;
    private String amount;
    private String metric;
    private IngredientType ingredientType;
    private int version;
    
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
    public String getAmount() {
	return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(String amount) {
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
     * @return the ingredientType
     */
    @OneToOne (cascade = { CascadeType.ALL })
    @JoinColumn(name="ingredientType")
    @NotNull(message="{validation.ingredient.NotEmpty.message}")
    public IngredientType getIngredientType() {
	return ingredientType;
    }

    /**
     * @param ingredientType
     *            the ingredientType to set
     */
    public void setIngredientType(IngredientType ingredientType) {
	this.ingredientType = ingredientType;
    }

}
