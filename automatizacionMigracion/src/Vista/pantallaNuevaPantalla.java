package Vista;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Modelo.campos;
import Modelo.codigoPantalla;
import Modelo.estructura;
import Modelo.pantalla;
import Modelo.variable;

import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTable;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

public class pantallaNuevaPantalla extends JFrame implements ActionListener{

	private JPanel general;
	private JButton btnFin;
	private JButton btnAnadir;
	private JButton btnEliminar;
	private JTextField nombre;
	private JTextField nombreCorto;
	private JTextField numeroPant;
	private JTextField descPant;
	private JTable tablaCampos;
	private DefaultTableModel modelo;
	private JCheckBox[] listaChk = new JCheckBox[13];
	
	private String nomPant;
	private String nomCortoPant;
	private ArrayList<campos> listaCampos;
	private boolean[] listaTeclas = new boolean[13];
	private ArrayList<String> listaNombreEstructuras;
	private ArrayList<codigoPantalla> listaCodigoPantallas;
	private int numPant;
	private String descripcion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pantallaNuevaPantalla frame = new pantallaNuevaPantalla(null, null, null);
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
	public pantallaNuevaPantalla(pantalla editPantalla, ArrayList<String> listaNombreEst, ArrayList<codigoPantalla> listaCodPantalla) {
		System.out.println("***Inicio inicializar nuevaPantalla***");
		if (editPantalla == null) {
			nombre = new JTextField();
			nombreCorto = new JTextField();
			numeroPant = new JTextField();
			descPant = new JTextField();
		} else {
			nombre = new JTextField(editPantalla.getNombre());
			nombreCorto = new JTextField(editPantalla.getNombreCorto());
			numeroPant = new JTextField(Integer.toString(editPantalla.getNumPant()));
			descPant = new JTextField(editPantalla.getDescPant());
			listaCampos = editPantalla.getLista();
			listaTeclas = editPantalla.getTeclas();
		}
		
		if(listaNombreEst == null) {
			listaNombreEstructuras = new ArrayList<String>();
		} else {
			listaNombreEstructuras = listaNombreEst;
		}
		
		if(listaCodPantalla == null) {
			listaCodigoPantallas = new ArrayList<codigoPantalla>();
		} else {
			listaCodigoPantallas = listaCodPantalla;
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 445);
		general = new JPanel();
		general.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(general);
		general.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		general.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Nombre de la pantalla:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel, BorderLayout.NORTH);
		
		nombre.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(nombre, BorderLayout.CENTER);
		nombre.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		general.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		//PANEL DE CAMPOS -- INICIO
		JPanel panel_campos = new JPanel();
		panel_campos.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_campos.setBounds(10, 48, 524, 181);
		panel_1.add(panel_campos);
		panel_campos.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_campos.add(scrollPane);
		
		tablaCampos = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {true, true, true, true};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
			
			
			
