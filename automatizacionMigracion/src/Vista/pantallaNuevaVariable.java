package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import Modelo.columna;
import Modelo.tabla;
import Modelo.variable;

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
	private JComboBox tabla_combo;
	private JComboBox columna_combo;
	private String nombreTabla;
	private JTextField columna;
	private String nombreColumna;
	private JComboBox tipo_pers;
	
	private JTable tablaVariables;
	private DefaultTableModel modelo;
	private JButton btnEliminar;
	private JButton btnAñadirVariable;
	private JLabel lblcant;
	
	//PERSONALIZADO
	private String tablaVar;
	private String columnaVar;
	private String tipoPersVar;
	private HashMap<String, tabla> infoTablas;
	private String anterior="";
	
	//VARRAY
	private ArrayList<variable> listaVariables = new ArrayList<variable>();
	private JLabel lblNombreEstructura;
	private JTextField nombreVarray;
	private String nombreEst;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pantallaNuevaVariable frame = new pantallaNuevaVariable(null, null);//{"Seleccionar", "VARCHAR2", "NUMBER", "DATE", "VARRAY", "Personalizado"}
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
	public pantallaNuevaVariable(variable editVariable, HashMap<String, tabla> infoTablas) {
		
		if(infoTablas !=null) {
			this.infoTablas = infoTablas;
		}
		
		setTitle("Nueva Variable");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
		
		lblcant = new JLabel("Cantidad:");
		lblcant.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblcant.setBounds(10, 70, 61, 20);
		contentPane.add(lblcant);
		
		cantidad = new JTextField();
		cantidad.setBounds(78, 70, 60, 20);
		contentPane.add(cantidad);
		cantidad.setColumns(10);
		
		tipo = new JComboBox();
		tipo.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar", "VARCHAR2", "NUMBER", "DATE", "VARRAY", "Personalizado"}));
		tipo.setBounds(49, 40, 111, 20);
		
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
		
		tabla_combo = new JComboBox();
		tabla_combo.setBounds(50, 25, 125, 18);
		tabla_combo.setEditable(true);
		tabla_combo.setVisible(false);
		tabla_combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabla_combo.getSelectedItem()!= null && tabla_combo.getSelectedItem().toString()!=anterior) {
					tabla tabla = infoTablas.get(tabla_combo.getSelectedItem().toString());
					if (tabla != null) {
						HashMap<String, columna> columnasHash = tabla.obtenerHashColumnas();
						ArrayList<String> columnas = new ArrayList<String>();
						for (Map.Entry<String, columna> entry : columnasHash.entrySet()) {
							String rowKey = entry.getKey();
							columnas.add(rowKey);
						}
										
						Collections.sort(columnas);
						String[] columnasList = new String[columnas.size()];
						columnasList = columnas.toArray(columnasList);
						
						columna_combo.setModel(new DefaultComboBoxModel<>(columnasList));
						columna_combo.setSelectedItem(null);
					}
					anterior = tabla_combo.getSelectedItem().toString();
				}
			}
		});
		AutoCompleteDecorator.decorate(tabla_combo);
		panelPersonalizado.add(tabla_combo);
		
		columna = new JTextField();
		columna.setFont(new Font("Tahoma", Font.PLAIN, 10));
		columna.setColumns(10);
		columna.setBounds(68, 48, 125, 18);
		panelPersonalizado.add(columna);
		columna.setVisible(false);
		
		columna_combo = new JComboBox();
		columna_combo.setBounds(68, 48, 125, 18);
		columna_combo.setEditable(true);
		columna_combo.setVisible(false);
		columna_combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (columna_combo.getSelectedItem()!= null && anterior != null) {
					tabla tabla = infoTablas.get(tabla_combo.getSelectedItem().toString());
					if (tabla != null) {
						columna columna = tabla.recogerColumna(columna_combo.getSelectedItem().toString());
						if(columna != null) {
							tipo_pers.setSelectedItem(columna.getTipo());
							cantidad.setText(String.valueOf(columna.getCant()));
						}
					}
				}
				else
				{
					tipo_pers.setSelectedItem("Seleccionar");
					cantidad.setText(null);
				}
			}
		});
		AutoCompleteDecorator.decorate(columna_combo);
		panelPersonalizado.add(columna_combo);
		
		tipo_pers = new JComboBox();
		tipo_pers.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar", "VARCHAR2", "NUMBER", "DATE"}));		
		tipo_pers.setBounds(42, 72, 95, 20);
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
		lblNombreEstructura = new JLabel("Nombre Varray:");
		lblNombreEstructura.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNombreEstructura.setBounds(10, 70, 110, 20);
		contentPane.add(lblNombreEstructura);
		lblNombreEstructura.setVisible(false);
		
		nombreVarray = new JTextField();
		nombreVarray.setColumns(10);
		nombreVarray.setBounds(121, 70, 124, 20);
		contentPane.add(nombreVarray);
		nombreVarray.setVisible(false);
		
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
					if(infoTablas != null) {
						ArrayList<String> tablas = new ArrayList<String>();
						for (Map.Entry<String, tabla> entry : infoTablas.entrySet()) {
				            String rowKey = entry.getKey();
				            tablas.add(rowKey);
				        }
						
						Collections.sort(tablas);
						String[] tablasList = new String[tablas.size()];
						tablasList = tablas.toArray(tablasList);
						
						tabla_combo.setModel(new DefaultComboBoxModel<>(tablasList));
						tabla_combo.setVisible(true);
						columna_combo.setVisible(true);
						
					} else {
						tabla.setVisible(true);
						columna.setEditable(true);
						columna.setVisible(true);
					}
					tipo_pers.setVisible(true);
				} else {
					lblTablaPers.setVisible(false);
					lblColumnaPers.setVisible(false);
					lblTipoPers.setVisible(false);
					tabla.setVisible(false);
					columna.setVisible(false);
					tipo_pers.setVisible(false);
					tabla_combo.setVisible(false);
					columna_combo.setVisible(false);
				}
				
				if(tipo.getSelectedItem()== "VARRAY") {
					lblcant.setVisible(false);
					lblNombreEstructura.setVisible(true);
					nombreVarray.setVisible(true);
					panelPersonalizado.setVisible(false);
					panelVarray.setVisible(true);
					cantidad.setVisible(false);
					cantidad.setText(null);
				} else {
					lblcant.setVisible(true);
					lblNombreEstructura.setVisible(false);
					panelPersonalizado.setVisible(true);
					panelVarray.setVisible(false);
					nombreVarray.setVisible(false);
				}
			}
		});
		contentPane.add(tipo);
		
		if (editVariable != null) {
			setTitle("Editar Variable: " + editVariable.getNombre());
			
			nombre.setText(editVariable.getNombre());
			if(editVariable.getOpcion() == "basico") {
				tipo.setSelectedItem(editVariable.getTipo());	
				
				if(editVariable.getTipo() != "DATE") {
					cantidad.setText(String.valueOf(editVariable.getCantidad()));
				}
			} else if(editVariable.getOpcion() == "pers") {
				tipo.setSelectedItem("Personalizado");
				
				tabla.setText(editVariable.getTabla());
				columna.setText(editVariable.getColumna());
				tipo_pers.setSelectedItem(editVariable.getTipoPers());
				if(editVariable.getTipoPers() != "DATE") {
					cantidad.setText(String.valueOf(editVariable.getCantidad()));
				}
			} else if(editVariable.getOpcion() == "varray") {
				tipo.setSelectedItem("VARRAY");
				
				nombreVarray.setText(editVariable.getNombreEstructura());
				
				if(editVariable.getCantVariables() > 0) {
					for(int var=0; var< editVariable.getCantVariables(); var++) {
						variable varPaso = editVariable.getVariable(var);
						
						modelo.addRow(new Object[]{varPaso.getNombre(),varPaso.getTipo(),varPaso.getCantidad()});
		                listaVariables.add(varPaso);
					}
				}
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==btnFinNuevaVariable) {
			if (nombre.getText().isEmpty() || tipo.getSelectedItem()=="Seleccionar")
			{
				System.out.println("Introduzca nombre y seleccione tipo." + tipo.getSelectedItem());
				JOptionPane.showMessageDialog(this, "Introduzca nombre y seleccione tipo.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			} 
			else if(tipo.getSelectedItem()== "VARCHAR2" && cantidad.getText().isEmpty()) 
			{
				System.out.println("Los datos varchar necesitan cantidad.");
				JOptionPane.showMessageDialog(this, "Los datos varchar necesitan cantidad.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else if(tipo.getSelectedItem()== "DATE" && !cantidad.getText().isEmpty()) 
			{
				System.out.println("Las fechas no tienen cantidad.");
				JOptionPane.showMessageDialog(this, "Las fechas no tienen cantidad.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else if(tipo.getSelectedItem()== "VARRAY" && listaVariables.size()<1) 
			{
				System.out.println("Al ser varray, necesita variables en la lista.");
				JOptionPane.showMessageDialog(this, "Al ser varray, necesita variables en la lista.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else if(tipo.getSelectedItem()== "Personalizado" && infoTablas == null && (tabla.getText().isEmpty() || columna.getText().isEmpty() || tipo_pers.getSelectedItem() =="Seleccionar"))
			{
				System.out.println("Al ser personalizado, necesita tabla, columna y tipo.");
				JOptionPane.showMessageDialog(this, "Al ser personalizado, necesita tabla, columna y tipo.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else if(tipo.getSelectedItem()== "Personalizado" && infoTablas != null && (tabla_combo.getSelectedItem().toString() == null|| columna_combo.getSelectedItem().toString() == null || tipo_pers.getSelectedItem() == "Seleccionar"))
			{
				System.out.println("Al ser personalizado, necesita tabla, columna y tipo.");
				JOptionPane.showMessageDialog(this, "Al ser personalizado, necesita tabla, columna y tipo.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else if(tipo.getSelectedItem()== "Personalizado" && tipo_pers.getSelectedItem() =="VARCHAR2" && cantidad.getText().isEmpty())
			{
				System.out.println("Los datos varchar necesitan cantidad.");
				JOptionPane.showMessageDialog(this, "Los datos varchar necesitan cantidad.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else if(tipo.getSelectedItem()== "VARRAY" && nombreVarray.getText().isEmpty()) 
			{
				System.out.println("La estructura varray debe tener un nombre, para poder reutilizar estructura.");
				JOptionPane.showMessageDialog(this, "La estructura varray debe tener un nombre, para poder reutilizar estructura.","Incorrecto", JOptionPane.ERROR_MESSAGE);
			}
			else
			{

				nombreVar = nombre.getText();
				tipoVar = String.valueOf(tipo.getSelectedItem());
				
				if(tipoVar == "Personalizado") {
					if(infoTablas != null) {
						tablaVar = tabla_combo.getSelectedItem().toString();
						columnaVar = columna_combo.getSelectedItem().toString();
					} else {
						tablaVar = tabla.getText();
						columnaVar = columna.getText();
					}
					
					tipoPersVar = String.valueOf(tipo_pers.getSelectedItem());
				}
				
				try{
					if(tipoVar == "VARRAY") {
						nombreEst = nombreVarray.getText();
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
					System.out.println("FIN var. Nombre Estructura:" + nombreEst);
					dispose();
				} catch (NumberFormatException exception) {
					System.out.println("La cantidad no es un numero.");
					JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero.","Incorrecto", JOptionPane.ERROR_MESSAGE);
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
					JOptionPane.showMessageDialog(this, "Tabla vacia","Incorrecto", JOptionPane.ERROR_MESSAGE);
				} 
				else
				{
					JOptionPane.showMessageDialog(this, "Selecciona una fila para borrar","Incorrecto", JOptionPane.ERROR_MESSAGE);
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
			
			pantallaNuevaVariable minuevaVariable= new pantallaNuevaVariable(null, infoTablas){
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
		if(nombreVar!= null) {
			if(tipoVar == "Personalizado") {
				System.out.println("PERS");
				return new variable(nombreVar,tipoVar,cantidadVar,
									tablaVar,columnaVar,tipoPersVar);
			} else if(tipoVar == "VARRAY") {
				System.out.println("ARRAY");
				return new variable(nombreVar,tipoVar,listaVariables,nombreEst);
			} else {
				System.out.println("EXTRA");
				return new variable(nombreVar,tipoVar,cantidadVar);
			}
		} else {
			return null;
		}
	}
}
