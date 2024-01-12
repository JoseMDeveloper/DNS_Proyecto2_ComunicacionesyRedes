package Utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ManejadorArchivo {
    private HashMap<String,String> dominiosIP;

    public ManejadorArchivo() {
        this.dominiosIP = new HashMap<String,String>();
    }
    
    public HashMap<String, String> getDominiosIP() //1. Direccion de dominio    2. Direccion Ip
    {
        return dominiosIP;
    }

    public void setDominiosIP(HashMap<String, String> dominiosIP) {
        this.dominiosIP = dominiosIP;
    }

    /**
     * 
     * @param nombreArchivo: Dirección del archivo
     * @throws IOException
     * @throws FileNotFoundException
     * Descripción: Carga los datos del archivo Master File y los almacena al HashMap
     * asociado al dominio y la IP, el dominio es la llave y la IP es el dato que se obtiene
     * dada la llave.
     */
    public void cargarArchivo(String nombreArchivo) throws IOException,FileNotFoundException{
        InputStreamReader input = new InputStreamReader(new FileInputStream(nombreArchivo));
        BufferedReader leer = new BufferedReader(input);
        String cad = leer.readLine();
        String aux[];
        cad = leer.readLine();

        while(!cad.equalsIgnoreCase("Comunicaciones y Redes"))
        {
            aux = cad.split(" ");
            dominiosIP.put(aux[0],aux[1]);
            cad = leer.readLine();
        }
        leer.close();
    }

    /**
     * 
     * @param referencia: Dominio asociado a la IP
     * @return
     * Descripción: Obtener dirección IP asociada dado el dominio.
     */
    public String obtenerDireccion(String referencia){
        return dominiosIP.get(referencia);
    }

    /**
     * 
     * @param nombreArchivo: Dirección del archivo
     * Descripción: Guardar los datos que se encuentran dentro del HashMap en el MakeFile.
     * Sobreescribiendo los datos especificados.
     */
    public void guardarArchivo(String nombreArchivo) {
        try {
            File file = new File(nombreArchivo);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write("Realizado por: Miguel Gonzalez, Fabio Buitrago, Jose Rodriguez");
            bw.newLine();
            
            for (Map.Entry<String, String> entry : dominiosIP.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                bw.write(key + " " + value);
                bw.newLine();
            }
            
            bw.write("Comunicaciones y Redes");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param dominio: Dominio dado por el usuario
     * @param direccion: Dirección IP asociada
     * Descripción: Añadir la dirección IP asociada el dominio en el HashMap.
     */
    public void agregarDireccion(String dominio, String direccion){
        dominiosIP.put(dominio,direccion);
    }

    @Override
    public String toString() {
        return "ManejadorArchivo [dominiosIP=" + dominiosIP + "]";
    }

    
}
