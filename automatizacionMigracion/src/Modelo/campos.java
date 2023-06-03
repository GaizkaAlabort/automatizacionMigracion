package Modelo;

import java.util.ArrayList;

public class campos {
	private String nombre;
	private String tipo;
	private boolean entrada;
	private boolean salida;
	
	public campos(String pNombre, String pTipo, boolean pIn, boolean pOut) {
		nombre = pNombre;
		tipo = pTipo;
		entrada = pIn;
		salida = pOut;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public boolean getIn() {
		return entrada;
	}
	
	public boolean getOut() {
		return salida;
	}
	
	public String getEntSal() {
		if(entrada && !salida) {
			return "IN ";
		}
		else if(!entrada && salida) {
			return "OUT ";
		}
		else if(entrada && salida) {
			return "IN OUT ";
		}
		else {//!entrada && !salida
			return "";
		}
	}
}
