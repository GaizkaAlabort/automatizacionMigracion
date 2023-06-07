package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.estructura;
import Modelo.variable;

public class pantallaEstructura extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField nombreEstructura;
	private JTable tablaVariablesEstructura;
	private JButton btnAñadirVariable; 
	private JButton btnTerminarCrearEstructura;
	private DefaultTableModel modelo;
	private JButton btnEliminar;
	
	private ArrayList<variable> listaVariables;
	private JPanel panel_1;
	private String nomEst;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pantallaEstructura frame = new pantallaEstructura(null);
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
	public pantallaEstructura(estructura editEstructura) {
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
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
					false, false, false
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
		
		if (editEstructura != null) {
			for (int i=0; i< listaVariables.size(); i++) {
				variable filaVariable = listaVariables.get(i);
				modelo.addRow(new Object[]{filaVariable.getNombre(),filaVariable.getTipo(),filaVariable.getCantidad()});
			}
		}
		
		scrollPane.setViewportView(tablaVariablesEstructura);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnTerminarCrearEstructura = new JButton("Terminar");
		btnTerminarCrearEstructura.addActionListener(this);
		panel.add(btnTerminarCrearEstructura, BorderLayout.EAST);
		
		btnAñadirVariable = new JButton("Añadir");
		btnAñadirVariable.addActionListener(this);
		panel.add(btnAñadirVariable, BorderLayout.WEST);
		
		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setBounds(89, 0, 84, 23);
		panel_1.add(btnEliminar);
		
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
		else if (e.getSource()==btnTerminarCrearEstructura) 
		{	
			if (nombreEstructura.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Introduzca nombre a la estructura");
				System.out.println("Introduzca nombre a la estructura");
			}
			else
			{
				if (listaVariables.size() == 0) {
					JOptionPane.showMessageDialog(null, "Introduzca alguna fila a la lista de variables");
					System.out.println("Lista de variables vacia");
				} else {
					nomEst = nombreEstructura.getText();
					System.out.println("Fin de la estructura: " + nomEst);
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
					JOptionPane.showMessageDialog(this, "Tabla vacia");
				} 
				else
				{
					JOptionPane.showMessageDialog(this, "Selecciona una fila para borrar");
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

}
