This page is intended to describe RESTful web services built into the project.The web services produce recipes in the database in a JSON format. This is done by an API named Jackson, which is a multi-purpose Java library for processing JSON data format. To use the API, a dependency for Jackson is recorded in the pom.xml
```
<dependency>
   <groupId>org.codehaus.jackson</groupId>
   <artifactId>jackson-mapper-asl</artifactId>
   <version>1.9.2</version>
</dependency>
```

Why did I chose Jackson as the API for the webservice ? Simplicity. If I choose XML over JSON, I would need to create a mapping file (e.g.: a schema) that describe how to map variables of a POJO to XML elements. Jackson does not requires a mapping file to marshal POJO to JSON. Jackson converts instance variables and the variables value of a POJO into a collection of key-value pairs of a JSON. A client, who marshalls the JSON, need to create a POJO with the same instance variable names with the key names in the JSON. Let's see that in action.

# Controller #

A method in the [HomeController](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/main/java/com/safe/stack/web/controller/HomeController.java) acts as a webservice endpoint. This method relies on Jackson to marshall a list of Recipe to JSON:

```
/**
 * @return every recipe in the database in a json format
 */
@RequestMapping(value="/json.allRecipes", method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody List<Recipe> findAllRecipe(){
    	return recipeService.findAll();
    }
```

This method can be accessed using a browser by typing this URL in its address bar: http://localhost:8080/recipe/json/allRecipes. The resulting JSON would look something like this:

```
[{"id":3,"name":"Handmade Udon Noodles","author":"La Fuji Mama","diet":"","authorLink":"http://www.lafujimama.com/2010/06/how-to-make-handmade-udon-noodles-its-easier-than-you-might-think/","picture":"udon_noodle.jpg","version":0,"account":[],"ingredients":[{"ingredientId":16,"amount":"4","metric":"tsp","ingredientType":{"id":16,"name":"salt","version":0},"version":0},{"ingredientId":17,"amount":"1","metric":"cup","ingredientType":{"id":17,"name":"warm water","version":0},"version":0},{"ingredientId":18,"amount":"2 1/2","metric":"cups","ingredientType":{"id":18,"name":"unbleached bread flour","version":0},"version":0},{"ingredientId":19,"amount":"1 1/4","metric":"cups","ingredientType":{"id":19,"name":"unbleached bread all purpose flour","version":0},"version":0}]}]
```

Jackson transforms Recipe objects to a collection of key-value pairs displayed in JSON format.

The [Recipe](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/main/java/com/safe/stack/domain/Recipe.java) class is annotated with jsonIgnoreProperties, which records getter methods that should not be marshalled into key-value pairs of a JSON:

```
@JsonIgnoreProperties({"authorNameWithoutSpace","formattedAuthorURL","numberOfLikes"})
```
# Securing the Web Service #
The web service is secured using a security framework that comes with Spring. This framework is configured in the security-context.xml:

```
<http pattern='/json/**' create-session="stateless">
   <intercept-url pattern='/json/**' access='ROLE_USER' />
   <http-basic />
</http>
```

The http tag records that any url that begins with json must be protected. Only a user that has a role of ROLE\_USER is allowed to access the web service. This restriction is defined in the intercept-url tag.

# Web Service Client #

A [client](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/web/controller/JSONClientTest.java) intended to produce a list of recipes from the json produced by the webservice is built into the project.

The client uses restTemplate, which is an Spring API designed to access RESTFul web services. The restTemplate is configured as a bean in [test-servlet-context.xml](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/resources/test-servlet-context.xml)
```
<beans:bean id="restTemplate" class="org.springframework.web.client.RestTemplate">
   <beans:constructor-arg ref="httpRequestFactory"/>
</beans:bean>
```

A bean named httpRequestFactory is injected into the restTemplate. This bean records the required user name and password to access the webservice. The configuration of this bean is as follow:

```
<beans:bean id="httpRequestFactory" class="org.springframework.http.client.HttpComponentsClientHttpRequestFactory">
		<beans:constructor-arg>
			<beans:bean class="org.apache.http.impl.client.DefaultHttpClient">
				<beans:property name="credentialsProvider">
					<beans:bean class="com.safe.stack.web.controller.CustomCredentialsProvider">
						<beans:property name="credentials">
							<beans:bean class="org.apache.http.auth.UsernamePasswordCredentials">
								<beans:constructor-arg name="userName" value="user1@recipe.com" />
								<beans:constructor-arg name="password" value="Passw0rd" />
							</beans:bean>
						</beans:property>
					</beans:bean>
				</beans:property>
			</beans:bean>
		</beans:constructor-arg>
	</beans:bean>
```
The JSONClientTest calls the webservice using this restTemplate
```
String json = restTemplate.getForObject(new URI("http://localhost:8080/recipe/json.allRecipes"), String.class);
```

The JSON is converted into a list of Recipes object using this method
```
private static <T> List<T> mapJsonToObjectList(T typeDef, String json, Class clazz) throws Exception {
		List<T> list;
		ObjectMapper mapper = new ObjectMapper();		
		TypeFactory t = TypeFactory.defaultInstance();
		list = mapper.readValue(json, t.constructCollectionType(ArrayList.class, clazz));
		return list;
}
```