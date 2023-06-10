package Modelo;

public class columna {
	private String nombreCol;
	private String tipo;
	private int cantidad;
	
	public columna(String pNombre,String pTipo,int pCant) {
		nombreCol = pNombre;
		tipo = pTipo;
		cantidad = pCant;
	}
	
	public String getNombre() {
		return nombreCol;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public int getCant() {
		return cantidad;
	}
	
}