			Class[] tipos = new Class[]{java.lang.Object.class,java.lang.Object.class,java.lang.Boolean.class,java.lang.Boolean.class};
			public Class getColumnClass(int column) {
				return tipos[column];
			}
		};
		tablaCampos.setModel(modelo);
		tablaCampos.setEnabled(true);
		
		modelo.addColumn("Nombre");
		modelo.addColumn("Tipo");
		modelo.addColumn("IN");
		modelo.addColumn("OUT");
		
		if (editPantalla != null) {
			for (int i=0; i< listaCampos.size(); i++) {
				campos campo = listaCampos.get(i);
				modelo.addRow(new Object[]{campo.getNombre(),campo.getTipo(),campo.getIn(),campo.getOut()});
			}
		}
		
		tablaCampos.getColumnModel().getColumn(0).setMaxWidth(250);
		tablaCampos.getColumnModel().getColumn(1).setMaxWidth(250);
		tablaCampos.getColumnModel().getColumn(2).setMaxWidth(50);
		tablaCampos.getColumnModel().getColumn(3).setMaxWidth(50);
		
		TableColumn sportColumn = tablaCampos.getColumnModel().getColumn(1);
		ArrayList<String> intermedio = new ArrayList<>(Arrays.asList(
                "PACK_TRF.RF_PANINFO",
                "PACK_TRF.RF_LOGIN",
                "PACK_TRF.RF_ERROR",
                "PACK_TRF.PAN_COM4"
                ));
		try{
			intermedio.addAll(listaNombreEstructuras);
		} catch(NullPointerException e) {
			
		}
		String[] comunes = new String[intermedio.size()];
		comunes = intermedio.toArray(comunes);
		
		JComboBox comboBox = new JComboBox(comunes);
		comboBox.setEditable(true);
		comboBox.addActionListener(this);
		AutoCompleteDecorator.decorate(comboBox);
		sportColumn.setCellEditor(new ComboBoxCellEditor(comboBox));
			
		scrollPane.setViewportView(tablaCampos);
		
		JPanel panel_3 = new JPanel();
		panel_campos.add(panel_3, BorderLayout.SOUTH);
		
		btnAnadir = new JButton("A\u00F1adir");
		btnAnadir.addActionListener(this);
		panel_3.add(btnAnadir);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		panel_3.add(btnEliminar);
		//PANEL DE CAMPOS -- FIN
		
		//PANEL DE TECLAS -- INICIO
		JPanel panel_teclas = new JPanel();
		panel_teclas.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_teclas.setBounds(10, 234, 524, 91);
		panel_1.add(panel_teclas);
		panel_teclas.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Indicar teclas a pulsar en pantalla:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(161, 6, 198, 21);
		panel_teclas.add(lblNewLabel_1);
		
		JCheckBox chkEnter = new JCheckBox("Enter");
		chkEnter.setBounds(231, 26, 61, 23);
		chkEnter.setSelected(true);
		panel_teclas.add(chkEnter);
		listaChk[0]= chkEnter;
		JCheckBox chkF1 = new JCheckBox("F1");
		chkF1.setBounds(26, 45, 43, 23);
		panel_teclas.add(chkF1);
		listaChk[1]= chkF1;
		JCheckBox chkF2 = new JCheckBox("F2");
		chkF2.setBounds(111, 45, 43, 23);
		panel_teclas.add(chkF2);
		listaChk[2]= chkF2;
		JCheckBox chkF3 = new JCheckBox("F3");
		chkF3.setBounds(196, 45, 43, 23);
		chkF3.setSelected(true);
		panel_teclas.add(chkF3);
		listaChk[3]= chkF3;
		JCheckBox chkF4 = new JCheckBox("F4");
		chkF4.setBounds(281, 45, 43, 23);
		panel_teclas.add(chkF4);
		listaChk[4]= chkF4;
		JCheckBox chkF5 = new JCheckBox("F5");
		chkF5.setBounds(366, 45, 43, 23);
		panel_teclas.add(chkF5);
		listaChk[5]= chkF5;
		JCheckBox chkF6 = new JCheckBox("F6");
		chkF6.setBounds(451, 45, 43, 23);
		panel_teclas.add(chkF6);
		listaChk[6]= chkF6;
		JCheckBox chkF7 = new JCheckBox("F7");
		chkF7.setBounds(26, 64, 43, 23);
		panel_teclas.add(chkF7);
		listaChk[7]= chkF7;
		JCheckBox chkF8 = new JCheckBox("F8");
		chkF8.setBounds(111, 64, 43, 23);
		panel_teclas.add(chkF8);
		listaChk[8]= chkF8;
		JCheckBox chkF9 = new JCheckBox("F9");
		chkF9.setBounds(196, 64, 43, 23);
		panel_teclas.add(chkF9);
		listaChk[9]= chkF9;
		JCheckBox chkF10 = new JCheckBox("F10");
		chkF10.setBounds(281, 64, 51, 23);
		panel_teclas.add(chkF10);
		listaChk[10]= chkF10;
		JCheckBox chkF11 = new JCheckBox("F11");
		chkF11.setBounds(366, 64, 51, 23);
		panel_teclas.add(chkF11);
		listaChk[11]= chkF11;
		JCheckBox chkF12 = new JCheckBox("F12");
		chkF12.setBounds(451, 64, 51, 23);
		panel_teclas.add(chkF12);
		listaChk[12]= chkF12;
		
		if (editPantalla != null) {
			for(int j=0; j < listaTeclas.length; j++) {
				listaChk[j].setSelected(listaTeclas[j]);
			}
		}
		//PANEL DE TECLAS -- FIN
		
		JLabel lblNomcort = new JLabel("Nombre corto de la pantalla:");
		lblNomcort.setBounds(40, 7, 163, 14);
		panel_1.add(lblNomcort);
		nombreCorto.setBounds(204, 4, 86, 20);
		nombreCorto.addKeyListener(new KeyAdapter() {
	         public void keyPressed(KeyEvent ke) {
	        	 int l = nombreCorto.getText().length();
	        	 if (ke.getKeyChar() == ' '){
	        		 nombreCorto.setEditable(false);
	        		 JOptionPane.showMessageDialog(null, "No se admiten espacios");
		         } else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE || ke.getKeyCode() == KeyEvent.VK_DELETE){
		        	 nombreCorto.setEditable(true);
		         } else if (l>=10) {
		        	 nombreCorto.setEditable(false);
	        		 JOptionPane.showMessageDialog(null, "Limitado a 10 letras/numeros");
		         }
	         }
		});
		panel_1.add(nombreCorto);
		
		JLabel lblNumeroPant = new JLabel("Numero de pantalla:");
		lblNumeroPant.setBounds(300, 7, 120, 14);
		panel_1.add(lblNumeroPant);
		numeroPant.setBounds(418, 4, 35, 20);
		
		/**Basada en pagina web 
		 * How can we make JTextField accept only numbers in Java?: https://www.tutorialspoint.com/how-can-we-make-jtextfield-accept-only-numbers-in-java
		 * Modificado para recoger numero limitado a teclas y a 3 digitos
		**/
		numeroPant.addKeyListener(new KeyAdapter() {
	         public void keyPressed(KeyEvent ke) {
	             String value = numeroPant.getText();
	             int l = value.length();
	             if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' && l<3)|| ke.getKeyCode() == KeyEvent.VK_BACK_SPACE || ke.getKeyCode() == KeyEvent.VK_DELETE) {
	            	numeroPant.setEditable(true);
	             } else if(l>=3){
	            	numeroPant.setEditable(false);
		            JOptionPane.showMessageDialog(null, "Solo se puede a�adir numero de 3 digitos");
	             } else {
	            	numeroPant.setEditable(false);
	            	JOptionPane.showMessageDialog(null, "Introduzca solo numeros (0-9)");
	             }
	         }
	    });
		
		numeroPant.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            	//NADA
            }

            @Override
            public void focusLost(FocusEvent e) {
            	if(!numeroPant.getText().trim().equals("")) {
            		String descripcionCod="";
            		int numero = Integer.parseInt(numeroPant.getText());
            		for(int v=0; v<listaCodigoPantallas.size();v++) {
            			if(numero == listaCodigoPantallas.get(v).getNumPant()) {
            				descripcionCod= listaCodigoPantallas.get(v).getDescPant();
            			}
            		}
            		
            		if(!descripcionCod.equals("")) 
            		{
            			descPant.setText(descripcionCod);
            			descPant.setEditable(false);
            		} else {
            			descPant.setText("");
            			descPant.setEditable(true);
            		}
            	}
            }
        });
		panel_1.add(numeroPant);
		
		JLabel lbldesc = new JLabel("A\u00F1adir descripci\u00F3n a la pantalla:");
		lbldesc.setBounds(40, 28, 185, 14);
		panel_1.add(lbldesc);
		descPant.setBounds(225, 25, 285, 20);
		descPant.addKeyListener(new KeyAdapter() {
	         public void keyPressed(KeyEvent ke) {
	        	 int l = descPant.getText().length();
	        	 if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE || ke.getKeyCode() == KeyEvent.VK_DELETE){
		        	 descPant.setEditable(true);
		         } else if (l>=40) {
		        	 descPant.setEditable(false);
	        		 JOptionPane.showMessageDialog(null, "Limitado a 40 letras/numeros");
		         }
	         }
		});
		panel_1.add(descPant);
		
		JPanel panel_2 = new JPanel();
		general.add(panel_2, BorderLayout.SOUTH);
		
		btnFin = new JButton("Terminar Pantalla");
		btnFin.addActionListener(this);
		panel_2.add(btnFin);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnFin) 
		{
			if (nombre.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Introduzca nombre a la pantalla");
				System.out.println("Introduzca nombre a la pantalla");
			}
			else if (nombreCorto.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Introduzca nombre corto a la pantalla, maximo 9 caracteres");
				System.out.println("Introduzca nombre corto a la pantalla, maximo 9 caracteres");
			}
			else if(numeroPant.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Introduzca numero a la pantalla");
				System.out.println("Introduzca numero a la pantalla");
			}
			else if(descPant.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Introduzca descripci�n a la pantalla");
				System.out.println("Introduzca descripci�n a la pantalla");
			}
			else if (tablaCampos.getRowCount()<= 0)
			{
				JOptionPane.showMessageDialog(null, "La pantalla necesita campos");
				System.out.println("La pantalla necesita campos");
			}
			else
			{
				nomPant = nombre.getText();
				nomCortoPant = nombreCorto.getText();
				numPant = Integer.parseInt(numeroPant.getText());
				descripcion = descPant.getText();
				String problema="";
				
				listaCampos = new ArrayList<campos>();
				for (int i = 0; i < tablaCampos.getRowCount(); i++) {
					if(problema.equals("")) {
						System.out.println("---"+ i +"---");
						try {
							System.out.println("Nombre:" + tablaCampos.getValueAt(i,0).toString());
							try {
								System.out.println("Tipo:" + tablaCampos.getValueAt(i,1).toString());
							} catch(NullPointerException ie) {
								problema = "Se debe asignar tipo a la fila " + i;
							}
						} catch(NullPointerException ie) {
							problema = "Se debe asignar nombre a la fila " + i;
						}
						
						if(problema.equals("")) {
							System.out.println("in:" + tablaCampos.getValueAt(i,2).toString());
							System.out.println("out:" + tablaCampos.getValueAt(i,3).toString());
							campos nuevoCampo = new campos(tablaCampos.getValueAt(i,0).toString(),tablaCampos.getValueAt(i,1).toString(),new Boolean(tablaCampos.getValueAt(i,2).toString()),new Boolean(tablaCampos.getValueAt(i,3).toString()));
							listaCampos.add(nuevoCampo);
						}
					}
					System.out.println("------");
				}
				
				if(problema.equals("")) {
					for(int j=0; j < listaTeclas.length; j++) {
						System.out.println("Tecla: " + j + ", estado: "+listaChk[j].isSelected());
						listaTeclas[j] = listaChk[j].isSelected();
					}
					System.out.println("------");
					System.out.println("Fin de la pantalla: " + nomPant + ", nombre corto: " + nomCortoPant);
					System.out.println("            Codigo: " + numPant + ", descripcion: " + descripcion);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, problema);
					System.out.println(problema);
				}
			}
		}
		else if (e.getSource()==btnAnadir) 
		{
			modelo.addRow(new Object[]{null,null,false,false});
		}
		else if (e.getSource()==btnEliminar)
		{
			if(tablaCampos.getSelectedRow()>=0){
				if(tablaCampos.isEditing()) {
					tablaCampos.getCellEditor().stopCellEditing();
				}
				System.out.println("Eliminar: " + tablaCampos.getSelectedRow());
				int filaSeleccionada = tablaCampos.getSelectedRow();
				modelo.removeRow(filaSeleccionada);
				System.out.println("Cantidad: " + modelo.getRowCount());
			}
		}
	}
	
	private JFrame getFrame(){
	    return this;
	}
	
	public pantalla getPantalla(){
	    return new pantalla(nomPant,nomCortoPant,listaCampos,listaTeclas,numPant,descripcion);
	}
}