package exception;

//Exception utilisé dans les classes LectureFichier et Fenetre
@SuppressWarnings("serial")
public class FormatInvalidException extends Exception{
    public FormatInvalidException(String msg){
        super(msg);
    }
}
