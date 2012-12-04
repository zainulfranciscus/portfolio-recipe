package com.safe.stack.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.safe.stack.service.jpa.AbstractServiceImplTest;


public class AccountTest extends AbstractServiceImplTest{
	
	@Autowired
	private LocalValidatorFactoryBean validator;
	
	@Test
	public void testEmailValidation()
	{
		Account acc = new Account();
		acc.setEmail("email@yahoo.com");
		
		Set<ConstraintViolation<Account>> violations = validator.validate(acc);
		
		assertNotNull(violations);
		assertEquals(1,violations.size());
		assertEquals("Password must not be empty",violations.iterator().next().getMessage());
		
		acc = new Account();
		acc.setPassword("passW0rd");
		
		violations = validator.validate(acc);
		 
		assertNotNull(violations);
		assertEquals(1,violations.size());
		assertEquals("Email must not be empty",violations.iterator().next().getMessage());
		
		acc = new Account();
		acc.setEmail("invalid_email");
		acc.setPassword("passW0rd");
		
		violations = validator.validate(acc);
		assertNotNull(violations);
		assertEquals(1,violations.size());
		assertEquals("{validation.email.invalid.format}",violations.iterator().next().getMessageTemplate());
		
		acc = new Account();
		acc.setEmail("email@yahoo.com");
		acc.setPassword("password");
		
		violations = validator.validate(acc);
		assertNotNull(violations);
		assertEquals(1,violations.size());
		assertEquals("{validation.password.invalid.format}",violations.iterator().next().getMessageTemplate());
				
		
	}

}
