package protocole;
import java.util.ArrayList;

import exception.OctetInvalidException;
import exception.ProtocoleInvalidException;
import traitement.LectureFichier;

public class MultiTrame {

    private ArrayList<Encapsulation> trames;
    public MultiTrame(LectureFichier fr) throws ProtocoleInvalidException, OctetInvalidException{
        trames=new ArrayList<>();
        int i=1;
        for(ArrayList<String> str : fr.getOctet()){
            trames.add(new Encapsulation(i, str));
            i++;
        }
        try{
            traitement();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Encapsulation> getTrames() {
        return trames;
    }
    
    public int size(){
        return trames.size();
    }
    

    private void traitement(){
        ArrayList<Encapsulation> container = new ArrayList<>();
        try{
            for(Encapsulation trame :trames){
                if(!container.contains(trame)){
                    if(trame.getTCP()!=null){
                        if(trame.getTCP().isSYN()){
                            modifier(trame,container);
                        }
                    }
                }
            } 
        }
        catch(NullPointerException e){}
    }
    
    private void modifier(Encapsulation trame,ArrayList<Encapsulation> container ){
        TCP tcp = trame.getTCP();
        long ack = tcp.getAcknowledgment();
        long seq = tcp.getSequence();
        boolean acknull=ack==0;
        tcp.setACK(0);
        tcp.setSEQ(0);
        container.add(trame);
        for(Encapsulation trame2 : trames){
            if(!container.contains(trame2)){
                TCP tcp2 = trame2.getTCP();
                if(tcp2.isSYN()){
                
                    break;
                }
                if(trame.equalsTransaction(trame2)){
                    tcp2.setACK(tcp2.getAcknowledgment()-ack);
                    tcp2.setSEQ(tcp2.getSequence()-seq);
                    container.add(trame2);
                }
                else{
                    if(trame.equalsContraryTransaction(trame2)){
                        if(acknull){
                            ack=tcp2.getSequence();
                            tcp2.setACK(1);
                            acknull=false;
                        }
                        else{
                            tcp2.setACK(tcp2.getAcknowledgment()-seq);
                        }
                        tcp2.setSEQ(tcp2.getSequence()-ack);
                        container.add(trame2);
                    }
                }
            }
        }
    }

    public ArrayList<Encapsulation> cloneTrames(){
        ArrayList<Encapsulation> res = new ArrayList<>();
        for(Encapsulation trame :trames){
            res.add(trame);
        }
        return res;
    }
}
