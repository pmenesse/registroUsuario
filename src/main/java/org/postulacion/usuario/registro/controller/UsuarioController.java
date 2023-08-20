package org.postulacion.usuario.registro.controller;

import java.util.Optional;

import org.postulacion.usuario.registro.entity.LoginRequest;
import org.postulacion.usuario.registro.entity.UsuarioRq;
import org.postulacion.usuario.registro.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/usuario")

public class UsuarioController {
	

	@Autowired
	private UsuarioService usuarioService; 
	
	@PostMapping(value = "registrar")
	public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRq usuario){
		
		return (ResponseEntity<?>) this.usuarioService.registrarUsuario(usuario);
					
	}
	
	@PostMapping(value = "actualizar")
	public ResponseEntity<?> updateUsuario(@RequestBody UsuarioRq usuario){
		
		return (ResponseEntity<?>) this.usuarioService.updateUsuario(usuario);
					
	}
	
	@DeleteMapping(value = "eliminar/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteUsuario(@PathVariable("id") String id){
		
		return (ResponseEntity<?>) this.usuarioService.deleteUsuario(id);
					
	}
	
	@PostMapping(value = {"obtener" , "obtener/{id}"})
	public ResponseEntity<?> getAllUsuarios(@PathVariable Optional<String> id){
		
		return (ResponseEntity<?>) this.usuarioService.getAllUsuarios(id);
					
	}
	
	
	@PostMapping(value = "reg/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request)
    {
        return (ResponseEntity<?>) this.usuarioService.loginUsuario(request);
    }
	
	
	

}
