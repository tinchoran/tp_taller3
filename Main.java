import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean seguir = true;

		while (seguir) {
			System.out.print("Ruta del directorio: ");
			String ruta = sc.nextLine().trim();
			File carpeta = new File(ruta);

			if (!carpeta.exists() || !carpeta.isDirectory()) {
				System.out.println("La ruta no existe o no es un directorio.");
				continue;
			}

			System.out.print("Nombre del archivo: ");
			String archivo = sc.nextLine().trim();

			if (archivo.equals("")) {
				System.out.println("El nombre del archivo no puede estar vacio.");
				continue;
			}

			System.out.print("Cantidad de hilos: ");
			int cantHilos;
			// leo con nextLine y parseo para evitar el problema del enter con nextInt
			try {
				cantHilos = Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Cantidad de hilos invalida.");
				continue;
			}

			if (cantHilos <= 0) {
				System.out.println("Cantidad de hilos invalida.");
				continue;
			}

			ColaDirectorios cola = new ColaDirectorios(carpeta);
			HiloBuscador[] hilos = new HiloBuscador[cantHilos];

			// Creo y arranco los hilos
			for (int i = 0; i < cantHilos; i++) {
				hilos[i] = new HiloBuscador(cola, archivo);
				hilos[i].start();
			}

			// espero a que terminen todos
			for (int i = 0; i < cantHilos; i++) {
				try {
					hilos[i].join();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}

			if (cola.getCantEncontrados() == 0) {
				System.out.println("No se encontro el archivo.");
			}

			System.out.print("Buscar otro archivo? (s/n): ");
			String resp = sc.nextLine().trim();
			if (!resp.equalsIgnoreCase("s")) {
				seguir = false;
			}
		}

		sc.close();
	}

}
