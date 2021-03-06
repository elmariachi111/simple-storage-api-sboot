package hello;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		String port = System.getenv("PORT");

		SpringApplication app = new SpringApplication(Application.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", port != null ? port : "8080"));
		app.run(args);
	}

	// @Bean
	// public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	// return args -> {
	// System.out.println("Let's inspect the beans provided by Spring Boot:");

	// String[] beanNames = ctx.getBeanDefinitionNames();
	// Arrays.sort(beanNames);
	// for (String beanName : beanNames) {
	// System.out.println(beanName);
	// }

	// };
	// }

}