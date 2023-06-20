package Modelo;

import org.json.JSONObject;

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
	
	public campos(JSONObject obj) {
		nombre = obj.getString("nombre");
		tipo = obj.getString("tipo");
		entrada = obj.getBoolean("in");
		salida = obj.getBoolean("out");
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
	
	public void actualizarTipo(String ptipo) {
		tipo = ptipo;
	}
}
