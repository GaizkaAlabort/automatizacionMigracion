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

import Modelo.editorAccion;
import Modelo.celdaAccion;
import Modelo.estructura;
import Modelo.variable;
import Modelo.eventosAccion;
import Modelo.ficheros;
import Modelo.generador;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTable;

public class listaEstructuras extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable tablaVariablesEstructura;
	private DefaultTableModel modelo;
	private JTable table;
	private JButton btnNuevaEstructura;
	private JButton btnGenerar;
	
	private ArrayList<estructura> listaEstructuras;
	private static String nomenclatura;
	private int codigo;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					listaEstructuras frame = new listaEstructuras(nomenclatura, -1);
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
	public listaEstructuras(String nomenclatura, int codigo) {
		System.out.println("***Inicio inicializar listaEstructuras***");
		this.nomenclatura = nomenclatura;
		System.out.println(this.nomenclatura);
		this.codigo = codigo;
		listaEstructuras = new ArrayList<estructura>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setName(nomenclatura);
		
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
		tablaVariablesEstructura.setRowHeight(35);
		eventosAccion eventos= new eventosAccion() {

			@Override
			public void editar(int fila) {
				System.out.println("Editar fila: " + fila);
				System.out.println("Tama�o: " + listaEstructuras.size());
				/**Basada en estructura extra�da de Stack Overflow
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
		
		btnGenerar = new JButton("Generar");
		btnGenerar.addActionListener(this);
		panel.add(btnGenerar, BorderLayout.EAST);
		
		btnNuevaEstructura = new JButton("Nueva estructura");
		btnNuevaEstructura.addActionListener(this);
		panel.add(btnNuevaEstructura, BorderLayout.WEST);
		System.out.println("***Fin inicializar listaEstructuras***");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnNuevaEstructura) 
		{
			/**Basada en estructura extra�da de Stack Overflow
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
		else if (e.getSource()== btnGenerar) 
		{
			System.out.println("EN PROCESO ...");
			
			if(listaEstructuras.size()==0) {
				JOptionPane.showMessageDialog(null, "Introduzca una estructura como minimo");
				System.out.println("ERROR: ListaEstructura vacia");
			} else {
				generador gen =new generador(new ficheros(nomenclatura, codigo, listaEstructuras));
			}
			
		}
		
	}
	
	private JFrame getFrame(){
	    return this;
	}

}
