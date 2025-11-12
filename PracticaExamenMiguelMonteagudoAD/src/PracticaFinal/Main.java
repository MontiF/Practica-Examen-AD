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
	private static Scanner sc = new Scanner(System.in); // Sacnner global paro no tener que abrir y cerrar todo el rato más scanners
	private static ArrayList<Planta> listaPlantas = new ArrayList<Planta>();
	private static ArrayList<Empleado> listaEmpleados = new ArrayList<Empleado>();

	public static void main(String[] args) {
		crearEstructuraCarpetas();
		Empleado empleadoIniciado = iniciarSesion(); //Se iguala al empleado dependiendo de lo que devuelva el inicio de sesion
	
		if (empleadoIniciado.getCargo() == Cargo.GESTOR) {
			System.out.println("Menu de Gestor");
			menuGestor(empleadoIniciado);
		} else if (empleadoIniciado.getCargo() == Cargo.VENDEDOR) {
			System.out.println("Menu de Vendedor");
			menuVendedor(empleadoIniciado);
		} else {
			System.out.println("Inicio de sesion fallido debido a un cargo incorrecto");
		}
		
		sc.close();
	}

	private static void menuVendedor(Empleado empleadoIniciado) {
		int eleccion = 0;
		do {
			System.out.println("Que desea hacer? \n 1. Visualizar el catalogo de plantas \n 2. Gestionar una venta \n 3. Buscar ticket \n 4. Salir");
		
			if (!sc.hasNextInt()) {
				System.out.println("Entrada inválida. Debe ser un número.");
				return; 
			}
			eleccion = sc.nextInt();
			sc.nextLine(); 
			
			switch (eleccion) {
			case 1:
				visualizarCatalogo();
				break;
			case 2:
				generarVentas(empleadoIniciado);
				break;
			case 3:
				buscarTicket();
				break;
			case 4:
				System.out.println("Has salido");
				break;
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		} while (eleccion != 4);
	}

	private static void buscarTicket() {
		int numeroTicket;
		System.out.println("Que ticket desea devolver (Escribe solo el numero)");
		if (!sc.hasNextInt()) {
			System.out.println("Entrada inválida");
			return;
		}
		numeroTicket = sc.nextInt();
		sc.nextLine(); 
		
		File ticketABuscar = new File("TICKETS/" + String.valueOf(numeroTicket) + ".txt");
		File ticketABuscarD = new File("DEVOLUCIONES/" + String.valueOf(numeroTicket) + ".txt");
		File ticketEncontrado;
		
		if (!ticketABuscar.exists() && !ticketABuscarD.exists()) {
			System.out.println("No existe el ticket con número " + numeroTicket);
			return; 
		}
		
		if (ticketABuscar.exists()) {
			ticketEncontrado = ticketABuscar;
		} else {
			ticketEncontrado = ticketABuscarD;
		}

		if (ticketEncontrado != null) { 
			try (BufferedReader br = new BufferedReader(new FileReader(ticketEncontrado))) {
				String linea;
				while ((linea = br.readLine()) != null) {
					System.out.println(linea);
				}
				System.out
						.println("__________________________________________________________________________________");
			} catch (IOException e) {
				System.out.println("Error al leer el contenido del ticket");
			}
		}
	}

	private static void generarVentas(Empleado empleadoIniciado) {
		int eleccion = 0;
		do {
			System.out.println("Que desea hacer? \n 1. Vender \n 2. Devolver \n 3. Salir");
			
			if (!sc.hasNextInt()) {
				System.out.println("Entrada inválida. Debe ser un número.");
				return;
			}
			eleccion = sc.nextInt();
			sc.nextLine(); 
			
			switch (eleccion) {
			case 1:
				vender(empleadoIniciado);
				break;
			case 2:
				devolver();
				break;
			case 3:
				System.out.println("Has salido");
				break;
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		} while (eleccion != 3);
	}

	private static void devolver() {
		int numeroTicket;
		System.out.println("Que ticket desea devolver(Escribe solo el numero)");
		
		if (!sc.hasNextInt()) {
			System.out.println("Entrada inválida");
			return;
		}
		numeroTicket = sc.nextInt();
		sc.nextLine(); 

		File ticketOriginal = new File("TICKETS/" + String.valueOf(numeroTicket) + ".txt");
		File ticketDevuelto = new File("DEVOLUCIONES/" + String.valueOf(numeroTicket) + ".txt");

		if (!ticketOriginal.exists()) {
			System.out.println("No existe el ticket con número " + numeroTicket + " en la carpeta TICKETS");
			return;
		}

		if (ticketDevuelto.exists()) {
			System.out.println(
					"El ticket con número " + numeroTicket + " ya fue devuelto previamente");
			return;
		}

		ArrayList<String> lineasTicket = new ArrayList<>();
		double totalDevolucion = 0.0;
		ArrayList<int[]> itemsDevueltos = new ArrayList<>(); 

		try (BufferedReader br = new BufferedReader(new FileReader(ticketOriginal))) {
			String linea;

			boolean esLineaProducto = false;
			while ((linea = br.readLine()) != null) {
				lineasTicket.add(linea); 

				if (linea.contains("CodigoProducto") && linea.contains("Cantidad")
						&& linea.contains("PrecioUnitario")) {
					esLineaProducto = true;
					continue; 
				}
				if (linea.contains("__________________________________________________________________________________")) {
					esLineaProducto = false;
				}

				if (esLineaProducto) {
					String[] partes = linea.trim().split("\\s+"); 

					if (partes.length >= 3) { 
						try {
							int codigo = Integer.parseInt(partes[0]);
							int cantidad = Integer.parseInt(partes[1]);
							itemsDevueltos.add(new int[] { codigo, cantidad }); 
						} catch (NumberFormatException ignored) {
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error al leer el ticket");
			return;
		}

		File plantasDat = new File("PLANTAS/plantas.dat");
		ArrayList<Planta> listaPlantasActualizada = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plantasDat))) {
			listaPlantasActualizada = (ArrayList<Planta>) ois.readObject();
		} catch (Exception e) {
			System.out.println("Error leyendo las plantas");
			return;
		}

		for (int[] item : itemsDevueltos) {
			int codigoItem = item[0];
			int cantidadItem = item[1];

			for (Planta p : listaPlantasActualizada) { 
				if (p.getCodigo() == codigoItem) {
					p.setStock(p.getStock() + cantidadItem);
					break; 
				}
			}
		}

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plantasDat))) {
			oos.writeObject(listaPlantasActualizada);
		} catch (IOException e) {
			System.out.println("Error actualizando el archivo de plantas después de la devolución");
		}

		try (FileWriter fw = new FileWriter(ticketDevuelto)) {

			for (String linea : lineasTicket) {
				if (linea.contains("Total:")) {
					String totalStr = linea.replaceAll("Total:\\s*", "").replaceAll("€.*", "").trim();
					try {
						totalDevolucion = Double.parseDouble(totalStr.replace(',', '.'));
						fw.write(String.format("Total: %.2f € (DEVUELTO)%n", -totalDevolucion));
					} catch (NumberFormatException e) {
						fw.write(linea + " (DEVUELTO)\n"); 
					}
				} else {
					String[] partes = linea.trim().split("\\s+");
					if (partes.length >= 3) {
						try {
							int codigo = Integer.parseInt(partes[0]);
							int cantidad = Integer.parseInt(partes[1]);
							double precio = Double.parseDouble(partes[2].replace(',', '.'));

							fw.write(String.format("%-15d %-15d %-15.2f%n", codigo, cantidad, precio));
						} catch (NumberFormatException e) {
							fw.write(linea + "\n");
						}
					} else {
						fw.write(linea + "\n");
					}
				}
			}

		} catch (IOException e) {
			System.out.println("Error al escribir el ticket de devolución: " + e.getMessage());
			return;
		}

		if (!ticketOriginal.delete()) {
			System.out.println("No se pudo eliminar el ticket");
		}
		System.out.println("Devolución del ticket " + numeroTicket + " procesada correctamente");
	}

	private static void vender(Empleado empleadoIniciado) {
		visualizarCatalogo();
		int idEmpleado = empleadoIniciado.getId();
		String nombreEmpleado = empleadoIniciado.getNombre();
		LocalDate fecha = LocalDate.now(); 
		int numFichero = 1;
		double total = 0;

		File rutaCarpetaT = new File("TICKETS");
		File rutaCarpetaD = new File("DEVOLUCIONES");

		numFichero = (rutaCarpetaT.listFiles().length + rutaCarpetaD.listFiles().length) + 1;

		File ticket = new File(rutaCarpetaT, String.valueOf(numFichero) + ".txt");
		try {
			ticket.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File plantasDat = new File("PLANTAS/plantas.dat");

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plantasDat))) {
			listaPlantas = (ArrayList<Planta>) ois.readObject();
		} catch (Exception e) {
			System.out.println("Error leyendo las plantas, lista vacía.");
			listaPlantas = new ArrayList<>();
		}

		try (FileWriter fw = new FileWriter(ticket)) {
			fw.write("Número Ticket:" + numFichero + "\n");
			fw.write("__________________________________________________________________________________\n");
			fw.write("Empleado que ha atendido: " + idEmpleado + "\n");
			fw.write("Nombre del empleado: " + nombreEmpleado + "\n\n");
			fw.write(String.format("%-15s %-15s %-15s%n", "CodigoProducto", "Cantidad", "PrecioUnitario"));

			while (true) {
				System.out.println("Escribe el codigo de la planta que deseas comprar (0 para salir):");
				int codigoPlanta = sc.nextInt();
				if (codigoPlanta == 0)
					break;

				System.out.println("Cuántas unidades desea comprar?");
				int unidadPlanta = sc.nextInt();
				sc.nextLine(); 

				boolean encontrada = false;

				for (Planta p : listaPlantas) {
					if (p.getCodigo() == codigoPlanta) {
						encontrada = true;
						if (p.getStock() >= unidadPlanta) {
							double subtotal = p.getPrecio() * unidadPlanta;
							total += subtotal;

							fw.write(
									String.format("%-15d %-15d %-15.2f%n", p.getCodigo(), unidadPlanta, p.getPrecio()));

							p.setStock(p.getStock() - unidadPlanta);

						} else {
							System.out.println("No hay suficiente stock (" + p.getStock() + ")");
						}
						break; 
					}
				}

				if (!encontrada)
					System.out.println("No se ha encontrado la planta");
			}

			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plantasDat))) {
				oos.writeObject(listaPlantas);
			} catch (IOException e) {
				System.out.println("Error actualizando el archivo de plantas");
			}

			fw.write("__________________________________________________________________________________\n");
			fw.write(String.format("Total: %.2f €%n", total));

			System.out.println("Venta finalizada. Ticket " + ticket.getName() + " generado");

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Termino la venta");
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
		int eleccion = 0;
		do {
			System.out.println(
					"Que desea hacer? \n 1. Dar de alta una planta \n 2. Dar de baja planta \n 3. Dar de alta un empleado \n 4. Dar de baja un empleado \n 5. Recuperar empelado \n 6. Ver estadisticas de ventas \n 7. Salir");
			if (!sc.hasNextInt()) {
				System.out.println("Entrada inválida. Debe ser un número.");
				return;
			}
			eleccion = sc.nextInt();
			switch (eleccion) {
			case 1:
				darAltaPlanta(); 
				break;
			case 2:
				darBajaPlanta(); 
				break;
			case 3:
				darAltaEmpleado();
				break;
			case 4:
				darBajaEmpleado();
				break;
			case 5:
				recuperarEmpleado();
				break;
			case 6:
				verEstadisticas();
				break;
			case 7:
				System.out.println("Has salido");
				break;
			default:
				System.out.println("Elige una opción correcta");
				break;
			}
		} while (eleccion != 7);
	}

	private static void darAltaPlanta() {
	}

	private static void darBajaPlanta() {
	}

	private static void darAltaEmpleado(){
		System.out.println("Introduce el ID del nuevo empleado");
		if (!sc.hasNextInt()) {
			System.out.println("El ID debe ser un número");
			return;
		}
		int id = sc.nextInt();
		if (id < 1000 || id > 9999) {
			System.out.println("El ID debe tener 4 digitos(1000-9999)");
			return;
		}
		sc.nextLine(); 

		for (Empleado e : listaEmpleados) {
			if (e.getId() == id) {
				System.out.println("El ID " + id + " ya existe");
				return;
			}
		}

		System.out.println("Introduce el nombre");
		String nombre = sc.nextLine();

		System.out.println("Introduce la contraseña");
		String password = sc.nextLine();

		Cargo cargo = null;
		int eleccionCargo = 0;
		do {
			System.out.println("Selecciona el Cargo (1 = VENDEDOR, 2 = GESTOR)");
			if (!sc.hasNextInt()) {
				System.out.println("Entrada inválida");
				sc.nextLine(); 
				continue;
			}
			eleccionCargo = sc.nextInt();
			sc.nextLine(); 

			if (eleccionCargo == 1) {
				cargo = Cargo.VENDEDOR;
			} else if (eleccionCargo == 2) {
				cargo = Cargo.GESTOR;
			} else {
				System.out.println("Opción no válida.");
			}
		} while (cargo == null);

		Empleado nuevoEmpleado = new Empleado(id, nombre, password, cargo);
		listaEmpleados.add(nuevoEmpleado);

		try (FileOutputStream FicheroEscritura = new FileOutputStream("EMPLEADOS/empleado.dat");
				ObjectOutputStream escritura = new ObjectOutputStream(FicheroEscritura)) {

			escritura.writeObject(listaEmpleados);
			System.out.println("Empleado dado de alta correctamente.");

		} catch (IOException e) {
			System.out.println("Error al guardar el nuevo empleado");
			e.printStackTrace();
			listaEmpleados.remove(nuevoEmpleado);
		}
	}

	private static void darBajaEmpleado() {
		verEmpleados(); 
		System.out.println("Introduce el ID del empleado a dar de baja");
		if (!sc.hasNextInt()) {
			System.out.println("Entrada inválida");
			return;
		}
		int id = sc.nextInt();
		sc.nextLine(); 

		Empleado empleadoADarDeBaja = null;
		int indice = -1; 

		for (int i = 0; i < listaEmpleados.size(); i++) {
			if (listaEmpleados.get(i).getId() == id) {
				empleadoADarDeBaja = listaEmpleados.get(i);
				indice = i;
				break;
			}
		}

		if (empleadoADarDeBaja == null) {
			System.out.println("No se encontró ningún empleado activo con el ID " + id);
			return;
		}

		File bajaDat = new File("EMPLEADOS/BAJA/empleadosBaja.dat");
		ArrayList<Empleado> listaBajas = new ArrayList<>();
		if (bajaDat.exists() && bajaDat.length() > 0) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bajaDat))) {
				listaBajas = (ArrayList<Empleado>) ois.readObject();
			} catch (Exception e) {
				System.out.println("Error al leer el archivo de bajas, se ha creado otro");
				listaBajas = new ArrayList<>(); 
			}
		}

		listaBajas.add(empleadoADarDeBaja);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bajaDat))) {
			oos.writeObject(listaBajas);
		} catch (IOException e) {
			System.out.println("No se pudo escribir en el archivo de bajas");
			e.printStackTrace();
			return; 
		}

		listaEmpleados.remove(indice);
		try (FileOutputStream FicheroEscritura = new FileOutputStream("EMPLEADOS/empleado.dat");
				ObjectOutputStream escritura = new ObjectOutputStream(FicheroEscritura)) {
			escritura.writeObject(listaEmpleados);
			System.out.println("Empleado dado de baja correctamente");
		} catch (IOException e) {
			System.out.println("Se dio de baja al empleado pero no se pudo actualizar empleado.dat");
			e.printStackTrace();
		}
	}

	private static void recuperarEmpleado() {
		System.out.println("Introduce el ID del empleado a recuperar");
		if (!sc.hasNextInt()) {
			System.out.println("Entrada inválida");
			return;
		}
		int id = sc.nextInt();
		sc.nextLine(); 

		File bajaDat = new File("EMPLEADOS/BAJA/empleadosBaja.dat");
		ArrayList<Empleado> listaBajas = new ArrayList<>();
		if (!bajaDat.exists() || bajaDat.length() == 0) {
			System.out.println("No hay empleados dados de baja para recuperar");
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bajaDat))) {
			listaBajas = (ArrayList<Empleado>) ois.readObject();
		} catch (Exception e) {
			System.out.println("Error al leer el archivo de bajas: " + e.getMessage());
			return;
		}

		Empleado empleadoARecuperar = null;
		int indiceBaja = -1; 
		
		for (int i = 0; i < listaBajas.size(); i++) {
			if (listaBajas.get(i).getId() == id) {
				empleadoARecuperar = listaBajas.get(i);
				indiceBaja = i;
				break;
			}
		}

		if (empleadoARecuperar == null) {
			System.out.println("No se encontró ningún empleado de baja con el ID " + id);
			return;
		}

		for (Empleado e : listaEmpleados) {
			if (e.getId() == id) {
				System.out
						.println("El ID " + id + " ya existe en la lista de empleados activos. No se puede recuperar");
				return;
			}
		}

		listaBajas.remove(indiceBaja);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bajaDat))) {
			oos.writeObject(listaBajas);
		} catch (IOException e) {
			System.out.println("No se pudo actualizar el archivo de bajas");
			e.printStackTrace();
			return; 
		}

		listaEmpleados.add(empleadoARecuperar);
		try (FileOutputStream FicheroEscritura = new FileOutputStream("EMPLEADOS/empleado.dat");
				ObjectOutputStream escritura = new ObjectOutputStream(FicheroEscritura)) {
			escritura.writeObject(listaEmpleados);
			System.out.println("Empleado recuperado correctamente");
		} catch (IOException e) {
			System.out.println("Se recuperó al empleado pero no se pudo actualizar empleado.dat");
			listaEmpleados.remove(empleadoARecuperar);
			e.printStackTrace();
		}
	}

	private static void verEstadisticas() {
		File carpetaTickets = new File("TICKETS");
		File[] listaTickets = carpetaTickets.listFiles();

		if (listaTickets == null || listaTickets.length == 0) {
			System.out.println("No hay tickets de venta para procesar.");
			return;
		}

		double totalRecaudado = 0.0;
		int ticketsProcesados = 0;

		for (File ticketFile : listaTickets) {
			if (ticketFile.isFile()) {
				try (BufferedReader br = new BufferedReader(new FileReader(ticketFile))) {
					String linea;
					while ((linea = br.readLine()) != null) {
						if (linea.trim().startsWith("Total:")) {
							String totalStr = linea.replaceAll("Total:\\s*", "").replaceAll("€.*", "").trim();

							try {
								double totalTicket = Double.parseDouble(totalStr.replace(',', '.'));
								totalRecaudado += totalTicket; 
								ticketsProcesados++;
							} catch (NumberFormatException e) {
							}
							break; 
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Total de tickets de venta " + ticketsProcesados);
		System.out.println(String.format("Total recaudado %.2f €", totalRecaudado));
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
					fw.write("    <planta>\n");
					fw.write("        <codigo>1</codigo>\n");
					fw.write("        <nombre>Cactus</nombre>\n");
					fw.write("        <foto>cactus.jpg</foto>\n");
					fw.write("        <descripcion>Planta suculenta del desierto.</descripcion>\n");
					fw.write("    </planta>\n");
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
					fw.write("    <planta>\n");
					fw.write("        <codigo>1</codigo>\n");
					fw.write("        <nombre>Cactus</nombre>\n");
					fw.write("        <foto>cactus.jpg</foto>\n");
					fw.write("        <descripcion>Planta suculenta del desierto.</descripcion>\n");
					fw.write("    </planta>\n");
					fw.write("</plantas>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void crearPlantasDat() {
		File rutaPlantasDat = new File("PLANTAS/plantas.dat");
		listaPlantas.clear(); 

		if (rutaPlantasDat.exists() && rutaPlantasDat.length() > 0) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaPlantasDat))) {
				listaPlantas = (ArrayList<Planta>) ois.readObject();
			} catch (Exception e) {
				System.out.println("Error leyendo plantas existentes. Se generarán nuevas.");
				listaPlantas = new ArrayList<>();
			}
		}

		try {
			File inputFile = new File("PLANTAS/plantas.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("planta");

			for (int i = 0; i < nList.getLength(); i++) {
				Element eElement = (Element) nList.item(i);
				int codigo = Integer.parseInt(eElement.getElementsByTagName("codigo").item(0).getTextContent());

				boolean existe = false;
				for (Planta p : listaPlantas) {
					if (p.getCodigo() == codigo) {
						existe = true;
						break;
					}
				}

				if (!existe) {
					float precio = numeroAleatorioPrecio();
					int stock = numeroAleatorioStock();
					listaPlantas.add(new Planta(codigo, precio, stock));
				}
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

				listaEmpleados = (ArrayList<Empleado>) ois.readObject();

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private static Empleado iniciarSesion() {
		verEmpleados(); 
		System.out.println("Introduce tu número de identificación de empleado");
		if (!sc.hasNextInt()) {
			System.out.println("Entrada inválida. Debe ser un número.");
			System.exit(0); 
			return null;
		}
		int identificacion = sc.nextInt();
		System.out.println("Introduce la contraseña");
		sc.nextLine(); 
		String password = sc.nextLine();

		for (Empleado e : listaEmpleados) {
			if (e.getId() == identificacion && e.getPassword().equals(password)) {
				System.out.println("Bienvenido, " + e.getNombre());
				return e; 
			}
		}
		System.out.println("Identificación o contraseña incorrecta");
		System.exit(0); 
		return null; 

	}

	private static void verEmpleados() {
		for (Empleado e : listaEmpleados) {
			System.out.println(e.toString());
		}
	}

	private static void generarEmpleados() {
		try (FileOutputStream FicheroEscritura = new FileOutputStream("EMPLEADOS/empleado.dat");
				ObjectOutputStream escritura = new ObjectOutputStream(FicheroEscritura)) {

			Empleado empleado1 = new Empleado(1452, "Teresa", "asb123", Cargo.VENDEDOR);
			Empleado empleado2 = new Empleado(6894, "Miguel Angel", "123qwe", Cargo.VENDEDOR);
			Empleado empleado3 = new Empleado(7532, "Natalia", "xs21qw4", Cargo.GESTOR);

			listaEmpleados.add(empleado1);
			listaEmpleados.add(empleado2);
			listaEmpleados.add(empleado3);

			escritura.writeObject(listaEmpleados);

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
