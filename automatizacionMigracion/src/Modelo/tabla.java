package Modelo;

import java.util.HashMap;

public class tabla {
	private String nombre; 
	private HashMap<String, columna> nombresColumnas = new HashMap<>();
	
	public tabla(String pNombre) {
		nombre = pNombre;
	}
	
	public tabla(String pNombre, columna pCol) {
		nombre = pNombre;
		nombresColumnas.put(pCol.getNombre(), pCol);
	}
	
	public void añadirColumna (String pNombre,String pTipo,int pCant) {
		if (nombresColumnas.containsKey(pNombre)) {
			
        } else {
        	columna nuevaColumna = new columna(pNombre,pTipo,pCant);
	
        	nombresColumnas.put(nuevaColumna.getNombre(), nuevaColumna);
        }
	}
	
	public columna recogerColumna (String nombreClave) {
		return nombresColumnas.get(nombreClave);
	}
	
	public int obtenerCantCol() {
		return nombresColumnas.size();
	}
	
	public HashMap<String, columna> obtenerHashColumnas(){
		return nombresColumnas;
	}
}
