package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vista.nomenclatura;
import Vista.pantallaNuevaVariable;
import Vista.pantallaEstructura;
import Vista.listaEstructuras;
import Vista.listaPantallas;
import Modelo.ficheros;
import Modelo.generador;


public class principal implements ActionListener{
	
	private nomenclatura nomenclatura;
	private listaEstructuras listaEstructura;
	private listaPantallas listaPantalla;
	
	public static void main(String[] args) {
		new principal().iniciar();
	}

	private void iniciar() {
		/**Se instancian las clases que van a ser unicas en el sistema*/
		nomenclatura = new nomenclatura();
	    nomenclatura.btnNewButton.addActionListener((ActionListener) this);
		Coordinador miCoordinador= new Coordinador();
		nomenclatura.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==nomenclatura.btnNewButton && nomenclatura.comprobarCampos()) {
			System.out.println("//PRINCIPAL// Nombre: " + nomenclatura.getNomenclatura() + ", Cod Peticion:" + nomenclatura.getCodPeticion());
			listaEstructura= new listaEstructuras();
			listaEstructura.btnGenerar.addActionListener((ActionListener) this);
			
			//Hacemos visible la nueva pantalla
			listaEstructura.setVisible(true);
		}
		else if(e.getSource()==listaEstructura.btnGenerar && listaEstructura.comprobarGenerar()) {
			listaEstructura.setVisible(false);
			System.out.println("//PRINCIPAL// Numero de estructuras: " + listaEstructura.getListaTam());
			listaPantalla = new listaPantallas(listaEstructura.getListaNomEst(nomenclatura.getNomenclatura()));
			listaPantalla.btnGenerar.addActionListener((ActionListener) this);
			
			//Hacemos visible la nueva pantalla
			listaPantalla.setVisible(true);
		}
		else if(e.getSource()==listaPantalla.btnGenerar)
		{
			System.out.println("//PRINCIPAL// Numero de pantallas: " + listaPantalla.getListaTam());
			
			//GENERAR FICHEROS
			generador gen =new generador(new ficheros(nomenclatura.getNomenclatura(), nomenclatura.getCodPeticion(), listaEstructura.getListaEst(), listaPantalla.getListaPant(),listaPantalla.getListaCodigosPantalla()));
		}
	}
}
