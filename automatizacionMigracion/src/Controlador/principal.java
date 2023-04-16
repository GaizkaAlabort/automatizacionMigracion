package Controlador;

import Vista.nomenclatura;
import Vista.pantallaNuevaVariable;
import Vista.pantallaEstructura;

public class principal {
	
	public static void main(String[] args) {
		new principal().iniciar();
	}

	private void iniciar() {
		
		/**Se instancian las clases que van a ser unicas en el sistema*/
		nomenclatura nomenclatura = new nomenclatura();
		Coordinador miCoordinador= new Coordinador();
		
		
		nomenclatura.setVisible(true);
	}
}
