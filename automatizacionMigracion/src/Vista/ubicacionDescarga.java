package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class ubicacionDescarga extends JFrame {

	private JPanel contentPane;
	private JTextField textUbi;
	private JTree tree;
	private DefaultMutableTreeNode selectednode;
	
	public JButton btnDescargar;
	public JButton btnCancelar;
	
	private String ubiIntroducido;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ubicacionDescarga frame = new ubicacionDescarga();
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
	public ubicacionDescarga() {
		setTitle("Elegir ubicación");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("C:\\");
		File directoryPath = new File("C:\\");
		//List of all files and directories
		mostrarCarpetas(directoryPath, "-", raiz);
		
		tree = new JTree(raiz);
		scrollPane.setViewportView(tree);
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
		        selectednode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		        
		        try{
      		        String ubicacion = ubiNodoArbol(selectednode);
			        
			        File folderPath = new File(ubicacion);
			        mostrarCarpetas(folderPath, "-", selectednode);
		        } catch (NullPointerException ne) {
		        	System.out.println("AL PULSAR: VACIO");
		        }
		    }
		});
		
		JPanel panel_botones = new JPanel();
		contentPane.add(panel_botones, BorderLayout.SOUTH);
		
		btnCancelar = new JButton("Cancelar");
		panel_botones.add(btnCancelar);
		
		btnDescargar = new JButton("Descargar");
		panel_botones.add(btnDescargar);
		
		JPanel panel_ubi = new JPanel();
		contentPane.add(panel_ubi, BorderLayout.NORTH);
		panel_ubi.setLayout(new BorderLayout(0, 0));
		
		JLabel lbl_ubi = new JLabel("Introduce ubicaci\u00F3n donde descargar:");
		panel_ubi.add(lbl_ubi, BorderLayout.NORTH);
		
		textUbi = new JTextField();
		panel_ubi.add(textUbi, BorderLayout.SOUTH);
		textUbi.setColumns(10);
	}
	
	public void mostrarCarpetas(File directoryPath, String guion, DefaultMutableTreeNode padre) {
		File contents[] = directoryPath.listFiles();
		try {
			for(int i=0; i<contents.length; i++) {
				if (contents[i].isDirectory() && !contents[i].getName().contains("$")) {  
					DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(contents[i].getName());
					padre.add(nodo);
					//mostrarCarpetas(contents[i], guion+"-", nodo);
				}
			}
		} catch (NullPointerException e){
			
		}
	}
	
	public String ubiNodoArbol(DefaultMutableTreeNode node) {
        String ubicacion = "";
        
        DefaultMutableTreeNode padre = (DefaultMutableTreeNode) node.getParent();
        while(padre.getUserObject() != "C:\\") {
        	ubicacion = padre.getUserObject() + "\\" + ubicacion;
        	padre = (DefaultMutableTreeNode) padre.getParent();
        }
        
        ubicacion = padre.getUserObject() + ubicacion + node.getUserObject();
        
        return ubicacion;
	}
	
	public boolean comprobarDescarga() {
		if(textUbi.getText() != null && !textUbi.getText().equals("")) {
			File archivo = new File(textUbi.getText());
			if (archivo.exists()) {
			    System.out.println("Existe");
			    ubiIntroducido = textUbi.getText();
				return true;
			}
			JOptionPane.showMessageDialog(this, "Debe escribir la direccion correctamente.","Ubicación erronea", JOptionPane.ERROR_MESSAGE);
			System.out.println("No Existe Ubi introducida");
		} else {
			if(selectednode != null) {
				try{
	  		        String ubicacion = ubiNodoArbol(selectednode);
			        File archivo = new File(ubicacion);
			        if (archivo.exists()) {
			        	System.out.println("Existe");
			        	ubiIntroducido = ubicacion;
					    return true;
					}
			        System.out.println("No Existe Ubi");
		        } catch (NullPointerException ne) {
		        	System.out.println("Ubi vacia");
		        }
			}
		}
		return false;
	}
	
	public String getUbicacion() {
		return ubiIntroducido;
	}
}
