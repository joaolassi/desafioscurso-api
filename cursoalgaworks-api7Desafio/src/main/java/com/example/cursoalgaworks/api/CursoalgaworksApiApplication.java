package com.example.cursoalgaworks.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.cursoalgaworks.api.config.property.AlgamoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class CursoalgaworksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursoalgaworksApiApplication.class, args);
	}

}
