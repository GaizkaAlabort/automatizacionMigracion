package Modelo;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Modelo.estructura;

public class ficheros {
	
	private String nomenclatura;
	private int codigo;
	private ArrayList<estructura> listaEst;
	private ArrayList<pantalla> listaPant;
	private ArrayList<codigoPantalla> listaCodigosPantalla;
		
	public ficheros(String pNombre, int pCodigo, ArrayList<estructura> plistaest, ArrayList<pantalla> plistapant, ArrayList<codigoPantalla> plistacodpantalla) {
		nomenclatura = pNombre;
		codigo = pCodigo;
		listaEst = plistaest;
		listaPant = plistapant;
		listaCodigosPantalla = plistacodpantalla;
	}
		
	public String getNombre() {
		return nomenclatura;
	}
	
	public int getCodigo() {
		return codigo;
	}
		
	public int getCant(String tipo) {
		switch (tipo) {
		case "est":
			return listaEst.size();
		case "pant":
			return listaPant.size();
		case "codPant":
			return listaCodigosPantalla.size();
		default:
			return -1;
		}
	}
		
	public estructura getEstructura(int pPosicion) {
		return listaEst.get(pPosicion);
	}
	
	public pantalla getPantalla(int pPosicion) {
		return listaPant.get(pPosicion);
	}
	
	public codigoPantalla getCodigosPantalla(int pPosicion) {
		return listaCodigosPantalla.get(pPosicion);
	}
	
	public void añadirVariable(String tipo, estructura pEstructura, pantalla pPantalla) {
		switch (tipo) {
		case "est":
			listaEst.add(pEstructura);
			break;
		case "pant":
			listaPant.add(pPantalla);
			break;
		default:
			break;
		}
	}
		
}
