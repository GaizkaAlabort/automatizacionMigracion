package Vista;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.estructura;
import Modelo.variable;
import Modelo.Accion.celdaAccion;
import Modelo.Accion.editorAccion;
import Modelo.Accion.eventosAccion;
import Modelo.ficheros;
import Modelo.generador;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JLabel;

public class listaEstructuras extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable tablaVariablesEstructura;
	private DefaultTableModel modelo;
	private JTable table;
	private JButton btnNuevaEstructura;
	public JButton btnGenerar;
	
	private ArrayList<estructura> listaEstructuras;
	private JPanel panel_1;
	private JPanel panel_2;
	public JButton btnNomenclatura;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					listaEstructuras frame = new listaEstructuras(null);
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
	public listaEstructuras(ArrayList<estructura> listaEst) {
		System.out.println("***Inicio inicializar listaEstructuras***");
		listaEstructuras = new ArrayList<estructura>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		tablaVariablesEstructura = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					false, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		modelo.addColumn("Estructura");
		modelo.addColumn("Accion");
		
		tablaVariablesEstructura.setModel(modelo);
		
		if(listaEst!=null){
			listaEstructuras=listaEst;
			for(int tam=0; tam<listaEstructuras.size();tam++)
			{
				modelo.addRow(new Object[]{listaEstructuras.get(tam).getNombre()});
			}
		}
		
		tablaVariablesEstructura.setRowHeight(35);
		eventosAccion eventos= new eventosAccion() {

			@Override
			public void editar(int fila) {
				System.out.println("Editar fila: " + fila);
				System.out.println("Tamaño: " + listaEstructuras.size());
				/**Basada en estructura extraída de Stack Overflow
				Pregunta: https://es.stackoverflow.com/questions/37403/c%C3%B3mo-puedo-cerrar-una-ventana-en-java-y-que-aparezca-la-ventana-anterior-que-la
				Autor: https://es.stackoverflow.com/users/19623/awes0mem4n
				Modificado para recogida de datos
				**/
				pantallaEstructura minuevaEstructura= new pantallaEstructura(listaEstructuras.get(fila)){
				            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
				            @Override
				            public void dispose(){
				                //Hacemos visible la pantalla de la estructura
				                getFrame().setVisible(true);
				                
				                //Cerramos la nueva pantalla
				                super.dispose();
				                
				                //Recogemos 
				                estructura nuevaEstructura = super.getEstructura();
				                System.out.println("pantallaEstructura. Nombre:" + nuevaEstructura.getNombre() + ", Variables: " + nuevaEstructura.getCantVariables());
				                modelo.setValueAt(nuevaEstructura.getNombre(), fila, 0);
				                listaEstructuras.set(fila, nuevaEstructura);
				                System.out.println(listaEstructuras.size());
				            }
				        };

				//Hacemos visible la nueva pantalla
				minuevaEstructura.setVisible(true);
				//Cerramos la principal
				getFrame().setVisible(false);
			}

			@Override
			public void eliminar(int fila) {
				if(tablaVariablesEstructura.isEditing()) {
					tablaVariablesEstructura.getCellEditor().stopCellEditing();
				}
				System.out.println("Borrar fila: " + fila);
				listaEstructuras.remove(fila);
				modelo.removeRow(fila);
			}
			
		};
		tablaVariablesEstructura.getColumnModel().getColumn(1).setCellRenderer(new celdaAccion());
		tablaVariablesEstructura.getColumnModel().getColumn(1).setCellEditor(new editorAccion(eventos));
		
		scrollPane.setViewportView(tablaVariablesEstructura);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		btnNuevaEstructura = new JButton("Nueva estructura");
		panel_1.add(btnNuevaEstructura);
		btnNuevaEstructura.addActionListener(this);
		
		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		
		btnNomenclatura = new JButton("Nomenclatura");
		panel_2.add(btnNomenclatura);
		
		lblNewLabel = new JLabel("          ");
		panel_2.add(lblNewLabel);
		
		btnGenerar = new JButton("Pantallas");
		panel_2.add(btnGenerar);
		
		lblNewLabel_1 = new JLabel("      ");
		panel_2.add(lblNewLabel_1);
		System.out.println("***Fin inicializar listaEstructuras***");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnNuevaEstructura) 
		{
			/**Basada en estructura extraída de Stack Overflow
			Pregunta: https://es.stackoverflow.com/questions/37403/c%C3%B3mo-puedo-cerrar-una-ventana-en-java-y-que-aparezca-la-ventana-anterior-que-la
			Autor: https://es.stackoverflow.com/users/19623/awes0mem4n
			Modificado para recogida de datos
			**/
			
			pantallaEstructura minuevaEstructura= new pantallaEstructura(null){
			            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
			            @Override
			            public void dispose(){
			                //Hacemos visible la pantalla de la estructura
			                getFrame().setVisible(true);
			                
			                //Cerramos la nueva pantalla
			                super.dispose();
			                
			                //Recogemos 
			                estructura nuevaEstructura = super.getEstructura();
			                System.out.println("pantallaEstructura. Nombre:" + nuevaEstructura.getNombre() + ", Variables: " + nuevaEstructura.getCantVariables());
			                modelo.addRow(new Object[]{nuevaEstructura.getNombre()});
			                listaEstructuras.add(nuevaEstructura);
			                System.out.println(listaEstructuras.size());
			            }
			        };

			//Hacemos visible la nueva pantalla
			minuevaEstructura.setVisible(true);
			//Cerramos la principal
			getFrame().setVisible(false);
		}
	}
	
	private JFrame getFrame(){
	    return this;
	}
	
	public boolean comprobarGenerar() {
		if(listaEstructuras.size()==0) {
			JOptionPane.showMessageDialog(null, "Introduzca una estructura como minimo");
			System.out.println("ERROR: ListaEstructura vacia");
			return false;
		} else {
			return true;
			//generador gen =new generador(new ficheros(nomenclatura, codigo, listaEstructuras));
		}
	}
	
	public ArrayList<estructura> getListaEst(){
		return listaEstructuras;
	}
	
	/*public ArrayList<String> getListaNomEst(String nomenclatura){
		ArrayList<String> listanombreestado = new ArrayList<String>();
		
		for (int i=0; i<listaEstructuras.size(); i++)
        {
        	estructura estructura = listaEstructuras.get(i);
        	
        	listanombreestado.add("PACK_" + nomenclatura + ".PAN_" + estructura.getNombre());
        }
		
		return listanombreestado;
	}*/
	
	public int getListaTam(){
		return listaEstructuras.size();
	}
}
