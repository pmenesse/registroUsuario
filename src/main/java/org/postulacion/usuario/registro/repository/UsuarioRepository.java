package org.postulacion.usuario.registro.repository;


import java.util.Optional;

import org.postulacion.usuario.registro.entity.UsuarioDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioDao, Integer>{
	
	Optional<UsuarioDao> findByEmail(String email);  

}
