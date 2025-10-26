package PracticaFinal;

public class Planta {
	private int codigo;
	private String nombre, foto, descripcion;
	private float precio;
	private int stock;
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
		return "Planta [codigo=" + codigo + ", nombre=" + nombre + ", foto=" + foto + ", descripcion=" + descripcion
				+ ", precio=" + precio +"€" +", stock=" + stock + "]";
	}
	
	

	
	
}
