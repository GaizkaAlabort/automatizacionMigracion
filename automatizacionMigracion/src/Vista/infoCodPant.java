package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Modelo.codigoPantalla;

public class infoCodPant extends JFrame {

	private JPanel contentPane;
	
	private DefaultTableModel modelo;
	public JButton btnCerrar;
	private JTable tablaCodPantalla;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					infoCodPant frame = new infoCodPant(null);
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
	public infoCodPant(ArrayList<codigoPantalla> listaCodPantalla) {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Codigos de pantalla");
		setBounds(100, 100, 450, 300);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCentral = new JPanel();
		panelCentral.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelCentral.add(scrollPane);
		
		tablaCodPantalla = new JTable();
		
		modelo=new DefaultTableModel(){
			boolean[] columnEditables = new boolean[] {
					false, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		modelo.addColumn("Codigo");
		modelo.addColumn("Descripción");
		
		if(listaCodPantalla!=null){
			for(int tam=0; tam<listaCodPantalla.size();tam++)
			{
				modelo.addRow(new Object[]{listaCodPantalla.get(tam).getNumPant(),listaCodPantalla.get(tam).getDescPant()});
			}
		}
		
		tablaCodPantalla.setModel(modelo);
		tablaCodPantalla.getColumnModel().getColumn(0).setPreferredWidth(75);
		tablaCodPantalla.getColumnModel().getColumn(1).setPreferredWidth(350);
		scrollPane.setViewportView(tablaCodPantalla);
		
		JPanel panelSur = new JPanel();
		contentPane.add(panelSur, BorderLayout.SOUTH);
		
		btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panelSur.add(btnCerrar);
	}
	
	public ArrayList<codigoPantalla> nuevaListaCodPant(){
		ArrayList<codigoPantalla> nuevaLista = new ArrayList<codigoPantalla>();
		for (int i = 0; i < tablaCodPantalla.getRowCount(); i++) {
			nuevaLista.add(new codigoPantalla((Integer) tablaCodPantalla.getValueAt(i, 0),(String) tablaCodPantalla.getValueAt(i, 1)));
		}
		return nuevaLista;
	}

}
