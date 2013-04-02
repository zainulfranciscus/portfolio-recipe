package com.safe.stack.domain;

import java.io.IOException;

import javax.annotation.PostConstruct;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * A class that read an excel spread sheet containing recipe names and
 * ingredients.
 * 
 * @author Zainul Franciscus
 * 
 */
@Component
public class ExcelSpreadSheet {

	// ----------------------------------------
	// Constants used to identify columns in an
	// excel spread sheet.
	// ----------------------------------------
	/**
	 * The first column must contain the name of the recipe
	 */
	private static final int NAME_COL = 0;

	/**
	 * The second column must contain the name of the author of the recipe
	 */
	private static final int AUTHOR_COL = 1;

	/**
	 * The third column must contain the type of diet of a recipe
	 */
	private static final int DIET_COL = 2;

	/**
	 * The fourth column must contain a URL to the author website
	 */
	private static final int AUTHOR_LINK_COL = 3;

	/**
	 * The fifth column must contain the name of the recipe's picture
	 */
	private static final int PICTURE_COL = 4;

	/**
	 * The sixth column must contain the name of ingredients for the recipe.
	 */
	private static final int INGREDIENT_COL = 5;

	/**
	 * The seventh column must contain the amount of the ingredients.
	 */
	private static final int AMOUNT_COL = 6;

	/**
	 * The eight column must contain the metric of the ingredient.
	 */
	private static final int METRIC_COL = 7;

	private Workbook workbook = null;
	private Sheet sheet = null;
	private int numOfRow = 0;

	@Autowired
	private Resource excelFile;
	
	@PostConstruct
	public void init() throws BiffException, IOException{
		workbook = Workbook.getWorkbook(excelFile.getFile());
		sheet = workbook.getSheet(0);
		numOfRow = sheet.getRows();
	}
	
	/**
	 * @return the number of rows of the supplied excel spread sheet
	 */
	public int getNumOfRow() {
		return numOfRow;
	}

	/**
	 * @param rowNumber
	 * @return the name of a recipe on the specified row number
	 */
	public String getRecipeName(int rowNumber) {
		return sheet.getCell(NAME_COL, rowNumber).getContents();
	}

	/**
	 * @param rowNumber
	 * @return the name of an author of a recipe from the specified row number
	 */
	public String getAuthorName(int rowNumber) {
		return sheet.getCell(AUTHOR_COL, rowNumber).getContents().trim();
	}

	/**
	 * @param rowNumber
	 * @return a picture name of a recipe from the specified row number
	 */
	public String getPicture(int rowNumber) {
		return sheet.getCell(PICTURE_COL, rowNumber).getContents().trim();
	}

	/**
	 * @param rowNumber
	 * @return an author name of a recipe from the specified row number
	 */
	public String getAuthor(int rowNumber) {
		return sheet.getCell(AUTHOR_LINK_COL, rowNumber).getContents().trim();
	}

	/**
	 * @param rowNumber
	 * @return a diet of a recipe from the specified row number
	 */
	public String getDiet(int rowNumber) {
		return sheet.getCell(DIET_COL, rowNumber).getContents().trim();
	}

	/**
	 * @param rowNumber
	 * @return an ingredient name of a recipe from the specified row number
	 */
	public String getIngredientName(int rowNumber) {
		return sheet.getCell(INGREDIENT_COL, rowNumber).getContents().trim();
	}

	/**
	 * @param rowNumber
	 * @return the amount of an ingredient for a recipe
	 */
	public String getIngredientAmount(int rowNumber) {
		return sheet.getCell(AMOUNT_COL, rowNumber).getContents().trim();
	}

	/**
	 * @param rowNumber
	 * @return the unit of measurement (metric) of an ingredient for a recipe.
	 */
	public String getIngredientMetric(int rowNumber) {
		return sheet.getCell(METRIC_COL, rowNumber).getContents().trim();
	}

}
