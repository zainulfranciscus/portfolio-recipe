package com.safe.stack.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    
    @Test
    public void testGetAuthorNameWithoutSpace()
    {
	Recipe r = new Recipe();
	r.setAuthor("Martha Stewart");
	
	assertEquals("marthastewart", r.getAuthorNameWithoutSpace());
	
	r = new Recipe();
	r.setAuthor("What's She's Cooking !");
	
	assertEquals("whatsshescooking", r.getAuthorNameWithoutSpace());
	
	
    }
    
    @Test
    public void testGetFormattedAuthorLink()
    {
	Recipe r = new Recipe();
	r.setAuthorLink("http://www.lafujimama.com/2010/07/japanese-strawberry-shortcake/");
	
	assertEquals("http://www.lafujimama.com/2010/07/japanese-strawberry-shortcake/", r.getFormattedAuthorURL());
	
	r.setAuthorLink("http://ohmyveggies.com/a-change-of-holiday-plans-a-cranberry-orange-spritzer-recipe/");
	
	assertEquals("http://ohmyveggies.com/a-change-of-holiday-plans-a-cranberry-orang...", r.getFormattedAuthorURL());
	
    }
}
