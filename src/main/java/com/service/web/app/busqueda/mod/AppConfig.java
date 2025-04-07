package com.service.web.app.busqueda.mod;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class AppConfig {

	@Bean("restClient")
	public RestTemplate registerRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofMegabytes(100)); // Tamaño máximo del archivo
		factory.setMaxRequestSize(DataSize.ofMegabytes(100)); // Tamaño máximo de la solicitud
		return factory.createMultipartConfig();
	}

}
