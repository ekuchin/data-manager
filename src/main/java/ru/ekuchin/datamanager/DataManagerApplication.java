package ru.ekuchin.datamanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.ekuchin.datamanager.config.Connection;
import ru.ekuchin.datamanager.config.Connections;

@SpringBootApplication
public class DataManagerApplication implements CommandLineRunner {

	@Autowired
	private Connections connections;

	public static void main(String[] args) {
		SpringApplication.run(DataManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Started ...");
	}
}
