package org.postulacion.usuario.registro;



import java.util.ArrayList;
import java.util.Date;

import org.postulacion.usuario.registro.entity.RoleDao;
import org.postulacion.usuario.registro.entity.UsuarioDao;
import org.postulacion.usuario.registro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SpringBootApplication()
public class RegistroApplication {

    

    
	public static void main(String[] args) {
		
		SpringApplication.run(RegistroApplication.class, args);
	
	}


	

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UsuarioRepository repositoryUser;

	@Bean
	CommandLineRunner init(){
		return args -> {
			repositoryUser.save(UsuarioDao.builder()
					.email("root@lab.col")
					.isActive(Boolean.TRUE)
					.name("Root")
					.username("root@lab.col")
					.password(passwordEncoder.encode("Register12"))
					.role(RoleDao.ADMIN)
					.phones(new ArrayList<>())
					.created(new Date())
					.build());
		};
	}

	
	
}
