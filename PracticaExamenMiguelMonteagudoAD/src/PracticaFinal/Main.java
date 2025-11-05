package PracticaFinal;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {

	private static ArrayList<Planta> listaPlantas = new ArrayList<Planta>();
	private static ArrayList<Empleado> ListaEmpleados = new ArrayList<Empleado>();

	public static void main(String[] args) {
		crearEstructuraCarpetas();
		Empleado empleadoIniciado = iniciarSesion();
		if (empleadoIniciado.getCargo() == Cargo.GESTOR) {
			System.out.println("Menu de Gestor");
			menuGestor(empleadoIniciado);
		} else if (empleadoIniciado.getCargo() == Cargo.VENDEDOR) {
			System.out.println("Menu de Vendedor");
			menuVendedor(empleadoIniciado);
		} else {
			System.out.println("Inicio de sesion fallido debido a un cargo incorrecto");
		}

	}

	private static void menuVendedor(Empleado empleadoIniciado) {
		Scanner sc = new Scanner(System.in);
		int eleccion = 0;
		do {
			System.out.println(
					"Que desea hacer? \n 1. Visualizar el catalogo de plantas \n 2. Hacer una venta \n 3. Buscar ticket \n 4. Salir");
			eleccion = sc.nextInt();
			switch (eleccion) {
			case 1:
				visualizarCatalogo();
				break;
			case 2:
				generarVentas(empleadoIniciado);
				break;
			case 3:
				// buscarTicket();
				break;
			case 4:
				System.out.println("Has salido");
				break;
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		} while (eleccion != 4);
		sc.close();
	}

	private static void generarVentas(Empleado empleadoIniciado) {
		Scanner sc = new Scanner(System.in);
		int eleccion = 0;
		do {
			System.out.println("Que desea hacer? \n 1. Vender \n 2. Devolver \n 3. Salir");
			switch (eleccion) {
			case 1:
				vender(empleadoIniciado);
				break;
			case 2:
				//devolver
				break;
			case 3:
				System.out.println("Has salido");
				break;
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		}while (eleccion != 3);
		sc.close();
	}
	private static void vender(Empleado empleadoIniciado) {
		visualizarCatalogo();
		Scanner sc = new Scanner(System.in);
		int idEmpleado = empleadoIniciado.getId();
		String nombreEmpleado = empleadoIniciado.getNombre();
		LocalDate fecha = LocalDate.now();
		System.out.println("Escribe el codigo de la planta que deseas comprar");
		int codigoPlanta = sc.nextInt();
		System.out.println("Cuantas plantas desea comprar");
		int unidadPlanta = sc.nextInt();
		
		sc.close();
		
	}
	
	private static void visualizarCatalogo() {
		File plantasDat = new File("PLANTAS/plantas.dat");

		File plantasXML = new File("PLANTAS/plantas.xml");

		try {

			try (FileInputStream fis = new FileInputStream(plantasDat);

					ObjectInputStream ois = new ObjectInputStream(fis)) {

				listaPlantas = (ArrayList<Planta>) ois.readObject();

			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(plantasXML);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("planta");

			System.out.println("CATALOGO DE PLANTAS");

			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					int codigo = Integer.parseInt(eElement.getElementsByTagName("codigo").item(0).getTextContent());
					String nombre = eElement.getElementsByTagName("nombre").item(0).getTextContent();
					String foto = eElement.getElementsByTagName("foto").item(0).getTextContent();
					String descripcion = eElement.getElementsByTagName("descripcion").item(0).getTextContent();
					Planta plantaDat = null;

					for (Planta p : listaPlantas) {
						if (p.getCodigo() == codigo) {
							plantaDat = p;
							break;
						}
					}

					if (plantaDat != null) {
						System.out.println("\tCódigo: " + codigo);
						System.out.println("Nombre: " + nombre);
						System.out.println("Foto: " + foto);
						System.out.println("Descripción: " + descripcion);
						System.out.println("Precio: " + plantaDat.getPrecio() + "€");
						System.out.println("Stock: " + plantaDat.getStock());
					} else {
						System.out.println("Planta con código " + codigo + " no tiene datos de precio/stock");
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private static void menuGestor(Empleado empleadoIniciado) {
		Scanner sc = new Scanner(System.in);
		int eleccion = 0;
		do {
			System.out.println(
					"Que desea hacer? \n 1. Dar de alta una planta \n 2. Dar de baja planta \n 3. Dar de alta un empleado \n 4. Dar de baja un empleado \n 5. Recuperar empelado \n 6. Ver estadisticas de ventas \n 7. Salir");
			eleccion = sc.nextInt();
			switch (eleccion) {
			case 1:
				// darAltaPlanta();
				break;
			case 2:
				// darBajaPlanta();
				break;
			case 3:
				// darAltaEmpleado();
				break;
			case 4:
				// darBajaEmpleado();
				break;
			case 5:
				// recuperarEmpleado();
				break;
			case 6:
				// verEstadisticas();
				break;
			case 7:
				System.out.println("Has salido");
				break;
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		} while (eleccion != 7);
		sc.close();
	}

	private static void crearEstructuraCarpetas() {
		int correcto = 0;
		String[] carpetas = { "PLANTAS", "PLANTAS/BAJA", "EMPLEADOS", "EMPLEADOS/BAJA", "TICKETS", "DEVOLUCIONES" };

		for (String ruta : carpetas) {
			File carpeta = new File(ruta);
			if (!carpeta.exists()) {
				if (carpeta.mkdirs()) {
					correcto++;
				} else {
					System.out.println("Carpeta " + ruta + " no se pudo crear correctamente.");
				}
			} else {
				correcto++;
			}

		}
		File carpetaEmpleados = new File("EMPLEADOS");
		if (carpetaEmpleados.exists()) {
			escribirEmpleadospredeterminados();
			correcto++;
		} else {
			if (carpetaEmpleados.mkdirs()) {
				escribirEmpleadospredeterminados();
				correcto++;
			} else {
				System.out.println("Carpeta " + carpetaEmpleados + " no se pudo crear correctamente.");
			}
		}

		File carpetaPlantas = new File("PLANTAS");
		if (!carpetaPlantas.exists()) {
			if (carpetaPlantas.mkdirs()) {
				crearPlantasXML();
				crearPlantasDat();
				correcto++;
				correcto++;
			} else {
				System.out.println("Carpeta " + carpetaPlantas + " no se pudo crear correctamente.");
			}
		} else {
			crearPlantasXML();
			crearPlantasDat();
			correcto++;
			correcto++;
		}

		if (correcto == 9) {
			System.out.println("Estructura cargada correctamente");
		}
	}

	private static void escribirEmpleadospredeterminados() {
		File rutaEmpleadosDat = new File("EMPLEADOS/empleado.dat");
		if (rutaEmpleadosDat.exists()) {
			if (rutaEmpleadosDat.length() == 0) {
				generarEmpleados();
			}
		} else {
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

	private static void crearPlantasXML() {
		File rutaPlantasXML = new File("PLANTAS/plantas.xml");
		if (!rutaPlantasXML.exists()) {
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
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (rutaPlantasXML.length() == 0) {
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
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void crearPlantasDat() {
	    File rutaPlantasDat = new File("PLANTAS/plantas.dat");
	    if (!rutaPlantasDat.exists()) {
	        try {
	            rutaPlantasDat.createNewFile();
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Plantas.dat no se pudo crear correctamente");
	        }
	    }

	    try {
	        File inputFile = new File("PLANTAS/plantas.xml");

	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();

	        NodeList nList = doc.getElementsByTagName("planta");

	        ArrayList<Planta> listaPlantas = new ArrayList<>();

	        for (int i = 0; i < nList.getLength(); i++) {
	            Element eElement = (Element) nList.item(i);
	            int codigo = Integer.parseInt(eElement.getElementsByTagName("codigo").item(0).getTextContent());
	            float precio = numeroAleatorioPrecio();
	            int stock = numeroAleatorioStock();

	            listaPlantas.add(new Planta(codigo, precio, stock));
	        }

	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaPlantasDat))) {
	            oos.writeObject(listaPlantas);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	private static void cargarEmpleados() {
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

	private static Empleado iniciarSesion() {
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
				return e;
			}
		}
		System.out.println("Identificación o contraseña incorrecta");
		return null;

	}

	private static void verEmpleados() {
		for (Empleado e : ListaEmpleados) {
			System.out.println(e.toString());
		}
	}

	private static void generarEmpleados() {
		try (FileOutputStream FicheroEscritura = new FileOutputStream("EMPLEADOS/empleado.dat");
				ObjectOutputStream escritura = new ObjectOutputStream(FicheroEscritura)) {

			Empleado empleado1 = new Empleado(1452, "Teresa", "asb123", Cargo.VENDEDOR);
			Empleado empleado2 = new Empleado(0234, "Miguel Angel", "123qwe", Cargo.VENDEDOR);
			Empleado empleado3 = new Empleado(7532, "Natalia", "xs21qw4", Cargo.GESTOR);

			ListaEmpleados.add(empleado1);
			ListaEmpleados.add(empleado2);
			ListaEmpleados.add(empleado3);

			escritura.writeObject(ListaEmpleados);

			System.out.println("Objetos escritos correctamente en empleado.dat");

		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	private static float numeroAleatorioPrecio() {

		float numero = ThreadLocalRandom.current().nextFloat(1, 500);
		return Math.round(numero * 100f) / 100f;

	}

	private static int numeroAleatorioStock() {
		return ThreadLocalRandom.current().nextInt(1, 501);
	}

}
