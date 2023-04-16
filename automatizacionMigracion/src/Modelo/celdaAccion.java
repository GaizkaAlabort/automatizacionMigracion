package Modelo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import Vista.botonesOpciones;

/**Basada en video 
 * JTable Custom Cell Button Action using Java Swing: https://www.youtube.com/watch?v=RMwufjRRHBU&ab_channel=RaVen
 * Modificado para recogida de datos
**/
public class celdaAccion extends DefaultTableCellRenderer{
	
	@Override
	public Component getTableCellRendererComponent(JTable tabla,Object o, boolean seleccionado,boolean bln1,int fila,int columna) {
		Component com= super.getTableCellRendererComponent(tabla,o,seleccionado,bln1,fila,columna);
		
		botonesOpciones accion = new botonesOpciones();
		
		if (seleccionado ==false) {
			accion.setBackground(Color.WHITE);
		} else {
			accion.setBackground(tabla.getSelectionBackground());
		}
		
		return accion;
	}

}
