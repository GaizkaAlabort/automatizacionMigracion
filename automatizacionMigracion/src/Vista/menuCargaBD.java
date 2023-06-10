package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import Modelo.tabla;
import Modelo.columna;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;

public class menuCargaBD extends JFrame {

	private JPanel contentPane;
	private final JComboBox listaArchivos = new JComboBox();
	
	private HashMap<String, tabla> nombresTablas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menuCargaBD frame = new menuCargaBD();
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
	public menuCargaBD() {
		setTitle("Cargar Base de Datos");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 328, 161);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		File carpeta = new File("infoTablasBBDD");
		String[] listado = carpeta.list();
		if (listado == null || listado.length == 0) {
		    System.out.println("No hay elementos dentro de la carpeta");
		    dispose();
		}
		else {
		    for (int i=0; i< listado.length; i++) {
		        System.out.println(listado[i]);
		    }
		}

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JComboBox listaArchivos = new JComboBox(listado);
		listaArchivos.setEditable(false);
		listaArchivos.setBounds(59, 47, 196, 19);
		contentPane.add(listaArchivos);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(10, 93, 87, 23);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnCancelar);
		
		JButton btnCargar = new JButton("Cargar");
		btnCargar.setBounds(222, 93, 80, 23);
		btnCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarArchivo(String.valueOf(listaArchivos.getSelectedItem()));
				dispose();
			}
		});
		contentPane.add(btnCargar);
		
		JLabel lblNewLabel = new JLabel("<html>Seleccione archivo con la informaci\u00F3n <br/> de las tablas a cargar:"
										+"</html>");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(25, 7, 260, 39);
		contentPane.add(lblNewLabel);
	}			
	
	private void cargarArchivo(String pNombreArchivo) {
	// TODO Auto-generated method stub
		String ubicacion = "infoTablasBBDD/" + pNombreArchivo;
		
		nombresTablas = new HashMap<>();
		
		try {
			BufferedReader lector = new BufferedReader(new FileReader(ubicacion));
			String[] fila;
			String linea;
			lector.readLine();
			while((linea = lector.readLine()) != null)
			{
				fila = linea.replaceAll("\"","").split(";");
				System.out.println(fila[0] +" "+fila[1]+" "+fila[2]+" "+fila[3] +" ");
				if (!nombresTablas.containsKey(fila[0])) {
		            nombresTablas.put(fila[0], new tabla(fila[0], new columna(fila[1],fila[2],Integer.parseInt(fila[3]))));
		        }
				else {
					tabla existe = nombresTablas.get(fila[0]);
					existe.añadirColumna (fila[1],fila[2],Integer.parseInt(fila[3]));
					nombresTablas.replace(fila[0],existe);
				}
			}
			lector.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, tabla> getInfoTablas(){
		return nombresTablas;
	}
}
