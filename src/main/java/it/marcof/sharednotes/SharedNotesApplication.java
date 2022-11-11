package it.marcof.sharednotes;

import it.marcof.sharednotes.model.Entity.NoteEntity;
import it.marcof.sharednotes.model.Entity.UserEntity;
import it.marcof.sharednotes.service.NoteService;
import it.marcof.sharednotes.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SharedNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharedNotesApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, NoteService noteService) {
		return args -> {
			userService.save(new UserEntity(null, "rossi", "1234", new ArrayList<>()));
			userService.save(new UserEntity(null, "bianchi", "1234", new ArrayList<>()));

			noteService.create(new NoteEntity(null, "Prova", "Contenuto", null), "rossi");
		};
	}

}
