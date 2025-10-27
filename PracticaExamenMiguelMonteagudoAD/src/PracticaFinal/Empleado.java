package PracticaFinal;
import java.io.*;

enum Cargo {GESTOR, VENDEDOR};

public class Empleado implements Serializable {
	private int id;
	private String nombre, password;
	private Cargo cargo;
	
	public Empleado(int id, String nombre, String password, Cargo cargo) {
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.cargo = cargo;
	}

	public Empleado(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return "Empleado [id=" + id + ", nombre=" + nombre + ", password=" + password + ", cargo=" + cargo + "]";
	}
	
	
	
	
}
