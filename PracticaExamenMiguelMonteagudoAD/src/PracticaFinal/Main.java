 package PracticaFinal;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		crearEstructuraCarpetas();
		iniciarSesion();
		
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
		ArrayList <Empleado> ListaEmpleados = new ArrayList <>();
		File rutaEmpleadosDat = new File("EMPLEADOS/empleado.dat");
		if(rutaEmpleadosDat.exists()) {
			if(rutaEmpleadosDat.length() == 0) {
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
		}else {
			try {
				rutaEmpleadosDat.createNewFile();
				System.out.println("Empleados.dat creado correctamente");
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
	public static void iniciarSesion() {	
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce tu número de identificación de empleado");
		int identificacion = sc.nextInt();
		System.out.println("Escribe la contraseña");
		sc.nextLine();
		String password = sc.nextLine();
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("EMPLEADOS/empleado.dat"))) {
		    for(Empleado e : ){
		        try {
		            Object obj = ois.readObject();
		            if (obj instanceof Empleado) {
		                if (e.getId() == identificacion && e.getPassword().equals(password)) {
		                	System.out.println("Inicio correcto");
		                	break;
		                }
		            }
		        } catch (EOFException | ClassNotFoundException eof) {
		            System.out.println("Identificacion o contraseña incorrecta");
		        	break;
		        }
		    }
		}catch (IOException e1) {
			e1.printStackTrace();
		}

		
	}
}
