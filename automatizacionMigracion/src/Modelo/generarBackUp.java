package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class generarBackUp {
	
	//Back up general
	public generarBackUp(ficheros pficheros) {
		comprobarCarpeta("backups");
		
		JSONObject  fichero= new JSONObject();
		
		fichero.put("nomenclatura", pficheros.getNombre());
		fichero.put("codigo",pficheros.getCodigo());
		
		if(pficheros.getCant("est") > 0) {
			JSONArray listaEst = new JSONArray();
			for (estructura est: pficheros.getEstLista()) {
				listaEst.put(json_estructura(est));
			}
			fichero.put("lista estructura",listaEst);
			
			new generarBackUp(pficheros.getEstLista());
		}
		
		if(pficheros.getCant("pant") > 0) {
			JSONArray listaPant = new JSONArray();
			for (pantalla pant: pficheros.getPantLista()) {
				listaPant.put(json_pantalla(pant));
			}
		fichero.put("lista pantalla",listaPant);
		}
		
		if(pficheros.getCant("codPant") > 0) {
			JSONArray listaCodPant = new JSONArray();
			for (codigoPantalla codpant: pficheros.getCodPantLista()) {
				listaCodPant.put(new JSONObject(codpant));
			}
			fichero.put("lista codpant",listaCodPant);
		}
		
		System.out.println("<-->");
		System.out.println(fichero);
		
		try {
			Date fechaCreacion = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		    String fecha = ft.format(fechaCreacion);
		    
		    comprobarCarpeta("backups/" + pficheros.getNombre());
		    
			FileWriter json = new FileWriter("backups/" + pficheros.getNombre() + "/" + pficheros.getNombre() + "_" + fecha + ".json");
			json.write(fichero.toString());
			json.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Back up de las estructuras
	public generarBackUp(ArrayList<estructura> listaEst) {
		System.out.println("/-/-/- ESTRUCTURA -/-/-/");
		comprobarCarpeta("backups");
		
		File estructuras = new File("backups/estructuras.txt");
		
		FileReader fr = null;
	    BufferedReader br = null;
	    FileWriter fichero = null;
	    BufferedWriter pw = null;
		
        ArrayList<String> listaNombreEst = new ArrayList<String>();
		ArrayList<String> jsons = new ArrayList<String>();
		
		if(estructuras.exists()) {
			try {
				fr = new FileReader (estructuras);
				br = new BufferedReader(fr);
				
				
				String[] listaNombres = br.readLine().split(";");
				listaNombreEst = new ArrayList<String>(Arrays.asList(listaNombres));
				System.out.println(listaNombreEst.size());
				String linea;
		        while((linea=br.readLine())!=null) {
		        	linea = br.readLine();
		        	jsons.add(linea);
		        	System.out.println(linea);
		        	br.readLine();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(estructura est: listaEst) {
			JSONObject json = json_estructura(est);
			
			if(listaNombreEst != null && listaNombreEst.contains(est.getNombre())) {
				int localizacion = listaNombreEst.indexOf(est.getNombre());
				jsons.set(localizacion, json.toString());
				
			} else {
				listaNombreEst.add(est.getNombre());
				jsons.add(json.toString());
			}
		}
		
		try {
			fichero = new FileWriter(estructuras);  
			pw = new BufferedWriter(fichero);
			
			String primeraLinea = "";
			for(int i=0; i<listaNombreEst.size(); i++) {
				if(i == 0) {
					primeraLinea = listaNombreEst.get(i);
				} else {
					primeraLinea = primeraLinea + ";" + listaNombreEst.get(i);
				}
			}
			
			pw.write(primeraLinea + "\r\n");
			
			for(int i=0; i<listaNombreEst.size(); i++) {
				pw.write(listaNombreEst.get(i) + "\r\n");
				pw.write(jsons.get(i) + "\r\n");
				pw.write("\r\n");
			}
			
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("/-/-/- .... -/-/-/");
	}
	
	public void comprobarCarpeta(String nombre) {
		System.out.println("ªªªCOMPROBAR EXISTENCIA CARPETA "+ nombre +"ªªª");
		File file = new File(nombre);
		
		if(file.exists()) {
			System.out.println("--No es necesario crear carpeta");
		} else {
			System.out.println("--Creando carpeta '"+ nombre +"'");
			file.mkdir();
		}
	}
	
	public JSONObject json_estructura(estructura est) {
		System.out.println("+ ESTRUCTURA +");
		JSONObject  estr= new JSONObject();
		estr.put("nombreEst", est.getNombre());
		
		JSONArray listaVar = new JSONArray();
		for(variable var: est.getLista()) {
			JSONObject  vari= new JSONObject(var);
			//vari = tipos_estructura(var);
			System.out.println("+ " + vari);
			listaVar.put(vari);
		}
		estr.put("lista variables", listaVar);
		
		System.out.println("+ JSON ESTRUCTURA final:"+ estr);
		return estr;
	}
	
	/*private JSONObject tipos_estructura(variable var) {
		JSONObject  vari= new JSONObject();
		if (var.getOpcion().equals("basico")) {
			vari.put("opcion", "basico");
			vari.put("nombreVar", var.getNombre());
			vari.put("tipoVar", var.getTipo());
			vari.put("cantVar", var.getCantidad());
			vari.put("tablaVar", "");
			vari.put("columnaVar", "");
			vari.put("tipoPersVar", "");
			vari.put("nombreVarrVar","");
			vari.put("listaVar", "");
		} else if (var.getOpcion().equals("pers")) {
			vari.put("opcion", "pers");
			vari.put("nombreVar", var.getNombre());
			vari.put("tipoVar", var.getTipo());
			vari.put("cantVar", var.getCantidad());
			vari.put("tablaVar", var.getTabla());
			vari.put("columnaVar", var.getColumna());
			vari.put("tipoPersVar", var.getTipoPers());
			vari.put("nombreVarrVar","");
			vari.put("listaVar", "");
		} else if (var.getOpcion().equals("varray")) {
			vari.put("opcion", "varray");
			vari.put("nombreVar", var.getNombre());
			vari.put("tipoVar", var.getTipo());
			vari.put("cantVar", var.getCantVariables());
			vari.put("tablaVar", "");
			vari.put("columnaVar", "");
			vari.put("tipoPersVar", "");
			vari.put("nombreVarrVar","");
			
			JSONArray listaVarrVar = new JSONArray();
			for(variable VarrVar: var.getLista()) {
				listaVarrVar.put(tipos_estructura(VarrVar));
			}
			vari.put("listaVar", listaVarrVar);
		}
		return vari;
	}*/
	
	public JSONObject json_pantalla(pantalla pant) {
		System.out.println("* PANTALLA *");
		JSONObject  panta= new JSONObject();
		panta.put("nombrePant", pant.getNombre());
		panta.put("nombreCorto", pant.getNombreCorto());
		
		JSONArray listaCampos = new JSONArray();
		for(campos camp: pant.getLista()) {
			JSONObject  campo= new JSONObject(camp);
			
			System.out.println("* " + campo);
			listaCampos.put(campo);
		}
		panta.put("lista campos", listaCampos);
		JSONArray listaTeclas = new JSONArray(pant.getTeclas());
		panta.put("lista teclas", listaTeclas);
		panta.put("codigoPantalla", pant.getCodigoPantalla());
		
		System.out.println("* JSON PANTALLA final:"+ panta);
		return panta;
	}
	
	
}
