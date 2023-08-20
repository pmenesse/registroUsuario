package org.postulacion.usuario.registro.services;

import java.util.Optional;

import org.postulacion.usuario.registro.entity.LoginRequest;
import org.postulacion.usuario.registro.entity.UsuarioDao;
import org.postulacion.usuario.registro.entity.UsuarioRq;
import org.springframework.http.ResponseEntity;



public interface UsuarioService  {
	
	ResponseEntity<?> registrarUsuario(UsuarioRq user);
	ResponseEntity<?> updateUsuario(UsuarioRq user);
	ResponseEntity<?> deleteUsuario(String id);

	ResponseEntity<?> loginUsuario(LoginRequest user);
	ResponseEntity<?> getAllUsuarios(Optional<String> id);

}
