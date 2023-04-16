package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class generador {
	
	private ficheros infoEstructura;
	
	public generador(ficheros pficheros) {
		infoEstructura = pficheros;
		
		try {
			paquete1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR AL GENERAR EL PRIMER PACK");
			e.printStackTrace();
		}
	}
	
	
	private void paquete1() throws IOException{
        String nomenclatura = infoEstructura.getNombre();
		String nombreArchivo = "PACK_" + nomenclatura;
		
		
		//-------SPC------------
		System.out.println("Se va a generar el archivo: " + nombreArchivo);
        
        int cantEstructuras = infoEstructura.getCantEstructuras();
        
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
        bw_spc.newLine();
        bw_spc.write("============================================================================*/");
        bw_spc.newLine();
        bw_spc.newLine();//ESPACIO VACIO
        
        //**ESTRUCTURAS**
        for (int i=0; i<cantEstructuras; i++)
        {
        	estructura estructura = infoEstructura.getEstructura(i);
        	
        	bw_spc.write("  -- Tipo de datos para pantalla " + estructura.getNombre());
            bw_spc.newLine();
            bw_spc.write("  TYPE PAN_" + estructura.getNombre() + " IS RECORD");
            bw_spc.newLine();
            bw_spc.write("  {");
            bw_spc.newLine();
            
            for (int j=0; j<estructura.getCantVariables(); j++)
            {
            	variable variable= estructura.getVariable(j);
            	
            	String linea = variable.getNombre() + " " + variable.getTipo();
            	if (variable.getCantidad() != -1)
            	{
            		linea = linea + "(" + variable.getCantidad() + ")";
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
        bw_spc.write("  /*SIN APLICAR*/");
        bw_spc.newLine();
        
        bw_spc.newLine();//ESPACIO VACIO
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
        		
        		if(variable.getTipo() == "NUMBER") {
        			bw_bdy.write("  G_CAM" + nombreEstructura + "." + variable.getNombre() + " := -1;");
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
        		
            	bw_bdy.write("  if G_CAM"+ nombreEstructura+"." + variable.getNombre() +" is not null then \r\n"
            			   + "    json_aux.put('"+ variable.getNombre() +"', G_CAM"+ nombreEstructura+"." + variable.getNombre() + "); \r\n"
            			   + "  else \r\n");
        		if(variable.getTipo() == "NUMBER") {
        			bw_bdy.write("    json_aux.put('"+ variable.getNombre() +"', 0 );");
        		} else {
        			bw_bdy.write("    json_aux.put('"+ variable.getNombre() +"', '' );");
        		}
        		bw_bdy.newLine();
        		bw_bdy.newLine(); //ESPACIO VACIO
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
        		
            	if(variable.getTipo() == "NUMBER") {
            		bw_bdy.write("G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := pljson_ext.get_number(json_aux, '" + variable.getNombre() +"');");
            	} else if(variable.getTipo() == "VARCHAR2") {
            		bw_bdy.write("G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := pljson_ext.get_string(json_aux, '" + variable.getNombre() +"');");
            	} else if(variable.getTipo() == "DATE") {
            		bw_bdy.write("G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := to_date(pljson_ext.get_string(json_aux, '" + variable.getNombre() +"'), 'DD/MM/YY HH24:MI:SS');");
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
        		
            	if(variable.getTipo() == "NUMBER") {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := ' || NVL(TO_CHAR(G_CAM"+ nombreEstructura +"." + variable.getNombre() +"),-1) || ';' || CHR(13);");
            	} else if(variable.getTipo() == "VARCHAR2") {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := '; \r\n"
            				   + "    IF G_CAM"+ nombreEstructura +"." + variable.getNombre() +" IS NULL THEN \r\n"
            				   + "       V_MENSA := V_MENSA ||'NULL' || ';' || CHR(13); \r\n"
            				   + "    ELSE \r\n"
            				   + "       V_MENSA := V_MENSA ||'''' || G_CAM"+ nombreEstructura +"." + variable.getNombre() +" ||''';' || CHR(13); \r\n"
            				   + "    END IF;");
            	} else if(variable.getTipo() == "DATE") {
            		bw_bdy.write("    V_MENSA := 'G_CAM"+ nombreEstructura +"." + variable.getNombre() +" := '; \r\n"
            				   + "    IF G_CAM"+ nombreEstructura +"." + variable.getNombre() +" IS NULL THEN \r\n"
            				   + "       V_MENSA := V_MENSA || '0/0/0' || ';' || CHR(13); \r\n"
            				   + "    ELSE \r\n"
            				   + "       V_MENSA := V_MENSA || '''' || to_char(G_CAM"+ nombreEstructura +"." + variable.getNombre() +",'DD-MM-RRRR HH24:MI:SS') ||''';' || CHR(13);\r\n"
            				   + "    END IF;");
            	}
            	bw_bdy.newLine();
            	
            	if(j==0)
            	{
            		bw_bdy.write("    V_LOG := V_MENSA;");
            	} else {
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
        bw_bdy.write("  /*SIN APLICAR*/");
        bw_bdy.newLine();
        
        
        bw_bdy.newLine();//ESPACIO VACIO
        bw_bdy.write("end " + nombreArchivo + ";");
        bw_bdy.newLine();
        bw_bdy.close();
        
    }
	
	
	
	
}
