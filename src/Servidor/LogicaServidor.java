package Servidor;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Datos.Header;
import Datos.Question;

import Utilidades.Adaptador;
import Utilidades.ManejadorArchivo;

public class LogicaServidor {

	private int puerto;
	private DatagramSocket socketUDP;
	private boolean salir;

	private Header head;
	private Question quest;

	private Adaptador adaptador;
	private ManejadorArchivo manejador;
	
	private static String nombreArchivo="C:/Users/Estudiante/Documents/DNS_Proyecto02_ComunicacionesyRedes/src/MasterFile.txt";

	/**
	 * 
	 * @param numPuerto: Número de puerto asociado
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Descripción: Constructor de LogicaServidor, se inicializa el puerto asociado
	 * (En este caso es el puerto 53), se instancia el nuevo adaptador y manejador de
	 * archivos asociado, además de cargar el MasterFile.txt para una consulta más
	 * eficiente de los dominios a partir del manejador.
	 */
	LogicaServidor(int numPuerto) throws FileNotFoundException, IOException{
		this.puerto= numPuerto;
		this.salir=false;
		adaptador = new Adaptador();
		manejador = new ManejadorArchivo();
		manejador.cargarArchivo(nombreArchivo);
		iniciarSocket();
	}
	
	/**
	 * 
	 * Descripción: Iniciar un servidor UDP (User Datagram Protocol). El método comienza 
	 * imprimiendo un mensaje indicando que el servidor UDP ha sido iniciado. Luego, dentro 
	 * de un bloque try-catch, se crea un objeto DatagramSocket que representa el socket del 
	 * servidor. El constructor de DatagramSocket recibe como parámetro el número de puerto 
	 * en el cual se desea escuchar las conexiones entrantes.
	 */
	public void iniciarSocket(){
		System.out.println("Iniciado el servidor UDP");
        try {
			this.socketUDP = new DatagramSocket(this.puerto);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Descripción:  Finalizar y cerrar el socket del servidor UDP. Al inicio, se imprime 
	 * un mensaje indicando que se está terminando el servidor UDP. Luego, se llama al método
	 * close() del objeto socketUDP para cerrar la conexión del socket.
	 */
	public void terminarSocket(){
		System.out.println("Terminando el servidor UDP");
        this.socketUDP.close();
	}
	
	/**
	 * 
	 * Descripción: Método encargado de recibir y procesar mensajes en el servidor UDP.
	 */
	public void escucharMensajes(){

		/**
		 * 1. Se crea un arreglo de bytes llamado bufferRecepcion con un tamaño de 512.
		 * Este arreglo se utilizará para recibir los datos del mensaje entrante.
		 * UDP se emplea en la mayoría de las consultas DNS debido a su eficiencia y baja 
		 * latencia, aunque tiene una restricción de tamaño de paquete de 512 bytes
		 */
		byte bufferRecepcion[]= new byte[6000];
		//byte bufferRecepcion[]= new byte[1024];
		
	    try {

			/**
			 * 2. Se inicia un bucle while que se ejecutará indefinidamente mientras la condición sea
			 * verdadera y la variable salir sea falsa.
			 */
		    while (true && !this.salir) {
				/**
				 * 
				 * RECIBIR EL MENSAJE DE CONSULTA DNS
				 * 3. Se crea un objeto DatagramPacket llamado datagramaRecibido con el arreglo bufferRecepcion 
				 * y su longitud. Este objeto se utilizará para recibir el mensaje entrante a través del socket UDP.
				 * 
				 * 4. Se utiliza el método receive() del objeto socketUDP para recibir el datagrama en el 
				 * datagramaRecibido. El programa se bloqueará en este punto hasta que se reciba un mensaje.
				 * 
				 * 5. Se crea un objeto DataInputStream a partir de un ByteArrayInputStream y se pasa el contenido 
				 * de datagramaRecibido.getData() como entrada. Esto permite leer los datos recibidos del datagrama.
				 * 
				 * 6. Se llama al método obtenerDatosPaquete() para procesar los datos recibidos del paquete y realizar 
				 * las acciones necesarias.
				 */
	        	DatagramPacket datagramaRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
	            this.socketUDP.receive(datagramaRecibido);
				DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(datagramaRecibido.getData()));
				obtenerDatosPaquete(dataInputStream);
				
				/**
				 * 
				 * GENERAR EL MENSAJE DE RESPUESTA DNS
				 * 7. Se crea un ByteArrayOutputStream y un DataOutputStream a partir de él. Esto permitirá escribir 
				 * los datos de respuesta en un flujo de bytes.
				 * 
				 * 8. Se llama al método enviarDatosPaquete() pasando el DataOutputStream como argumento. Este método 
				 * se encarga de procesar los datos del paquete y decidir si se deben enviar datos de respuesta.
				 * 
				 * 9. Si enviarDatosPaquete() devuelve verdadero, es decir, se consigue la IP asociada al dominio, se 
				 * obtiene el arreglo de bytes de byteArrayOutputStream y se crea un DatagramPacket llamado respuesta 
				 * con estos datos, la dirección y el puerto del datagramaRecibido.
				 * 
				 * 10. Se utiliza el método send() del objeto socketUDP para enviar la respuesta en el respuesta al 
				 * remitente del mensaje original.
				 */
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

				if(enviarDatosPaquete(dataOutputStream))
				{
					byte[] bufferEnviar = byteArrayOutputStream.toByteArray();
					DatagramPacket respuesta = new DatagramPacket(bufferEnviar, bufferEnviar.length, datagramaRecibido.getAddress(), datagramaRecibido.getPort());
					this.socketUDP.send(respuesta);
				}

		    }
	    }
		 catch (IOException e) {
			e.printStackTrace();
		 }
	}

	/**
	 * 
	 * @param dataInputStream
	 * @throws IOException
	 * Descripción: Método encargado de leer y obtener los datos de un paquete DNS a partir 
	 * de un DataInputStream.
	 */
	public void obtenerDatosPaquete(DataInputStream dataInputStream) throws IOException{
	
		/* Comienzo Lectura de Header */

		/**
		 * 
		 *                                      1  1  1  1  1  1
		 *		  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                      ID                       |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                    QDCOUNT                    |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                    ANCOUNT                    |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                    NSCOUNT                    |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                    ARCOUNT                    |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *
		 */

		head = new Header();

		head.setID(dataInputStream.readShort());
		
		head.setFlag(dataInputStream.readShort());

		head.setQDCOUNT(dataInputStream.readShort());

		head.setANCOUNT(dataInputStream.readShort());

		head.setNSCOUNT(dataInputStream.readShort());

		head.setARCOUNT(dataInputStream.readShort());

		/* Finalización Lectua de Header */

		/* Impresión de Header para verificación */

		System.out.println(head.toString());

		/* Finalización Impresión de Header */

		/* Comienzo Lectura de Question */

		/**
		 * 
		 *                                      1  1  1  1  1  1
		 *		  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                                               |
		 *		/                     QNAME                     /
		 *		/                                               /
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                     QTYPE                     |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                     QCLASS                    |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
         *
		 */

		quest = new Question();

		quest.setQNAME(adaptador.parseQName(dataInputStream));

		quest.setQTYPE(dataInputStream.readShort());

		quest.setQCLASS(dataInputStream.readShort());

		/* Finalización Lectura de Question */

		/* Impresión de Question para verificación */

		System.out.println(quest.toString());

		/* Finalización Impresión de Question */
	}
	
	/**
	 * 
	 * @param dataOutputStream
	 * @throws IOException
	 * Descripción: Genera el paquete determinado para enviar como respuesta DNS.
	 */
	public boolean enviarDatosPaquete(DataOutputStream dataOutputStream) throws IOException
	{
		/* Escritura de la cabecera */

		dataOutputStream.writeShort(head.getID());

		dataOutputStream.writeShort(head.getFlag().generarValorRespuesta());

		dataOutputStream.writeShort(head.getQDCOUNT());

		/* Establecer ANCOUNT como 1 para establecer que es una respuesta */
		dataOutputStream.writeShort((short)1);

		dataOutputStream.writeShort(head.getNSCOUNT());

		dataOutputStream.writeShort(head.getARCOUNT());

		/* Fin de la escritura de la cabecera */
		
		/* Escritura de Question */

		/* Generación del pointer actual */
		int pointerPaquete = dataOutputStream.size();
		int pointerOffset = 0xC000 | pointerPaquete;

		/* Escritura de la dirección IP guardada en Bytes desde el adaptador */
		dataOutputStream.write(adaptador.getByteArrayOutputStream().toByteArray());

		/* Establecer finalización de la escritura de la dirección IP */
		dataOutputStream.writeByte(0);

		dataOutputStream.writeShort(quest.getQTYPE());
		dataOutputStream.writeShort(quest.getQCLASS());

		/* Finalización Escritura de Question */
		
		/* Escritura de Respuesta */

		/**
		 * 
		 *									     1  1  1  1  1  1
		 *		   0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                                               |
		 *		/                                               /
		 *		/                      NAME                     /
		 *		|                                               |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                      TYPE                     |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                     CLASS                     |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                      TTL                      |
		 *		|                                               |
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *		|                   RDLENGTH                    |
		 * 		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--|
		 *		/                     RDATA                     /
		 *		/                                               /
		 *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		 *
		 */

		/* Escritura de NAME */
		dataOutputStream.writeShort(pointerOffset);

		/* TYPE: 1 (A) */
		dataOutputStream.writeShort((short)1);

		/* CLASS: 1 (IN) */
		dataOutputStream.writeShort((short)1);

		/* TTL: 500 (Tiempo):  Cuánto tiempo un registro DNS puede ser almacenado en 
		 * caché antes de que expire y deba ser refrescado desde la fuente de autoridad. */
		dataOutputStream.writeShort(0);
		dataOutputStream.writeShort((short)500);

		
		/* QTYPE = 1 (IPV4) */
		if(quest.getQTYPE()==1)
		{
			/* Búsqueda dirección IP asociada */
			String ipAsociada=obtenerDireccionIp();

			if (ipAsociada == null){
				System.out.println("Direccion no encontrada");
				return false;
			}

			String arregloIP[] = ipAsociada.split("\\.");

			/* RDLENGTH */
			dataOutputStream.writeShort((short)4);

			/* RDATA */
			Integer ipParse;
            for(String str:arregloIP)
			{                
            	ipParse = Integer.valueOf(str);
            	dataOutputStream.writeByte(ipParse.byteValue());
            }

		}

		/* QTYPE = 28 (IPV6) */
		if(quest.getQTYPE()==28)
		{
			/* Búsqueda dirección IP asociada */
			String ipAsociada=obtenerDireccionIp();
			if (ipAsociada == null){
				System.out.println("Direccion no encontrada");
				return false;
			}
			String arregloIP[] = ipAsociada.split("\\.");

			/* RDLENGTH */
			dataOutputStream.writeShort((short)128);

			/* RDATA */
			Integer ipParse;
            for(String str:arregloIP)
			{                
            	ipParse = Integer.valueOf(str);
            	dataOutputStream.writeByte(ipParse.byteValue());
            }
		}

		/* Finalización Escritura de Respuesta */
		return true;
	}
	
	/**
	 * 
	 * @return
	 * Descripción: Método encargado de obtener la dirección IP asociada a un nombre de dominio 
	 * (QNAME) utilizando el objeto manejador y las funcionalidades de resolución de nombres del sistema.
	 * 
	 * 1. Se intenta obtener la dirección IP asociada al nombre de dominio quest.getQNAME() utilizando el 
	 * método obtenerDireccion() del objeto manejador. Este método busca en un HashMap que contiene los 
	 * dominios con sus direcciones IP asociadas si la dirección IP ya ha sido almacenada previamente para 
	 * ese nombre de dominio.
	 * 
	 * 2. Si la dirección IP no se encuentra en la estructura de datos (direccionIpAsociada es null), se 
	 * procede a obtener la dirección IP utilizando InetAddress.getByName(quest.getQNAME()). Este método 
	 * realiza una consulta DNS para resolver el nombre de dominio y obtener la dirección IP correspondiente.
	 * 
	 * 3. Si se obtiene una dirección IP válida, se agrega la asociación entre el nombre de dominio y la 
	 * dirección IP al manejador mediante el método agregarDireccion(). Luego, se guarda el archivo mediante 
	 * manejador.guardarArchivo(nombreArchivo) para persistir los cambios.
	 * 
	 * 4. Se asigna la dirección IP obtenida a la variable direccionIpAsociada y se imprime en la consola 
	 * para verificar.
	 * 
	 * 5. Finalmente, se devuelve la dirección IP obtenida a través de return direccionIpAsociada.

	 */
	public String obtenerDireccionIp()
	{
		try{
			String direccionIpAsociada=manejador.obtenerDireccion(quest.getQNAME());
			if(direccionIpAsociada==null)
			{
				InetAddress direccionIP = InetAddress.getByName(quest.getQNAME());
				manejador.agregarDireccion(quest.getQNAME(),direccionIP.getHostAddress());
				manejador.guardarArchivo(nombreArchivo);
				direccionIpAsociada=direccionIP.getHostAddress();
			}

			System.out.println("\nDOMINIO: " + quest.getQNAME());
			System.out.println("Dirección IP asociada es: "+ direccionIpAsociada);

			return direccionIpAsociada;
		}
		catch(UnknownHostException e)
		{
			System.out.println("La extensión no es válida");
		}
		return null;
	}
}