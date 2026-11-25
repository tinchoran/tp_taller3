import java.io.File;

public class HiloBuscador extends Thread {

	private ColaDirectorios cola;
	private String archivoBuscado;

	public HiloBuscador(ColaDirectorios cola, String archivoBuscado) {
		this.cola = cola;
		this.archivoBuscado = archivoBuscado;
	}

	@Override
	public void run() {
		// pido carpetas hasta que la cola devuelva null
		File dir = cola.sacar();
		while (dir != null) {
			File[] archivos = dir.listFiles();
			// listFiles puede dar null si no hay permisos en esa carpeta
			if (archivos != null) {
				for (int i = 0; i < archivos.length; i++) {
					if (archivos[i].isDirectory()) {
						cola.poner(archivos[i]);
					} else if (archivos[i].isFile() && archivos[i].getName().equals(archivoBuscado)) {
						cola.avisarEncontrado(archivos[i].getAbsolutePath());
					}
				}
			}
			// Avisa que ya revisé esta carpeta
			cola.terminoDir();
			dir = cola.sacar();
		}
	}

}
