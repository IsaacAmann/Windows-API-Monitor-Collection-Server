package com.CollectionServer;

import com.CollectionServer.Services.RateLimitService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.annotation.Configuration;



@SpringBootApplication
public class CollectionServerApplication {

	@Autowired
	private RateLimitService rateLimitService;

	public static void main(String[] args) {
		SpringApplication.run(CollectionServerApplication.class, args);
	}
	
	
	@Configuration
	@EnableWebMvc
	public class Config implements WebMvcConfigurer
	{
		//Allows cross origin resource sharing (CORS)
		@Override
		public void addCorsMappings(CorsRegistry registry)
		{
			registry.addMapping("/**");
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry)
		{
			RateLimitService.RateLimitInterceptor interceptor = rateLimitService.getInterceptor();

			registry.addInterceptor(interceptor).addPathPatterns("/*");
		}
	}

}


