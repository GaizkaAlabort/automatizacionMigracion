package Modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class columna {
	private String nombreCol;
	private String tipo;
	private int cantidad;
	
	public columna(String pNombre,String pTipo,int pCant) {
		nombreCol = pNombre;
		tipo = pTipo;
		cantidad = pCant;
	}
	
	public String getNombre() {
		return nombreCol;
	}
	
	public String getNombreBasico() {
		Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)_([a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(nombreCol);
		
		if (matcher.find()) {
			return matcher.group(2);
		}
		else {
			return nombreCol;
		}
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public int getCant() {
		return cantidad;
	}
	
}
