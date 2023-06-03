package Modelo;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class pantalla {
	
	private String nombrePantalla;
	private String nombreCortoPantalla;
	private ArrayList<campos> listaCampos;
	private boolean[] listaTeclas = new boolean[13];
	private codigoPantalla codigoPantalla;
	
	public pantalla(String pNombre, String pCortNom, ArrayList<campos> pListaCampos, boolean[] pListaTeclas, int pNumero, String pDescripcion) {
		nombrePantalla = pNombre;
		nombreCortoPantalla = pCortNom;
		listaCampos = pListaCampos;
		listaTeclas = pListaTeclas;
		codigoPantalla = new codigoPantalla(pNumero, pDescripcion);
	}
	
	public String getNombre() {
		return nombrePantalla;
	}
	
	public String getNombreCorto() {
		return nombreCortoPantalla;
	}
	
	public ArrayList<campos> getLista(){
		return listaCampos;
	}
	
	public int getCantCampos() {
		return listaCampos.size();
	}
	
	public campos getCampo(int pPosicion) {
		return listaCampos.get(pPosicion);
	}
	
	public boolean[] getTeclas() {
		return listaTeclas;
	}
	
	public boolean getActTecla(int posicion) {
		return listaTeclas[posicion];
	}
	
	public String getTecla(int posicion) {
		switch(posicion) {
		case 0:
			return "ENTER";
		case 1:
			return "F1";
		case 2:
			return "F2";
		case 3:
			return "F3";
		case 4:
			return "F4";
		case 5:
			return "F5";
		case 6:
			return "F6";
		case 7:
			return "F7";
		case 8:
			return "F8";
		case 9:
			return "F9";
		case 10:
			return "F10";
		case 11:
			return "F11";
		case 12:
			return "F12";	
		default:
			return "";
		}
	}
	
	public codigoPantalla getCodigoPantalla(){
		return codigoPantalla;
	}
	
	public int getNumPant() {
		return codigoPantalla.getNumPant();
	}
	
	public String getDescPant() {
		return codigoPantalla.getDescPant();
	}
}
