package traitement;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import exception.FormatInvalidException;

/** Permet de lire un fichier trace au format text HexDump.
Stock dans une liste de liste les trames ainsi que chaque octet des trames **/

public class LectureFichier {
    private ArrayList<ArrayList<String>> octets= new ArrayList<>() ; // Correspond au octet dans le fichier

    public LectureFichier(String file) throws IOException, FormatInvalidException {
        readLine(file);
        checkOctet();
    }

    private void readLine(String file) throws FormatInvalidException,IOException{
        int ligne=1; // iterateur pour savoir la ligne(utile si une erreur est detecte
        int sizeMaxOctet =16*3+7-1; // La taille a lire 
        int sizeMinOctet=7;
        BufferedReader br;
        br = new BufferedReader(
                new InputStreamReader(
                    new BufferedInputStream(
                        new FileInputStream(new File(file)))));
        
        String line;
        ArrayList<String> octet=new ArrayList<>();
        while((line = br.readLine())!= null){
            if(line.length()==0){
                continue;
            }
            if(line.subSequence(0, 4).equals("0000")){
                if(octet.size()!=0){
                    //Ici on enleve les espaces engendre par le dernier octet
                    String lastListOctet=octet.get(octet.size()-1);
                    while(lastListOctet.charAt(lastListOctet.length()-1)==' '){
                        lastListOctet=lastListOctet.substring(0, lastListOctet.length()-1);
                    }
                    octet.remove(octet.size()-1);
                    String [] s = lastListOctet.split(" ");
                    for(int i =0;i<s.length;i++ ){
                        octet.add(s[i]);
                    }
                }
                octet=new ArrayList<>();
                octets.add(octet);
            }
            if(line.length()<sizeMaxOctet){
                //Le cas ou il manque des octets et non la position des octets 
                if(line.length()>=sizeMinOctet ){
                    if(br.readLine()==null){
                        String getOctet=line.substring(sizeMinOctet, line.length());
                        octet.add(getOctet);
                        break;
                    }
                    else{
                        int lineOctet=line.length()-sizeMinOctet;
                        int nbOctet =(lineOctet)/3; 
                        int nbOctetManquant = 16-nbOctet;
                        throw new FormatInvalidException("Erreur a la ligne "+ ligne+ ", il manque entre "+ (nbOctetManquant-1)  +" et " + nbOctetManquant +" octet");
                    }
                }
                else{
                    throw new FormatInvalidException("Il manque les 4 hexa qui marque la ligne et les 16 octets a la ligne "+ligne);
                }
            }
            String getOctet=line.substring(sizeMinOctet, sizeMaxOctet);
            String [] s = getOctet.split(" ");
            for(int i =0;i<s.length;i++ ){
                octet.add(s[i]);
            }
            ligne++;
        }
        if(octet.size()==0){
            throw new FormatInvalidException("Le fichier est vide...");
        }
        //Ici on enleve les espaces engendre par le dernier octet
        String lastListOctet=octet.get(octet.size()-1);
        while(lastListOctet.charAt(lastListOctet.length()-1)==' '){
            lastListOctet=lastListOctet.substring(0, lastListOctet.length()-1);
        }
        octet.remove(octet.size()-1);
        String [] s = lastListOctet.split(" ");
        for(int i =0;i<s.length;i++ ){
            octet.add(s[i]);
        }
    }
    
    /*
    Permet de tester si la liste d'octet est en hexadecimal et n'est pas falsifier
     */
    private void checkOctet() throws FormatInvalidException{
        int ligne=1;
        int oc=1;
        for(ArrayList<String> octet : octets){
            for(String str: octet){
                for(String o : str.split(" ")){
                    if(!checkHexa(o,ligne)){
                        throw new FormatInvalidException("Les octets sont falsifie a la ligne "+ligne+ " a l'octet "+oc);
                    }
                    oc++;
                }
                ligne++;
                oc=1;
            }
        }
    }

    /*
     * Permet de tester si un octet est en hexadecimale
     */
    private boolean checkHexa(String test,int ligne) throws FormatInvalidException{
        if(test.length()<=1 ){
            throw new FormatInvalidException("Il y a un espace ou un hexa en trop à l'octet "+ligne);
        }
        char o1 = test.toLowerCase().charAt(0);
        char o2 = test.toLowerCase().charAt(1);
        return ((o1>='0' && o1<='9') || (o1>='a' &&  o1<='f')) && ((o2>='0' && o2<='9') || (o2>='a' &&  o2<='f'));
    }

    public ArrayList<String> getOctet(int i){
        return octets.get(i);
    }

    public ArrayList<ArrayList<String>> getOctet(){
        return octets;
    }
    public String toString(){
        String s="";
        for(ArrayList<String> octet : octets){
            for(String str: octet){
                s+=str+" ";
            }
            s+="\n";
        }
        return s;
    }
}
