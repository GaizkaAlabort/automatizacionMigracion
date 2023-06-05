package Modelo.Info;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import Vista.botonesOpciones;

public class editorInfo extends DefaultCellEditor
{
	protected JButton btn;
	private eventoInfo event;
	
	public editorInfo(eventoInfo event) {
		super(new JCheckBox());
		
		this.event = event;
	}
	 
	//OVERRIDE A COUPLE OF METHODS
	@Override
	public Component getTableCellEditorComponent(JTable table, Object obj,
			boolean selected, int row, int col) {
		btn=new JButton();
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				event.info(row);
			}
		});
		btn.setIcon(new ImageIcon(botonesOpciones.class.getResource("/imagenes/interrogante.png")));
		return btn;
	}
}

