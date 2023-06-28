package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import Modelo.columna;
import Modelo.estructura;
import Modelo.tabla;
import Modelo.Accion.celdaAccion;
import Modelo.Accion.editorAccion;
import Modelo.Accion.eventosAccion;
import net.miginfocom.swing.MigLayout;

public class listaEstructuras extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable tablaVariablesEstructura;
	private DefaultTableModel modelo;
	private JButton btnNuevaEstructura;
	public JButton btnGenerar;
	
	private ArrayList<estructura> listaEstructuras;
	public JButton btnNomenclatura;
	private JButton btnCargarBD;
	private JButton btnCargarEst;
	
	private HashMap<String, tabla> infoTablas;
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
		
		setTitle("Lista Estructuras");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 330);
		setResizable(false);
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
				pantallaEstructura minuevaEstructura= new pantallaEstructura(listaEstructuras.get(fila),infoTablas){
				            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
				            @Override
				            public void dispose(){
				                //Hacemos visible la pantalla de la estructura
				                getFrame().setVisible(true);
				                
				                //Cerramos la nueva pantalla
				                super.dispose();
				                
				                if(this.forzado == false) {
				                	//Recogemos 
					                estructura nuevaEstructura = super.getEstructura();
					                System.out.println("pantallaEstructura. Nombre:" + nuevaEstructura.getNombre() + ", Variables: " + nuevaEstructura.getCantVariables());
					                modelo.setValueAt(nuevaEstructura.getNombre(), fila, 0);
					                listaEstructuras.set(fila, nuevaEstructura);
					                System.out.println(listaEstructuras.size());
				                }
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
		panel.setLayout(new BorderLayout(0, 0));
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel.add(panel_1, BorderLayout.NORTH);
		
		JPanel panel_1_Center = new JPanel();
		panel_1_Center.setLayout(new MigLayout("", "[279px]", "[5px:n][][5px:n,center]"));
		panel_1.add(panel_1_Center, BorderLayout.CENTER);
		
		JPanel panel_1_cargar = new JPanel();
		panel_1_cargar.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1_cargar.setLayout(new BorderLayout(0, 0));
		panel_1.add(panel_1_cargar, BorderLayout.EAST);
		
		btnNuevaEstructura = new JButton("Nueva estructura");
		btnNuevaEstructura.addActionListener(this);
		panel_1_Center.add(btnNuevaEstructura, "cell 0 1,alignx center,growy");
		
		JPanel panel_btn = new JPanel();
		panel_1_cargar.add(panel_btn, BorderLayout.CENTER);
		
		btnCargarBD = new JButton("BD");
		btnCargarBD.addActionListener(this);
		panel_btn.add(btnCargarBD);
		
		btnCargarEst = new JButton("Estructura");
		btnCargarEst.addActionListener(this);
		panel_btn.add(btnCargarEst);
		
		
		JPanel panel_lbl = new JPanel();
		panel_lbl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_1_cargar.add(panel_lbl, BorderLayout.NORTH);
		
		JLabel lblCargar = new JLabel("Cargar:");
		panel_lbl.add(lblCargar);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		
		btnNomenclatura = new JButton("Nomenclatura");
		btnNomenclatura.setIcon(new ImageIcon(listaPantallas.class.getResource("/imagenes/flecha-izq.png")));
		panel_2.add(btnNomenclatura);
		
		JLabel lblNewLabel = new JLabel("          ");
		panel_2.add(lblNewLabel);
		
		btnGenerar = new JButton("Pantallas");
		btnGenerar.setIcon(new ImageIcon(listaPantallas.class.getResource("/imagenes/flecha-dch.png")));
		btnGenerar.setHorizontalTextPosition(SwingConstants.LEFT);
		panel_2.add(btnGenerar);
		
		JLabel lblNewLabel_1 = new JLabel("      ");
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
			
			pantallaEstructura minuevaEstructura= new pantallaEstructura(null,infoTablas){
			            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
			            @Override
			            public void dispose(){
			                //Hacemos visible la pantalla de la estructura
			                getFrame().setVisible(true);
			                
			                //Cerramos la nueva pantalla
			                super.dispose();
			                
			                if(this.forzado == false) {
				                //Recogemos 
				                estructura nuevaEstructura = super.getEstructura();
				                System.out.println("pantallaEstructura. Nombre:" + nuevaEstructura.getNombre() + ", Variables: " + nuevaEstructura.getCantVariables());
				                modelo.addRow(new Object[]{nuevaEstructura.getNombre()});
				                listaEstructuras.add(nuevaEstructura);
				                System.out.println(listaEstructuras.size());
			                }
			            }
			        };

			//Hacemos visible la nueva pantalla
			minuevaEstructura.setVisible(true);
			//Cerramos la principal
			getFrame().setVisible(false);
		}
		else if(e.getSource()==btnCargarBD) 
		{
			File file = new File("infoTablasBBDD");
			
			if(file.exists()) {
				
				String[] listado = file.list(new FilenameFilter() {
				    @Override
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".csv");
				    }
				});
				
				if (listado == null || listado.length == 0) {
				    
					System.out.println("No hay elementos dentro de la carpeta");
				    JOptionPane.showMessageDialog(null, "No hay elementos dentro de la carpeta","Faltan archivos", JOptionPane.ERROR_MESSAGE);
					btnCargarBD.setEnabled(false);
					
				} else {
				    
				    Object nomGen = JOptionPane.showInputDialog(null,"Seleccione archivo a cargar:",
							"Cargar base de datos", JOptionPane.QUESTION_MESSAGE, null,
							listado,"Seleccionar");
				    
				    if(nomGen != null) {
				    	
				    	String ubicacion = "infoTablasBBDD/" + nomGen;
					
				    	infoTablas = new HashMap<>();
						
						try {
							
							BufferedReader lector = new BufferedReader(new FileReader(ubicacion));
							String[] fila;
							String linea;
							lector.readLine();
							
							while((linea = lector.readLine()) != null)
							{
								fila = linea.replaceAll("\"","").split(";");
								if (!infoTablas.containsKey(fila[0])) {
									infoTablas.put(fila[0], new tabla(fila[0], new columna(fila[1],fila[2],Integer.parseInt(fila[3]))));
						        }
								else {
									tabla existe = infoTablas.get(fila[0]);
									existe.añadirColumna (fila[1],fila[2],Integer.parseInt(fila[3]));
									infoTablas.replace(fila[0],existe);
								}
							}
							
							lector.close();
							System.out.println("BD cargada de " + infoTablas.size() + " tablas.");
							
						} catch (Exception ne) {
							ne.printStackTrace();
						}
				    }
				}
				
				/*menuCargaBD cargarMenu = new menuCargaBD() {
					@Override
		            public void dispose(){
						super.dispose();
						
						infoTablas = super.getInfoTablas();
						if(infoTablas != null) {
							System.out.println("BD cargada de " + infoTablas.size() + " tablas.");
						}
					}
				};
				
				cargarMenu.setVisible(true);*/
			} else {
				JOptionPane.showMessageDialog(null, "No existe carpeta infoTablasBBDD con informacion de la base de datos","Falta carpeta", JOptionPane.ERROR_MESSAGE);
				btnCargarBD.setEnabled(false);
			}
		}
		else if(e.getSource()==btnCargarEst)
		{
			File carpeta = new File("backups");
			
			if(carpeta.exists()) {
				File archivo = new File("backups/estructuras.txt");
				
				if(archivo.exists()) {
					try {
						FileReader fr = new FileReader (archivo);
						BufferedReader br = new BufferedReader(fr);
						
						String[] listaNombres = br.readLine().split(";");
						
						Object nomEstr = JOptionPane.showInputDialog(null,"Cargar estructura",
												"Cargar estructura", JOptionPane.QUESTION_MESSAGE, null,
												listaNombres,"Seleccionar");
						String seleccion = (String) nomEstr;
						System.out.println(seleccion);
						
						if(seleccion != null) {
							String linea = "";
							Boolean fin = false;
							String JSON = "";
							while((linea=br.readLine())!=null && fin == false) {
					        	if(linea.equals(seleccion)) {
					        		JSON = br.readLine();
					        		fin = true;
					        	} else {
					        		br.readLine();
					        	}
					        	br.readLine();
					        }
							
							System.out.println(JSON);
							JSONObject json = new JSONObject(JSON);
							estructura nuevaEstructura =  new estructura(json);
							
							System.out.println("cargada estructura. Nombre:" + nuevaEstructura.getNombre() + ", Variables: " + nuevaEstructura.getCantVariables());
			                modelo.addRow(new Object[]{nuevaEstructura.getNombre()});
			                listaEstructuras.add(nuevaEstructura);
			                System.out.println(listaEstructuras.size());
						}
						
						br.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "No existe archivo de backup con informacion de estructuras","Falta archivo", JOptionPane.ERROR_MESSAGE);
					btnCargarEst.setEnabled(false);
				}
			} else {
				JOptionPane.showMessageDialog(null, "No existe carpeta backup con informacion de estructuras","Falta carpeta", JOptionPane.ERROR_MESSAGE);
				btnCargarEst.setEnabled(false);
			}
		}
	}
	
	private JFrame getFrame(){
	    return this;
	}
	
	public boolean comprobarGenerar() {
		if(listaEstructuras.size()==0) {
			JOptionPane.showMessageDialog(null, "Introduzca una estructura como minimo","Incorrecto", JOptionPane.ERROR_MESSAGE);
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
