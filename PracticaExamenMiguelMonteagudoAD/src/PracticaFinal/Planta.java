package PracticaFinal;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class Planta  implements Serializable{
	private int codigo;
	private float precio;
	private int stock;
	
	public Planta(int codigo, float precio, int stock) {
		super();
		this.codigo = codigo;
		this.precio = precio;
		this.stock = stock;
	}
	
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
		return "Planta [codigo=" + codigo + ", precio=" + precio +"€" +", stock=" + stock ;
	}
	
	

	
	
}
