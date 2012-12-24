package com.safe.stack.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.safe.stack.service.jpa.AbstractServiceImplTest;

public class RecipeTest extends AbstractServiceImplTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    public void testRecipeName() {
	Recipe recipe = new Recipe();
	recipe.setAuthor("me");
	recipe.setPicture("pic");
	Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);

	assertNotNull(violations);
	assertEquals(1, violations.size());
	assertEquals("Please specify the name of this recipe.", violations.iterator().next()
		.getMessage());
    }

    
    @Test
    public void testRecipeAuthor() {
	Recipe recipe = new Recipe();
	recipe.setName("rice rolls");
	recipe.setPicture("pic");
	Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);

	assertNotNull(violations);
	assertEquals(1, violations.size());
	assertEquals("Please specify the publisher of this recipe.", violations.iterator().next()
		.getMessage());
    }

    @Test
    public void testLikeByUser() {

	Account acc = new Account();
	acc.setEmail("sandra");

	Set<Account> accounts = new HashSet<Account>();
	accounts.add(acc);

	Recipe r = new Recipe();
	r.setAccount(accounts);

	assertTrue(r.isLikedByUser("sandra"));
	assertFalse(r.isLikedByUser("adrian"));

    }
}
