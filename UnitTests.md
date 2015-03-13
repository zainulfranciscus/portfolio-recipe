This page describes how unit tests classes are developed for the project. The unit test has two components: the infrastructure classes, and the unit test classes.

There are are two type of infrastructure classes: controller infrastructure, which is a set of classes intended to setup the required infrastructure for running unit tests of a controller. And, service infrastructure , which is a set of classes intended to setup the required infrastructure for testing service classes.


# Controller Infrastructure #

The first class is the [ControllerTestConfig](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/config/ControllerTestConfig.java) class. This class is annotated with @Configuration annotation to indicate that this class is an Application context class. The profile annotation indicates a profile associated with this class, which is test.

```
@Configuration
@Profile("test")
public class ControllerTestConfig {

}
```

The second infrastructure class is the [AbstrackControllerTest](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/web/controller/AbstractControllerTest.java) class. This class is intended to be a superclass for every unit tests class that test a controller. This class has some annotations. The @RunWith annotation describes that child classes of this class will be run by a unit test. The  context configuration annotation describes the Configuration class used by this class. The active profile annotation indicates that child classes of this class should use the test profile for running unit tests.

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ControllerTestConfig.class })
@ActiveProfiles("test")
@Ignore
public class AbstractControllerTest {

}
```

# Service Infrastructure #

The first infrastructure class is [ServiceTestConfig](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/config/ServiceTestConfig.java) class. This class defines an application context for testing service layer classes.

```
@Configuration
@ImportResource({"classpath:datasource-tx-jpa.xml","classpath:servlet-context.xml","classpath:security-context.xml"})
@ComponentScan(basePackages={"com.safe.stack.service.jpa"})
@Profile("test")
public class ServiceTestConfig {
	
	
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:sql_scripts/DDL_ACCOUNT.sql")
				.addScript("classpath:sql_scripts/DDL_RECIPE.sql")
				.addScript("classpath:sql_scripts/DDL_LIKED_RECIPE.sql")
				.addScript("classpath:sql_scripts/DDL_INGREDIENT_TYPE.sql")
				.addScript("classpath:sql_scripts/DDL_INGREDIENTS.sql")
				.addScript("classpath:sql_scripts/DDL_RECIPE_INGREDIENT.sql")								
				.build();
	}
	
	@Bean(name="databaseTester")
	public DataSourceDatabaseTester dataSourceDatabaseTester() {
		DataSourceDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource());
		return databaseTester;
	}
	
	@Bean(name="xlsDataFileLoader")
	public XlsDataFileLoader xlsDataFileLoader() {
		return new XlsDataFileLoader();
	}

}
```

The @ImportResource annotation indicates that this class loads xml files that records the required configurations for running a test for a service layer class.

Then the @ComponentScan annotation is applied to instruct Spring to scan the service layer beans that we want to test. The @Profile annotation specifies that the beans defined in this class belong to the test profile. Second, within the class, another dataSource bean was declared that executes only the schema.sql script to the H2 database without any data. The databaseTester and xlsDataFileLoader beans were used by the custom test execution listener for loading test data from the Excel file. Note that the dataSourceDatabaseTester bean was constructed using the dataSource bean defined for the testing environment.

The second infrastructure class is [AbstractServiceImplTest](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/service/jpa/AbstractServiceImplTest.java) class. This class is very similar to [AbstrackControllerTest](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/web/controller/AbstractControllerTest.java) class. Any child class of this class will use data source define int he ServiceTestConfig class instead the datasource define in datasource-tx-jpa.xml.

The @TestExecutionListeners annotation indicates that a listener will be registered for every unit tests. That listener is  [ServiceTestExecutionListener](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/test/listener/ServiceTestExecutionListener.java) which implements a number of methods. There are two methods that are of interest to us. The beforeTestMethod, and the afterTestMethod.

The beforeTestMethod checks whether a unit test method is annotated with a DataSet annotation. If the annotation exists, the test data will be loaded from the specified Excel file.

```
public void beforeTestMethod(TestContext testCtx) throws Exception {

// Check for existence of DataSets annotation for the method under testing
    DataSets dataSetAnnotation =  testCtx.getTestMethod().getAnnotation(DataSets.class);
		
    if ( dataSetAnnotation == null ) {
	return;
    }
		
    String dataSetName = dataSetAnnotation.setUpDataSet();

    // Populate data set if data set name exists
    if ( ! dataSetName.equals("") ) {
      databaseTester = (IDatabaseTester) testCtx.getApplicationContext().getBean("databaseTester");
      XlsDataFileLoader xlsDataFileLoader = (XlsDataFileLoader) testCtx.getApplicationContext().getBean("xlsDataFileLoader");
      IDataSet dataSet = xlsDataFileLoader.load(dataSetName);
	
      databaseTester.setDataSet(dataSet);	
      databaseTester.onSetup();
    }
}
```

The afterTestMethod cleans up the test data.
```
public void afterTestMethod(TestContext arg0) throws Exception {
  // Clear up testing data if exists
  if (databaseTester != null) {			
	databaseTester.onTearDown();			
  }				
}
```
# Unit Test Classes #

A service layer unit test uses excel spreadsheet for loading test data. This is achieved using @DataSet annotation that indicate testing data must be loaded from an excel spreadsheet. For example, the [RecipeServiceImplTest](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/service/jpa/RecipeServiceImplTest.java) class:

```
...
@DataSets(setUpDataSet = "/com/safe/stack/service/jpa/recipeTestData.xls")
@Test
public void testFindAllIngredientType() {
   List<IngredientType> ingredientTypes = recipeService.findAllIngredientTypes();

   assertNotNull(ingredientTypes);
   assertEquals(3, ingredientTypes.size());

   assertEquals(0, ingredientTypes.get(0).getId().intValue());
   assertEquals("egg", ingredientTypes.get(0).getName());

   assertEquals(1, ingredientTypes.get(1).getId().intValue());
   assertEquals("rice", ingredientTypes.get(1).getName());

   assertEquals(2, ingredientTypes.get(2).getId().intValue());
   assertEquals("potato", ingredientTypes.get(2).getName());

}
...
```

A controller test is similar with a service layer test. The only difference is that controller tests do not require to load data from a datasource. For example: [HomeControllerTest](https://code.google.com/p/portfolio-recipe/source/browse/trunk/RecipeProject/src/test/java/com/safe/stack/web/controller/HomeControllerTest.java):

```
@Test
public void testLoginFail() {
   HomeController homeController = new HomeController();
   ExtendedModelMap uiModel = new ExtendedModelMap();

   MessageSource mockMessageSource = mock(MessageSource.class);
   when(mockMessageSource.getMessage("message_login_fail", new Object[] {}, Locale.UK)).thenReturn("Invalid user name or password");

   ReflectionTestUtils.setField(homeController, "messageSource", mockMessageSource);

   String result = homeController.loginFail(uiModel, Locale.UK, new Account());

   assertNotNull(result);
   assertEquals(result, HomeController.RECIPE_LOGIN_PAGE);
}
```

The @Test annotation is a JUnit annotation that indicates this method is a unit test.