package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

import Modelo.ficheros;
import Modelo.generador;
import Modelo.generarBackUp;
import Vista.listaEstructuras;
import Vista.listaPantallas;
import Vista.nomenclatura;
import Vista.ubicacionDescarga;


public class principal implements ActionListener{
	
	private Vista.cargarGeneral cargarGeneral;
	private nomenclatura nomenclatura;
	private listaEstructuras listaEstructura;
	private listaPantallas listaPantalla;
	private ubicacionDescarga ubiDesc;
	
	private ficheros recogido;
	
	private boolean vueltaNomEst=false;
	private boolean vueltaEstPant=false;
	
	public static void main(String[] args) {
		new principal().iniciar();
	}

	private void iniciar() {
		/**Se instancian las clases que van a ser unicas en el sistema*/
		cargarGeneral = new Vista.cargarGeneral();
		cargarGeneral.btnCargar.addActionListener(this);
		cargarGeneral.btnNueva.addActionListener(this);
		cargarGeneral.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == cargarGeneral.btnCargar) 
		{
			String seleccion = cargarGeneral.comprobarVariasCopias();
			System.out.println(seleccion);
			if(seleccion != null)
			{
				cargarGeneral.dispose();
				recogido  = new ficheros(seleccion);
				listaEstructura= new listaEstructuras(recogido.getEstLista());
				listaEstructura.btnGenerar.addActionListener(this);
				listaEstructura.btnNomenclatura.addActionListener(this);
				listaEstructura.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent e) {
			        	// Ask for confirmation before terminating the program.
			        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
			        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
			        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
			        			new Object[] { "Cancelar", "Salir"},"opcion 1");
			        			     
			        	salida(seleccion);
			        }
				});
				
				listaEstructura.setVisible(true);
				vueltaEstPant = true;
			};
		}
		else if(e.getSource() == cargarGeneral.btnNueva) 
		{
			recogido = new ficheros();
			cargarGeneral.dispose();
			nomenclatura = new nomenclatura(null,-1);
		    nomenclatura.btnNewButton.addActionListener((ActionListener) this);
			nomenclatura.setVisible(true);
		}
		else if(nomenclatura!=null && e.getSource()==nomenclatura.btnNewButton && nomenclatura.comprobarCampos()) 
		{
			recogido.setNomYCod(nomenclatura.getNomenclatura(),nomenclatura.getCodPeticion());
			if(vueltaNomEst) {
				System.out.println("//PRINCIPAL EDITADO// Nombre: " + recogido.getNombre() + ", Cod Peticion:" + recogido.getCodigo());
				//ArrayList<estructura> listaEst = listaEstructura.getListaEst();
				listaEstructura.dispose();
				listaEstructura= new listaEstructuras(recogido.getEstLista());
				listaEstructura.btnGenerar.addActionListener(this);
				listaEstructura.btnNomenclatura.addActionListener(this);
				listaEstructura.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent e) {
			        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
			        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
			        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
			        			new Object[] { "Cancelar", "Salir"},"opcion 1");
			        			     
			        	salida(seleccion);
			        }
				});
				vueltaNomEst = false;
			}
			else{
				System.out.println("//PRINCIPAL// Nombre: " + recogido.getNombre() + ", Cod Peticion:" + recogido.getCodigo());
				listaEstructura= new listaEstructuras(null);
				listaEstructura.btnGenerar.addActionListener((ActionListener) this);
				listaEstructura.btnNomenclatura.addActionListener((ActionListener) this);
				listaEstructura.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent e) {
			        	// Ask for confirmation before terminating the program.
			        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
			        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
			        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
			        			new Object[] { "Cancelar", "Salir"},"opcion 1");
			        			     
			        	salida(seleccion);
			        }
				});
			}
			//Hacemos visible la nueva pantalla
			listaEstructura.setVisible(true);
		}
		else if(listaEstructura!=null && e.getSource()==listaEstructura.btnGenerar && listaEstructura.comprobarGenerar()) 
		{
			recogido.setListaEst(listaEstructura.getListaEst());
			listaEstructura.setVisible(false);
			if(vueltaEstPant) {
				if(listaPantalla!=null) {
					listaPantalla.dispose();
				}
				listaPantalla = new listaPantallas(recogido.getNombre(),recogido.getEstLista(),recogido.getPantLista(),recogido.getCodPantLista());
				listaPantalla.btnGenerar.addActionListener((ActionListener) this);
				listaPantalla.btnEstructura.addActionListener((ActionListener) this);
				listaPantalla.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent e) {
			        	// Ask for confirmation before terminating the program.
			        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
			        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
			        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
			        			new Object[] { "Cancelar", "Salir"},"opcion 1");
			        			     
			        	salida(seleccion);
			        }
				});
				vueltaEstPant = false;
			}
			else{
				System.out.println("//PRINCIPAL// Numero de estructuras: " + listaEstructura.getListaTam() + "=" + recogido.getCant("est"));
				listaPantalla = new listaPantallas(recogido.getNombre(),recogido.getEstLista(),null,null);
				listaPantalla.btnGenerar.addActionListener((ActionListener) this);
				listaPantalla.btnEstructura.addActionListener((ActionListener) this);
				listaPantalla.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent e) {
			        	// Ask for confirmation before terminating the program.
			        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
			        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
			        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
			        			new Object[] { "Cancelar", "Salir"},"opcion 1");
			        			     
			        	salida(seleccion);
			        }
				});
			}
			
			//Hacemos visible la nueva pantalla
			listaPantalla.setVisible(true);
		}
		else if(listaPantalla!=null && e.getSource()==listaPantalla.btnGenerar && listaPantalla.comprobarGenerar())
		{
			recogido.setListaPant(listaPantalla.getListaPant());
			recogido.setListaCodPant(listaPantalla.getListaCodigosPantalla());
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
			ubiDesc.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent e) {
		        	// Ask for confirmation before terminating the program.
		        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
		        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
		        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
		        			new Object[] { "Cancelar", "Salir"},"opcion 1");
		        			     
		        	salida(seleccion);
		        }
			});
			
			//Hacemos visible la nueva pantalla
			ubiDesc.setVisible(true);
		}
		else if(ubiDesc!=null && e.getSource()==ubiDesc.btnDescargar && ubiDesc.comprobarDescarga())
		{
			ubiDesc.setVisible(false);
			
			new generarBackUp(recogido);
			
			String ubicacion = ubiDesc.getUbicacion();
			//GENERAR FICHEROS
			generador gen =new generador(recogido, ubicacion);
		}
		else if(listaEstructura!=null && e.getSource()==listaEstructura.btnNomenclatura)
		{
			recogido.setListaEst(listaEstructura.getListaEst());
			listaEstructura.setVisible(false);
			vueltaNomEst = true;
			if(nomenclatura!=null) {
				nomenclatura.dispose();
			}
			nomenclatura = new nomenclatura(recogido.getNombre(),recogido.getCodigo());
		    nomenclatura.btnNewButton.addActionListener((ActionListener) this);
			nomenclatura.setVisible(true);
			nomenclatura.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent e) {
		        	// Ask for confirmation before terminating the program.
		        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
		        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
		        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
		        			new Object[] { "Cancelar", "Salir"},"opcion 1");
		        			     
		        	salida(seleccion);
		        }
			});
		}
		else if(listaPantalla!=null && e.getSource()==listaPantalla.btnEstructura)
		{
			recogido.setListaPant(listaPantalla.getListaPant());
			recogido.setListaCodPant(listaPantalla.getListaCodigosPantalla());
			listaPantalla.setVisible(false);
			vueltaEstPant = true;
			
			listaEstructura.dispose();
			listaEstructura= new listaEstructuras(recogido.getEstLista());
			listaEstructura.btnGenerar.addActionListener((ActionListener) this);
			listaEstructura.btnNomenclatura.addActionListener((ActionListener) this);
			listaEstructura.setVisible(true);
			listaEstructura.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent e) {
		        	// Ask for confirmation before terminating the program.
		        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar el programa?",
		        			"Cerrar Programa",JOptionPane.YES_NO_CANCEL_OPTION,
		        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
		        			new Object[] { "Cancelar", "Salir"},"opcion 1");
		        			     
		        	salida(seleccion);
		        }
			});
		}
		else if(ubiDesc!=null && e.getSource()==ubiDesc.btnCancelar)
		{
			ubiDesc.setVisible(false);
			
			listaPantalla.setVisible(true);
		}
	}
	
	private void salida(int seleccion) {
		if (seleccion != -1){
			System.out.println("Seleccionada: " + seleccion);
    		if(seleccion+1==2) {
    			if(listaPantalla != null) {
    				new generarBackUp(recogido);
    			} else {
    				new generarBackUp(recogido/*new ficheros(nomenclatura.getNomenclatura(), nomenclatura.getCodPeticion(), listaEstructura.getListaEst(), null, null)*/);
    			}
    			System.exit(0);
    		}
    	}
	}
}
