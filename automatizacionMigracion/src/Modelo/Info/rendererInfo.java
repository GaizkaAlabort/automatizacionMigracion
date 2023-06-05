package Modelo.Info;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import Vista.botonesOpciones;

//BUTTON RENDERER CLASS
public class rendererInfo extends DefaultTableCellRenderer
{	
	@Override
	public Component getTableCellRendererComponent(JTable tabla,Object o, boolean seleccionado,boolean bln1,int fila,int columna) {
		Component com= super.getTableCellRendererComponent(tabla,o,seleccionado,bln1,fila,columna);
		
		JButton accion = new JButton();		
		accion.setIcon(new ImageIcon(botonesOpciones.class.getResource("/imagenes/interrogante.png")));
		
		return accion;
	}
}