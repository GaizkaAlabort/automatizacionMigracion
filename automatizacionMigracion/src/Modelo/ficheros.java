package Modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

public class ficheros{
	
	private String nomenclatura;
	private int codigo;
	private ArrayList<estructura> listaEst;
	private ArrayList<pantalla> listaPant;
	private ArrayList<codigoPantalla> listaCodigosPantalla;
	
	public ficheros() {
		nomenclatura = null;
		codigo = -1;
		listaEst = null;
		listaPant = null;
		listaCodigosPantalla = null;
	}
	
	public void setNomYCod(String pNombre, int pCodigo) {
		nomenclatura = pNombre;
		codigo = pCodigo;
	}
	
	public void setListaEst(ArrayList<estructura> plistaest) {
		listaEst = plistaest;
	}
	
	public void setListaPant(ArrayList<pantalla> plistapant) {
		listaPant = plistapant;
	}
	
	public void setListaCodPant(ArrayList<codigoPantalla> plistacodpantalla) {
		listaCodigosPantalla = plistacodpantalla;
	}
		
	public ficheros(String pNombre, int pCodigo, ArrayList<estructura> plistaest, ArrayList<pantalla> plistapant, ArrayList<codigoPantalla> plistacodpantalla) {
		nomenclatura = pNombre;
		codigo = pCodigo;
		listaEst = plistaest;
		listaPant = plistapant;
		listaCodigosPantalla = plistacodpantalla;
	}
	
	public ficheros(String ubi) {
		cargarJSON(ubi);
	}
		
	public String getNombre() {
		return nomenclatura;
	}
	
	public int getCodigo() {
		return codigo;
	}
		
	public int getCant(String tipo) {
		try{
			switch (tipo) {
				case "est":
					return listaEst.size();
				case "pant":
					return listaPant.size();
				case "codPant":
					return listaCodigosPantalla.size();
				default:
					return 0;
			}
		} catch (NullPointerException npe) {
			return 0;
		}
	}
		
	public estructura getEstructura(int pPosicion) {
		return listaEst.get(pPosicion);
	}
	
	public ArrayList<estructura> getEstLista(){
		return listaEst;
	}
	
	public pantalla getPantalla(int pPosicion) {
		return listaPant.get(pPosicion);
	}
	
	public ArrayList<pantalla> getPantLista(){
		return listaPant;
	}
	
	public codigoPantalla getCodigosPantalla(int pPosicion) {
		return listaCodigosPantalla.get(pPosicion);
	}
	
	public ArrayList<codigoPantalla> getCodPantLista(){
		return listaCodigosPantalla;
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
	
	private void cargarJSON(String ubiJSON) {
		JSONObject obj;
		try (InputStream input = new FileInputStream(ubiJSON)) {
		    obj = new JSONObject(new JSONTokener(input));
		    System.out.println(obj);
		    
		    nomenclatura= obj.getString("nomenclatura");
		    codigo = obj.getInt("codigo");

		    try {
		    	JSONArray estJSON = obj.getJSONArray("lista estructura");
		    	System.out.println(estJSON);
		    	System.out.println(estJSON.length());
		    	
		    	listaEst = new ArrayList<estructura>();
		    	for(int est = 0; est < estJSON.length(); est++) {
		    		estructura nuevaEstructura = new estructura(estJSON.getJSONObject(est));
		    		listaEst.add(nuevaEstructura);
		    	}
		    	
		    	
		    	JSONArray pantJSON = obj.getJSONArray("lista pantalla");
		    	System.out.println(pantJSON);
		    	System.out.println(pantJSON.length());
		    	
		    	listaPant = new ArrayList<pantalla>();
		    	for(int pant = 0; pant < pantJSON.length(); pant++) {
		    		pantalla nuevapantalla = new pantalla(pantJSON.getJSONObject(pant));
		    		listaPant.add(nuevapantalla);
		    	}
		    	
		    	
		    	JSONArray codpantJSON = obj.getJSONArray("lista codpant");
		    	System.out.println(codpantJSON);
		    	System.out.println(codpantJSON.length());
		    	
		    	listaCodigosPantalla = new ArrayList<codigoPantalla>();
		    	for(int cp = 0; cp < codpantJSON.length(); cp++) {
		    		codigoPantalla nuevocodpantalla = new codigoPantalla(codpantJSON.getJSONObject(cp).getInt("numPant"),codpantJSON.getJSONObject(cp).getString("descPant"));
		    		listaCodigosPantalla.add(nuevocodpantalla);
		    	}
		    	
		    	
		    }catch (JSONException e) {
		    	
		    }
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
