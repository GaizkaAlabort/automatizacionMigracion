package Modelo.Accion;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JCheckBox;

import Vista.botonesOpciones;

/**Basada en video 
 * JTable Custom Cell Button Action using Java Swing: https://www.youtube.com/watch?v=RMwufjRRHBU&ab_channel=RaVen
 * Modificado para recogida de datos
**/
public class editorAccion extends DefaultCellEditor{
	
	private eventosAccion event;
	public editorAccion(eventosAccion event) {
		super(new JCheckBox());
		this.event = event;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable tabla,Object o, boolean bln,int fila,int columna) {
		botonesOpciones accion = new botonesOpciones();
		accion.eventos(event,fila);
		accion.setBackground(tabla.getSelectionBackground());
		return accion;
	}
}
