/**
 * Created on Jan 4, 2012
 */
package com.safe.stack.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation intended to instruct unit test classes to load 
 * test data from an excel spreadsheet.
 * 
 * @author Zainul Franciscus
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSets {

	String setUpDataSet() default "";

}
