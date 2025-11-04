package PracticaFinal;

import java.io.*;
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

	private static ArrayList<Empleado> ListaEmpleados = new ArrayList<Empleado>();

	public static void main(String[] args) {
		crearEstructuraCarpetas();
		Cargo cargoIniciado = iniciarSesion();
		if (cargoIniciado == Cargo.GESTOR) {
			System.out.println("Menu de Gestor");
			menuGestor();
		} else if (cargoIniciado == Cargo.VENDEDOR) {
			System.out.println("Menu de Vendedor");
			menuVendedor();
		} else {
			System.out.println("Inicio de sesion fallido");
		}

	}

	private static void menuVendedor() {
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
				// generarVentas();
				break;
			case 3:
				// buscarTicket();
				break;
			case 4:
				System.out.println("Has salido");
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		} while (eleccion != 4);
		sc.close();
	}

	private static void visualizarCatalogo() {
		try {
			FileInputStream fis = new FileInputStream("PLANTAS/plantas.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			System.out.println("Catalogo de plantas");
			while (true) {
				try {
					Planta planta = (Planta) ois.readObject();
					System.out.println(planta);
				} catch (EOFException e) {
					break;
				}
			}
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void menuGestor() {
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
			case 5:
				// recuperarEmpleado();
			case 6:
				// verEstadisticas();
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

			FileOutputStream fos = new FileOutputStream("PLANTAS/plantas.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("planta");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					int codigo = Integer.parseInt(eElement.getElementsByTagName("codigo").item(0).getTextContent());
					float precio = numeroAleatorioPrecio();
					int stock = numeroAleatorioStock();

					Planta planta = new Planta(codigo, precio, stock);
					oos.writeObject(planta);
				}
			}

			oos.close();
			fos.close();
			System.out.println("Plantas.dat generado correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Plantas.dat no se pudo generar correctamente");
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

	private static Cargo iniciarSesion() {
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
