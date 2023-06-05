package Modelo;

import java.util.ArrayList;

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
		
		nombre_varray = pNombreEst;
		listaVariables=pListaVariables;
		opcion = "varray";
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
