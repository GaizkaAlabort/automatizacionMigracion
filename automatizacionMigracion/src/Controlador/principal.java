package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Modelo.codigoPantalla;
import Modelo.estructura;
import Modelo.ficheros;
import Modelo.generador;
import Modelo.pantalla;
import Vista.listaEstructuras;
import Vista.listaPantallas;
import Vista.nomenclatura;
import Vista.ubicacionDescarga;


public class principal implements ActionListener{
	
	private nomenclatura nomenclatura;
	private listaEstructuras listaEstructura;
	private listaPantallas listaPantalla;
	private ubicacionDescarga ubiDesc;
	
	private boolean vueltaNomEst=false;
	private boolean vueltaEstPant=false;
	
	public static void main(String[] args) {
		new principal().iniciar();
	}

	private void iniciar() {
		/**Se instancian las clases que van a ser unicas en el sistema*/
		nomenclatura = new nomenclatura(null,-1);
	    nomenclatura.btnNewButton.addActionListener((ActionListener) this);
		Coordinador miCoordinador= new Coordinador();
		nomenclatura.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==nomenclatura.btnNewButton && nomenclatura.comprobarCampos()) 
		{
			if(vueltaNomEst) {
				System.out.println("//PRINCIPAL EDITADO// Nombre: " + nomenclatura.getNomenclatura() + ", Cod Peticion:" + nomenclatura.getCodPeticion());
				ArrayList<estructura> listaEst = listaEstructura.getListaEst();
				listaEstructura.dispose();
				listaEstructura= new listaEstructuras(listaEst);
				listaEstructura.btnGenerar.addActionListener((ActionListener) this);
				listaEstructura.btnNomenclatura.addActionListener((ActionListener) this);
				vueltaNomEst = false;
			}
			else{
				System.out.println("//PRINCIPAL// Nombre: " + nomenclatura.getNomenclatura() + ", Cod Peticion:" + nomenclatura.getCodPeticion());
				listaEstructura= new listaEstructuras(null);
				listaEstructura.btnGenerar.addActionListener((ActionListener) this);
				listaEstructura.btnNomenclatura.addActionListener((ActionListener) this);
			}
			//Hacemos visible la nueva pantalla
			listaEstructura.setVisible(true);
		}
		else if(listaEstructura!=null && e.getSource()==listaEstructura.btnGenerar && listaEstructura.comprobarGenerar()) 
		{
			listaEstructura.setVisible(false);
			if(vueltaEstPant) {
				ArrayList<pantalla> listaPant = listaPantalla.getListaPant();
				ArrayList<codigoPantalla> listaCodPant = listaPantalla.getListaCodigosPantalla();
				listaPantalla.dispose();
				listaPantalla = new listaPantallas(nomenclatura.getNomenclatura(),listaEstructura.getListaEst(),listaPant,listaCodPant);
				listaPantalla.btnGenerar.addActionListener((ActionListener) this);
				listaPantalla.btnEstructura.addActionListener((ActionListener) this);
				vueltaEstPant = false;
			}
			else{
				System.out.println("//PRINCIPAL// Numero de estructuras: " + listaEstructura.getListaTam());
				listaPantalla = new listaPantallas(nomenclatura.getNomenclatura(),listaEstructura.getListaEst(),null,null);
				listaPantalla.btnGenerar.addActionListener((ActionListener) this);
				listaPantalla.btnEstructura.addActionListener((ActionListener) this);
			}
			
			//Hacemos visible la nueva pantalla
			listaPantalla.setVisible(true);
		}
		else if(listaPantalla!=null && e.getSource()==listaPantalla.btnGenerar && listaPantalla.comprobarGenerar())
		{
			listaPantalla.setVisible(false);
			
			System.out.println("//PRINCIPAL// Numero de pantallas: " + listaPantalla.getListaTam());
			ubiDesc = new ubicacionDescarga(){
	            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
	            @Override
	            public void dispose(){
	            	ubiDesc.setVisible(false);
	    			listaPantalla.setVisible(true);
	            }
			};
			ubiDesc.btnCancelar.addActionListener((ActionListener) this);
			ubiDesc.btnDescargar.addActionListener((ActionListener) this);
			
			//Hacemos visible la nueva pantalla
			ubiDesc.setVisible(true);
		}
		else if(ubiDesc!=null && e.getSource()==ubiDesc.btnDescargar && ubiDesc.comprobarDescarga())
		{
			ubiDesc.setVisible(false);
			
			String ubicacion = ubiDesc.getUbicacion();
			//GENERAR FICHEROS
			generador gen =new generador(new ficheros(nomenclatura.getNomenclatura(), nomenclatura.getCodPeticion(), listaEstructura.getListaEst(), listaPantalla.getListaPant(),listaPantalla.getListaCodigosPantalla()), ubicacion);
		}
		else if(listaEstructura!=null && e.getSource()==listaEstructura.btnNomenclatura)
		{
			listaEstructura.setVisible(false);
			String nombre = nomenclatura.getNomenclatura();
			int codPet = nomenclatura.getCodPeticion();
			vueltaNomEst = true;
			nomenclatura.dispose();
			nomenclatura = new nomenclatura(nombre,codPet);
		    nomenclatura.btnNewButton.addActionListener((ActionListener) this);
			nomenclatura.setVisible(true);
		}
		else if(listaPantalla!=null && e.getSource()==listaPantalla.btnEstructura)
		{
			listaPantalla.setVisible(false);
			ArrayList<estructura> listaEst = listaEstructura.getListaEst();
			vueltaEstPant = true;
			listaEstructura.dispose();
			listaEstructura= new listaEstructuras(listaEst);
			listaEstructura.btnGenerar.addActionListener((ActionListener) this);
			listaEstructura.btnNomenclatura.addActionListener((ActionListener) this);
			listaEstructura.setVisible(true);
		}
		else if(ubiDesc!=null && e.getSource()==ubiDesc.btnCancelar)
		{
			ubiDesc.setVisible(false);
			
			listaPantalla.setVisible(true);
		}
	}
}
