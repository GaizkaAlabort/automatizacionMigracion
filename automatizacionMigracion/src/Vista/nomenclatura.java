package Vista;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class nomenclatura extends JFrame{

	private JPanel contentPane;
	private JTextField nomenclatura;
	public JButton btnNewButton;
	private JLabel lblInfo;
	private JLabel lblInfo2;
	private JLabel lblCodigo;
	private JTextField codigo;
	
	private int cod;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					nomenclatura frame = new nomenclatura("prue",12345);
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
	public nomenclatura(String nombre, int codpet) {
		
		setTitle("Nueva Migración");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 166);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JLabel lblNomenclatura = new JLabel("Introduzca nomenclatura del modulo:");
		contentPane.add(lblNomenclatura, "cell 0 0,alignx center,aligny center");
		
		nomenclatura = new JTextField();
		contentPane.add(nomenclatura, "cell 0 1,alignx center,aligny center");
		nomenclatura.setColumns(10);
		
		btnNewButton = new JButton("Empezar");
		//btnNewButton.addActionListener((ActionListener) this);
		
		lblCodigo = new JLabel("Introduzca codigo de la solicitud:");
		contentPane.add(lblCodigo);
		
		codigo = new JTextField();
		codigo.setColumns(10);
		contentPane.add(codigo);
		contentPane.add(btnNewButton, "cell 0 2,alignx center,aligny center");
		
		lblInfo = new JLabel("La nomenclatura debe corresponder con el nombre a dar a la");
		lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 9));
		contentPane.add(lblInfo);
		
		lblInfo2 = new JLabel("pantalla, al escribir PRUE se creara los ficheros como PACK_PRUE");
		lblInfo2.setFont(new Font("Tahoma", Font.PLAIN, 9));
		contentPane.add(lblInfo2);
		
		if(nombre!=null && codpet!=-1)
		{
			nomenclatura.setText(nombre);
			setTitle("Editar nomenclatura o codigo");
			codigo.setText(String.valueOf(codpet));
			btnNewButton.setText("CONTINUAR");
		}
	}

	public boolean comprobarCampos() {
		if (nomenclatura.getText().isEmpty())
		{
			System.out.println("Falta nombrar al modulo.");
			JOptionPane.showMessageDialog(null, "Introduzca nomenclatura");
			return false;
		}
		else
		{
			try{
				if(codigo.getText().isEmpty()) {
					cod = -1;
					System.out.println("Falta introducir codigo peticion.");
					JOptionPane.showMessageDialog(null, "Introduzca codigo peticion");
					return false;
				} else {
					cod = Integer.parseInt(codigo.getText());
					dispose();
					return true;
				}
				//listaEstructuras lista1= new listaEstructuras(nomenclatura.getText(), cod);
				//Hacemos visible la nueva pantalla
				//lista1.setVisible(true);
				
				//Cerramos la principal
			} catch (NumberFormatException exception) {
				System.out.println("El codigo no es un numero.");
				return false;
			}			
		}
	}
	
	public String getNomenclatura() {
		return nomenclatura.getText();
	}
	
	public int getCodPeticion() {
		return cod;
	}
	
}
