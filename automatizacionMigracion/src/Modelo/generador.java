package Modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class generador {
	
	private ficheros infoEstructura;
	
	private String nomenclatura;
	private int codigoPet;
	private String modulo;
	private String fecha;
	private int cantEstructuras;
	private int cantPantallas;
	private int cantCodPantallas;
	
	private String ubicacionZip;
	
	public generador(ficheros pficheros, String ubicacion) {
		System.out.println("******INICIO DEL GENERADOR DE FICHEROS******");
		infoEstructura = pficheros;
		ubicacionZip = ubicacion;
		System.out.println("-+Descargando en " + ubicacionZip + "+-");
		
		nomenclatura = infoEstructura.getNombre();
        codigoPet = infoEstructura.getCodigo();
        cantEstructuras = infoEstructura.getCant("est");
        cantPantallas = infoEstructura.getCant("pant");
        cantCodPantallas = infoEstructura.getCant("codPant");
		
        modulo = nomenclatura;
        if(nomenclatura.substring(0, 3) == "trf" || nomenclatura.substring(0, 3) == "TRF")
        {
        	modulo =  nomenclatura.replaceFirst("(trf|TRF)", "");
        	modulo =  modulo.replaceAll("[^a-zA-Z]", "");
        }
        else
        {
        	nomenclatura = "TRF"+nomenclatura.toUpperCase();
        	modulo =  modulo.replaceAll("[^a-zA-Z]", "");
        }
        System.out.println("MODULO: " + modulo + " de la NOMENCLATURA: "+ nomenclatura);
        
        Date fechaCreacion = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
	    fecha = ft.format(fechaCreacion);
        
		try {
			paquete1();
			paqueteCON();
			paqueteWS();
			controller();
			instr();
			guia();
			
			generarZip();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR AL GENERAR EL PRIMER PACK");
			e.printStackTrace();
		}
	}
	
	
	private void paquete1() throws IOException{        
        
		String nombreArchivo = "PACK_" + nomenclatura;
	    
		//-------SPC------------
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
        
        System.out.println("INICIO DE ARCHIVO SPC");
        FileWriter fw_spc = new FileWriter("archivosTemp/" + nombreArchivo + ".spc");
        
        BufferedWriter bw_spc = new BufferedWriter(fw_spc);
        
        bw_spc.write("create or replace package "+ nombreArchivo + " is \r\n"
        			+ "/*============================================================================ \r\n"
        			+ "  Procedimiento: " + nombreArchivo + "\r\n"
        			+ "  Version: 1.0 \r\n"
        			+ "  Descripcion: Paquete para " + nomenclatura);
        bw_spc.newLine();
        bw_spc.newLine();//ESPACIO VACIO
        bw_spc.write("  " + fecha + "    1.0    " + codigoPet + " Migración del modulo " + nomenclatura + "\r\n"
        			+"                       Modulo " + modulo + "\r\n"
        			+"                       Creación");
        bw_spc.newLine();
        bw_spc.write("============================================================================*/");
        bw_spc.newLine();
        bw_spc.newLine();//ESPACIO VACIO
        
        //**ESTRUCTURAS**
        for (int i=0; i<cantEstructuras; i++)
        {
        	estructura estructura = infoEstructura.getEstructura(i);
        	
        	for (int j=0; j<estructura.getCantVariables(); j++)
            {
        		variable variable= estructura.getVariable(j);
        		
        		if (variable.getTipo()== "VARRAY") {
        			
        			bw_spc.write("  -- Array para estructura " + estructura.getNombre() + ", " + variable.getNombreEstructura());
		            bw_spc.newLine();
		            bw_spc.write("  TYPE " + variable.getNombreEstructura() + " IS RECORD");
		            bw_spc.newLine();
		            bw_spc.write("  {");
		            bw_spc.newLine();
        			
        			for (int k=0; k<variable.getCantVariables(); k++)
        			{
	        			variable variableVarray = variable.getVariable(k);

	                	String linea ="";
	                	if (variableVarray.getTipo() == "VARCHAR2" ||  variableVarray.getTipo() == "NUMBER" || variableVarray.getTipo() == "DATE") {
	                		linea = variableVarray.getNombre() + " " + variableVarray.getTipo();
	    	            	if (variableVarray.getCantidad() != -1)
	    	            	{
	    	            		linea = linea + "(" + variableVarray.getCantidad() + ")";
	    	            	}
	                	} else if (variableVarray.getTipo() == "Personalizado") {
	                		linea = variableVarray.getNombre() + " " + variableVarray.getTabla() + "." + variableVarray.getColumna() + "%TYPE";
	                	}
	                	
	                	if (k < variable.getCantVariables()- 1) 
	                	{
	                		linea = linea + ",";
	                	}
	                	
	                	bw_spc.write("       " + linea);
	                    bw_spc.newLine();  
	                     
        			}
        			
        			bw_spc.write("  };");
	                bw_spc.newLine();
	                bw_spc.newLine();//ESPACIO VACIO
	                
	                bw_spc.write("  TYPE VR_" + variable.getNombreEstructura() + " IS VARRAY(10) OF " + variable.getNombreEstructura() + ";");
	                bw_spc.newLine();
	                bw_spc.newLine();//ESPACIO VACIO
                }
                
            }
        	
        	bw_spc.write("  -- Tipo de datos para pantalla " + estructura.getNombre());
            bw_spc.newLine();
            bw_spc.write("  TYPE PAN_" + estructura.getNombre() + " IS RECORD");
            bw_spc.newLine();
            bw_spc.write("  {");
            bw_spc.newLine();
            
            for (int j=0; j<estructura.getCantVariables(); j++)
            {
            	variable variable= estructura.getVariable(j);
            	
            	String linea ="";
            	if (variable.getTipo() == "VARCHAR2" ||  variable.getTipo() == "NUMBER" || variable.getTipo() == "DATE") {
            		linea = variable.getNombre() + " " + variable.getTipo();
	            	if (variable.getCantidad() != -1)
	            	{
	            		linea = linea + "(" + variable.getCantidad() + ")";
	            	}
            	} else if (variable.getTipo() == "Personalizado") {
            		linea = variable.getNombre() + " " + variable.getTabla() + "." + variable.getColumna() + "%TYPE";
            	} else if (variable.getTipo() == "VARRAY") {
            		linea = variable.getNombre() + " VR_" + variable.getNombreEstructura();
            	}
            	
            	if (j < estructura.getCantVariables()- 1) 
            	{
            		linea = linea + ",";
            	}
            	
            	 bw_spc.write("       " + linea);
                 bw_spc.newLine();
            }
            
            bw_spc.write("  };");
            bw_spc.newLine();
            bw_spc.newLine();//ESPACIO VACIO
        }
        
        //**VARIABLES GLOBALES**
        bw_spc.write("  -- Variables globales");
        bw_spc.newLine();
        for (int i=0; i<cantEstructuras; i++)
        {
        	estructura estructura = infoEstructura.getEstructura(i);
        	String nombreEstructura = estructura.getNombre();
        	
        	bw_spc.write("  G_CAM" + nombreEstructura + "  PAN_" + nombreEstructura + ";  -- Campos de pantalla " + nombreEstructura);
            bw_spc.newLine();
        }
        
        //**DECLARAR INICIADORES (INI)**
        bw_spc.newLine();//ESPACIO VACIO
        bw_spc.write("  -- Procedimientos de inicializacion \r\n"
        			 + "  -- de variables globales");
        bw_spc.newLine();
        for (int i=0; i<cantEstructuras; i++)
        {
        	estructura estructura = infoEstructura.getEstructura(i);
        	String nombreEstructura = estructura.getNombre();
        	
        	bw_spc.write("  PROCEDURE INI_CAM" + nombreEstructura + ";");
            bw_spc.newLine();
        }
        bw_spc.write("  PROCEDURE INI_GLOBALES;");
        bw_spc.newLine();
        
        //**DECLARAR extraccion, actualizadores y log (SEL, UPD y LOG)**
        bw_spc.newLine();//ESPACIO VACIO
        bw_spc.write("  -- Procedimientos de extraccion, actualizacion \r\n"
        		   + "  -- y log de variables globales");
        bw_spc.newLine();
        for (int i=0; i<cantEstructuras; i++)
        {
        	estructura estructura = infoEstructura.getEstructura(i);
        	String nombreEstructura = estructura.getNombre();
        	
        	bw_spc.write("  PROCEDURE SEL_CAM" + nombreEstructura + "(json IN OUT pljson); \r\n"
        			   + "  PROCEDURE UPD_CAM" + nombreEstructura + "(json IN pljson); \r\n"
        			   + "  PROCEDURE LOG_CAM" + nombreEstructura + "(P_TIPLOG NUMBER); \r\n");
            bw_spc.newLine();//ESPACIO VACIO
        }
        
        //**DECLARAR logica negocio**
        bw_spc.write("  -- Procedimientos de logica de negocio");
        bw_spc.newLine();
        //**PANTALLAS**
        for (int i=0; i<cantPantallas; i++)
        {
        	pantalla pantalla = infoEstructura.getPantalla(i);
        	
        	String procedimiento = "  PROCEDURE " + pantalla.getNombre() + "(";
        	
        	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		procedimiento = procedimiento + pantalla.getCampo(c).getNombre() + " " + pantalla.getCampo(c).getEntSal() + pantalla.getCampo(c).getTipo();
        		if(c<pantalla.getCantCampos()-1) {
        			procedimiento = procedimiento + ", \r\n               ";
        		}
            }
        	procedimiento = procedimiento + ");";
        	bw_spc.write(procedimiento);
            bw_spc.newLine();
            bw_spc.newLine();//ESPACIO VACIO
        }
        bw_spc.write("end " + nombreArchivo + ";");
        bw_spc.newLine();
        bw_spc.close();
        
        
        //-------BODY------------
        System.out.println("INICIO DE ARCHIVO BDY");
        FileWriter fw_bdy = new FileWriter("archivosTemp/" + nombreArchivo + ".bdy");
        
        BufferedWriter bw_bdy = new BufferedWriter(fw_bdy);
        
        bw_bdy.write("create or replace package body "+ nombreArchivo + " is");
        bw_bdy.newLine();
        bw_bdy.newLine();//ESPACIO VACIO
        bw_bdy.write("V_CODPACK VARCHAR(20) := '"+ nombreArchivo + "-';");
        bw_bdy.newLine();
        bw_bdy.newLine();//ESPACIO VACIO
        
        
        //**INICIALIZACION (INI)**
        bw_bdy.write("-- Procedimientos de inicializacion");
        bw_bdy.newLine();
        for (int i=0; i<cantEstructuras; i++)
        {
        	estructura estructura = infoEstructura.getEstructura(i);
        	String nombreEstructura = estructura.getNombre();
        	
        	bw_bdy.write("PROCEDURE INI_CAM" + nombreEstructura + " IS");
        	bw_bdy.newLine();
        	bw_bdy.write("BEGIN");
        	bw_bdy.newLine();
        	for (int j=0; j<estructura.getCantVariables(); j++)
        	{
        		variable variable = estructura.getVariable(j);
        		
        		if(variable.getTipo() == "NUMBER" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "NUMBER")) {
        			bw_bdy.write("  G_CAM" + nombreEstructura + "." + variable.getNombre() + " := -1;");
        		} else if (variable.getTipo() == "VARRAY"){
        			bw_bdy.write("  G_CAM" + nombreEstructura + "." + variable.getNombre() + " := VR_" + variable.getNombreEstructura() + "();");
        			bw_bdy.newLine();
        			bw_bdy.write("  G_CAM" + nombreEstructura + "." + variable.getNombre() + ".EXTEND(10);");
        			bw_bdy.newLine();
        			bw_bdy.write("  FOR i IN 1..10 LOOP");
        			bw_bdy.newLine();
        			
        			for (int k=0; k<variable.getCantVariables(); k++)
        			{
	        			variable variableVarray = variable.getVariable(k);
	        			if(variableVarray.getTipo() == "NUMBER" || (variableVarray.getTipo() == "Personalizado" && variableVarray.getTipoPers() == "NUMBER")) {
	            			bw_bdy.write("    G_CAM" + nombreEstructura + "." + variable.getNombre() + "(I)." + variableVarray.getNombre() + " := -1;");
	            		} else {
	            			bw_bdy.write("    G_CAM" + nombreEstructura + "." + variable.getNombre() + "(I)." + variableVarray.getNombre() + " := null;");
	            		}
	        			bw_bdy.newLine();
        			}
        			
        			bw_bdy.write("  END LOOP;");
        		} else {
        			bw_bdy.write("  G_CAM" + nombreEstructura + "." + variable.getNombre() + " := null;");
        		}
        		bw_bdy.newLine();
        	}
        	bw_bdy.write("END INI_CAM" + nombreEstructura + ";");
        	bw_bdy.newLine();
        	bw_bdy.write("----------------------------------------------------------------------------------");
        	bw_bdy.newLine();
        }
        bw_bdy.write("PROCEDURE INI_GLOBALES IS");
        bw_bdy.newLine();
        bw_bdy.write("BEGIN");
        bw_bdy.newLine();
        for (int i=0; i<cantEstructuras; i++)
        {
        	String nombreEstructura = infoEstructura.getEstructura(i).getNombre();
        	bw_bdy.write("  INI_CAM" + nombreEstructura + ";");
            bw_bdy.newLine();
        }
        bw_bdy.write("END INI_GLOBALES;");
        bw_bdy.newLine();
        bw_bdy.write("----------------------------------------------------------------------------------");
    	bw_bdy.newLine();
    	
    	bw_bdy.write("-- \r\n" 
    				+ "-- Procedimientos de extraccion y actualizacion \r\n"
    				+ "-- de variables globales \r\n"
    				+ "--");
        bw_bdy.newLine();
        
        for (int i=0; i<cantEstructuras; i++)
        {
        	String nombreEstructura = infoEstructura.getEstructura(i).getNombre();
        	
        	bw_bdy.write("PROCEDURE SEL_CAM"+ nombreEstructura +"(json IN OUT pljson) IS \r\n"
        			+ "  json_aux pljson; \r\n"
        			+ " BEGIN \r\n" 
        			+ "  json_aux := pljson('{}'); \r\n");
            bw_bdy.newLine(); //ESPACIO VACIO
            
            estructura estructura = infoEstructura.getEstructura(i);
            for (int j=0; j<estructura.getCantVariables(); j++)
        	{
            	variable variable = estructura.getVariable(j);
        		
            	if (variable.getTipo() != "VARRAY") {
	            	bw_bdy.write("  if G_CAM"+ nombreEstructura+"." + variable.getNombre() +" is not null then \r\n"
	            			   + "    json_aux.put('"+ variable.getNombre() +"', G_CAM"+ nombreEstructura+"." + variable.getNombre() + "); \r\n"
	            			   + "  else \r\n");
	        		if(variable.getTipo() == "NUMBER" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "NUMBER")) {
	        			bw_bdy.write("    json_aux.put('"+ variable.getNombre() +"', 0 );");
	        		} else {
	        			bw_bdy.write("    json_aux.put('"+ variable.getNombre() +"', '' );");
	        		}
	        		bw_bdy.newLine();
	        		bw_bdy.write("  end if; \r\n");
        		} else {
        			bw_bdy.write("  IF G_CAM"+ nombreEstructura+"." + variable.getNombre() +" IS NOT NULL THEN \r\n"
	            			   + "    ARR := pljson_list(); \r\n" 
        					   + "    json_arr_aux := pljson('{}'); \r\n");
        			bw_bdy.newLine(); //ESPACIO VACIO
        			
        			bw_bdy.write("    FOR ln_indice IN 1..G_CAM"+ nombreEstructura+"." + variable.getNombre() +".count LOOP \r\n"
	            			   + "      P_ARTICULOS := G_CAM"+ nombreEstructura+"." + variable.getNombre() +"(ln_indice); \r\n");
        			
        			for (int k=0; k<variable.getCantVariables(); k++)
        			{
        				bw_bdy.newLine(); //ESPACIO VACIO
        				
        				variable variableVarray = variable.getVariable(k);
        				
	        			bw_bdy.write("      if P_ARTICULOS." + variableVarray.getNombre() +" is not null then \r\n"
		            			   + "        json_arr_aux.put('"+ variableVarray.getNombre() +"', P_ARTICULOS." + variableVarray.getNombre() + "); \r\n"
		            			   + "      else \r\n");
		        		
	        			if(variableVarray.getTipo() == "NUMBER" || (variableVarray.getTipo() == "Personalizado" && variableVarray.getTipoPers() == "NUMBER")) {
		        			bw_bdy.write("        json_aux.put('"+ variableVarray.getNombre() +"', 0 );");
		        		} else {
		        			bw_bdy.write("        json_aux.put('"+ variableVarray.getNombre() +"', '' );");
		        		}
	        			bw_bdy.newLine();
	        			bw_bdy.write("      end if; \r\n");
        			}
        			
	        		bw_bdy.newLine(); //ESPACIO VACIO
	        		bw_bdy.write("      ARR.APPEND(json_arr_aux.to_json_value); \r\n"
	        				   + "    END LOOP; \r\n"
	        				   + "  ELSE \r\n"
	        				   + "    ARR := pljson_list(); \r\n"
	        				   + "    json_arr_aux := pljson('{}'); \r\n");
	        		
	        		for (int k=0; k<variable.getCantVariables(); k++)
        			{
        				bw_bdy.newLine(); //ESPACIO VACIO
        				
        				variable variableVarray = variable.getVariable(k);
        				
        				if(variableVarray.getTipo() == "NUMBER" || (variableVarray.getTipo() == "Personalizado" && variableVarray.getTipoPers() == "NUMBER")) {
    	        			bw_bdy.write("    json_arr_aux.put('"+ variableVarray.getNombre() +"', 0 );");
    	        		} else {
    	        			bw_bdy.write("    json_arr_aux.put('"+ variableVarray.getNombre() +"', '' );");
    	        		}
        			}
	        		bw_bdy.newLine();
	        		bw_bdy.newLine(); //ESPACIO VACIO
	        		bw_bdy.write("    ARR.APPEND(json_arr_aux.to_json_value); \r\n"
	        				   + "    json_aux.put('"+ variable.getNombre() +"',arr); \r\n"
	        				   + "  END IF; \r\n");
        		}
        	}
            
            bw_bdy.write("  json.put('PAN_"+ nombreEstructura +"',json_aux); \r\n"
            			 + "END SEL_CAM"+ nombreEstructura +";");
            bw_bdy.newLine();
            bw_bdy.write("----------------------------------------------------------------------------------");
            bw_bdy.newLine();
            
            bw_bdy.write("PROCEDURE UPD_CAM"+ nombreEstructura +"(json IN pljson) IS \r\n"
       			       + "  json_aux pljson; \r\n"
       			       + "BEGIN \r\n"
       			       + "  INI_CAM"+ nombreEstructura +"; \r\n"
       			       + "  \r\n"
       			       + "  json_aux := pljson_ext.get_json(json,'PAN_"+ nombreEstructura +"');"
       			       + "  \r\n");
            
            for (int j=0; j<estructura.getCantVariables(); j++)
        	{
            	variable variable = estructura.getVariable(j);
        		
            	if(variable.getTipo() == "NUMBER" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "NUMBER")) {
            		bw_bdy.write("  G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := pljson_ext.get_number(json_aux, '" + variable.getNombre() +"');");
            	} else if(variable.getTipo() == "VARCHAR2" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "VARCHAR2")) {
            		bw_bdy.write("  G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := pljson_ext.get_string(json_aux, '" + variable.getNombre() +"');");
            	} else if(variable.getTipo() == "DATE" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "DATE")) {
            		bw_bdy.write("  G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := to_date(pljson_ext.get_string(json_aux, '" + variable.getNombre() +"'), 'DD/MM/YY HH24:MI:SS');");
            	} else if(variable.getTipo() == "VARRAY") {
            		bw_bdy.write("  arr := pljson_ext.get_pljson_list(json_aux,'"+ variable.getNombre() +"'); \r\n"
	        				   + "  IF arr.COUNT > 0 THEN \r\n"
	        				   + "    G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := VR_"+ variable.getNombreEstructura() +"(); \r\n"
	        				   + "    FOR i IN 1..ARR.COUNT LOOP \r\n"
	        				   + "      datos := arr.get(i).to_char(false); \r\n"
	        				   + "      json_arr_aux := pljson(datos); \r\n"
	        				   + "      G_CAM" + nombreEstructura +"."+ variable.getNombre()+".EXTEND; \r\n");
                	
            		for (int k=0; k<variable.getCantVariables(); k++)
        			{
            			variable variableVarray = variable.getVariable(k);
	            		if(variableVarray.getTipo() == "NUMBER" || (variableVarray.getTipo() == "Personalizado" && variableVarray.getTipoPers() == "NUMBER")) {
	                		bw_bdy.write("      G_CAM"+ nombreEstructura +"." + variable.getNombre() +"(I)."+ variableVarray.getNombre() +" := pljson_ext.get_number(json_arr_aux, '" + variableVarray.getNombre() +"');");
	                	} else if(variableVarray.getTipo() == "VARCHAR2" || (variableVarray.getTipo() == "Personalizado" && variableVarray.getTipoPers() == "VARCHAR2")) {
	                		bw_bdy.write("      G_CAM"+ nombreEstructura +"." + variable.getNombre() +"(I)."+ variableVarray.getNombre() +" := pljson_ext.get_string(json_arr_aux, '" + variableVarray.getNombre() +"');");
	                	} else if(variableVarray.getTipo() == "DATE" || (variableVarray.getTipo() == "Personalizado" && variableVarray.getTipoPers() == "DATE")) {
	                		bw_bdy.write("      G_CAM"+ nombreEstructura +"." + variable.getNombre() +"(I)."+ variableVarray.getNombre() +" := to_date(pljson_ext.get_string(json_arr_aux, '" + variableVarray.getNombre() +"'), 'DD/MM/YY HH24:MI:SS');");
	                	}
	            		bw_bdy.newLine();
        			}
            		bw_bdy.write("    END LOOP; \r\n"
            			       + "  END IF;");
            	}
            	bw_bdy.newLine();
        	}
            
            bw_bdy.newLine();//ESPACIO VACIO
            bw_bdy.write("END UPD_CAM"+ nombreEstructura +";");
            bw_bdy.newLine();
            bw_bdy.write("----------------------------------------------------------------------------------");
            bw_bdy.newLine();
            
            bw_bdy.write("PROCEDURE LOG_CAM"+ nombreEstructura +"(P_TIPLOG NUMBER) IS \r\n"
            		   + "  V_LOG CLOB := EMPTY_CLOB(); \r\n"
            		   + "  V_MENSA VARCHAR2(1000); \r\n"
            		   + "BEGIN \r\n");
            
            bw_bdy.newLine();//ESPACIO VACIO
            
            bw_bdy.write("  -- Las trazas estan activadas \r\n"
            		   + "  IF PACK_TRF.HAYLOG THEN \r\n"
            		   + "    -- Construimos el mensaje con los valores de los campos de la variable global \r\n");
            
            for (int j=0; j<estructura.getCantVariables(); j++)
        	{
            	variable variable = estructura.getVariable(j);
        		
            	if(variable.getTipo() == "NUMBER" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "NUMBER")) {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := ' || NVL(TO_CHAR(G_CAM"+ nombreEstructura +"." + variable.getNombre() +"),-1) || ';' || CHR(13);");
            	} else if(variable.getTipo() == "VARCHAR2" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "VARCHAR2")) {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := '; \r\n"
            				   + "    IF G_CAM"+ nombreEstructura +"." + variable.getNombre() +" IS NULL THEN \r\n"
            				   + "       V_MENSA := V_MENSA ||'NULL' || ';' || CHR(13); \r\n"
            				   + "    ELSE \r\n"
            				   + "       V_MENSA := V_MENSA ||'''' || G_CAM"+ nombreEstructura +"." + variable.getNombre() +" ||''';' || CHR(13); \r\n"
            				   + "    END IF;");
            	} else if(variable.getTipo() == "DATE" || (variable.getTipo() == "Personalizado" && variable.getTipoPers() == "DATE")) {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := '; \r\n"
            				   + "    IF G_CAM"+ nombreEstructura +"." + variable.getNombre() +" IS NULL THEN \r\n"
            				   + "       V_MENSA := V_MENSA || '0/0/0' || ';' || CHR(13); \r\n"
            				   + "    ELSE \r\n"
            				   + "       V_MENSA := V_MENSA || '''' || to_char(G_CAM"+ nombreEstructura +"." + variable.getNombre() +",'DD-MM-RRRR HH24:MI:SS') ||''';' || CHR(13);\r\n"
            				   + "    END IF;");
            	} else if(variable.getTipo() == "VARRAY") {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := ' || CHR(13); \r\n"
            				   + "    IF G_CAM"+ nombreEstructura +"." + variable.getNombre() + "IS NULL THEN \r\n"
            				   + "        V_MENSA := V_MENSA ||'NULL'||';'||CHR(13); \r\n"
            				   + "        DBMS_LOB.WRITEAPPEND(V_LOG,LENGTH(V_MENSA),V_MENSA); \r\n"
            				   + "    ELSE \r\n"
            				   + "        DBMS_LOB.WRITEAPPEND(V_LOG,LENGTH(V_MENSA),V_MENSA); \r\n"
            				   + "        FOR I IN 1..G_CAM"+ nombreEstructura +"."+ variable.getNombre() +".COUNT LOOP \r\n"
            				   + "          V_MENSA := 'I = '|| NVL(TO_CHAR(I),'NULL') || CHR(13); \r\n");
            		
            		for (int k=0; k<variable.getCantVariables(); k++)
        			{
            			variable variableVarray = variable.getVariable(k);
            			bw_bdy.write("          V_MENSA := V_MENSA || '"+variableVarray.getNombre()+" '||I||'G_CAM"+ nombreEstructura +"."+ variable.getNombre() +"(I)."+variableVarray.getNombre() +"|| CHR(13);");
            			bw_bdy.newLine();
        			}
            		
            		bw_bdy.write("          DBMS_LOB.WRITEAPPEND(V_LOG,LENGTH(V_MENSA),V_MENSA); \r\n"
         				   	   + "        END LOOP \r\n"
         				   	   + "    END IF;");
            	}
            	bw_bdy.newLine();
            	
            	if(j==0 && variable.getTipo() != "VARRAY")
            	{
            		bw_bdy.write("    V_LOG := V_MENSA;");
            	} else if(variable.getTipo() != "VARRAY"){
            		bw_bdy.write("    DBMS_LOB.WRITEAPPEND(V_LOG,LENGTH(V_MENSA),V_MENSA);");
            	}
            	bw_bdy.newLine();
            	bw_bdy.newLine();//ESPACIO VACIO
        	}
            
            
            bw_bdy.write("    -- Grabamos la traza \r\n"
            		   + "    PACK_TRF.LOG_DATOSBD(PACK_TRF.G_OPERAR,PACK_TRF.G_ERROR,V_LOG,P_TIPLOG); \r\n"
            		   + "  END IF; \r\n"
            		   + "END LOG_CAM"+ nombreEstructura +";");
            bw_bdy.newLine();
            bw_bdy.write("----------------------------------------------------------------------------------");
            bw_bdy.newLine();
            
        }
        
        bw_bdy.write("-- \r\n"
        		   + "-- Procedimientos de logica de negocio \r\n"
        		   + "--");
        bw_bdy.newLine();
        //**PANTALLAS**
        for (int i=0; i<cantPantallas; i++)
        {
        	pantalla pantalla = infoEstructura.getPantalla(i);
        	
        	bw_bdy.write("/*============================================================================ \r\n"
        				+" Función: " + pantalla.getNombre() + "\r\n"
        				+" Version: 1.00 \r\n"
        				+" " + fecha + "  Creacion \r\n"
        				+"============================================================================*/");
        	bw_bdy.newLine();
        	String procedimiento = "PROCEDURE " + pantalla.getNombre() + "(";
        	
        	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		procedimiento = procedimiento + pantalla.getCampo(c).getNombre() + " " + pantalla.getCampo(c).getEntSal() + pantalla.getCampo(c).getTipo();
        		if(c<pantalla.getCantCampos()-1) {
        			procedimiento = procedimiento + ", \r\n               ";
        		}
            }
        	procedimiento = procedimiento + ") IS";
        	bw_bdy.write(procedimiento);
        	bw_bdy.newLine();
        	bw_bdy.newLine();//ESPACIO VACIO
        	bw_bdy.write("  V_MODULO VARCHAR2(30) := V_CODPACK||'"+ pantalla.getNombreCorto() +"'; \r\n"
        				+"  \r\n"
        				+"  e_error EXCEPTION; \r\n"
        				+"  \r\n"
        				+"BEGIN \r\n"
        				+"  PACK_TRF.Mensaje_Log(PACKDEBUG.LOG_SIM,V_MODULO,'***Inicio***',P_ERROR); \r\n");
        	
        	int contTeclas = 0;
        	for(int t=0; t<pantalla.getTeclas().length;t++)
        	{
        		if(pantalla.getActTecla(t))
        		{
	        		String apartado;
	        		if(contTeclas==0){
	        			apartado = "  IF ";
	        		} else {
	        			apartado = "  ELSIF ";
	        		}
	        		apartado = apartado + "P_PANORI.TECLA = PACK_TRF.TECLA_" + pantalla.getTecla(t) + " THEN \r\n"
	        				   + "    --Han pulsado " + pantalla.getTecla(t) + "\r\n"
	        				   + "    PACK_TRF.Mensaje_Log(PACKDEBUG.LOG_SIM,V_MODULO,'TECLA_"+ pantalla.getTecla(t) +"',P_ERROR); \r\n"; 
	        		
	        		if(t==0) {//Tecla de continuar en pantalla
	        			apartado = apartado + "    \r\n"
	        					   + "    P_ERROR.CODERR :=0;\r\n";
	        		} else if (t == 3) {//Unica tecla para volver a la pantalla anterior
	        			apartado = apartado + "    P_ERROR.CODERR :=-"+ t +";\r\n"
		     					   + "    P_PANDES.CODMOD := P_PANORI.MODANT;\r\n"
		        				   + "    P_PANDES.CODPAN := P_PANORI.PANANT;\r\n"
		     					   + "    raise e_error;\r\n";
	        		} else {
	        			apartado = apartado + "    P_ERROR.CODERR :="+ t +";\r\n"
			     				   + "    P_ERROR.MSGERR := ;\r\n"//FALTA
			        			   + "    P_ERROR.CAMERR := 8;\r\n"
			     				   + "    raise e_error;\r\n";
	        		}
	        		bw_bdy.write(apartado);
	        		contTeclas++;
        		}
        	}
        	bw_bdy.write("  ELSE \r\n"
        			    +"    P_ERROR.CODERR := -1;\r\n"
        				+"    P_ERROR.MSGERR := ;\r\n"
        				+"    P_ERROR.LITERR := 'Tecla no válida.';\r\n"
        				+"	  P_ERROR.CAMERR := 8;\r\n"
        				+"    raise e_error;\r\n"
        				+"  END IF;\r\n");
        	
        	bw_bdy.write("  -- Comprobar si hay mensajes que mostrar al operario \r\n"
        				+"  PACK_TRF.PRE_COM4_MENSAJES(P_OPERAR,P_CAMCOM4,P_PANDES,P_ERROR); \r\n  \r\n"
        				+"  PACK_TRF.Mensaje_Log(PACKDEBUG.LOG_SIM,V_MODULO,'***Fin***',P_ERROR); \r\n  \r\n"
        				+"EXCEPTION \r\n"
        				+"  WHEN e_error THEN \r\n"
        				+"    PACK_TRF.Mensaje_Log(PACKDEBUG.LOG_SIM,V_MODULO,'***Fin '||P_ERROR.LITERR||'***',P_ERROR); \r\n"
        				+"    P_PANDES.CODMOD := P_PANORI.CODMOD; \r\n"
        				+"    P_PANDES.CODPAN := P_PANORI.CODPAN; \r\n"
        				+"    -- Comprobar si hay mensajes que mostrar al operario \r\n"
        				+"    PACK_TRF.PRE_COM4_MENSAJES(P_OPERAR,P_CAMCOM4,P_PANDES,P_ERROR); \r\n    \r\n"
        				+"  WHEN OTHERS THEN \r\n"
        				+"    PACK_TRF.Mensaje_Log(PACKDEBUG.LOG_SIM,V_MODULO,'***Fin Others***',P_ERROR); \r\n"
        				+"    P_ERROR.CODERR := -999; \r\n"
        				+"    P_ERROR.ORAERR := SQLCODE; \r\n"
        				+"    P_ERROR.MSGERR := 'ERROR "+pantalla.getNombreCorto()+"'; \r\n"
        				+"    P_ERROR.LITERR := SUBSTR(SQLERRM,1,200); \r\n"
        				+"    P_ERROR.CAMERR := 8; \r\n"
        				+"    P_PANDES.CODMOD:= '"+nomenclatura+"'; \r\n"
        				+"    P_PANDES.CODPAN:= '"+modulo+"'; \r\n");
        	bw_bdy.write("END " + pantalla.getNombre());
        	bw_bdy.newLine();
        }
        bw_bdy.newLine();//ESPACIO VACIO
        bw_bdy.write("end " + nombreArchivo + ";");
        bw_bdy.newLine();
        bw_bdy.close();
        
    }
	
	/* 
	 * 
	 * SE GENERA EL PAQUETE CON
	 * 
	 */
	private void paqueteCON() throws IOException{
		String nombreArchivo = "PACK_" + nomenclatura + "_CON";
	    
		//-------SPC------------
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
		System.out.println("INICIO DE ARCHIVO SPC_CON");
        FileWriter fw_spc = new FileWriter("archivosTemp/" + nombreArchivo + ".spc");
        
        BufferedWriter bw_spc = new BufferedWriter(fw_spc);
        
        bw_spc.write("create or replace package "+ nombreArchivo + " is \r\n"
    			+ "/*============================================================================ \r\n"
    			+ "  Procedimiento: " + nombreArchivo + "\r\n"
    			+ "  Version: 1.0 \r\n"
    			+ "  Descripcion: Paquete para " + nomenclatura);
	    bw_spc.newLine();
	    bw_spc.newLine();//ESPACIO VACIO
	    bw_spc.write("  " + fecha + "    1.0    " + codigoPet + " Migración del modulo " + nomenclatura + "\r\n"
	    			+"                       Modulo " + modulo + "\r\n"
	    			+"                       Creación");
	    bw_spc.newLine();
	    bw_spc.write("============================================================================*/");
	    bw_spc.newLine();
	    bw_spc.newLine();//ESPACIO VACIO
	    
	    bw_spc.write("  -- Procedimientos de logica de negocio");
	    bw_spc.newLine();
	    for (int i=0; i<cantPantallas; i++)
        {
	    	String nombrePantalla = infoEstructura.getPantalla(i).getNombre();
	    	bw_spc.write("  PROCEDURE " + nombrePantalla + "_CON;");
	    	bw_spc.newLine();
        }
	    bw_spc.write("  -- Procedimientos de extraccion, actualizacion \r\n"
	    		   + "  -- y log de variables globales");
	    bw_spc.newLine();
	    for (int i=0; i<cantPantallas; i++)
        {
	    	String nombrePantalla = infoEstructura.getPantalla(i).getNombre();
	    	bw_spc.write("  PROCEDURE SEL_" + nombrePantalla + "(json IN OUT PLJSON); \r\n"
	    				+"  PROCEDURE UPD_" + nombrePantalla + "(json IN pljson); \r\n"
	    				+"  PROCEDURE LOG_" + nombrePantalla + "(P_TPLOG NUMBER);");
	    	bw_spc.newLine();
        }
	    bw_spc.newLine();//ESPACIO VACIO
	    bw_spc.write("end " + nombreArchivo + ";");
        bw_spc.newLine();
        bw_spc.close();
        
        
        //-------BODY------------
        System.out.println("INICIO DE ARCHIVO BDY_CON");
        FileWriter fw_bdy = new FileWriter("archivosTemp/" + nombreArchivo + ".bdy");
        
        BufferedWriter bw_bdy = new BufferedWriter(fw_bdy);
        
        bw_bdy.write("create or replace package body " + nombreArchivo + " is");
        bw_bdy.newLine();
        for (int i=0; i<cantPantallas; i++)
        {
        	pantalla pantalla = infoEstructura.getPantalla(i);
        	
        	bw_bdy.write("----------------------------------------------------------------------------------\r\n"
        				+ "PROCEDURE " + pantalla.getNombre() + "_CON IS \r\n"
        				+"  V_PIS_MODULO   PARAM_INTEGRA_SAP.PIS_MODULO%TYPE := '"+ nomenclatura +"'; \r\n"
        				+"  V_PIS_ACCESO   PARAM_INTEGRA_SAP.PIS_ACCESO%TYPE := '"+ pantalla.getNombreCorto()+"'; \r\n"
        				+"  V_PIS_CODIGO   PARAM_INTEGRA_SAP.PIS_CODIGO%TYPE := 'SAP'; \r\n"
        				+"  \r\n"
        				+"  ws boolean; -- Indicador modo WS \r\n"
        				+"  url PARAM_INTEGRA_SAP.PIS_WSURL%TYPE; \r\n"
        				+"  json pljson; \r\n"
        				+"  datos clob; \r\n"
        				+"  \r\n"
        				+"BEGIN \r\n"
        				+"  \r\n"
        				+"  -- Inicializamos variable global del tratamiento de errores \r\n"
        				+"  PACK_TRF.INI_ERROR(PACK_TRF.G_ERROR,V_PIS_ACCESO); \r\n"
        				+"  \r\n"
        				+"  -- Inicializamos variable global que contiene las variables globales de BBDD \r\n"
        				+"  PACK_TRF.INI_PERSISTENCIA(PACK_TRF.G_PERSISTENCIA); \r\n"
        				+"  -- Guardamos traza con valores de variables globales \r\n"
        				+"  -- antes de ejecutar logica de negocio \r\n"
        				+"  \r\n"
        				+"  LOG_" + pantalla.getNombre() + "(PACK_TRF.RF_CON); \r\n"
        				+"  PACK_TRF.COMPROBAR_WS(V_PIS_MODULO,V_PIS_ACCESO,V_PIS_CODIGO,ws,url); \r\n"
        				+"  \r\n"
        				+"  IF ws THEN \r\n"
        				+"    -- Obtengo el JSON con los datos \r\n"
        				+"    json := pljson('{}'); \r\n"
        				+"    SEL_"+ pantalla.getNombre() + "(json); \r\n"
        				+"    datos := json.to_char(false); \r\n"
        				+"    -- Guardamos traza antes de llamar a WS \r\n"
        				+"    PACK_TRF.LOG_DATOSBD(PACK_TRF.G_OPERAR,PACK_TRF.G_ERROR,datos,PACK_TRF.CON_WS); \r\n"
        				+"    -- Llamo al WS \r\n"
        				+"    PACK_TRF.LLAMAR_WS(url,datos); \r\n"
        				+"    -- Guardamos traza despues de llamar a WS \r\n"
        				+"    PACK_TRF.LOG_DATOSBD(PACK_TRF.G_OPERAR,PACK_TRF.G_ERROR,datos,PACK_TRF.WS_CON); \r\n"
        				+"    -- Actualizo las estructuras con los datos recibidos \r\n"
        				+"    json := pljson(datos); \r\n"
        				+"    UPD_"+ pantalla.getNombre() + "(json); \r\n"
        				+"  ELSE \r\n"
        				+"    PACK_"+ nomenclatura + "." + pantalla.getNombre() + "(");
        	int cont_pan=0;
        	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		campos campo = pantalla.getCampo(c);
        		String apartado = "";
        		switch(campo.getTipo()) {
	    			case "PACK_TRF.RF_PANINFO":
	    				if(cont_pan == 0) {
	    					apartado = "PACK_TRF.G_PANORI";
	    					cont_pan += 1;
	    				} else if (cont_pan == 1) {
	    					apartado = "PACK_TRF.G_PANDES";
	    					cont_pan += 1;
	    				}
	    				break;
	    			case "PACK_TRF.RF_LOGIN":
	    				apartado = "PACK_TRF.G_OPERAR";
	    				break;
	    			case "PACK_TRF.RF_ERROR":
	    				apartado = "PACK_TRF.G_ERROR";
	    				break;
	    			case "PACK_TRF.PAN_COM4":
	    				apartado = "PACK_TRF.G_CAMCOM4";
	    				break;
	    			default:
	    				String tipo = campo.getTipo();
	    				int punto = tipo.indexOf(".",4);
	    				String estructura = tipo.substring(punto+1,tipo.length());
	    				String paquete = tipo.substring(0,punto-1);
	    				apartado = paquete +".G_CAM"+estructura.replace("PAN_","");
	    				break;
	    		}
        		if(c<pantalla.getCantCampos()-1) {
        			apartado = apartado + ",";
        		}
        		
        		if(c%4 == 0 && c>0) {
        			apartado = apartado + "\r\n                ";
        		}
        		
	    		bw_bdy.write(apartado);
            }
        	
        	bw_bdy.write("); \r\n"
        				+"  END IF; \r\n"
        				+"  \r\n"
        				+"  -- Guardamos traza con valores de variables globales \r\n"
        				+"  -- despues de ejecutar logica de negocio \r\n"
        				+"  LOG_"+ pantalla.getNombre() + "(PACK_TRF.CON_RF); \r\n"
        				+"  \r\n"
        				+"EXCEPTION \r\n"
        				+"  WHEN OTHERS THEN \r\n"
        				+"    --Error al llamar al WS \r\n"
        				+"    PACK_TRF.G_ERROR.CODERR := -999; \r\n"
        				+"    PACK_TRF.G_ERROR.ORAERR := SQLCODE; \r\n"
        				+"    PACK_TRF.G_ERROR.MSGERR := '    ERROR EN WS     '; \r\n"
        				+"    PACK_TRF.G_ERROR.LITERR := SUBSTR(SQLERRM,1,200); \r\n"
        				+"    PACK_TRF.G_ERROR.CAMERR := 8; \r\n"
        				+"    PACK_TRF.G_PANDES.CODMOD := 'TRFCOM01'; \r\n"
        				+"    PACK_TRF.G_PANDES.CODPAN := 'COM0'; \r\n"
        				+"END " + pantalla.getNombre() + "_CON; \r\n");
        	bw_bdy.write("---------------------------------------------------------------------------------- \r\n"
        			    +"PROCEDURE SEL_" + pantalla.getNombre() + "(json IN OUT PLJSON) IS \r\n"
        			    +"  /* \r\n"
        			    +"    Convierte las estructuras a utilizar en "+ pantalla.getNombre() +" en un JSON para pasar al WS"
        			    +"  */ \r\n"
        			    +"BEGIN \r\n");
        	
        	int cont_pan_sel=0;
        	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		campos campo = pantalla.getCampo(c);
        		String apartado = "  --" + campo.getNombre() + "\r\n";
        		switch(campo.getTipo()) {
        			case "PACK_TRF.RF_PANINFO":
        				if(cont_pan_sel == 0) {
        					apartado = apartado + "  PACK_TRF.SEL_PANORI(json);\r\n";
        					cont_pan_sel += 1;
        				} else if (cont_pan_sel == 1) {
        					apartado = apartado + "  PACK_TRF.SEL_PANDES(json);\r\n";
        					cont_pan_sel += 1;
        				}
        				break;
        			case "PACK_TRF.RF_LOGIN":
        				apartado = apartado + "  PACK_TRF.SEL_OPERAR(json);\r\n";
        				break;
        			case "PACK_TRF.RF_ERROR":
        				apartado = apartado + "  PACK_TRF.SEL_ERROR(json);\r\n";
        				break;
        			case "PACK_TRF.PAN_COM4":
        				apartado = apartado + "  PACK_TRF.SEL_CAMCOM4(json);\r\n";
        				break;
        			default:
        				String tipo = campo.getTipo();
        				int punto = tipo.indexOf(".",4);
        				String estructura = tipo.substring(punto+1,tipo.length());
        				String paquete = tipo.substring(0,punto-1);
        				apartado = apartado +"  "+ paquete +".SEL_CAM"+estructura.replace("PAN_","")+"(json);\r\n";
        				break;
        		}
        		bw_bdy.write(apartado);
        		
            }
        	bw_bdy.write("  --P_PERSISTENCIA \r\n"
        				+"  PACK_TRF.SEL_PERSISTENCIA(json);\r\n");
        	bw_bdy.write("EXCEPTION \r\n"
        				+"  WHEN OTHERS THEN \r\n"
        				+"    NULL; \r\n"
        				+"END SEL_" + pantalla.getNombre() + ";\r\n");
        	
        	bw_bdy.write("\r\n"
        				+"PROCEDURE UPD_" + pantalla.getNombre() + "(json IN PLJSON) IS \r\n"
        				+"  /* \r\n"
	    			    +"    Actualizo los datos de las estructuras a partir del JSON"
	    			    +"  */ \r\n"
	    			    +"BEGIN \r\n");
        	
        	int cont_pan_upd=0;
        	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		campos campo = pantalla.getCampo(c);
        		String apartado = "  --" + campo.getNombre() + "\r\n";
        		switch(campo.getTipo()) {
        			case "PACK_TRF.RF_PANINFO":
        				if(cont_pan_upd == 0) {
        					apartado = apartado + "  PACK_TRF.UPD_PANORI(json);\r\n";
        					cont_pan_upd += 1;
        				} else if (cont_pan_upd == 1) {
        					apartado = apartado + "  PACK_TRF.UPD_PANDES(json);\r\n";
        					cont_pan_upd += 1;
        				}
        				break;
        			case "PACK_TRF.RF_LOGIN":
        				apartado = apartado + "  PACK_TRF.UPD_OPERAR(json);\r\n";
        				break;
        			case "PACK_TRF.RF_ERROR":
        				apartado = apartado + "  PACK_TRF.UPD_ERROR(json);\r\n";
        				break;
        			case "PACK_TRF.PAN_COM4":
        				apartado = apartado + "  PACK_TRF.UPD_CAMCOM4(json);\r\n";
        				break;
        			default:
        				String tipo = campo.getTipo();
        				int punto = tipo.indexOf(".",4);
        				String estructura = tipo.substring(punto+1,tipo.length());
        				String paquete = tipo.substring(0,punto-1);
        				apartado = apartado +"  "+ paquete +".UPD_CAM"+estructura.replace("PAN_","")+"(json);\r\n";
        				break;
        		}
        		bw_bdy.write(apartado);
        		
            }
        	bw_bdy.write("  --P_PERSISTENCIA \r\n"
        				+"  PACK_TRF.UPD_PERSISTENCIA(json);\r\n");
	    	bw_bdy.write("EXCEPTION \r\n"
	    				+"  WHEN OTHERS THEN \r\n"
	    				+"    NULL; \r\n"
	    				+"END UPD_" + pantalla.getNombre() + ";\r\n");
	    	
	    	bw_bdy.write("\r\n"
	    				+"PROCEDURE LOG_" + pantalla.getNombre() + "(P_TIPLOG NUMBER) IS \r\n"
	    				+"  "
	    			    +"BEGIN \r\n");

	    	int cont_pan_log=0;
        	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		campos campo = pantalla.getCampo(c);
        		String apartado = "  --" + campo.getNombre() + "\r\n";
        		switch(campo.getTipo()) {
        			case "PACK_TRF.RF_PANINFO":
        				if(cont_pan_log == 0) {
        					apartado = apartado + "  PACK_TRF.LOG_PANORI(json);\r\n";
        					cont_pan_log += 1;
        				} else if (cont_pan_log == 1) {
        					apartado = apartado + "  PACK_TRF.LOG_PANDES(json);\r\n";
        					cont_pan_log += 1;
        				}
        				break;
        			case "PACK_TRF.RF_LOGIN":
        				apartado = apartado + "  PACK_TRF.LOG_OPERAR(json);\r\n";
        				break;
        			case "PACK_TRF.RF_ERROR":
        				apartado = apartado + "  PACK_TRF.LOG_ERROR(json);\r\n";
        				break;
        			case "PACK_TRF.PAN_COM4":
        				apartado = apartado + "  PACK_TRF.LOG_CAMCOM4(json);\r\n";
        				break;
        			default:
        				String tipo = campo.getTipo();
        				int punto = tipo.indexOf(".",4);
        				String estructura = tipo.substring(punto+1,tipo.length());
        				String paquete = tipo.substring(0,punto-1);
        				apartado = apartado +"  "+ paquete +".LOG_CAM"+estructura.replace("PAN_","")+"(json);\r\n";
        				break;
        		}
        		bw_bdy.write(apartado);
        		
            }
        	bw_bdy.write("  --P_PERSISTENCIA \r\n"
        				+"  PACK_TRF.LOG_PERSISTENCIA(json);\r\n");
	    	bw_bdy.write("EXCEPTION \r\n"
	    				+"  WHEN OTHERS THEN \r\n"
	    				+"    NULL; \r\n"
	    				+"END LOG_" + pantalla.getNombre() + ";\r\n");
        }
        
        bw_bdy.newLine();//ESPACIO VACIO
        bw_bdy.write("end " + nombreArchivo + ";");
        bw_bdy.newLine();
        bw_bdy.close();
	}
	
	/* 
	 * 
	 * SE GENERA EL PAQUETE WS
	 * 
	 */
	private void paqueteWS() throws IOException{
		String nombreArchivo = "PACK_" + nomenclatura + "_WS";
	    
		//-------SPC------------
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
		System.out.println("INICIO DE ARCHIVO SPC_WS");
        FileWriter fw_spc = new FileWriter("archivosTemp/" + nombreArchivo + ".spc");
        
        BufferedWriter bw_spc = new BufferedWriter(fw_spc);
        
        bw_spc.write("create or replace package "+ nombreArchivo + " is \r\n"
	    			+ "/*============================================================================ \r\n"
	    			+ "  Procedimiento: " + nombreArchivo + "\r\n"
	    			+ "  Version: 1.0 \r\n"
	    			+ "  Descripcion: Paquete para " + nomenclatura);
	    bw_spc.newLine();
	    bw_spc.newLine();//ESPACIO VACIO
	    bw_spc.write("  " + fecha + "    1.0    " + codigoPet + " Migración del modulo " + nomenclatura + "\r\n"
	    			+"                       Modulo " + modulo + "\r\n"
	    			+"                       Creación");
	    bw_spc.newLine();
	    bw_spc.write("============================================================================*/");
	    bw_spc.newLine();
	    bw_spc.newLine();//ESPACIO VACIO
	    
	    for (int i=0; i<cantPantallas; i++)
        {
	    	String nombrePantalla = infoEstructura.getPantalla(i).getNombre();
	    	bw_spc.write("  PROCEDURE " + nombrePantalla + "_WS(datos IN OUT CLOB);");
	    	bw_spc.newLine();
        }
        
	    bw_spc.newLine();//ESPACIO VACIO
	    bw_spc.write("end " + nombreArchivo + ";");
	    bw_spc.newLine();
	    bw_spc.close();
        
        
        //-------BODY------------
        System.out.println("INICIO DE ARCHIVO BDY_WS");
        FileWriter fw_bdy = new FileWriter("archivosTemp/" + nombreArchivo + ".bdy");
        
        BufferedWriter bw_bdy = new BufferedWriter(fw_bdy);
        
        bw_bdy.write("create or replace package body"+ nombreArchivo + " is \r\n");
        for (int i=0; i<cantPantallas; i++)
        {
	    	String nombrePantalla = infoEstructura.getPantalla(i).getNombre();
	    	bw_bdy.write("---------------------------------------------------------------------------------- \r\n"
	    				+"PROCEDURE " + nombrePantalla + "_WS(datos IN OUT CLOB) IS \r\n"
	    				+"  json pljson; \r\n"
	    				+"BEGIN \r\n"
	    				+"  --Actualizo los datos de las estructuras a partir del JSON \r\n"
	    				+"  json := pljson(datos); \r\n"
	    				+"  PACK_"+ nomenclatura + "_CON.UPD_" + nombrePantalla + "(json); \r\n"
	    				+"  \r\n"
	    				+"  -- Guardamos traza con valores de variables globales \r\n"
	    				+"  -- antes de ejecutar logica de negocio \r\n"
	    				+"  PACK_"+ nomenclatura + "_CON.LOG_" + nombrePantalla + "(PACK_TRF.WS_NEG); \r\n"
	    				+"  \r\n"
	    				+"  -- Llamo al procedimiento \r\n"
	    				+"  PACK_"+ nomenclatura + "." + nombrePantalla + "(");
	    	int cont_pan=0;
	    	pantalla pantalla = infoEstructura.getPantalla(i);
	    	for (int c=0; c<pantalla.getCantCampos(); c++)
            {
        		campos campo = pantalla.getCampo(c);
        		String apartado = "";
        		switch(campo.getTipo()) {
	    			case "PACK_TRF.RF_PANINFO":
	    				if(cont_pan == 0) {
	    					apartado = "PACK_TRF.G_PANORI";
	    					cont_pan += 1;
	    				} else if (cont_pan == 1) {
	    					apartado = "PACK_TRF.G_PANDES";
	    					cont_pan += 1;
	    				}
	    				break;
	    			case "PACK_TRF.RF_LOGIN":
	    				apartado = "PACK_TRF.G_OPERAR";
	    				break;
	    			case "PACK_TRF.RF_ERROR":
	    				apartado = "PACK_TRF.G_ERROR";
	    				break;
	    			case "PACK_TRF.PAN_COM4":
	    				apartado = "PACK_TRF.G_CAMCOM4";
	    				break;
	    			default:
	    				String tipo = campo.getTipo();
	    				int punto = tipo.indexOf(".",4);
	    				String estructura = tipo.substring(punto+1,tipo.length());
	    				String paquete = tipo.substring(0,punto-1);
	    				apartado = paquete +".G_CAM"+estructura.replace("PAN_","");
	    				break;
	    		}
        		if(c<pantalla.getCantCampos()-1) {
        			apartado = apartado + ",";
        		}
        		
        		if(c%4 == 0 && c>0) {
        			apartado = apartado + "\r\n                ";
        		}
        		
	    		bw_bdy.write(apartado);
            }
	    	bw_bdy.write("); \r\n"
	    				+"  -- Convierto a JSON para devolver al WS \r\n"
	    				+"  PACK_"+ nomenclatura + "_CON.SEL_" + nombrePantalla +"(json); \r\n"
	    				+"  datos := json.to_char(false); \r\n"
	    				+"  \r\n"
	    				+"  -- Guardamos traza con valores de variables globales \r\n"
	    				+"  -- despues de ejecutar logica de negocio \r\n"
	    				+"  PACK_"+ nomenclatura + "_CON.LOG_" + nombrePantalla + "(PACK_TRF.NEG_WS); \r\n"
	    				+"EXCEPTION \r\n"
	    				+"  WHEN OTHERS THEN \r\n"
	    				+"    PACK_TRF_WS.TRATAR_ERROR(datos,SLQCODE,SQLERRM); \r\n"
	    				+"END "+ nombrePantalla + "_WS;");
	    	bw_bdy.newLine();
        }
        
        bw_bdy.newLine();//ESPACIO VACIO
        bw_bdy.write("end " + nombreArchivo + ";");
        bw_bdy.newLine();
        bw_bdy.close();
	}
	
	/* 
	 * 
	 * SE GENERA EL Controller java
	 * 
	 */
	private void controller() throws IOException{
		String nombreArchivo = nomenclatura.toLowerCase() + "Controller";
	    
		//-------Controller------------
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
		System.out.println("INICIO DE CONTROLLER");
        FileWriter fw_ctr = new FileWriter("archivosTemp/" + nombreArchivo + ".java");
        
        BufferedWriter bw_ctr = new BufferedWriter(fw_ctr);
        
        bw_ctr.write("/*============================================================================ \r\n"
        			+"  "+nomenclatura.toLowerCase()+"Controller \r\n"
        			+"  Version: 1.0 \r\n"
        			+"  Descripcion: Webservice para modulo " + nomenclatura + "\r\n"
        			+"  "+fecha+"    1.0    Creacion\r\n"
        			+"============================================================================*/ \r\n"
        			+"package es.inlogconsulting.ws.Controller;\r\n"
        			+"import org.springframework.beans.factory.annotation.Autowired;\r\n"
        			+"import org.springframework.web.bind.annotation.RequestBody;\r\n"
        			+"import org.springframework.web.bind.annotation.RequestMapping;\r\n"
        			+"import org.springframework.web.bind.annotation.RequestMethod;\r\n"
        			+"import org.springframework.web.bind.annotation.RestController;\r\n"
        			+"\r\n"
        			+"import es.inlogconsulting.ws.Dao.DatabaseDao;\r\n"
        			+"\r\n"
        			+"@RestController\r\n"
        			+"public class " +nombreArchivo+" {\r\n"
        			+"    @Autowired\r\n"
        			+"	  private DatabaseDao dbDao; \r\n"
        			+"	  \r\n"
        			+"	  static final String PACKAGE = PACK_"+ nomenclatura.toUpperCase() +"_WS; //Nombre paquete a ejecutar\r\n");
        
        for (int i=0; i<cantPantallas; i++)
        {
	    	String nombrePantalla = infoEstructura.getPantalla(i).getNombre();
	    	
	    	bw_ctr.write("    \r\n"
	    				+"    @RequestMapping(value = \"/"+nomenclatura.toLowerCase()+"/"+nombrePantalla.toLowerCase()+"\", method = RequestMethod.PUT, produces = \"application/json\")\r\n"
	    				+"    public String "+nombrePantalla+"(@RequestBody(required = false) String json) {\r\n"	
	    				+"        String nomprc = \""+nombrePantalla +"_WS\";\r\n"
	    				+"		  json =(String) dbDao.executeSP(PACKAGE, nomprc, json);\r\n"
	    				+"        return json;\r\n"
	    				+"    }\r\n");
        }
        
        bw_ctr.write("}");
        bw_ctr.newLine();
        bw_ctr.close();
	}
	
	/* 
	 * 
	 * SE GENERA EL archivo instrucciones de BBDD
	 * 
	 */
	private void instr() throws IOException{
		//-------SGA------------
		String nombreArchivo = "instr_SGA_"+codigoPet +"_Migracion_"+nomenclatura.toUpperCase();
	    
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
		System.out.println("INICIO DE instr-SGA");
        FileWriter fw_sga = new FileWriter("archivosTemp/" + nombreArchivo + ".sql");
        
        BufferedWriter bw_sga = new BufferedWriter(fw_sga);
        
        bw_sga.write("---\r\n"
        			+"-- Id: "+nombreArchivo+".sql\r\n"
        			+"-- Script de instalacion principal\r\n"
        			+"--\r\n"
        			+"\r\n"
        			+"---\r\n"
        			+"-- Directorios instalacion\r\n"
        			+"define dircmb='./'\r\n"
        			+"define dirjob='../sql/Bd9/job/'\r\n"
        			+"define dirfnc='../sql/Bd9/pl/fnc/'\r\n"
        			+"define dirprc='../sql/Bd9/pl/prc/'\r\n"
        			+"define dirspc='../sql/Bd9/pl/spc/'\r\n"
        			+"define dirbdy='../sql/Bd9/pl/bdy/'\r\n"
        			+"define dirtrg='../sql/Bd9/trg/'\r\n"
        			+"-- Tablas, vistas, datos, secuencias, grants y sinonimos se instalaran desde este instalador\r\n"
        			+"\r\n"
        			+"-- Tablespaces\r\n"
        			+"define ts_data='USERS'\r\n"
        			+"define ts_indx='INDX'\r\n"
        			+"define ts_temp='TEMP01'\r\n"
        			+"\r\n"
        			+"-- Tablas\r\n"
        			+"-- Foreign Keys\r\n"
        			+"\r\n"
        			+"-- Create table\r\n"
        			+"\r\n"
        			+"-- Comentarios\r\n"
        			+"\r\n"
        			+"-- Primary Keys\r\n"
        			+"\r\n"
        			+"-- Indices\r\n"
        			+"\r\n"
        			+"-- Checks\r\n"
        			+"\r\n"
        			+"-- Vistas\r\n"
        			+"\r\n"
        			+"-- Datos\r\n");
        
        for (int i=0; i<cantCodPantallas; i++)
        {
	    	codigoPantalla codPantalla = infoEstructura.getCodigosPantalla(i);
	    	
	    	bw_sga.write("INSERT INTO RF_PANTALLAS(RFP_MODULO,RFP_CODIGO,RFP_DENOMI) VALUES('"+nomenclatura.toUpperCase()+"','"+ modulo + codPantalla.getNumPant()+"','"+codPantalla.getDescPant()+"');\r\n");
        }
        bw_sga.write("\r\n");
        
        for (int i=0; i<cantPantallas; i++)
        {
        	pantalla Pantalla = infoEstructura.getPantalla(i);
        	
        	bw_sga.write("INSERT INTO PARAM_INTEGRA_SAP(PIS_MODULO,PIS_ACCESO,PIS_CODIGO,PIS_DENOMI,PIS_VALALF,PIS_WSURL)\r\n"
        			   + " VALUES('"+nomenclatura.toUpperCase()+"','"+Pantalla.getNombreCorto()+"','SAP','Comunicación con SAP? (S/N)','S','http://172.20.0.125:8091/"+nomenclatura.toLowerCase()+"/"+Pantalla.getNombre().toLowerCase()+"');\r\n");
        }
        bw_sga.write("\r\n"
        			+"-- Secuencias\r\n"
        			+ "\r\n"
        			+ "-- Grants\r\n"
        			+ "\r\n"
        			+ "-- Sinonimos\r\n"
        			+ "\r\n"
        			+ "--\r\n"
        			+ "-- procedimientos, funciones, paquetes, triggers\r\n"
        			+ "@&dirspc.pack_" + nomenclatura.toLowerCase()+".spc\r\n"
        			+ "@&dirspc.pack_" + nomenclatura.toLowerCase()+"_con.spc\r\n"
        			+ "@&dirspc.pack_" + nomenclatura.toLowerCase()+"_ws.spc\r\n"
        			+ "@&dirbdy.pack_" + nomenclatura.toLowerCase()+".bdy\r\n"
        			+ "@&dirbdy.pack_" + nomenclatura.toLowerCase()+"_con.bdy\r\n"
        			+ "@&dirbdy.pack_" + nomenclatura.toLowerCase()+"_ws.bdy\r\n"
        			+ "\r\n"
        			+ "--fin\r\n");
        
        bw_sga.close();
        
        //-------SYSTEM------------
      		nombreArchivo = "instr_SYSTEM_"+codigoPet +"_Migracion_"+nomenclatura.toUpperCase();
      	    
      		System.out.println("Se va a generar el archivo: " + nombreArchivo);
      		System.out.println("INICIO DE instr-SYSTEM");
            FileWriter fw_system = new FileWriter("archivosTemp/" + nombreArchivo + ".sql");
              
            BufferedWriter bw_system = new BufferedWriter(fw_system);
              
            bw_system.write("---\r\n"
		            	   +"-- Id: "+nombreArchivo+".sql\r\n"
		            	   +"-- Script de instalacion principal\r\n"
		            	   +"--\r\n"
		            	   +"---\r\n"
		            	   +"  \r\n"
		            	   +"-- Directorios instalacion\r\n"
		            	   +"define dircmb='./'\r\n"
		            	   +"define dircreauser='../sql/Bd9/CreaUser/'\r\n"
		            	   +"-- Tablas, vistas, datos, secuencias, grants y sinonimos se instalaran desde este instalador\r\n"
		                   +"  \r\n"
		            	   +"  \r\n"
		            	   +"-- Tablespaces\r\n"
		            	   +"define ts_data='USERS'\r\n"
		            	   +"define ts_indx='INDX'\r\n"
		            	   +"define ts_temp='TEMP01'\r\n"
		            	   +"  \r\n"
		            	   +"define us_sgl='VIRSGLXXX'\r\n"
		            	   +"define us_int='VIRINTXXX'\r\n"
		            	   +"define us_sga='ADM_VIRTOXXX'\r\n"
		            	   +"define us_ax='USAXXXX'\r\n"
		            	   +"define us_sap='USSAPXXX'\r\n"
		            	   +"define us_utl='USUTLXXX'\r\n"
		            	   +"define us_mar='MARKEMXXX'\r\n"
		            	   +"define us_e80='E80XXX'\r\n"
		            	   +"define us_meca='MECALUXXXX'\r\n"
		            	   +"define us_dem='DEMATICXXX'\r\n"
		            	   +"define us_syseur='SYSEUROPEXXX'\r\n"
		            	   +"define us_sinter='SINTERPACKXXX'\r\n"
		            	   +"define us_dis='DISCOVERXXX'\r\n"
		            	   +"define us_disofn='DISC_OFNXXX'\r\n"
		            	   +"define us_uswaf='WAFXXX'\r\n"
		            	   +"  \r\n"
		            	   +"define us_gelsga='GELSGAXXX'\r\n"
		            	   +"define us_etiq='ETIQUETASXXX'\r\n"
		            	   +"define us_nlink='NLINKXXX'\r\n"
		            	   +"define us_persga='SISLOGPERXXX'\r\n"
		            	   +"define us_prv='VIRPRVXXX'\r\n"
		            	   +"define us_cam='VIRCAMXXX'\r\n"
		            	   +"define us_mtr='VIRMTRXXX'\r\n"
		            	   +"define us_ingerus='INGERUSXXX'\r\n"
		            	   +"define us_urtasun='URTASUNXXX'\r\n"
		            	   +"define us_goitek='GOITEKXXX'\r\n"
		            	   +"define us_federico='FEDERICOXXX'\r\n"
		            	   +"define us_uvirto='UVIRTOXXX'\r\n"
		            	   +"  \r\n"
		            	   +"-- Usuarios\r\n"
		            	   +"  \r\n"
		            	   +"-- Tablas\r\n"
		            	   +"  \r\n"
		            	   +"-- Create table\r\n"
		            	   +"  \r\n"
		            	   +"-- Comentarios\r\n"
		            	   +"  \r\n"
		            	   +"-- Primary Keys\r\n"
		            		+ "\r\n"
		            		+ "-- Foreign Keys\r\n"
		            		+ "\r\n"
		            		+ "-- Indices\r\n"
		            		+ "\r\n"
		            		+ "-- Checks\r\n"
		            		+ "\r\n"
		            		+ "-- Vistas\r\n"
		            		+ "\r\n"
		            		+ "-- Datos\r\n"
		            		+ "\r\n"
		            		+ "-- Secuencias\r\n"
		            		+ "\r\n"
		            		+ "-- Grants\r\n"
		            		+ "GRANT EXECUTE ON ADM_VIRTOPAX.PACK_"+nomenclatura.toUpperCase()+"_WS TO USSAPPAX;\r\n"
		            		+ "\r\n"
		            		+ "-- Sinonimos\r\n"
		            		+ "CREATE OR REPLACE SYNONYM USSAPPAX.PACK_"+nomenclatura.toUpperCase()+"_WS FOR ADM_VIRTOPAX.PACK_"+nomenclatura.toUpperCase()+"_WS;\r\n"
		            		+ "\r\n"
		            		+ "--\r\n"
		            		+ "-- procedimientos, funciones, paquetes, triggers\r\n"
		            		+ "--@&dirjob.\r\n"
		            		+ "--@&dirfnc.\r\n"
		            		+ "--@&dirprc.\r\n"
		            		+ "--@&dirspc.\r\n"
		            		+ "--@&dirbdy\r\n"
		            		+ "--@&dirtrg.\r\n"
		            		+ "\r\n"
		            		+ "\r\n"
		            		+ "\r\n"
		            		+ "--fin\r\n");  
            
            bw_system.close();
	}
	
	/* 
	 * 
	 * SE GENERA EL archivo guia
	 * 
	 */
	private void guia() throws IOException{
		String nombreArchivo = "gi_"+codigoPet +"_Migracion_"+nomenclatura.toUpperCase();
	    
		//-------Guia------------
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
		System.out.println("INICIO DE GUIA");
        FileWriter fw_gi = new FileWriter("archivosTemp/" + nombreArchivo + ".txt");
        
        BufferedWriter bw_gi = new BufferedWriter(fw_gi);
        
        bw_gi.write("--- \r\n"
        		+"-- Id: "+ nombreArchivo +".txt \r\n"
        		+"-- Guia de instalacion \r\n"
        		+"--\r\n"
        		+"-- "+codigoPet+". Migración del módulo " + modulo+" ("+nomenclatura.toUpperCase()+")\r\n"
        		+ "---\r\n"
        		+ "  \r\n"
        		+ "1- Base de Datos:\r\n"
        		+ "-cmb:                 -\r\n"
        		+ "						SGA/instr_SGA_"+codigoPet+"_Migracion_"+nomenclatura.toUpperCase()+".sql\r\n"
        		+ "						SYSTEM/instr_SYSTEM_"+codigoPet+"_Migracion_"+nomenclatura.toUpperCase()+".SQL\r\n"
        		+ "-tab:				 -\r\n"
        		+ "-vis:                 -\r\n"
        		+ "-seq:				 -\r\n"
        		+ "-job:                 -\r\n"
        		+ "-fnc:                 -\r\n"
        		+ "-prc:                 -\r\n"
        		+ "-spc:                 SGA\r\n"
        		+ "							pack_"+nomenclatura.toLowerCase()+".spc\r\n"
        		+ "							pack_"+nomenclatura.toLowerCase()+"_con.spc\r\n"
        		+ "							pack_"+nomenclatura.toLowerCase()+"_ws.spc\r\n"
        		+ "-bdy:                 SGA\r\n"
        		+ "							pack_"+nomenclatura.toLowerCase()+".bdy\r\n"
        		+ "							pack_"+nomenclatura.toLowerCase()+"_con.bdy\r\n"
        		+ "							pack_"+nomenclatura.toLowerCase()+"_ws.bdy\r\n"
        		+ "-sql:                 -\r\n"
        		+ "\r\n"
        		+ "-trg:                 -\r\n"
        		+ "\r\n"
        		+ "-cron:                -\r\n"
        		+ "\r\n"
        		+ "-sql:                 -\r\n"
        		+ "\r\n"
        		+ "2-Procesos (proc):\r\n"
        		+ "-obj:                 SGA\r\n"
        		+ "\r\n"
        		+ "-mak:                 -\r\n"
        		+ "\r\n"
        		+ "-compilacion:         -\r\n"
        		+ "\r\n"
        		+ "3- Servidor (scripts):\r\n"
        		+ "-cmd:                 -\r\n"
        		+ "\r\n"
        		+ "-sql:                 -\r\n"
        		+ "\r\n"
        		+ "5- Pantallas/Reports (Ruta de Menu):\r\n"
        		+ "\r\n"
        		+ "--fin");
        
        bw_gi.close();
	}
	
	/**Basada en una pagina 
	 * Create zip file from multiple files with ZipOutputStream: https://examples.javacodegeeks.com/java-development/core-java/util/zip/create-zip-file-from-multiple-files-with-zipoutputstream/
	 * Modificado para los archivos generados
	**/
	private void generarZip() {
		try {
			byte[] buffer = new byte[1024];
 
			FileOutputStream fos;
			
			fos = new FileOutputStream( ubicacionZip + "\\" + nomenclatura + ".zip");
			
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			File[] files = new File("archivosTemp").listFiles();
			//Recorrer archivos de "archivosTemp"
			for (int i=0; i < files.length; i++) {
			    FileInputStream fis = new FileInputStream(files[i]);
	 
			    //Crear nueva entrada del zip con cada archivo
			    zos.putNextEntry(new ZipEntry(files[i].getName()));
			     
			    int length;
	 
			    while ((length = fis.read(buffer)) > 0) {
			        zos.write(buffer, 0, length);
			    }
	 
			    zos.closeEntry();
	 
			    // cerrar archivo
			    fis.close();
		    } 
			
			// close the ZipOutputStream
            zos.close();
            
            // borrar todos los archivos generados
            for(File file : files) {
            	file.delete();
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
