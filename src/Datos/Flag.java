package Datos;

public class Flag {
    /**
    * 
    *                                       1  1  1  1  1  1
    *		  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
    *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    *		|                      ID                       |
    *		+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    * Flag->|QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
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
    private boolean QR;
    private byte Opcode;
    private boolean AA;
    private boolean TC;
    private boolean RD;
    private boolean RA;
    private byte Z;
    private byte RCODE;
    
    public Flag() {}
    public boolean isQR() {
        return QR;
    }
    public void setQR(boolean qR) {
        QR = qR;
    }
    public byte getOpcode() {
        return Opcode;
    }
    public void setOpcode(byte opcode) {
        Opcode = opcode;
    }
    public boolean isAA() {
        return AA;
    }
    public void setAA(boolean aA) {
        AA = aA;
    }
    public boolean isTC() {
        return TC;
    }
    public void setTC(boolean tC) {
        TC = tC;
    }
    public boolean isRD() {
        return RD;
    }
    public void setRD(boolean rD) {
        RD = rD;
    }
    public boolean isRA() {
        return RA;
    }
    public void setRA(boolean rA) {
        RA = rA;
    }
    public byte getZ() {
        return Z;
    }
    public void setZ(byte z) {
        Z = z;
    }
    public byte getRCODE() {
        return RCODE;
    }
    public void setRCODE(byte rCODE) {
        RCODE = rCODE;
    }

    /**
     * 
     * @return
     * Descripción: Genera el valor numérico del campo de respuesta DNS utilizando las 
     * propiedades de la instancia actual, asignando los bits correspondientes a las 
     * diferentes banderas según su estado.
     */
    public short generarValorRespuesta() {
        /**
         * 1. Se inicializa la variable result como 0, que es el valor inicial del campo de respuesta.
         * 
         * 2. Se utiliza la operación OR binaria y desplazamientos a la izquierda para asignar los valores 
         * de las diferentes banderas al campo result, de acuerdo con las propiedades de la instancia actual.
         * 
         * 3. Si la bandera QR es falsa (indicando que no es una respuesta), se establece el bit más 
         * significativo (MSB) del campo result utilizando result |= 1 << 15.
         * 
         * 4. Se asigna el valor del opcode mediante el operador OR binario y un desplazamiento de bits a 
         * la izquierda: result |= (getOpcode() & 0xF) << 11.
         *
         * 5. Se verifica cada una de las banderas (AA, TC, RD, RA) y, si son verdaderas, se establece el 
         * bit correspondiente en el campo result utilizando el operador OR binario y un desplazamiento de 
         * bits a la izquierda.
         * 
         * 6. Si la bandera RA es falsa (indicando que no se admite la recursión), se establece el bit 
         * correspondiente en el campo result utilizando el operador OR binario y un desplazamiento de bits 
         * a la izquierda.
         * 
         * 7. Se asigna el valor del campo Z mediante el operador OR binario y un desplazamiento de bits a 
         * la izquierda: result |= (getZ() & 0x7) << 4.
         * 
         * 8. Se asigna el valor del campo RCODE mediante el operador OR binario: result |= getRCODE() & 
         * 0xF.
         * 
         * 9. Finalmente, se devuelve el valor resultante del campo de respuesta.
         */
        short result = 0;

        if (!isQR()) {
            result |= 1 << 15;
        }

        result |= (getOpcode() & 0xF) << 11;

        if (isAA()) {
            result |= 1 << 10;
        }

        if (isTC()) {
            result |= 1 << 9;
        }

        if (isRD()) {
            result |= 1 << 8;
        }

        if (!isRA()) {
            result |= 1 << 7;
        }

        result |= (getZ() & 0x7) << 4;
        result |= getRCODE() & 0xF;

        return result;
    }

    @Override
    public String toString() {
    return "Flag [QR=" + (QR ? 1 : 0) + ", Opcode=" + Opcode + ", AA=" + (AA ? 1 : 0) + ", TC=" + (TC ? 1 : 0)
            + ", RD=" + (RD ? 1 : 0) + ", RA=" + (RA ? 1 : 0) + ", Z=" + Z + ", RCODE=" + RCODE + "]";
    }
}
