package com.safe.stack.web.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.safe.stack.domain.Recipe;

/**
 * 
 * A web service client intended to test a Restful web service in the project.
 * @author Zainul Franciscus
 *
 */
public class JSONClientTest {

	public static void main(String[] args) throws Exception {

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:test-servlet-context.xml");
		ctx.refresh();

		RestTemplate restTemplate = ctx.getBean("restTemplate", RestTemplate.class);

		String json = restTemplate.getForObject(new URI("http://localhost:8080/recipe/json.allRecipes"), String.class);

		List<Recipe> recipes = mapJsonToObjectList(new Recipe(), json, Recipe.class);
		
		for(Recipe recipe: recipes){
			System.out.println(ToStringBuilder.reflectionToString(recipe));
		}
	
	}

	
	/**
	 * Create a list of objects from the provided json
	 * 
	 * @param typeDef
	 * @param json
	 * @param clazz 
	 * @return a List of objects, which are instances of clazz
	 * @throws Exception
	 */
	private static <T> List<T> mapJsonToObjectList(T typeDef, String json, Class clazz) throws Exception {
		List<T> list;
		ObjectMapper mapper = new ObjectMapper();		
		TypeFactory t = TypeFactory.defaultInstance();
		list = mapper.readValue(json, t.constructCollectionType(ArrayList.class, clazz));
		return list;
	}

}
