package com.skinversity.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class SkinversityApplication {

	public static void main(String[] args) {
		//load the .env file
		Dotenv dotenv = Dotenv.configure().load();

		//PostgreSQL database
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

/*		System.setProperty("SECURITY_PASSWORD", dotenv.get("SECURITY_PASSWORD"));
		System.setProperty("SECURITY_USERNAME", dotenv.get("SECURITY_USERNAME"));*/

		//OAUTH
		System.setProperty("CLIENT_ID", dotenv.get("CLIENT_ID"));
		System.setProperty("CLIENT_SECRET", dotenv.get("CLIENT_SECRET"));



		SpringApplication.run(SkinversityApplication.class, args);
	}

}
