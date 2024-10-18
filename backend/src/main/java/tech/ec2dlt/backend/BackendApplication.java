package tech.ec2dlt.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"tech.ec2dlt.backend.config", "tech.ec2dlt.backend.controller", "tech.ec2dlt.backend.service", "tech.ec2dlt.backend.entity", "tech.ec2dlt.backend.repository"  })

public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
