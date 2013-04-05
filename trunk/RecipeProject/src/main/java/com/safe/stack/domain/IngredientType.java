package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class is intended to map IngredientType table to a Java class.
 * 
 * @author Zainul Franciscus 
 *
 */
@Entity
@Table(name = "IngredientType", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@NamedQueries({ @NamedQuery(name = "IngredientType.findAll", query = "select t from IngredientType t") })
public class IngredientType {

    /**
     * an Id for this ingredient type
     */
    private Integer id;
    /**
     * a name for this ingredient type
     */
    private String name;
    /**
     * A number used for optimistic locking
     */
    private int version;

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "Id")
    public Integer getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    @Column(name = "name")   
    @NotNull(message="{validation.ingredient.NotEmpty.message}")
    @NotEmpty(message="{validation.ingredient.NotEmpty.message}")    
    public String getName() {	
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	if(!StringUtils.isEmpty(name))
	{
	    this.name = name.trim();
	}
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

}

