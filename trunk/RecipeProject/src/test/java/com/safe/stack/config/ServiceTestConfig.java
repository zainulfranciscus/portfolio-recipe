/**
 * Created on Jan 4, 2012
 */
package com.safe.stack.config;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author Zainul Franciscus
 * 
 */
@Configuration
@ImportResource({"classpath:datasource-tx-jpa.xml","classpath:servlet-context.xml"})
@ComponentScan(basePackages={"com.safe.stack.service.jpa"})
@Profile("test")
public class ServiceTestConfig {
	
	
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:DDL_ACCOUNT.sql")
				.addScript("classpath:DDL_RECIPE.sql")
				.addScript("classpath:DDL_LIKED_RECIPE.sql")
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
