package com.example.formapp.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "datasource.appform")
@EnableJpaRepositories(entityManagerFactoryRef = "appformEntityManagerFactory", transactionManagerRef = "appformTransactionManager", basePackages = "com.example.formapp.repository.appform")
@EnableTransactionManagement
@Setter
@Getter
public class DataSourceConfig {
	private HashMap<String, String> hikari;
	private HashMap<String, String> dsProperies;
	private String jndiName;
	private String jdbcUrl;
	private String username;
	private String password;
	private String driverClassName;
	private boolean selfDatasource;

	@Primary
	@Bean(name = "appformDataSource")
	public DataSource appformDataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		if (selfDatasource) {
			dataSource.setJdbcUrl(jdbcUrl);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setDriverClassName(driverClassName);
			dataSource.setPoolName(getHikari().get("poolName"));
		} else {
			dataSource.setDataSourceJNDI(jndiName);
			dataSource.setAutoCommit(false);
		}
		dataSource.setMaximumPoolSize(Integer.valueOf(getHikari().get("maximumPoolSize")));
		dataSource.setMinimumIdle(Integer.valueOf(getHikari().get("minimumIdle")));
		dataSource.setIdleTimeout(Integer.valueOf(getHikari().get("idleTimeout")));
		dataSource.setMaxLifetime(Long.valueOf(getHikari().get("maxLifetime")));
		dataSource.setConnectionTimeout(Long.valueOf(getHikari().get("connectionTimeout")));
		return dataSource;
	}

	@Primary
	@Bean(name = "appformTransactionManager")
	public PlatformTransactionManager appformTransactionManager(
			@Qualifier("appformEntityManagerFactory") EntityManagerFactory appformEntityManagerFactory) {
		return new JpaTransactionManager(appformEntityManagerFactory);
	}

	@Primary
	@Bean(name = "appformEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean appformEntityManagerFactory(
			@Qualifier("appformDataSource") DataSource thirdDataSource, EntityManagerFactoryBuilder builder) {

		return builder.dataSource(thirdDataSource).properties(dsProperies)
				.packages("com.example.formapp.model.appform").build();
	}

}
