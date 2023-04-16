package Vista;

import javax.swing.JPanel;

import Modelo.botonAccion;
import Modelo.eventosAccion;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class botonesOpciones extends JPanel {
	
	private botonAccion btnEditar;
	private botonAccion btnBorrar;
	
	/**Basada en video 
	 * JTable Custom Cell Button Action using Java Swing: https://www.youtube.com/watch?v=RMwufjRRHBU&ab_channel=RaVen
	 * Modificado para recogida de datos
	**/
	public botonesOpciones() {
		
		btnEditar = new botonAccion();
		/**Imagen para editar obtenida de flaticon.es de forma gratuita:
		 * https://www.flaticon.es/icono-gratis/editar_1159876?term=editar&page=1&position=28&origin=tag&related_id=1159876
		**/
		btnEditar.setIcon(new ImageIcon(botonesOpciones.class.getResource("/imagenes/editar.png")));
		add(btnEditar);
		
		btnBorrar = new botonAccion();
		/**Imagen para editar obtenida de flaticon.es de forma gratuita:
		 * https://www.flaticon.es/icono-gratis/borrar_7666109?term=borrar&page=1&position=4&origin=search&related_id=7666109
		**/
		btnBorrar.setIcon(new ImageIcon(botonesOpciones.class.getResource("/imagenes/borrar.png")));
		add(btnBorrar);

	}
	
	public void eventos(eventosAccion event, int fila) {
		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				event.editar(fila);
			}
		});
		
		btnBorrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				event.eliminar(fila);
			}
		});
		
	}

}
