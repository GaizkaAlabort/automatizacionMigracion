package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.estructura;
import Modelo.tabla;
import Modelo.variable;
import Modelo.EditarVariable.editorEditVar;
import Modelo.EditarVariable.eventoEditVar;
import Modelo.EditarVariable.rendererEditVar;
import net.miginfocom.swing.MigLayout;

public class pantallaEstructura extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField nombreEstructura;
	private JTable tablaVariablesEstructura;
	private JButton btnAñadirVariable; 
	private JButton btnTerminarCrearEstructura;
	private DefaultTableModel modelo;
	private JButton btnEliminar;
	
	private ArrayList<variable> listaVariables;
	private String nomEst;
	
	private HashMap<String, tabla> infoTablas;
	
	public boolean forzado = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pantallaEstructura frame = new pantallaEstructura(null, null);
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
	public pantallaEstructura(estructura editEstructura, HashMap<String, tabla> infoTablas) {
		System.out.println("***Inicio inicializar pantallaEstructura***");
		if (editEstructura == null) {
			listaVariables = new ArrayList<variable>();
			nombreEstructura = new JTextField();
			setTitle("Nueva estructura");
		} else {
			listaVariables = editEstructura.getLista();
			nombreEstructura = new JTextField(editEstructura.getNombre());
			setTitle("Editar Estructura: " + editEstructura.getNombre());
		}
		
		if(infoTablas != null) {
			this.infoTablas = infoTablas;
		}
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	// Ask for confirmation before terminating the program.
	        	int seleccion = JOptionPane.showOptionDialog( null,"¿Desea abandonar la pantalla?",
	        			"Cerrar Pantalla",JOptionPane.YES_NO_CANCEL_OPTION,
	        			JOptionPane.INFORMATION_MESSAGE,null,// null para icono por defecto.
	        			new Object[] { "Cancelar", "Atras"},"opcion 1");
	        			     
	        	if (seleccion != -1){
	        		if(seleccion+1==2) {
	        			forzado = true;
	        			dispose();
	        		}
	        	}
	        }
		});
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel opciones = new JPanel();
		contentPane.add(opciones, BorderLayout.NORTH);
		opciones.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Nombre de la estructura:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		opciones.add(lblNewLabel, BorderLayout.NORTH);
		
		nombreEstructura.setHorizontalAlignment(SwingConstants.CENTER);
		opciones.add(nombreEstructura, BorderLayout.CENTER);
		nombreEstructura.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		tablaVariablesEstructura = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					false, false, false, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		};
		tablaVariablesEstructura.setModel(modelo);
		tablaVariablesEstructura.setEnabled(true);
		
		modelo.addColumn("Nombre");
		modelo.addColumn("Tipo");
		modelo.addColumn("Cantidad");
		modelo.addColumn(" ");
		
		tablaVariablesEstructura.getColumnModel().getColumn(3).setMaxWidth(30);
		
		if (editEstructura != null) {
			for (int i=0; i< listaVariables.size(); i++) {
				variable filaVariable = listaVariables.get(i);
				modelo.addRow(new Object[]{filaVariable.getNombre(),filaVariable.getTipo(),filaVariable.getCantidad(),null});
			}
		}
		
		eventoEditVar evento= new eventoEditVar() {
			@Override
			public void editar(int fila, JButton btn) {
				System.out.println("FILA: " + fila);
				
				pantallaNuevaVariable editarVariable= new pantallaNuevaVariable(listaVariables.get(fila), infoTablas){
		            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
		            @Override
		            public void dispose(){
		                //Hacemos visible la pantalla de la estructura
		                getFrame().setVisible(true);
		                
		                //Recogemos 
		                variable nuevaVariable = super.getVariable();
		                if(nuevaVariable != null) {
		                	System.out.println("pantallaEstructura. Nombre:" + nuevaVariable.getNombre() + ", Cantidad:" + nuevaVariable.getCantidad() +", Tipo:" + nuevaVariable.getTipo());
				            boolean existe = buscarNombreVariable(nuevaVariable.getNombre(),fila);
			                
		                	if(existe == true) {
		        				JOptionPane.showMessageDialog(this, "El nombre de variable ya esta usado.","Nombre repetido", JOptionPane.ERROR_MESSAGE);
		                		
		                	} else {
		                		//Cerramos la nueva pantalla
				                super.dispose();
			                
				                modelo.setValueAt(nuevaVariable.getNombre(), fila, 0);
				                modelo.setValueAt(nuevaVariable.getTipo(), fila, 1);
				                modelo.setValueAt(nuevaVariable.getCantidad(), fila, 2);
				                listaVariables.set(fila, nuevaVariable);
				                System.out.println(listaVariables.size());
				                btn.setEnabled(true);
		                	}
		                	
		                	
		                } else {
		                	//Cerramos la nueva pantalla
	                		super.dispose();
	                		btn.setEnabled(true);
		                }
		                
		            }
		        };

				//Hacemos visible la nueva pantalla
		        editarVariable.setVisible(true);
		
			}
		};
		tablaVariablesEstructura.getColumnModel().getColumn(3).setCellRenderer(new rendererEditVar());
		tablaVariablesEstructura.getColumnModel().getColumn(3).setCellEditor(new editorEditVar(evento));
		
		scrollPane.setViewportView(tablaVariablesEstructura);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btnAñadirVariable = new JButton("Añadir");
		btnAñadirVariable.addActionListener(this);
		panel.setLayout(new MigLayout("", "[][100px][][100px][]", "[23px]"));
		panel.add(btnAñadirVariable, "cell 0 0,growx,aligny center");
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setBounds(89, 0, 84, 23);
		panel.add(btnEliminar, "cell 2 0,alignx center,aligny center");
		
		btnTerminarCrearEstructura = new JButton("Terminar");
		btnTerminarCrearEstructura.addActionListener(this);
		panel.add(btnTerminarCrearEstructura, "cell 4 0,alignx center,aligny center");
		
		System.out.println("***Fin inicializar pantallaEstructura***");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnAñadirVariable) 
		{
			/**Basada en estructura extraída de Stack Overflow
			Pregunta: https://es.stackoverflow.com/questions/37403/c%C3%B3mo-puedo-cerrar-una-ventana-en-java-y-que-aparezca-la-ventana-anterior-que-la
			Autor: https://es.stackoverflow.com/users/19623/awes0mem4n
			Modificado para recogida de datos
			**/
			
			pantallaNuevaVariable minuevaVariable= new pantallaNuevaVariable(null, infoTablas){
			            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
			            @Override
			            public void dispose(){
			                //Hacemos visible la pantalla de la estructura
			                getFrame().setVisible(true);
			                
			                //Recogemos 
			                variable nuevaVariable = super.getVariable();
			                if(nuevaVariable != null) {
				                System.out.println("pantallaEstructura. Nombre:" + nuevaVariable.getNombre() + ", Cantidad:" + nuevaVariable.getCantidad() +", Tipo:" + nuevaVariable.getTipo() );
				                boolean existe = buscarNombreVariable(nuevaVariable.getNombre(),-1);
				                
			                	if(existe == true) {
			        				JOptionPane.showMessageDialog(this, "El nombre de variable ya esta usado.","Nombre repetido", JOptionPane.ERROR_MESSAGE);
			                		
			                	} else {
			                		//Cerramos la nueva pantalla
			                		super.dispose();
			                		
					                modelo.addRow(new Object[]{nuevaVariable.getNombre(),nuevaVariable.getTipo(),nuevaVariable.getCantidad(),null});
					                listaVariables.add(nuevaVariable);
					                System.out.println(listaVariables.size());
					                
					                btnAñadirVariable.setEnabled(true);
			                	}
			                } else {
			                	//Cerramos la nueva pantalla
		                		super.dispose();
		                		btnAñadirVariable.setEnabled(true);
			                }
			            }
			        };

			//Hacemos visible la nueva pantalla
			minuevaVariable.setVisible(true);
			
			btnAñadirVariable.setEnabled(false);
			
		}
		else if (e.getSource()==btnTerminarCrearEstructura) 
		{	
			if (nombreEstructura.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Introduzca nombre a la estructura","Incorrecto", JOptionPane.ERROR_MESSAGE);
				System.out.println("Introduzca nombre a la estructura");
			}
			else
			{
				if (listaVariables.size() == 0) {
					JOptionPane.showMessageDialog(null, "Introduzca alguna fila a la lista de variables","Incorrecto", JOptionPane.ERROR_MESSAGE);
					System.out.println("Lista de variables vacia");
				} else {
					nomEst = nombreEstructura.getText();
					System.out.println("Fin de la estructura: " + nomEst);
					
					//new generarBackUp(new estructura(nomEst,listaVariables));
					dispose();
				}
				
			}
			
		}
		else if (e.getSource()==btnEliminar)
		{
			if(tablaVariablesEstructura.getSelectedRowCount() == 1) 
			{
				System.out.println(tablaVariablesEstructura.getSelectedRow());
				listaVariables.remove(tablaVariablesEstructura.getSelectedRow());
				modelo.removeRow(tablaVariablesEstructura.getSelectedRow());
			}
			else
			{
				if(tablaVariablesEstructura.getRowCount() == 0)
				{
					JOptionPane.showMessageDialog(this, "Tabla vacia","Incorrecto", JOptionPane.ERROR_MESSAGE);
				} 
				else
				{
					JOptionPane.showMessageDialog(this, "Selecciona una fila para borrar","Incorrecto", JOptionPane.ERROR_MESSAGE);
				}
					
			}
		}
		
	}
	
	private JFrame getFrame(){
	    return this;
	}
	
	public estructura getEstructura(){
	    return new estructura(nomEst,listaVariables);
	}
	
	private boolean buscarNombreVariable(String nombre, int editar) {
		for (int r = 0; r < tablaVariablesEstructura.getRowCount(); r++) {
		    String valor = (String) modelo.getValueAt(r, 0);
		    if(nombre.equals(valor) && editar != r) {
		    	return true;
		    }
		}
		return false;
	}

}
