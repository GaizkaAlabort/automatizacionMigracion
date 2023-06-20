package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class cargarGeneral extends JFrame {

	private JPanel contentPane;
	private JComboBox copiasSeguridad;
	private JCheckBox chkReciente;
	public JButton btnCargar;
	public JButton btnNueva;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cargarGeneral frame = new cargarGeneral();
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
	public cargarGeneral() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cargar copia");
		setBounds(100, 100, 337, 167);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		/*APARTADO OPCIONES*/
		JPanel panel_opciones = new JPanel();
		contentPane.add(panel_opciones, BorderLayout.CENTER);
		panel_opciones.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_combo = new JPanel();
		panel_opciones.add(panel_combo, BorderLayout.CENTER);
		
		File carpeta = new File("backups");
		
		ArrayList<String> nombresCarpetas = new ArrayList<String>();
		nombresCarpetas.add("Seleccionar");
		if(carpeta.exists()) {
			File[] files = carpeta.listFiles();
			//Recorrer carpetas
			for (File file : files) {
				if(file.isDirectory()) {
					nombresCarpetas.add(file.getName());
				}
			}
		}
		String[] comunes = new String[nombresCarpetas.size()];
		comunes = nombresCarpetas.toArray(comunes);
		
		copiasSeguridad = new JComboBox(comunes);
		panel_combo.add(copiasSeguridad);
		
		JPanel panel_chk = new JPanel();
		panel_opciones.add(panel_chk, BorderLayout.SOUTH);
		
		chkReciente = new JCheckBox("\u00BFRecoger mas reciente?");
		chkReciente.setSelected(true);
		panel_chk.add(chkReciente);
		
		JLabel lblNewLabel_2 = new JLabel("\u00BFDesea cargar json con migraci\u00F3n?");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_opciones.add(lblNewLabel_2, BorderLayout.NORTH);
		
		/*APARTADO BOTONES*/
		JPanel panel_botones = new JPanel();
		contentPane.add(panel_botones, BorderLayout.SOUTH);
		
		btnCargar = new JButton("Cargar");
		/*btnCargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String seleccion = comprobarVariasCopias();
				if(seleccion != null)
				{
					dispose();
				};
			}
		});*/
		panel_botones.add(btnCargar);
		
		btnNueva = new JButton("Nueva migraci\u00F3n");
		panel_botones.add(btnNueva);
		
		//IMAGEN
		Icon errorIcon = UIManager.getIcon("OptionPane.questionIcon");
		JLabel foto = new JLabel(errorIcon);
		
		JPanel panel_imagen = new JPanel();
		contentPane.add(panel_imagen, BorderLayout.WEST);
		
		panel_imagen.add(foto);
	}
	
	public String comprobarVariasCopias() {
		if(!copiasSeguridad.getSelectedItem().equals("Seleccionar")) {
			File carpeta = new File("backups/" + copiasSeguridad.getSelectedItem());
			String ubiArchivo = "backups/" + copiasSeguridad.getSelectedItem() + "/";
			
			File[] files = carpeta.listFiles();
			
			if(files.length > 1) {
				
				if(chkReciente.isSelected()) {
					Arrays.sort(files);
					System.out.println("ARCHIVO MAS RECIENTE");
					return ubiArchivo + files[files.length-1].getName();
				} else {
					ArrayList<String> nombreJSON = new ArrayList<String>();
					nombreJSON.add("Seleccionar");
					for (File file : files) {
						nombreJSON.add(file.getName());
					}
					String[] comunes = new String[nombreJSON.size()];
					comunes = nombreJSON.toArray(comunes);
					
					String seleccion = "Seleccionar";
					while(seleccion != null && seleccion.equals("Seleccionar")) {
						Object color = JOptionPane.showInputDialog(null,"Seleccione un archivo por fecha",
							   "Fechas", JOptionPane.QUESTION_MESSAGE, null,
							   comunes,"Seleccionar");
						seleccion = (String) color;
					}
					
					System.out.println("ARCHIVO ELEGIDO");
					if(seleccion == null) {
						return null;
					} else {
						return ubiArchivo + seleccion;
					}
					
				}
			} else {
				System.out.println("ARCHIVO SOLITARIO");
				return ubiArchivo + files[0].getName();
			}
		} else {
			System.out.println("ARCHIVO SIN ELEGIR");
			return null;
		}
	}

}
