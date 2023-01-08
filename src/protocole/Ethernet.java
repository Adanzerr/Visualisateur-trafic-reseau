package protocole;

import exception.OctetInvalidException;

/**
 * Classe de representation d'une entete Ethernet
 */
public class Ethernet extends Protocole {
    
    private String typeProtocolCode;    //12e a 13e octets
    private String typeProtocol;
    private String crc;

    /**
     * Constructeur recupere les octets de la trame, analyse les 14 premiers octets
     * @throws OctetInvalidException
     */

    public Ethernet(String entete) throws OctetInvalidException{

         //en Tout 14 octets de l'entete Ethernet traiter
        super(entete,"Ethernet");
        //12e a 13e octets
        typeProtocolCode = get(12)+get(13);
        setTypeProtocoleCode();
    }

    /**
     * Dans le cas ou on a une avec un crc
     * @param entete
     * @param enqueue
     * @throws OctetInvalidException
     */
    public Ethernet(String entete, String enqueue) throws OctetInvalidException{
        this(entete);
        crc=enqueue;
    }

    private void setTypeProtocoleCode() throws OctetInvalidException{
        if(typeProtocolCode.equalsIgnoreCase("0800")) {
        	typeProtocol = "IPv4";
        }
        if(typeProtocolCode.equalsIgnoreCase("86DD")) {
        	typeProtocol = "IPv6";
        }
        if(typeProtocolCode.equalsIgnoreCase("0806")) {
        	typeProtocol = "ARP";
        }
        if(typeProtocolCode.equalsIgnoreCase("8035")) {
        	typeProtocol = "RARP";
        }
        if(typeProtocolCode.equalsIgnoreCase("809B")) {
        	typeProtocol = "AppleTalk";
        }
        if(typeProtocolCode.equalsIgnoreCase("88CD")) {
        	typeProtocol = "SRECOS III";
        }
        if(typeProtocolCode.equalsIgnoreCase("0600")) {
        	typeProtocol = "XNS";
        }
        if(typeProtocolCode.equalsIgnoreCase("8100")) {
        	typeProtocol = "VLAN";
        }
        if(typeProtocol==null){
            typeProtocol="???";
            throw new OctetInvalidException("Le type du protocol apres la couche 2(ethernet) n'existe pas!");
           
        }
    }
    
    /*
     * Verifie si la prochaine couche est IPv4
     */
    public boolean nextIsIPv4(){
        return typeProtocol.equalsIgnoreCase("IPv4");
    }
     
    
    public String getNextProtocol(){
        return typeProtocol;
    }

}