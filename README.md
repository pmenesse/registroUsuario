# registroUsuario
Mantenedor de usuario con control de acceso JwT, con JPA Hibernate , BD en memoria con H2.

# Login
{{SERVER}}/api/usuario/login 
  Usuario defecto : root@lab.col - Register12
  
# registrar un usuario y telefonos : 
POST :  {{SERVER}}/api/usuario/registrar

# Actualizar un usuario y telefonos : 
POST :  {{SERVER}}/api/usuario/actualizar

# Eliminar un usuario y telefonos asociados : 
POST :  {{SERVER}}/api/usuario/eliminar/{id}

# Obtener un usuario y telefonos asociados : 
POST :  {{SERVER}}/api/usuario/obtener/{id} 

# Obtener todos los usuario y telefonos asociados : 
POST :  {{SERVER}}/api/usuario/obtener

# Acceso a BD en memoria H2 
  Path consola BD h2 :
  {{SERVER}}/h2-console/
  BD : H2 (Usuario : "sa" - pass : "password" )

# versiones : 
- Java 17
- Spring boot 3.1.0
- Spring security 
- Tomcat embebido 
- jjwt 0.11.5
- lombok:1.18.28

  






