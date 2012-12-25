package com.safe.stack.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.safe.stack.service.jpa.AbstractServiceImplTest;

public class IngredientTest extends AbstractServiceImplTest {

    @Autowired
    private LocalValidatorFactoryBean validator;
    
    @Test
    public void testIngredientValidation() {
	Ingredient ingr = new Ingredient();
	ingr.setAmount("20");
	ingr.setMetric("g");

	IngredientType ingredientType = new IngredientType();
	ingredientType.setName("");
	
	
	Set<ConstraintViolation<IngredientType>> violations = validator.validate(ingredientType);

	assertNotNull(violations);
	assertEquals(1, violations.size());
	assertEquals("Please specify an ingredient.", violations.iterator().next().getMessage());
	
	ingredientType = new IngredientType();
	ingredientType.setName(" ");
	
	
	violations = validator.validate(ingredientType);
	
	assertNotNull(violations);
	assertEquals(1, violations.size());
	assertEquals("Please specify an ingredient.", violations.iterator().next().getMessage());
	
	ingredientType = new IngredientType();
	ingredientType.setName("arecipe");
	
	
	violations = validator.validate(ingredientType);
	
	assertEquals(0, violations.size());
	

    }

   

}
