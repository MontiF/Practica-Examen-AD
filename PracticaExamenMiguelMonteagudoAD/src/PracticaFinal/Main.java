package PracticaFinal;

import java.io.File;

public class Main {
	
	public static void main(String[] args) {
		crearEstructuraCarpetas();
		
	}

	public static void crearEstructuraCarpetas() {
		String [] carpetas = {
			"PLANTAS",
			"PLANTAS/BAJA",
			"EMPLEADOS",
			"EMPLEADOS/BAJA",
			"TICKETS",
			"DEVOLUCIONES"
		};
		
		for(String ruta: carpetas) {
			File carpeta = new File(ruta);
			if(!carpeta.exists()) {
				if(carpeta.mkdirs()) {
					System.out.println("Carpeta "+ruta +" creada correctamente.");
				}else {
					System.out.println("Carpeta "+ruta +" no se pudo crear correctamente.");
				}
			}else {
				System.out.println("Carpeta " +ruta +" ya exite.");
			}
			
		}
	}
}
