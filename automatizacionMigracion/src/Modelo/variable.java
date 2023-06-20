package Modelo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class variable {
	
	private String nombreVariable;
	private String tipo;
	private int cantidad;
	
	private String tabla;
	private String columna;
	private String tipo_pers;
	
	private ArrayList<variable> listaVariables;
	private String nombre_varray;
	
	private String opcion;
	
	public variable(String pNombre, String pTipo, int pCantidad) {
		nombreVariable = pNombre;
		tipo = pTipo;
		cantidad = pCantidad;
		opcion = "basico";
	}
	
	//OPCION VARIABLE PERSONALIZADO
	public variable(String pNombre, String pTipo, int pCantidad, 
					String pTabla, String pColumna, String pTipoPers) {
		nombreVariable = pNombre;
		tipo = pTipo;
		cantidad = pCantidad;
		
		tabla = pTabla;
		columna = pColumna;
		tipo_pers = pTipoPers;
		opcion = "pers";
	}
	
	//OPCION VARIABLE VARRAY
	public variable(String pNombre, String pTipo, ArrayList<variable> pListaVariables,String pNombreEst) {
		nombreVariable = pNombre;
		tipo = pTipo;
		cantidad = pListaVariables.size();
		
		nombre_varray = pNombreEst;
		listaVariables=pListaVariables;
		opcion = "varray";
	}
	
	public variable(JSONObject obj) {
		System.out.println(obj.getString("opcion"));
		if(obj.getString("opcion").equals("basico")) {
			nombreVariable = obj.getString("nombre");
			tipo = obj.getString("tipo");
			cantidad = obj.getInt("cantidad");
			opcion = "basico";
		} else if(obj.getString("opcion").equals("pers")) {
			nombreVariable = obj.getString("nombre");
			tipo = obj.getString("tipo");
			cantidad = obj.getInt("cantidad");
			
			tabla = obj.getString("tabla");
			columna = obj.getString("columna");
			tipo_pers = obj.getString("tipoPers");
			opcion = "pers";
		} else if(obj.getString("opcion").equals("varray")) {
			nombreVariable = obj.getString("nombre");
			tipo = obj.getString("tipo");
			
			nombre_varray = obj.getString("nombreEstructura");
			opcion = "varray";
			
			System.out.println(obj);
			JSONArray varJSON = obj.getJSONArray("lista");
			System.out.println(varJSON.length());
	    	listaVariables = new ArrayList<variable>();
	    	for(int var = 0; var < varJSON.length(); var++) {
	    		System.out.println(varJSON.getJSONObject(var));
	    		variable nuevavariable = new variable(varJSON.getJSONObject(var));
	    		listaVariables.add(nuevavariable);
	    	}
			cantidad = listaVariables.size();
		}
	}

	
	public String getNombre() {
		return nombreVariable;
	}

	public String getTipo() {
		return tipo;
	}

	public int getCantidad() {
		return cantidad;
	}
	
	//--PERSONALIZADO--
	public String getTabla() {
		return tabla;
	}
	
	public String getColumna() {
		return columna;
	}
	
	public String getTipoPers() {
		return tipo_pers;
	}
	
	//--VARRAY--
	public String getNombreEstructura() {
		return nombre_varray;
	}
	
	public int getCantVariables() {
		return listaVariables.size();
	}
	
	public variable getVariable(int pPosicion) {
		return listaVariables.get(pPosicion);
	}
	
	public void añadirVariable(variable pVariable) {
		listaVariables.add(pVariable);
	}
	
	public ArrayList<variable> getLista(){
		return listaVariables;
	}
	
	public String getOpcion() {
		return opcion;
	}
}
