package Modelo;

import java.util.ArrayList;

import Modelo.estructura;

public class ficheros {
	
	private String nomenclatura;
	private int codigo;
	private ArrayList<estructura> lista;
		
	public ficheros(String pNombre, int pCodigo, ArrayList<estructura> plista) {
		nomenclatura = pNombre;
		codigo = pCodigo;
		lista = plista;
	}
		
	public String getNombre() {
		return nomenclatura;
	}
	
	public int getCodigo() {
		return codigo;
	}
		
	public int getCantEstructuras() {
		return lista.size();
	}
		
	public estructura getEstructura(int pPosicion) {
		return lista.get(pPosicion);
	}
		
	public void añadirVariable(estructura pEstructura) {
		lista.add(pEstructura);
	}
		
}
