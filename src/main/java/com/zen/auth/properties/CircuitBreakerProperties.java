package com.zen.auth.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.instances.circuit-breaker-service")
public class CircuitBreakerProperties {

    private int failureRateThreshold;
    private int minimumNumberOfCalls;
    private boolean automaticTransitionFromOpenToHalfOpenEnabled;
    private String waitDurationInOpenState;
    private int permittedNumberOfCallsInHalfOpenState;
    private int slidingWindowSize;
    private String slidingWindowType;
	public int getFailureRateThreshold() {
		return failureRateThreshold;
	}
	public void setFailureRateThreshold(int failureRateThreshold) {
		this.failureRateThreshold = failureRateThreshold;
	}
	public int getMinimumNumberOfCalls() {
		return minimumNumberOfCalls;
	}
	public void setMinimumNumberOfCalls(int minimumNumberOfCalls) {
		this.minimumNumberOfCalls = minimumNumberOfCalls;
	}
	public boolean isAutomaticTransitionFromOpenToHalfOpenEnabled() {
		return automaticTransitionFromOpenToHalfOpenEnabled;
	}
	public void setAutomaticTransitionFromOpenToHalfOpenEnabled(boolean automaticTransitionFromOpenToHalfOpenEnabled) {
		this.automaticTransitionFromOpenToHalfOpenEnabled = automaticTransitionFromOpenToHalfOpenEnabled;
	}
	public String getWaitDurationInOpenState() {
		return waitDurationInOpenState;
	}
	public void setWaitDurationInOpenState(String waitDurationInOpenState) {
		this.waitDurationInOpenState = waitDurationInOpenState;
	}
	public int getPermittedNumberOfCallsInHalfOpenState() {
		return permittedNumberOfCallsInHalfOpenState;
	}
	public void setPermittedNumberOfCallsInHalfOpenState(int permittedNumberOfCallsInHalfOpenState) {
		this.permittedNumberOfCallsInHalfOpenState = permittedNumberOfCallsInHalfOpenState;
	}
	public int getSlidingWindowSize() {
		return slidingWindowSize;
	}
	public void setSlidingWindowSize(int slidingWindowSize) {
		this.slidingWindowSize = slidingWindowSize;
	}
	public String getSlidingWindowType() {
		return slidingWindowType;
	}
	public void setSlidingWindowType(String slidingWindowType) {
		this.slidingWindowType = slidingWindowType;
	}
    
    
}
