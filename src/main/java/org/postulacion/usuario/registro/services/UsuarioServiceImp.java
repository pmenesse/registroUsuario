package org.postulacion.usuario.registro.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.postulacion.usuario.registro.entity.BooleanResponse;
import org.postulacion.usuario.registro.entity.LoginRequest;
import org.postulacion.usuario.registro.entity.RoleDao;
import org.postulacion.usuario.registro.entity.TokenKey;
import org.postulacion.usuario.registro.entity.UsuarioDao;
import org.postulacion.usuario.registro.entity.UsuarioRq;
import org.postulacion.usuario.registro.repository.UsuarioRepository;
import org.postulacion.usuario.registro.security.filters.JwtService;
import org.postulacion.usuario.registro.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService {

	private static final String ERROR_AL_ACTUALIZAR_REGISTRO = "Error al actualizar registro : ";
	private static final String ID_USUARIO_ID_INEXISTENTE = "No ha sido posible elminar el usuario, Id inexistente ";
	private static final String NO_EXISTEN_REGISTROS_DE_USUARIOS = "No Existen registros de Usuarios ";
	private static final String MSJ_USUARIO_REGIST = "Email ya registrado.";
	private static final String MSJ_EMAIL_INVALIDO = "El formato del Email no corresponde Ej: (aaaaaaa@dominio.cl)";
	private static final String MSJ_PASS_INVALIDO = "El formato de password no cumple las condiciones de seguridad. (Una "
			+ "Mayuscula, letras minúsculas, y dos minimo dos numeros) )";
	private static final String CREDENCIALES_INCORRECTAS = "Error de acceso ";
	private static final String CREDENCIALES_CORRECTAS = "Usuario registrado : ";

	@Autowired
	UsuarioRepository repositoryUser;

	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;



	@Override
	public ResponseEntity<?> registrarUsuario(UsuarioRq user) {

		ResponseEntity<?> entityRs = null;
		BooleanResponse mensaje = new BooleanResponse();

		Optional<UsuarioDao> tieneRegistroMail = repositoryUser.findByEmail(user.getEmail());

		if (!Utils.validaEmail(user.getEmail())) {
			mensaje.setMensaje(MSJ_EMAIL_INVALIDO);
			entityRs = new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
		} else if (!Utils.validaContrasenia(user.getPassword())) {
			mensaje.setMensaje(MSJ_PASS_INVALIDO);
			entityRs = new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
		} else if (tieneRegistroMail.isEmpty()) {
			
			UsuarioDao saveUserDao = UsuarioDao.builder()
					.email(user.getEmail())
					.username(user.getEmail())
					.isActive(Boolean.TRUE)
					.name(user.getName())
					.phones(user.getPhones())
					.created(new Date())
					.role(RoleDao.ADMIN)
					.password(passwordEncoder.encode(user.getPassword()))
					.build();
			
			saveUserDao.setToken(jwtService.getToken(saveUserDao));
			
			UsuarioDao saveUser = repositoryUser.save(saveUserDao);
			saveUser.setRole(null);
			saveUser.setPassword(null);
			saveUser.setUsername(null);


			entityRs = new ResponseEntity<>(saveUser, HttpStatus.OK);

		} else {
			mensaje.setMensaje(MSJ_USUARIO_REGIST);
			entityRs = new ResponseEntity<>(mensaje, HttpStatus.IM_USED);

		}

		return entityRs;
	}

	@Override
	public ResponseEntity<?> loginUsuario(LoginRequest request) {
		ResponseEntity<?> entityRs = null;
		BooleanResponse mensaje = new BooleanResponse();
		try {

			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			UserDetails user = repositoryUser.findByEmail(request.getEmail()).orElseThrow();
			String token = jwtService.getToken(user);

			UsuarioDao usuarioSave = (UsuarioDao) user;
			usuarioSave.setToken(token);
			usuarioSave.setLastLogin(new Date());
			repositoryUser.save(usuarioSave);

			TokenKey tokenMsj = new TokenKey();
			tokenMsj.setToken(token);
			tokenMsj.setMensaje(CREDENCIALES_CORRECTAS + request.getEmail());
			entityRs = new ResponseEntity<>(tokenMsj, HttpStatus.OK);

		} catch (Exception e) {
			mensaje.setMensaje(CREDENCIALES_INCORRECTAS + ":  " + e.getMessage());
			entityRs = new ResponseEntity<>(mensaje, HttpStatus.OK);
		}
		return entityRs;
	}

	@Override
	public ResponseEntity<?> updateUsuario(UsuarioRq user) {

		ResponseEntity<?> entityRs = null;
		BooleanResponse mensaje = new BooleanResponse();

		try {
			UsuarioDao saveUser = repositoryUser.findById(user.getId()).get();

			if (!Utils.validaEmail(user.getEmail())) {
				mensaje.setMensaje(MSJ_EMAIL_INVALIDO);
				entityRs = new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);

			} else if (!Utils.validaContrasenia(user.getPassword())) {
				mensaje.setMensaje(MSJ_PASS_INVALIDO);
				entityRs = new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
			} else {

				Boolean isUsuarioActivo = null != user.getIsActive()?user.getIsActive():Boolean.TRUE;
				UsuarioDao saveUpdateFilter = UsuarioDao.builder()
						.email(user.getEmail())
						.id(saveUser.getId())
						.username(user.getEmail())
						.isActive(user.getIsActive())
						.name(user.getName())
						.token(saveUser.getToken())
						.phones(user.getPhones())
						.created(saveUser.getCreated())
						.role(saveUser.getRole())
						.modified(new Date())
						.lastLogin(saveUser.getLastLogin())
						.isActive(isUsuarioActivo)
						.password(passwordEncoder.encode(user.getPassword())).build();

				saveUser = repositoryUser.save(saveUpdateFilter);
				saveUser.setRole(null);
				saveUser.setPassword(null);
				saveUser.setUsername(null);
				saveUser.setToken(null);
				entityRs = new ResponseEntity<>(saveUser, HttpStatus.OK);

			}
			
		} catch (Exception e) {
			mensaje.setMensaje(ERROR_AL_ACTUALIZAR_REGISTRO + e);
			entityRs = new ResponseEntity<>(mensaje, HttpStatus.OK);
		}

		return entityRs;
	}

	@Override
	public ResponseEntity<?> getAllUsuarios(Optional<String> id) {

		ResponseEntity<?> entityRs = null;
		BooleanResponse mensaje = new BooleanResponse();
		List<UsuarioDao> listUsuario = new ArrayList<>();

		try {

			if (!id.isPresent()) {
				listUsuario = repositoryUser.findAll();

				entityRs = new ResponseEntity<>(filterListResp(listUsuario), HttpStatus.OK);

			} else {
				Optional<UsuarioDao> usuarioId = repositoryUser.findById(Integer.parseInt(id.get()));
				listUsuario.add(usuarioId.get());
				entityRs = new ResponseEntity<>(filterListResp(listUsuario), HttpStatus.OK);
			}

			if (listUsuario.isEmpty()) {
				mensaje.setMensaje(NO_EXISTEN_REGISTROS_DE_USUARIOS);
				entityRs = new ResponseEntity<>(mensaje, HttpStatus.ACCEPTED);

			}

		} catch (Exception e) {

			mensaje.setMensaje("No Existe registro de Usuario : " + e.getMessage());
			entityRs = new ResponseEntity<>(mensaje, HttpStatus.ACCEPTED);

		}

		return entityRs;
	}

	@Override
	public ResponseEntity<?> deleteUsuario(String id) {

		BooleanResponse mensaje = new BooleanResponse();
		if (repositoryUser.existsById(Integer.parseInt(id))) {
			repositoryUser.deleteById(Integer.parseInt(id));		
			mensaje.setMensaje("El Usuario con ID : "+ id + " ha sido eliminado");
		} else {
			mensaje.setMensaje(ID_USUARIO_ID_INEXISTENTE);

		}

		
		return new ResponseEntity<>(mensaje, HttpStatus.ACCEPTED);
	}


	
	private List<UsuarioDao> filterListResp(List<UsuarioDao> listUsuario){
		
		return listUsuario = listUsuario.stream().peek(lista ->{ 
			lista.setPassword(null);
			lista.setUsername(null);
			lista.setToken(null);
			lista.setRole(null);
			}).collect(Collectors.toList());
		
	}
	
	
	
	

}
