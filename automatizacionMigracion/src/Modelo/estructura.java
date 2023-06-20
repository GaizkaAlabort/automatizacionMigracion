package Modelo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public estructura(JSONObject obj) {
		nombreEstructura = obj.getString("nombreEst");
		
		JSONArray varJSON = obj.getJSONArray("lista variables");
    	
    	lista = new ArrayList<variable>();
    	for(int var = 0; var < varJSON.length(); var++) {
    		variable nuevavariable = new variable(varJSON.getJSONObject(var));
    		lista.add(nuevavariable);
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
