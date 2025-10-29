package PracticaFinal;

public class Planta {
	private int codigo;
	private float precio;
	private int stock;
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	@Override
	public String toString() {
		return "Planta [codigo=" + codigo + ", precio=" + precio +"€" +", stock=" + stock + "]";
	}
	
	

	
	
}
