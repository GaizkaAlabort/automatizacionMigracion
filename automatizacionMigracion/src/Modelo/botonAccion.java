package Modelo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**Basada en video 
 * JTable Custom Cell Button Action using Java Swing: https://www.youtube.com/watch?v=RMwufjRRHBU&ab_channel=RaVen
 * Modificado para recogida de datos
**/
public class botonAccion extends JButton{
	
	private boolean botonPresionado;
	
	public botonAccion() {
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(3,3,3,3));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				botonPresionado = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent me) {
				botonPresionado = false;
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics grafica) {
		Graphics2D g2 = (Graphics2D)grafica.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();
		int size = Math.min(width,height);
		int x = (width-size)/2;
		int y = (height-size)/2;
		if(botonPresionado) {
			g2.setColor(Color.YELLOW);
		} else {
			g2.setColor(Color.ORANGE);
		}
		g2.fill(new Ellipse2D.Double(x,y,size,size));
		g2.dispose();
		super.paintComponent(grafica);
	}
}
