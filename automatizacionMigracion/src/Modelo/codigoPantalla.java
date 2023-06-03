package Modelo;

import java.util.ArrayList;

public class codigoPantalla {
	private int numeroPantalla;
	private String descripcionPantalla;
	
	public codigoPantalla(int pNumero, String pDescripcion) {
		numeroPantalla = pNumero;
		descripcionPantalla = pDescripcion;
	}
	
	public int getNumPant() {
		return numeroPantalla;
	}
	
	public String getDescPant() {
		return descripcionPantalla;
	}
}
