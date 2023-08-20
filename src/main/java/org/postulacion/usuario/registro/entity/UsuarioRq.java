package org.postulacion.usuario.registro.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRq implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	private Integer id;
	
	@NotBlank
	@Size(max = 120)
	private String name;
	
	@NotBlank
	private String password;
	
	
	@NotBlank
	@Size(max = 90)
	private String email;
	
	
	private Date lastLogin; 

    private Date created;
    
    private Boolean isActive;
	
	private List<PhoneDao> phones;


	
}
