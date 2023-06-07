package Modelo;

import java.util.ArrayList;

public class estructura {
	
	private String nombreEstructura;
	private ArrayList<variable> lista;
	
	public estructura(String pNombre, ArrayList<variable> pLista) {
		nombreEstructura = pNombre;
		if (pLista == null) {
			lista = new ArrayList<variable>();
		} else {
			lista = pLista;
		}
	}
	
	public String getNombre() {
		return nombreEstructura;
	}
	
	public int getCantVariables() {
		return lista.size();
	}
	
	public variable getVariable(int pPosicion) {
		return lista.get(pPosicion);
	}
	
	public void añadirVariable(variable pVariable) {
		lista.add(pVariable);
	}
	
	public ArrayList<variable> getLista(){
		return lista;
	}
	
}
