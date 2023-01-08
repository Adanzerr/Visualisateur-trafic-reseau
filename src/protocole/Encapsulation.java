package protocole;

import java.util.ArrayList;

import exception.OctetInvalidException;
import exception.ProtocoleInvalidException;

//La classe Encapsulation permet de regrouper tout les protocoles d'une trame.Elle contient egalement des methode pour créer le tableau de l'interface graphique
public class Encapsulation {
    
    private ArrayList<String> octets;
    private int id;
    private Ethernet eth;
    private IPv4 ipv4=null;
    private TCP tcp=null;
    private HTTP http;
    
    /*
     * c'est une facade de la trame qui permet de regrouper tout les protocoles d'une trame;
     */
    public Encapsulation(int id,ArrayList<String> octets) throws ProtocoleInvalidException, OctetInvalidException{
        this.octets=octets;
        // On recupere l'entete de eth dans la trame puis on l'instancie
        this.id=id;
        int index=0;
        String e = subList(index, index+13);
        index+=13;
        eth = new Ethernet(e);
        try{
            //On verifie si la couche suivante est bien ipv4
            if(!eth.nextIsIPv4()){
                throw new ProtocoleInvalidException("Protocole IPv4 et non un protocole "+eth.getNextProtocol()+" pour la couche 3!");
            }
            // On recupere l'entete de ipv4 dans la trame puis on l'instancie(sans l'option)
            String ip = subList(index+1, index+20);
            ipv4 = new IPv4(ip);
            if(!ipv4.nextIsTCP()){
                throw new ProtocoleInvalidException("Protocole TCP et non un protocole "+ipv4.getNextProtocol()+" pour la couche 4!");
            }
            //On verfie s'il y a une option en regardant la longueur de l"entete ip
            int lengthIPv4 = ipv4.getLength();
            //Si la longueur est supérieur a 20 alors on reinstancie ipv4 avec la nouvelle taille 
            if(lengthIPv4!=20){
                ip= subList(index+1,index+lengthIPv4);
                ipv4 = new IPv4(ip);
            }
            index+=lengthIPv4;
            e=subList(index+1, octets.size()-1);
            tcp=new TCP(e);
            index+= tcp.getLength();
            if(tcp.getHasNext()){
                http =new HTTP(subList(index, octets.size()-1));
            }
        }catch(ProtocoleInvalidException e1){} 
    }
    
    /*
     * Pour creer une table dans l'interface
     */
    public String[] getData(int i){
        String protocol = "";
        String info = "";
        if(ipv4==null){
            String[] res = {""+i,"??","??",eth.getNextProtocol(),""+octets.size(),"Pas d'information sur cette trame"};
            return res;
        }
        if(tcp==null){
            String[] res = {""+i,ipv4.getSrc(),ipv4.getDest(),ipv4.getNextProtocol(),""+octets.size(),"Pas d'information sur cette trame" };
            return res;
        }
        if(http==null){
            protocol="TCP";
            info+=tcp.essential();
        }
        else{
            protocol="HTTP";
            info += http.info();
        }
        String[] res = {""+i,ipv4.getSrc(),ipv4.getDest(),protocol,""+octets.size(),info};
        return res; 
    }
    
    /*
     * Met sous forme de sous forme de String d'octets, decoupe pour un espace, la liste d'octet commencant par @indexDebut et finissant par @indexFin
     */
    private String subList(int indexDebut,int indexFin){
        String res = "";
        for(int i=indexDebut;i<=indexFin-1;i++){
            res+=octets.get(i)+ " ";
        }
        res+=octets.get(indexFin);
        return res;
    }
    
    public String getEssential(int i){
        StringBuilder sb = new StringBuilder();
        if(ipv4==null){
            sb.append(""+i+" ? "+" ? "+" "+eth.getNextProtocol()+" "+ octets.size()+ " Pas d'information sur cette trame\n" );
            return sb.toString();
        }
        if(tcp==null){
            sb.append(""+i+" "+ipv4.getSrc()+" "+ipv4.getDest()+" "+ipv4.getNextProtocol()+" "+ octets.size()+ " Pas d'information sur cette trame\n" );
            return sb.toString();
        }
        sb.append(""+i+" "+ipv4.getSrc()+" "+ipv4.getDest()+" ");
        if(http==null){
            sb.append("TCP ");
            sb.append(octets.size()+" ");
            sb.append(tcp.essential()+"\n");
        }
        else{
            sb.append("HTTP ");
            sb.append(octets.size()+" ");
            sb.append(http.info()+"\n");
        }
        return sb.toString();
    }


    public TCP getTCP(){
        return tcp;
    }

    public IPv4 getIPv4(){
        return ipv4;
    }

    public boolean equalsTransaction(Encapsulation ft){
        if(this==ft) return false;
        return ft.getTCP().getSrc() == getTCP().getSrc() && ft.getTCP().getDest() == getTCP().getDest() && ft.getIPv4().getSrc().equalsIgnoreCase(getIPv4().getSrc()) &&  ft.getIPv4().getDest().equalsIgnoreCase(getIPv4().getDest());
    }

    public boolean equalsContraryTransaction(Encapsulation ft){
        return ft.getTCP().getSrc() == getTCP().getDest() && ft.getTCP().getDest() == getTCP().getSrc() && ft.getIPv4().getSrc().equalsIgnoreCase(getIPv4().getDest()) &&  ft.getIPv4().getDest().equalsIgnoreCase(getIPv4().getSrc());
    }
}
