package exception;

//Exception utilisé dans les classes Encapsulation Ethernet IPv4 MultiTrame
@SuppressWarnings("serial")
public class OctetInvalidException extends Exception{
    public OctetInvalidException(String msg){
        super(msg);
    }
}