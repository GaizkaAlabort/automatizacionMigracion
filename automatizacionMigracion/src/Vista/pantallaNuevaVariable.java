package Vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import Modelo.variable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;

public class pantallaNuevaVariable extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField nombre;
	private JTextField cantidad;
	private JButton btnFinNuevaVariable;
	private JComboBox tipo;
	
	private String nombreVar;
	private String tipoVar;
	private int cantidadVar;
	
	private JTextField tabla;
	private String nombreTabla;
	private JTextField columna;
	private String nombreColumna;
	private JComboBox tipo_pers;
	
	private JTable tablaVariables;
	private DefaultTableModel modelo;
	private JButton btnEliminar;
	private JButton btnAñadirVariable;
	
	//PERSONALIZADO
	private String tablaVar;
	private String columnaVar;
	private String tipoPersVar;
	
	//VARRAY
	private ArrayList<variable> listaVariables = new ArrayList<variable>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pantallaNuevaVariable frame = new pantallaNuevaVariable();
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
	public pantallaNuevaVariable() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 271, 296);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblnombre = new JLabel("Nombre:");
		lblnombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblnombre.setBounds(10, 10, 65, 20);
		contentPane.add(lblnombre);
		
		nombre = new JTextField();
		nombre.setBounds(73, 10, 120, 20);
		contentPane.add(nombre);
		nombre.setColumns(10);
		
		JLabel lbltipo = new JLabel("Tipo:");
		lbltipo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbltipo.setBounds(10, 40, 39, 20);
		contentPane.add(lbltipo);
		
		JLabel lblcant = new JLabel("Cantidad:");
		lblcant.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblcant.setBounds(10, 70, 61, 20);
		contentPane.add(lblcant);
		
		cantidad = new JTextField();
		cantidad.setBounds(78, 70, 60, 20);
		contentPane.add(cantidad);
		cantidad.setColumns(10);
		
		tipo = new JComboBox();
		tipo.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar", "VARCHAR2", "NUMBER", "DATE", "VARRAY", "Personalizado"}));
		tipo.setBounds(49, 40, 92, 20);
		
		btnFinNuevaVariable = new JButton("Aceptar");
		btnFinNuevaVariable.setBounds(156, 223, 89, 23);
		btnFinNuevaVariable.addActionListener(this);
		contentPane.add(btnFinNuevaVariable);
		
		
		//PANEL DE PERSONALIZADO -- INICIO
		JPanel panelPersonalizado = new JPanel();
		panelPersonalizado.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelPersonalizado.setBounds(10, 95, 235, 99);
		contentPane.add(panelPersonalizado);
		panelPersonalizado.setLayout(null);
		
		JLabel lblPersonalizado = new JLabel("Personalizado:");
		lblPersonalizado.setBounds(75, 6, 97, 14);
		panelPersonalizado.add(lblPersonalizado);
		
		JLabel lblTablaPers = new JLabel("Tabla:");
		lblTablaPers.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTablaPers.setBounds(8, 25, 44, 18);
		panelPersonalizado.add(lblTablaPers);
		lblTablaPers.setVisible(false);
		
		JLabel lblColumnaPers = new JLabel("Columna:");
		lblColumnaPers.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblColumnaPers.setBounds(8, 48, 60, 18);
		panelPersonalizado.add(lblColumnaPers);
		lblColumnaPers.setVisible(false);
		
		JLabel lblTipoPers = new JLabel("Tipo:");
		lblTipoPers.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTipoPers.setBounds(8, 72, 36, 20);
		panelPersonalizado.add(lblTipoPers);
		lblTipoPers.setVisible(false);
		
		tabla = new JTextField();
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tabla.setBounds(50, 25, 125, 18);
		panelPersonalizado.add(tabla);
		tabla.setColumns(10);
		tabla.setVisible(false);
		
		columna = new JTextField();
		columna.setFont(new Font("Tahoma", Font.PLAIN, 10));
		columna.setColumns(10);
		columna.setBounds(68, 48, 125, 18);
		panelPersonalizado.add(columna);
		columna.setVisible(false);
		
		tipo_pers = new JComboBox();
		tipo_pers.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar", "VARCHAR2", "NUMBER", "DATE"}));		
		tipo_pers.setBounds(42, 72, 80, 20);
		tipo_pers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) {
				if(tipo_pers.getSelectedItem()== "DATE") {
					cantidad.setVisible(false);
					cantidad.setText(null);
				} else {
					cantidad.setVisible(true);
				}
			}
		});
		panelPersonalizado.add(tipo_pers);
		tipo_pers.setVisible(false);
		//PANEL DE PERSONALIZADO -- FIN
		
		
		//PANEL DE VARRAY -- INICIO
		JPanel panelVarray = new JPanel();
		panelVarray.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelVarray.setBounds(10, 95, 235, 125);
		contentPane.add(panelVarray);
		panelVarray.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelVarray.add(scrollPane, BorderLayout.CENTER);
		
		tablaVariables = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		};
		tablaVariables.setModel(modelo);
		tablaVariables.setEnabled(true);
		
		modelo.addColumn("Nombre");
		modelo.addColumn("Tipo");
		modelo.addColumn("Cantidad");
		
		scrollPane.setViewportView(tablaVariables);
		
		JPanel panel = new JPanel();
		panelVarray.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnAñadirVariable = new JButton("Añadir");
		btnAñadirVariable.addActionListener(this);
		panel.add(btnAñadirVariable);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		panel.add(btnEliminar);
		
		panelVarray.setVisible(false);
		//PANEL DE VARRAY -- FIN
		
		
		tipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tipo.getSelectedItem()== "DATE") {
					cantidad.setVisible(false);
					cantidad.setText(null);
				} else {
					cantidad.setVisible(true);
				}
				
				if(tipo.getSelectedItem()== "Personalizado") {
					lblTablaPers.setVisible(true);
					lblColumnaPers.setVisible(true);
					lblTipoPers.setVisible(true);
					tabla.setVisible(true);
					columna.setVisible(true);
					tipo_pers.setVisible(true);
				} else {
					lblTablaPers.setVisible(false);
					lblColumnaPers.setVisible(false);
					lblTipoPers.setVisible(false);
					tabla.setVisible(false);
					columna.setVisible(false);
					tipo_pers.setVisible(false);
				}
				
				if(tipo.getSelectedItem()== "VARRAY") {
					panelPersonalizado.setVisible(false);
					panelVarray.setVisible(true);
					cantidad.setVisible(false);
					cantidad.setText(null);
				} else {
					panelPersonalizado.setVisible(true);
					panelVarray.setVisible(false);
				}
			}
		});
		contentPane.add(tipo);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==btnFinNuevaVariable) {
			if (nombre.getText().isEmpty() || tipo.getSelectedItem()=="Seleccionar")
			{
				System.out.println("Introduzca nombre y seleccione tipo." + tipo.getSelectedItem());
			} 
			else if(tipo.getSelectedItem()== "VARCHAR2" && cantidad.getText().isEmpty()) 
			{
				System.out.println("Los datos varchar necesitan cantidad.");
			}
			else if(tipo.getSelectedItem()== "DATE" && !cantidad.getText().isEmpty()) 
			{
				System.out.println("Las fechas no tienen cantidad.");
			}
			else if(tipo.getSelectedItem()== "VARRAY" && listaVariables.size()<1) {
				System.out.println("Al ser varray, necesita variables en la lista.");
			}
			else if(tipo.getSelectedItem()== "Personalizado" && (tabla.getText().isEmpty() || columna.getText().isEmpty() || tipo_pers.getSelectedItem() =="Seleccionar"))
			{
				System.out.println("Al ser personalizado, necesita tabla, columna y tipo.");
			}
			else if(tipo.getSelectedItem()== "Personalizado" && tipo_pers.getSelectedItem() =="VARCHAR2" && cantidad.getText().isEmpty())
			{
				System.out.println("Los datos varchar necesitan cantidad.");
			}
			else
			{

				nombreVar = nombre.getText();
				tipoVar = String.valueOf(tipo.getSelectedItem());
				
				if(tipoVar == "Personalizado") {
					tablaVar = tabla.getText();
					columnaVar = columna.getText();
					tipoPersVar = String.valueOf(tipo_pers.getSelectedItem());
				}
				
				try{
					if(tipoVar == "VARRAY") {
						cantidadVar = listaVariables.size();
						System.out.println("La lista tiene " + cantidadVar + " variables." );
					}
					else if (cantidad.getText().isEmpty()) {
						cantidadVar = -1;
					} 
					else {
						cantidadVar = Integer.parseInt(cantidad.getText());
					}
					
					System.out.println("FIN. Nombre:" + nombreVar + ", Cantidad:" + cantidadVar +", Tipo:" + tipoVar );
					System.out.println("FIN pers. Tabla:" + tablaVar + ", Columna:" + columnaVar + ", Tipo:" + tipoPersVar);
					dispose();
				} catch (NumberFormatException exception) {
					System.out.println("La cantidad no es un numero.");
				}
			}
		} 
		else if (e.getSource()==btnEliminar)
		{
			if(tablaVariables.getSelectedRowCount() == 1) 
			{
				System.out.println(tablaVariables.getSelectedRow());
				listaVariables.remove(tablaVariables.getSelectedRow());
				modelo.removeRow(tablaVariables.getSelectedRow());
			}
			else
			{
				if(tablaVariables.getRowCount() == 0)
				{
					JOptionPane.showMessageDialog(this, "Tabla vacia");
				} 
				else
				{
					JOptionPane.showMessageDialog(this, "Selecciona una fila para borrar");
				}
					
			}
		}
		else if (e.getSource()==btnAñadirVariable) 
		{
			/**Basada en estructura extraída de Stack Overflow
			Pregunta: https://es.stackoverflow.com/questions/37403/c%C3%B3mo-puedo-cerrar-una-ventana-en-java-y-que-aparezca-la-ventana-anterior-que-la
			Autor: https://es.stackoverflow.com/users/19623/awes0mem4n
			Modificado para recogida de datos
			**/
			
			pantallaNuevaVariable minuevaVariable= new pantallaNuevaVariable(){
			            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
			            @Override
			            public void dispose(){
			                //Hacemos visible la pantalla de la estructura
			                getFrame().setVisible(true);
			                
			                //Cerramos la nueva pantalla
			                super.dispose();
			                
			                //Recogemos 
			                variable nuevaVariable = super.getVariable();
			                System.out.println("pantallaEstructura. Nombre:" + nuevaVariable.getNombre() + ", Cantidad:" + nuevaVariable.getCantidad() +", Tipo:" + nuevaVariable.getTipo() );
			                modelo.addRow(new Object[]{nuevaVariable.getNombre(),nuevaVariable.getTipo(),nuevaVariable.getCantidad()});
			                listaVariables.add(nuevaVariable);
			                System.out.println(listaVariables.size());
			            }
			        };

			//Hacemos visible la nueva pantalla
			minuevaVariable.setVisible(true);
			//Cerramos la principal
			getFrame().setVisible(false);
			
		}
	}
	
	private JFrame getFrame(){
	    return this;
	}
	
	public variable getVariable() {
		if(tipoVar == "Personalizado") {
			System.out.println("PERS");
			return new variable(nombreVar,tipoVar,cantidadVar,
								tablaVar,columnaVar,tipoPersVar);
		} else if(tipoVar == "VARRAY") {
			System.out.println("ARRAY");
			return new variable(nombreVar,tipoVar,listaVariables);
		} else {
			System.out.println("EXTRA");
			return new variable(nombreVar,tipoVar,cantidadVar);
		}
	}
}
