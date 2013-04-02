package com.safe.stack.service;

import java.io.IOException;

import jxl.read.biff.BiffException;

import org.springframework.context.support.GenericXmlApplicationContext;

public class RecipeUploader {

    public static void main(String[] args) throws BiffException, IOException {

	System.setProperty("spring.profiles.active", "mysql");

	GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
	ctx.load("classpath:jpa-app-context.xml","classpath:security-context.xml","classpath:datasource-tx-jpa.xml");
	ctx.refresh();
	
	RecipeService recipeService = ctx.getBean("recipeService", RecipeService.class);
	recipeService.importData();

    }

}
