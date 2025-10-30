package PracticaFinal;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

	private static ArrayList<Empleado> ListaEmpleados = new ArrayList<Empleado>();
	
	public static void main(String[] args) {
		crearEstructuraCarpetas();
		Cargo cargoIniciado = iniciarSesion();
		if(cargoIniciado == Cargo.GESTOR){
			System.out.println("Menu de Gestor");
			menuGestor();
		}else if(cargoIniciado == Cargo.VENDEDOR) {
			System.out.println("Menu de Vendedor");
			menuVendedor();
		}else {
			System.out.println("Inicio de sesion fallido");
		}
	}

	private static void menuVendedor() {
		Scanner sc = new Scanner(System.in);
		
	}

	private static void menuGestor() {
		
	}

	public static void crearEstructuraCarpetas() {
		int correcto = 0;
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
					correcto++;
				}else {
					System.out.println("Carpeta "+ruta +" no se pudo crear correctamente.");
				}
			}else{
				correcto++;
			}
			
		}
		File carpetaEmpleados = new File("EMPLEADOS");
		if(carpetaEmpleados.exists()){
			escribirEmpleadospredeterminados();
			correcto++;
		}else {
			if(carpetaEmpleados.mkdirs()) {
				escribirEmpleadospredeterminados();
				correcto++;
			}else {
				System.out.println("Carpeta "+carpetaEmpleados +" no se pudo crear correctamente.");
			}
		}
		
		File carpetaPlantas = new File("PLANTAS");
		if(!carpetaPlantas.exists()){
			if(carpetaPlantas.mkdirs()) {
				crearPlantasDat();
				crearPlantasXML();
				correcto++;
				correcto++;
			}else {
				System.out.println("Carpeta "+carpetaPlantas +" no se pudo crear correctamente.");
			}
		}else {
			crearPlantasDat();
			crearPlantasXML();
			correcto++;
			correcto++;
		}
		
		if(correcto == 9) {
			System.out.println("Estructura cargada correctamente");
		}
	}
	public static void escribirEmpleadospredeterminados(){
		File rutaEmpleadosDat = new File("EMPLEADOS/empleado.dat");
		if(rutaEmpleadosDat.exists()) {
			if(rutaEmpleadosDat.length() == 0) {
				generarEmpleados();
			}
		}else {
			try {
				rutaEmpleadosDat.createNewFile();
				generarEmpleados();
				System.out.println("Empleado.dat creado correctamente");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cargarEmpleados();

	}
	public static void crearPlantasDat(){
		File rutaPlantasDat = new File("PLANTAS/plantas.dat");
		if(!rutaPlantasDat.exists()) {
			try {
				rutaPlantasDat.createNewFile();
				System.out.println("Planta.dat creado correctamente");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public static void crearPlantasXML(){
		File rutaPlantasXML = new File("PLANTAS/planta.xml");
		if(!rutaPlantasXML.exists()) {
			try {
				rutaPlantasXML.createNewFile();
				try (FileWriter fw = new FileWriter(rutaPlantasXML)) {
					fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	                fw.write("<plantas>\n");
	                fw.write("    <planta>\n");
	                fw.write("        <codigo>1</codigo>\n");
	                fw.write("        <nombre>Cactus</nombre>\n");
	                fw.write("        <foto>cactus.jpg</foto>\n");
	                fw.write("        <descripcion>Planta suculenta del desierto.</descripcion>\n");
	                fw.write("    </planta>\n");
	                fw.write("</plantas>");
				}catch(IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			if(rutaPlantasXML.length() == 0) {
				try (FileWriter fw = new FileWriter(rutaPlantasXML)) {
					fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	                fw.write("<plantas>\n");
	                fw.write("    <planta>\n");
	                fw.write("        <codigo>1</codigo>\n");
	                fw.write("        <nombre>Cactus</nombre>\n");
	                fw.write("        <foto>cactus.jpg</foto>\n");
	                fw.write("        <descripcion>Planta suculenta del desierto.</descripcion>\n");
	                fw.write("    </planta>\n");
	                fw.write("</plantas>");
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void cargarEmpleados() {
	    File rutaEmpleadosDat = new File("EMPLEADOS/empleado.dat");
	    if (rutaEmpleadosDat.exists() && rutaEmpleadosDat.length() > 0) {
	        try (FileInputStream fis = new FileInputStream(rutaEmpleadosDat);
	             ObjectInputStream ois = new ObjectInputStream(fis)) {
	            
	            ListaEmpleados = (ArrayList<Empleado>) ois.readObject();
	            
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static Cargo iniciarSesion() {	
		verEmpleados();
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce tu número de identificación de empleado");
		int identificacion = sc.nextInt();
		System.out.println("Introduce la contraseña");
		sc.nextLine();
		String password = sc.nextLine();
		
		for (Empleado e : ListaEmpleados) {
			if (e.getId() == identificacion && e.getPassword().equals(password)) {
				System.out.println("Bienvenido, " + e.getNombre());
	            return e.getCargo();
	        }else {
	        System.out.println("Identificación o contraseña incorrecta");
	        return null;
	        }
	    }
		return null;

	}
	 static void verEmpleados(){
		for (Empleado e : ListaEmpleados) {
	    		System.out.println(e.toString());
	    	}
	 }
	 public static void generarEmpleados(){
	 try (FileOutputStream FicheroEscritura = new FileOutputStream("EMPLEADOS/empleado.dat");
             ObjectOutputStream escritura = new ObjectOutputStream(FicheroEscritura)) {

            	            
            Empleado empleado1 = new Empleado(1452,"Teresa","asb123",Cargo.VENDEDOR);
            Empleado empleado2 = new Empleado(0234,"Miguel Angel","123qwe",Cargo.VENDEDOR);
            Empleado empleado3 = new Empleado(7532,"Natalia","xs21qw4", Cargo.GESTOR);
            
            ListaEmpleados.add(empleado1);
            ListaEmpleados.add(empleado2);
            ListaEmpleados.add(empleado3);
            
            escritura.writeObject(ListaEmpleados);
            

            System.out.println("Objetos escritos correctamente en empleado.dat");
            
        } catch (IOException i) {
            i.printStackTrace();
        }
	 }
}
