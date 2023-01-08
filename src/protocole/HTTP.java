package protocole;

import java.util.ArrayList;
import java.util.List;

//Classe de representation d'une entete HTTP

public class HTTP extends Protocole{

    private String version="";
    private String codeStatut="";
    private String message="";
    private String requete;
    private List<String[]> entete= new ArrayList<>();
    private int index=0;
    
    public HTTP(String o) {
        super(o,"HTTP");
        version = separateChampEspace();
        index++;
        codeStatut = separateChampEspace();
        index++;
        message = separateChampLigne();
        index+=2;
        requete = version + " " + codeStatut + " " + message;
        try{
            while(!get(index, index+2).equalsIgnoreCase("0d0a")){
                String champEntete = separateChampEspace();
                index++;
                String valChamp = separateChampLigne();
                index+=2;
                String [] ajt = {champEntete,valChamp};
                entete.add(ajt);
            }
        }
        catch(Exception e){

        }
    }

    public static boolean isNext(String index){
        if(index.contains("GET") || index.contains("HTTP")){
            return true;
        }
        return false;
    }
    /*
     * Correspond au nom du champs d'entete dans les ligne d'entete
     */
    private String separateChampEspace(){
        String res="";
        while(!get(index).equals("20")){
            res+=hexToasciiz(get(index++));
        }
        return res;
    }

    /*
     * Correspond au valeur du champ dans les ligne d'entete
     */
    private String separateChampLigne(){
        String res="";
        while(!get(index,index+2).equals("0d0a")){
            res+=hexToasciiz(get(index++));
        }
        return res;
    }

    public String info(){
        return requete;
    }
}