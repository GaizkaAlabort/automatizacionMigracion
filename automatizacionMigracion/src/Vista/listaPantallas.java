package Vista;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.celdaAccion;
import Modelo.editorAccion;
import Modelo.eventosAccion;
import Modelo.codigoPantalla;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Modelo.pantalla;

public class listaPantallas extends JFrame implements ActionListener{

	private JPanel general;
	private JButton btnNuevaPantalla;
	public JButton btnGenerar;
	private JScrollPane scrollPane;
	private JTable tablaVariablesPantallas;
	private DefaultTableModel modelo;
	
	private ArrayList<pantalla> listaPantallas;
	private ArrayList<String> listaNombreEstructuras;
	private ArrayList<codigoPantalla> listaCodigosPantalla;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArrayList<String> listaNombreEst=new ArrayList<String>();
					listaPantallas frame = new listaPantallas(listaNombreEst);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public listaPantallas(ArrayList<String> listaNombreEst) {
		System.out.println("***Inicio inicializar listaPantallas***");
		listaPantallas = new ArrayList<pantalla>();
		listaCodigosPantalla = new ArrayList<codigoPantalla>();
		if(listaNombreEst.size()==0) {
			listaNombreEstructuras = new ArrayList<String>();
		} else {
			listaNombreEstructuras = listaNombreEst;
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		general = new JPanel();
		general.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(general);
		general.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		general.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnGenerar = new JButton("Generar");
		btnGenerar.addActionListener(this);
		panel.add(btnGenerar, BorderLayout.EAST);
		
		btnNuevaPantalla = new JButton("Nueva pantalla");
		btnNuevaPantalla.addActionListener(this);
		panel.add(btnNuevaPantalla, BorderLayout.WEST);
		
		scrollPane = new JScrollPane();
		general.add(scrollPane, BorderLayout.CENTER);
		
		tablaVariablesPantallas = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					false, false, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		modelo.addColumn("Pantalla");
		modelo.addColumn("Codigo");
		modelo.addColumn("Accion");
		
		tablaVariablesPantallas.setModel(modelo);
		tablaVariablesPantallas.getColumnModel().getColumn(0).setMaxWidth(400);
		tablaVariablesPantallas.getColumnModel().getColumn(1).setMaxWidth(50);
		tablaVariablesPantallas.getColumnModel().getColumn(2).setMaxWidth(70);
		tablaVariablesPantallas.setRowHeight(35);
		
		eventosAccion eventos= new eventosAccion() {

			@Override
			public void editar(int fila) {
				System.out.println("Editar fila: " + fila);
				System.out.println("Tamaño: " + listaPantallas.size());
				/**Basada en estructura extraída de Stack Overflow
				Pregunta: https://es.stackoverflow.com/questions/37403/c%C3%B3mo-puedo-cerrar-una-ventana-en-java-y-que-aparezca-la-ventana-anterior-que-la
				Autor: https://es.stackoverflow.com/users/19623/awes0mem4n
				Modificado para recogida de datos
				**/
				pantallaNuevaPantalla minuevaPantalla= new pantallaNuevaPantalla(listaPantallas.get(fila), listaNombreEstructuras,listaCodigosPantalla){
				            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
				            @Override
				            public void dispose(){
				                //Hacemos visible la pantalla de la estructura
				                getFrame().setVisible(true);
				                
				                //Cerramos la nueva pantalla
				                super.dispose();
				                
				                //Recogemos 
				                pantalla nuevaPantalla = super.getPantalla();
				                System.out.println("pantalla. Nombre: " + nuevaPantalla.getNombre() + ", Numero: " + nuevaPantalla.getNumPant());
				                modelo.setValueAt(nuevaPantalla.getNombre(), fila, 0);
				                modelo.setValueAt(nuevaPantalla.getNumPant(), fila, 1);
				                int numero = listaPantallas.get(fila).getNumPant();
				                listaPantallas.set(fila, nuevaPantalla);
				                System.out.println("Tamaño de la lista de pantallas: " + listaPantallas.size());
				                if(numero != nuevaPantalla.getNumPant()) {
				                	int indice = -1;
				                	//Comprobar que existe otra pantalla con ese codigo
					                for(int k=0; k< listaPantallas.size();k++) {
					                	if(numero==listaPantallas.get(k).getNumPant()) {
					                		indice=k;
					                	}
					                }
					                
					                //Si no existe, borrar de la lista
					                if (indice != -1) {
					        			System.out.println("La pantalla "+ numero + " está en el índice " + indice);
					        		} else {
					        			for(int num=0;num<listaCodigosPantalla.size();num++)
						                {
						                	if(numero==listaCodigosPantalla.get(num).getNumPant()) {
						                		indice =num;
						                	}
						                }
					        			
					        			listaCodigosPantalla.remove(indice);
					        			System.out.println("Borrado "+ numero + " de " + indice);
					                }
				                }
				            }
				        };

				//Hacemos visible la nueva pantalla
				minuevaPantalla.setVisible(true);
				//Cerramos la principal
				getFrame().setVisible(false);
			}

			@Override
			public void eliminar(int fila) {
				System.out.println("Borrar fila: " + fila);
				if(tablaVariablesPantallas.isEditing()) {
					tablaVariablesPantallas.getCellEditor().stopCellEditing();
				}
				listaPantallas.remove(fila);
				modelo.removeRow(fila);
				System.out.println("Tamaño de la lista de pantallas: " + listaPantallas.size());
			}
			
		};
		tablaVariablesPantallas.getColumnModel().getColumn(2).setCellRenderer(new celdaAccion());
		tablaVariablesPantallas.getColumnModel().getColumn(2).setCellEditor(new editorAccion(eventos));
		
		scrollPane.setViewportView(tablaVariablesPantallas);
		System.out.println("***Fin inicializar listaEstructuras***");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnNuevaPantalla) 
		{
			/**Basada en estructura extraída de Stack Overflow
			Pregunta: https://es.stackoverflow.com/questions/37403/c%C3%B3mo-puedo-cerrar-una-ventana-en-java-y-que-aparezca-la-ventana-anterior-que-la
			Autor: https://es.stackoverflow.com/users/19623/awes0mem4n
			Modificado para recogida de datos
			**/
			
			pantallaNuevaPantalla minuevaPantalla= new pantallaNuevaPantalla(null, listaNombreEstructuras,listaCodigosPantalla){
			            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
			            @Override
			            public void dispose(){
			                //Hacemos visible la pantalla de la estructura
			                getFrame().setVisible(true);
			                
			                //Cerramos la nueva pantalla
			                super.dispose();
			                
			                //Recogemos 
			                pantalla nuevaPantalla = super.getPantalla();
			                System.out.println("pantalla. Nombre:" + nuevaPantalla.getNombre()+ ", Numero: " + nuevaPantalla.getNumPant());
			                modelo.addRow(new Object[]{nuevaPantalla.getNombre(),nuevaPantalla.getNumPant()});
			                listaPantallas.add(nuevaPantalla);
			                System.out.println(listaPantallas.size());
			                
			                int indice = -1;	
			                for(int num=0;num<listaCodigosPantalla.size();num++)
			                {
			                	if(nuevaPantalla.getNumPant()==listaCodigosPantalla.get(num).getNumPant()) {
			                		indice=num;
			                	}
			                }
			                
			        		if (indice != -1) {
			        			System.out.println("La pantalla "+ nuevaPantalla.getNumPant() + " está en el índice " + indice);
			        		} else {
			        			listaCodigosPantalla.add(nuevaPantalla.getCodigoPantalla());
			                }
			            }
			        };

			//Hacemos visible la nueva pantalla
			minuevaPantalla.setVisible(true);
			//Cerramos la principal
			getFrame().setVisible(false);
		}		
	}
	
	private JFrame getFrame(){
	    return this;
	}
	
	public ArrayList<pantalla> getListaPant(){
		return  listaPantallas;
	}
	
	public int getListaTam(){
		return listaPantallas.size();
	}
	
	public ArrayList<codigoPantalla> getListaCodigosPantalla(){
		return listaCodigosPantalla;
	}
}
