package com.safe.stack.service;

import java.io.File;
import java.io.IOException;

import jxl.read.biff.BiffException;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RecipeUploader {

    public static void main(String[] args) throws BiffException, IOException {

	System.setProperty("spring.profiles.active", "mysql");

	GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
	ctx.load("classpath:jpa-app-context.xml","security-context.xml");
	ctx.refresh();

	Resource resourceToImport = new ClassPathResource("recipe.xls");
	File recipesSpreadsheet = resourceToImport.getFile();
	
	RecipeService recipeService = ctx.getBean("recipeService", RecipeService.class);
	recipeService.importData(recipesSpreadsheet);

    }

}