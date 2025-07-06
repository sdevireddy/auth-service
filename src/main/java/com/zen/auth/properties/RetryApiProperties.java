package com.zen.auth.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "resilience4j.retry.instances.retr-api")
public class RetryApiProperties {

    private int maxAttempts;
    private String waitDuration;
	public int getMaxAttempts() {
		return maxAttempts;
	}
	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
	public String getWaitDuration() {
		return waitDuration;
	}
	public void setWaitDuration(String waitDuration) {
		this.waitDuration = waitDuration;
	}
    
}

