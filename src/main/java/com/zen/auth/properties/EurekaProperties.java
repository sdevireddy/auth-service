package com.zen.auth.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "eureka.client.service-url")
public class EurekaProperties {

    private String defaultZone;

	public String getDefaultZone() {
		return defaultZone;
	}

	public void setDefaultZone(String defaultZone) {
		this.defaultZone = defaultZone;
	}
    
    
}

