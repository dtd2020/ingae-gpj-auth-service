package mz.gov.inage.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GpjAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpjAuthServiceApplication.class, args);
		System.out.println("GpjAuthServiceApplication");
	}

}
