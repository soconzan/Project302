package org.example.project302.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt") // application.properties 에서 씀. jwt.어쩌구
public class JwtProperties {

	private String issuer;
	private String secretKey;
	private int duration;
	private	int refreshDuration;
}
