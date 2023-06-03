package Vista;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

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
					nomenclatura frame = new nomenclatura();
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
	public nomenclatura() {
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
				} else {
					cod = Integer.parseInt(codigo.getText());
				}
				//listaEstructuras lista1= new listaEstructuras(nomenclatura.getText(), cod);
				//Hacemos visible la nueva pantalla
				//lista1.setVisible(true);
				
				//Cerramos la principal
				dispose();
				return true;
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
