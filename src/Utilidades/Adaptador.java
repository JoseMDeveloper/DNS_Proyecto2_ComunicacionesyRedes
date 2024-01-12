package Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Adaptador {

	private ByteArrayOutputStream byteArrayOutputStream;
	
	public Adaptador() {
		byteArrayOutputStream = new ByteArrayOutputStream();
    }

	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}

	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

	/**
	 * 
	 * @param dataInputStream
	 * @return
	 * @throws IOException
	 * Descripción: Leer y analizar un campo de nombre de dominio (QNAME) en un formato DNS 
	 * comprimido y convertirlo en una cadena legible.
	 */
    public String parseQName(DataInputStream dataInputStream) throws IOException
    {
		/**
		 * 1. Se crea un objeto ByteArrayOutputStream llamado byteArrayOutputStream, que se utilizará 
		 * para almacenar los bytes que representan el campo de nombre de dominio.
		 * 
		 * 2. Se crea un objeto StringBuilder llamado qnameBuilder, que se utilizará para construir la 
		 * cadena del nombre de dominio.
		 * 
		 * 3. Se inicia un bucle mientras se lee cada byte del DataInputStream y se comprueba si su valor 
		 * recLen es mayor que 0. Esto indica que hay más partes del nombre de dominio para leer.
		 * 
		 * 4. Dentro del bucle, se crea un arreglo de bytes record con la longitud recLen, y se leen los 
		 * bytes correspondientes desde dataInputStream utilizando readFully(). Luego, se convierte el 
		 * arreglo de bytes en una cadena recordString utilizando el conjunto de caracteres UTF-8.
		 * 
		 * 5. Se escriben los bytes leídos en el byteArrayOutputStream para mantener un registro de los 
		 * bytes del campo de nombre de dominio.
		 * 
		 * 6. Se agrega la cadena recordString al qnameBuilder, seguida de un punto "." para separar las 
		 * partes del nombre de dominio.
		 * 
		 * 7. Después de salir del bucle, se obtiene la cadena final del nombre de dominio QNAME a partir 
		 * del qnameBuilder.
		 *
		 * 8. Si la cadena QNAME termina con un punto ".", se elimina ese punto final utilizando substring().
		 * 
		 * 9. Finalmente, se devuelve la cadena QNAME como resultado del método.
		 */
		byteArrayOutputStream = new ByteArrayOutputStream();
        StringBuilder qnameBuilder = new StringBuilder();
		int recLen;
		while ((recLen = dataInputStream.readByte()) > 0) 
        {
    		byte[] record = new byte[recLen];
    		dataInputStream.readFully(record);
    		String recordString = new String(record, StandardCharsets.UTF_8);
			byteArrayOutputStream.write(recLen);
			byteArrayOutputStream.write(record);
    		qnameBuilder.append(recordString).append(".");
		}

		String QNAME = qnameBuilder.toString();
		if (QNAME.endsWith(".")) 
        {
    		QNAME = QNAME.substring(0, QNAME.length() - 1);
		}
        return QNAME;
    }
	
}
