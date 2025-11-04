package PracticaFinal;
import java.io.*;

enum Cargo {GESTOR, VENDEDOR};

public class Empleado implements Serializable {
	private int identificacion;
	private String nombre, password;
	private Cargo cargo;
	
	public Empleado(int identificacion, String nombre, String password, Cargo cargo) {
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.password = password;
		this.cargo = cargo;
	}

	public Empleado(int id, String nombre) {
		this.identificacion = id;
		this.nombre = nombre;
	}


	public int getId() {
		return identificacion;
	}

	public void setId(int id) {
		this.identificacion = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return "Empleado [identificacion=" + identificacion + ", nombre=" + nombre + ", cargo=" + cargo + "]";
	}


	
	
	
	
}
