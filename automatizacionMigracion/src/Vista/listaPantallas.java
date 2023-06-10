package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.codigoPantalla;
import Modelo.estructura;
import Modelo.pantalla;
import Modelo.Accion.celdaAccion;
import Modelo.Accion.editorAccion;
import Modelo.Accion.eventosAccion;

public class listaPantallas extends JFrame implements ActionListener{

	private JPanel general;
	private JButton btnNuevaPantalla;
	public JButton btnGenerar;
	private JScrollPane scrollPane;
	private JTable tablaVariablesPantallas;
	private DefaultTableModel modelo;
	
	private ArrayList<pantalla> listaPantallas;
	private ArrayList<estructura> listaEstructuras;
	private ArrayList<codigoPantalla> listaCodigosPantalla;
	private String nomenclatura;
	private JPanel panel_generar;
	private JPanel panel_opciones;
	public JButton btnEstructura;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArrayList<estructura> listaNombreEst=new ArrayList<estructura>();
					String nomenclatura = "PRUE";
					listaPantallas frame = new listaPantallas(nomenclatura,listaNombreEst, null, null);
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
	public listaPantallas(String nomenclatura,ArrayList<estructura> listaNombreEst, ArrayList<pantalla> listaPant, ArrayList<codigoPantalla> listaCodPant) {
		System.out.println("***Inicio inicializar listaPantallas***");
		listaPantallas = new ArrayList<pantalla>();
		this.nomenclatura=nomenclatura;
		
		if(listaNombreEst.size()==0) {
			listaEstructuras = new ArrayList<estructura>();
		} else {
			listaEstructuras = listaNombreEst;
		}
		
		if(listaCodPant == null) {
			listaCodigosPantalla = new ArrayList<codigoPantalla>();
		} else {
			listaCodigosPantalla = listaCodPant;
		}
		
		setTitle("Lista Pantallas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		general = new JPanel();
		general.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(general);
		general.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		general.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		panel_generar = new JPanel();
		panel.add(panel_generar, BorderLayout.NORTH);
		
		btnNuevaPantalla = new JButton("Nueva pantalla");
		panel_generar.add(btnNuevaPantalla);
		btnNuevaPantalla.addActionListener(this);
		
		panel_opciones = new JPanel();
		panel.add(panel_opciones, BorderLayout.SOUTH);
		
		btnEstructura = new JButton("Estructuras");
		panel_opciones.add(btnEstructura);
		
		lblNewLabel = new JLabel("          ");
		panel_opciones.add(lblNewLabel);
		
		btnGenerar = new JButton("Finalizar");
		panel_opciones.add(btnGenerar);
		
		lblNewLabel_1 = new JLabel("     ");
		panel_opciones.add(lblNewLabel_1);
		btnGenerar.addActionListener(this);
		
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
		
		if(listaPant!=null){
			listaPantallas=listaPant;
			actualizarNomenclaturaCampos();
			for(int tam=0; tam<listaPantallas.size();tam++)
			{
				modelo.addRow(new Object[]{listaPantallas.get(tam).getNombre(),listaPantallas.get(tam).getCodigoPantalla()});
			}
		}
		
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
				pantallaNuevaPantalla minuevaPantalla= new pantallaNuevaPantalla(nomenclatura,listaPantallas.get(fila), listaEstructuras,listaCodigosPantalla){
				            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
				            @Override
				            public void dispose(){
				                //Hacemos visible la pantalla de la estructura
				                getFrame().setVisible(true);
				                
				                //Cerramos la nueva pantalla
				                super.dispose();
				                
				                //Recogemos 
				                pantalla nuevaPantalla = super.getPantalla();
				                System.out.println("pantalla. Nombre: " + nuevaPantalla.getNombre() + ", Numero: " + nuevaPantalla.getCodigoPantalla());
				                modelo.setValueAt(nuevaPantalla.getNombre(), fila, 0);
				                modelo.setValueAt(nuevaPantalla.getCodigoPantalla(), fila, 1);
				                int numero = listaPantallas.get(fila).getCodigoPantalla();
				                listaPantallas.set(fila, nuevaPantalla);
				                System.out.println("Tamaño de la lista de pantallas: " + listaPantallas.size());
				                listaCodigosPantalla = super.getListaCodPant();
				                if(numero != nuevaPantalla.getCodigoPantalla()) {
				                	int indice = -1;
				                	//Comprobar que existe otra pantalla con ese codigo
					                for(int k=0; k< listaPantallas.size();k++) {
					                	if(numero==listaPantallas.get(k).getCodigoPantalla()) {
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
					        			listaCodigosPantalla.add(super.getCodPant());
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
			
			pantallaNuevaPantalla minuevaPantalla= new pantallaNuevaPantalla(nomenclatura,null, listaEstructuras,listaCodigosPantalla){
			            //Con esto cuando llamemos a dispose de la nueva pantalla abrimos la de la estructura
			            @Override
			            public void dispose(){
			                //Hacemos visible la pantalla de la estructura
			                getFrame().setVisible(true);
			                
			                //Cerramos la nueva pantalla
			                super.dispose();
			                
			                //Recogemos 
			                pantalla nuevaPantalla = super.getPantalla();
			                System.out.println("pantalla. Nombre:" + nuevaPantalla.getNombre()+ ", Numero: " + nuevaPantalla.getCodigoPantalla());
			                modelo.addRow(new Object[]{nuevaPantalla.getNombre(),nuevaPantalla.getCodigoPantalla()});
			                listaPantallas.add(nuevaPantalla);
			                System.out.println(listaPantallas.size());
			                
			                listaCodigosPantalla = super.getListaCodPant();
			                int indice = -1;	
			                for(int num=0;num<listaCodigosPantalla.size();num++)
			                {
			                	if(nuevaPantalla.getCodigoPantalla()==listaCodigosPantalla.get(num).getNumPant()) {
			                		indice=num;
			                	}
			                }
			                
			        		if (indice != -1) {
			        			System.out.println("La pantalla "+ nuevaPantalla.getCodigoPantalla() + " está en el índice " + indice);
			        		} else {
			        			listaCodigosPantalla.add(super.getCodPant());
			                }
			            }
			        };

			//Hacemos visible la nueva pantalla
			minuevaPantalla.setVisible(true);
			//Cerramos la principal
			getFrame().setVisible(false);
		}		
	}
	
	private void actualizarNomenclaturaCampos() {
		for(int tamano=0; tamano<listaPantallas.size();tamano++) {
			listaPantallas.get(tamano).actualizarNomenclatura(nomenclatura);
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
	
	public boolean comprobarGenerar() {
		if(listaPantallas.size()==0) {
			JOptionPane.showMessageDialog(null, "Introduzca una estructura como minimo","Incorrecto", JOptionPane.ERROR_MESSAGE);
			System.out.println("ERROR: ListaEstructura vacia");
			return false;
		} else{
			ArrayList<String> listaNombreEstructura = new ArrayList<String>();
			for(int le=0;le<listaEstructuras.size();le++)
			{
				listaNombreEstructura.add(listaEstructuras.get(le).getNombre());
			}
			
			String mensajeError = "Campos de pantallas vacios:\n";
			for(int lp=0; lp<listaPantallas.size(); lp++) {
				pantalla pantallaAComprobar = listaPantallas.get(lp);
				
				int contCamposVacios =0;
				String mensajeCampo = "";
				for(int cp=0; cp<pantallaAComprobar.getCantCampos();cp++)
				{
					String campo = pantallaAComprobar.getCampo(cp).getTipo();
					Pattern pattern = Pattern.compile("PACK_([a-zA-Z0-9]+).PAN_([a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(campo);
						
					//Comprobamos patron para recoger el nombre de la estructura
					if (matcher.find()) 
					{
						if(!listaNombreEstructura.contains(matcher.group(2)) && (matcher.group(1) != "TRF"))
						{
							if(contCamposVacios==0)
							{
								mensajeCampo = mensajeCampo + pantallaAComprobar.getCampo(cp).getNombre();
							}
							else
							{
								mensajeCampo = mensajeCampo +", "+ pantallaAComprobar.getCampo(cp).getNombre();
							}
							contCamposVacios++;
						};
					}
				}
				
				if(!mensajeCampo.isEmpty())
				{
					mensajeError = mensajeError + "  - Pantalla " + pantallaAComprobar.getNombre() + ": " + mensajeCampo + "\n";
				}
			}
			
			if(mensajeError.equals("Campos de pantallas vacios:\n"))
			{
				return true;
			} 
			else
			{
				JOptionPane.showMessageDialog(null, mensajeError,"Incorrecto", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			//generador gen =new generador(new ficheros(nomenclatura, codigo, listaEstructuras));
		}
	}
}
