package exception;

////Exception utilisé dans les classes Encapsulation et MultiTrame
@SuppressWarnings("serial")
public class ProtocoleInvalidException extends Exception{
    public ProtocoleInvalidException(String msg){
        super(msg);
    }
}
