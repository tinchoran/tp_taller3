import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ColaDirectorios {

	private List<File> directorios;
	private int cantEncontrados;
	private int activos; // cantidad de hilos revisando carpetas ahora
	private boolean terminado;

	public ColaDirectorios(File carpetaInicial) {
		directorios = new LinkedList<>();
		directorios.add(carpetaInicial);
		cantEncontrados = 0;
		activos = 0;
		terminado = false;
	}

	public synchronized void poner(File dir) {
		directorios.add(dir);
		notifyAll();
	}

	public synchronized File sacar() {
		// si no hay carpetas pero algun hilo sigue buscando, esperamos
		while (directorios.size() == 0 && !terminado) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (directorios.size() == 0) {
			return null;
		}
		activos++;
		return directorios.remove(0);
	}

	public synchronized void terminoDir() {
		activos--;
		// Si ya no quedan carpetas ni hilos activos aviso que termino
		if (directorios.size() == 0 && activos == 0) {
			terminado = true;
			notifyAll();
		}
	}

	public synchronized void avisarEncontrado(String ruta) {
		cantEncontrados++;
		System.out.println(ruta);
	}

	public synchronized int getCantEncontrados() {
		return cantEncontrados;
	}

}
