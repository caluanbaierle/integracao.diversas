package br.com.caluan.integracao.diversas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

	public static void main(String[] args) {
		new SpringApplication(Application.class).run(args);
	}
}

