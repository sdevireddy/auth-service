package com.zen.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.zen.auth.properties.CircuitBreakerProperties;
import com.zen.auth.properties.EurekaProperties;
import com.zen.auth.properties.HikariPoolProperties;
import com.zen.auth.properties.JwtProperties;
import com.zen.auth.properties.RetryApiProperties;
import com.zen.auth.properties.TenantDatasourceProperties;

@EnableConfigurationProperties({
    TenantDatasourceProperties.class,
    JwtProperties.class,
    HikariPoolProperties.class,
    RetryApiProperties.class,
    CircuitBreakerProperties.class,
    EurekaProperties.class
})
@SpringBootApplication
@EnableDiscoveryClient

public class AuthServiceApplication {
	private static ApplicationContext applicationContext;


	public static void main(String[] args) {
		applicationContext = SpringApplication.run(AuthServiceApplication.class, args);
		//displayAllBeans();
	}

	public static void displayAllBeans() {
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : allBeanNames) {
			System.out.println(beanName);
		}
	}


}