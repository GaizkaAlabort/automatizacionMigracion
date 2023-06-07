package Modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pantalla {
	
	private String nombrePantalla;
	private String nombreCortoPantalla;
	private ArrayList<campos> listaCampos;
	private boolean[] listaTeclas = new boolean[13];
	private int codigoPantalla;
	
	public pantalla(String pNombre, String pCortNom, ArrayList<campos> pListaCampos, boolean[] pListaTeclas, int pNumero) {
		nombrePantalla = pNombre;
		nombreCortoPantalla = pCortNom;
		listaCampos = pListaCampos;
		listaTeclas = pListaTeclas;
		codigoPantalla = pNumero;
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
	
	public int getCodigoPantalla(){
		return codigoPantalla;
	}
	
	public void actualizarNomenclatura(String nomenclatura) {
		for(int tam=0;tam<listaCampos.size(); tam++) {
			campos campo = listaCampos.get(tam);
			
			Pattern pattern = Pattern.compile("PACK_([a-zA-Z0-9]+).PAN_([a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(campo.getTipo());
			
			ArrayList<String> intermedio = new ArrayList<>(Arrays.asList(
	                "TRF", nomenclatura));
			
			//Comprobamos patron para recoger el nombre de la estructura
			if (matcher.find()) {
				if (!intermedio.contains(matcher.group(1)))
				{
					campo.actualizarTipo("PACK_" + nomenclatura + ".PAN_" + matcher.group(2));
					listaCampos.set(tam, campo);
				}
			}
		}
	}
		
}
