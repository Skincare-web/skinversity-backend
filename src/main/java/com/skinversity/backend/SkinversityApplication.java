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

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_ACCESS_EXPIRATION", dotenv.get("JWT_ACCESS_EXPIRATION"));

		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("MAIL_SMTP_AUTH", dotenv.get("MAIL_SMTP_AUTH"));
		System.setProperty("MAIL_SMTP_STARTTLS_ENABLE", dotenv.get("MAIL_SMTP_STARTTLS_ENABLE"));



		SpringApplication.run(SkinversityApplication.class, args);
	}

}
