package PracticaFinal;

import java.io.*;

public class Empleado implements Serializable {
	private int id;
	private String nombre, password, cargo;
	
	public Empleado(int id, String nombre, String password, String cargo) {
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.cargo = cargo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContra() {
		return password;
	}

	public void setContra(String contra) {
		this.password = contra;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return "Empleado [id=" + id + ", nombre=" + nombre + ", password=" + password + ", cargo=" + cargo + "]";
	}
	
	
	
	
}
