package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.estructura;
import Modelo.variable;

public class infoEstructura extends JFrame {

	private JPanel contentPane;
	
	private ArrayList<estructura> listaEstructuras;
	private ArrayList<String> listaNombreEstructuras;
	private String seleccion;
	
	private JTable tablaVariables;
	private DefaultTableModel modelo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					infoEstructura frame = new infoEstructura(null,null,"prueba");
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
	public infoEstructura(String nomenclatura, ArrayList<estructura> listaNombreEst, String seleccion) {
		if(listaNombreEst == null) {
			listaEstructuras = new ArrayList<estructura>();
			listaNombreEstructuras = new ArrayList<String>();
		} else {
			listaEstructuras = listaNombreEst;
			listaNombreEstructuras = new ArrayList<String>();
			for (int i=0; i<listaNombreEst.size(); i++)
	        {
	        	estructura estructura = listaNombreEst.get(i);
	        	
	        	listaNombreEstructuras.add("PACK_" + nomenclatura + ".PAN_" + estructura.getNombre());
	        }
		}
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Estructuras");
		setBounds(100, 100, 450, 324);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
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
		
		JLabel lblNewLabel = new JLabel("Seleccione estructura:");
		panel.add(lblNewLabel);
		
		JComboBox tipoCampos = new JComboBox(comunes);
		tipoCampos.setEditable(true);
		
		JPanel info = new JPanel();
		contentPane.add(info, BorderLayout.CENTER);
		info.setLayout(null);
		
		JLabel lblPaninfo = new JLabel("<html>Guarda informacion acerca de la pantalla:"
										+ "<ul>\n"
										+ "<li>Codigo Modelo\n"
						                + "<li>Codigo Pantalla\n"
						                + "<li>Tecla pulsada\n"
						                + "<li>Codigo Modulo anterior\n"
						                + "<li>Codigo Pantalla anterior\n"
						                + "</ul>\n"
						              +"</html>\r\n");
		lblPaninfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPaninfo.setVerticalAlignment(SwingConstants.TOP);
		lblPaninfo.setBounds(10, 11, 404, 199);
		info.add(lblPaninfo);
		lblPaninfo.setVisible(false);
		
		JLabel lblLogin = new JLabel("<html>Guarda informacion acerca de los parametros de entrada a la aplicación:"
										+ "<ul>\n"
										+ "<li>Operario\n"
						                + "<li>Terminal\n"
						                + "<li>Instalacion\n"
						                + "<li>Almacen\n"
						                + "<li>Idioma\n"
						                + "</ul>\n"
						            +"</html>\r\n");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setVerticalAlignment(SwingConstants.TOP);
		lblLogin.setBounds(10, 11, 404, 199);
		info.add(lblLogin);
		lblLogin.setVisible(false);
		
		JLabel lblError = new JLabel("<html>Guarda informacion acerca de los errores:"
										+ "<ul>\n"
										+ "<li>Codigo de error\n"
						                + "<li>Mensaje\n"
						                + "<li>Literal\n"
						                + "<li>Mensaje log\n"
						                + "</ul>\n"
	              					+"</html>\r\n");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setVerticalAlignment(SwingConstants.TOP);
		lblError.setBounds(10, 11, 404, 199);
		info.add(lblError);
		lblError.setVisible(false);
		
		JLabel lblCom4 = new JLabel("<html>Guarda informacion acerca del envio de logs."
						           +"</html>\r\n");
		lblCom4.setHorizontalAlignment(SwingConstants.CENTER);
		lblCom4.setVerticalAlignment(SwingConstants.TOP);
		lblCom4.setBounds(10, 11, 404, 199);
		info.add(lblCom4);
		lblCom4.setVisible(false);
		
		JPanel panel_lista = new JPanel();
		panel_lista.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_lista.setBounds(10, 11, 404, 199);
		info.add(panel_lista);
		panel_lista.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_lista.add(scrollPane);
		
		tablaVariables = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		};
		tablaVariables.setModel(modelo);
		tablaVariables.setEnabled(true);
		
		modelo.addColumn("Nombre");
		modelo.addColumn("Tipo");
		
		scrollPane.setViewportView(tablaVariables);
		
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBounds(168, 214, 89, 23);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		info.add(btnCerrar);
		panel_lista.setVisible(false);
		
		tipoCampos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblPaninfo.setVisible(false);
				lblLogin.setVisible(false);
				lblError.setVisible(false);
				lblCom4.setVisible(false);
				panel_lista.setVisible(false);
				modelo.setRowCount(0);
				if(tipoCampos.getSelectedItem().equals("PACK_TRF.RF_PANINFO")) {
					lblPaninfo.setVisible(true);
				} else if(tipoCampos.getSelectedItem().equals("PACK_TRF.RF_LOGIN")) {
					lblLogin.setVisible(true);
				} else if(tipoCampos.getSelectedItem().equals("PACK_TRF.RF_ERROR")) {
					lblError.setVisible(true);
				} else if(tipoCampos.getSelectedItem().equals("PACK_TRF.PAN_COM4")) {
					lblCom4.setVisible(true);
				} else {
					/**Basada en pregunta de stackoverflow 
					 * PREGUNTA: ¿Cómo extraer parte de una cadena según un patrón?: https://es.stackoverflow.com/questions/123704/c%C3%B3mo-extraer-parte-de-una-cadena-seg%C3%BAn-un-patr%C3%B3n
					 * RESPUESTA: por Mariano (https://ideone.com/9BUIdQ)
					 * Modificado para el patron de nomenclatura + estructura
					**/
					String campo = tipoCampos.toString();
					Pattern pattern = Pattern.compile("PACK_([a-zA-Z0-9]+).PAN_([a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(campo);
					
					//Comprobamos patron para recoger el nombre de la estructura
					if (matcher.find()) {
					    //Coincidió => obtener el valor del grupo 2, referencia a la estructura.
					    for (int i=0; i<listaNombreEst.size(); i++)
				        {
				        	System.out.println("I: "+ i + ", nombre-> "+listaNombreEst.get(i).getNombre());
				        	if(listaNombreEst.get(i).getNombre().equals(matcher.group(2))) 
				        	{
				        		System.out.println("I: "+ i + ", cant-> "+listaNombreEst.get(i).getCantVariables());
				        		estructura est = listaNombreEst.get(i);
				        		
				        		for(int k=0; k<est.getCantVariables();k++)
				        		{
				        			System.out.println("K: "+ k + ", nombre-> "+est.getVariable(k).getNombre() + ", tipo-> " + est.getVariable(k).getTipo());
				        			//Recogemos 
					                variable variable = est.getVariable(k);
					                modelo.addRow(new Object[]{variable.getNombre(),variable.getTipo()});
				        		}
				        	}
				        }
					} else {
					    //No coincidió
					    System.out.println("No valido");
					}
					panel_lista.setVisible(true);
				}
			}
		});
		panel.add(tipoCampos);
		
		if(seleccion.equals("PACK_TRF.RF_PANINFO") || seleccion.equals("PACK_TRF.RF_LOGIN") || seleccion.equals("PACK_TRF.RF_ERROR") || seleccion.equals("PACK_TRF.PAN_COM4"))
		{
			tipoCampos.setSelectedItem(seleccion);
		}
		else if (seleccion !=null)
		{
			
			
			tipoCampos.setSelectedItem(seleccion);
		}
	}
}
