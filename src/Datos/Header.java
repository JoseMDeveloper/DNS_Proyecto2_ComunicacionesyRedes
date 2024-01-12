package Datos;

public class Header {
    /**
    * 
    *                                       1  1  1  1  1  1
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
    private short ID;
    private Flag flag;
    private short QDCOUNT;
    private short ANCOUNT;
    private short NSCOUNT;
    private short ARCOUNT;
    
    public Header() {
    }

    public short getID() {
        return ID;
    }
    public void setID(short iD) {
        ID = iD;
    }
    public Flag getFlag() {
        return flag;
    }
    /**
     * 
     * @param flag
     * Descripción: onfigurar los valores de las diferentes banderas (flags) 
     * en un campo de 16 bits representado por el parámetro flag.
     * Se utilizan operaciones de desplazamiento y máscaras para extraer los 
     * valores de las banderas individuales a partir del campo flag.
     */
    public void setFlag(short flag) {
        Flag flagAuc=new Flag();
        flagAuc.setQR(((flag>>15)&1)==1);
        flagAuc.setOpcode((byte)((flag>>11)&0xF));
        flagAuc.setAA(((flag>>10)&1)==1);
        flagAuc.setTC(((flag>>9)&1)==1);
        flagAuc.setRD(((flag>>8)&1)==1);
        flagAuc.setRA(((flag>>7)&1)==1);
        flagAuc.setZ((byte)((flag>>4)&0xF));
        flagAuc.setRCODE((byte)((flag)&0xF));
        this.flag = flagAuc;
    }
    public short getQDCOUNT() {
        return QDCOUNT;
    }
    public void setQDCOUNT(short qDCOUNT) {
        QDCOUNT = qDCOUNT;
    }
    public short getANCOUNT() {
        return ANCOUNT;
    }
    public void setANCOUNT(short aNCOUNT) {
        ANCOUNT = aNCOUNT;
    }
    public short getNSCOUNT() {
        return NSCOUNT;
    }
    public void setNSCOUNT(short nSCOUNT) {
        NSCOUNT = nSCOUNT;
    }
    public short getARCOUNT() {
        return ARCOUNT;
    }
    public void setARCOUNT(short aRCOUNT) {
        ARCOUNT = aRCOUNT;
    }

    @Override
    public String toString() {
        return "\nInformación Header:\n ->El ID es: " + Integer.toBinaryString(ID) + 
                                    "\n ->Flag es: " + flag.toString() + 
                                    "\n ->QDCOUNT: " + Integer.toBinaryString(QDCOUNT) + 
                                    "\n ->ANCOUNT: " + Integer.toBinaryString(ANCOUNT) + 
                                    "\n ->NSCOUNT: " + Integer.toBinaryString(NSCOUNT) + 
                                    "\n ->ARCOUNT: " + Integer.toBinaryString(ARCOUNT) +"\n";
    }

    
}
