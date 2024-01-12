package Servidor;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ServidorChat {
   
    /**
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Descripción: Punto de entrada del programa Java.
	 * Se crea una instancia de la clase "LogicaServidor" 
	 * pasando el valor 53 (Puerto UDP para DNS) como argumento. 
	 * A continuación, se llama al método "escucharMensajes" 
	 * de esa instancia.
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		 
		LogicaServidor servidor= new LogicaServidor(53);
		
		servidor.escucharMensajes();

	}

}
