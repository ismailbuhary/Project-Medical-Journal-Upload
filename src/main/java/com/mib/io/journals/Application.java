package com.mib.io.journals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class Application {

	public static final String ROOT;

	static {
		ROOT = System.getProperty("upload-dir", System.getProperty("user.home") + "/upload");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplicationBuilder(Application.class).build();
		app.run(args);

	}

}