package Datos;

public class Question {

    /**
    * 
    *                                       1  1  1  1  1  1
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

    private String QNAME;
    private short QTYPE;
    private short QCLASS;

    public Question() {
    }

    public String getQNAME() {
        return QNAME;
    }
    public void setQNAME(String qNAME) {
        QNAME = qNAME;
    }
    public short getQTYPE() {
        return QTYPE;
    }
    public void setQTYPE(short qTYPE) {
        QTYPE = qTYPE;
    }
    public short getQCLASS() {
        return QCLASS;
    }
    public void setQCLASS(short qCLASS) {
        QCLASS = qCLASS;
    }

    @Override
    public String toString() {
        return "\nInformación Question:\n ->QNAME es: " + QNAME + 
                                    "\n ->QTYPE es: " + Integer.toBinaryString(QTYPE) +  
                                    "\n ->QCLASS es: " + Integer.toBinaryString(QCLASS) +"\n";
    }   
}
