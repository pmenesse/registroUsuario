package org.postulacion.usuario.registro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	public static Boolean validaEmail(String email) {
	    Pattern pattern = Pattern.compile(REGEX_EMAIL);
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	
	private static final String REGEX_EMAIL = "^[-\\w.%+]{1,64}@(?:[A-Za-z0-9-]{1,63}\\.){1,125}[A-Za-z]{2,63}$";


public static Boolean validaContrasenia(String cont) {
	String regexClave = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*[0-9].{2}).{4,}$";
	Pattern pattern = Pattern.compile(regexClave);
	    
	 Matcher matcher = pattern.matcher(cont);
	 return matcher.matches();
			
			
}




}