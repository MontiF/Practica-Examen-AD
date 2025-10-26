package PracticaFinal;

import java.time.LocalDate;
import java.util.ArrayList;

public class Ticket extends Empleado{
	private int numeroTicket, idEmpleado;
	private String nombreEmpleado;
	private LocalDate fecha;
	private ArrayList<LineaVenta> lineas;
	private float total;
	
	
	public Ticket(int numeroTicket, int idEmpleado, String nombreEmpleado, LocalDate fecha, ArrayList<LineaVenta> lineas, float total) {
		super(idEmpleado, nombreEmpleado, nombreEmpleado, nombreEmpleado);
		this.numeroTicket = numeroTicket;
		this.fecha = fecha;
		this.lineas = lineas;
		this.total = total;
	}


	public int getNumeroTicket() {
		return numeroTicket;
	}


	public void setNumeroTicket(int numeroTicket) {
		this.numeroTicket = numeroTicket;
	}


	public int getIdEmpleado() {
		return getId();
	}


	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}


	public String getNombreEmpleado() {
		return getNombre();
	}


	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}


	public ArrayList<LineaVenta> getLineas() {
		return lineas;
	}


	public void setLineas(ArrayList<LineaVenta> lineas) {
		this.lineas = lineas;
	}


	public float getTotal() {
		return total;
	}


	public void setTotal(float total) {
		this.total = total;
	}
	
	
	
	
	
}
