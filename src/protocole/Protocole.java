package protocole;

import java.util.ArrayList;

public abstract class Protocole {


    protected ArrayList<String> octets = new ArrayList<>();
    private String name;

    public Protocole(String o, String name){
        //On split en 1 octet la liste d'octet
        for(String s : o.split(" ")){
            octets.add(s);
        }      
        this.name=name;
    }

    /*
     * Converti les octets en hexa en decimale
     */
    public int hexToDec(String hex){
        int dec=-1;
        dec = Integer.parseInt(hex,16);
        return dec;
    }

    /*
     * Converti les octets en hexa
     * @param indice de l'octets dans la trame
     * 
     */
    public int hexToDec(int index){
        String hex=get(index);
        int dec=-1;
        dec = Integer.parseInt(hex,16);
        return dec;
    }
    /*
     * Convertie les octets en decimale en hexa
     */
    public String decToHex(int dec){
        return Integer.toHexString(dec);
    }
    
    /*
     * Donne seulement les 4 bits(en hexa) d'un octet c'est a dire un hexa
     * @return un tableau 
     * Exemple : 
     *          getFourBytes("AF")->[A,F]
     * Pas besoin d'exception car elles sont deja traites auparavant, dans la classe FileReader
     */
    public String[] getBytes(String octet){
        String s[] = new String[octet.length()];
        int i=0;
        for(char c : octet.toCharArray()){
            s[i] = String.valueOf(c);
            i++;
        }
        return s;
    }

    /*
     * Convertit des octets en binaire
     * @return un String codee en binaire (suite de 1 et 0)
     * Exemple : 
     *          hexToBinary("a9")->"10101001"
     */
    public String hexToBinary(String octet){
        String s="";
        String[] tab=getBytes(octet);
        for(String parcours:tab){
            int val = hexToDec(parcours);
            String bin = Integer.toBinaryString(val);
            if(bin.length()<4){
                String zero="";
                for(int i=0;i<4-bin.length();i++){
                    zero+="0";
                }
                bin=zero+bin;
            }
            s=s+bin;
        }
        return s;
    }

    public int binaryToDec(String binary){
        return Integer.parseInt(binary,2);
    }

    public long hexToLong(String hex){
        return Long.parseLong(hex,16);
    }
    /*
     * Converti un hexa en un caractere ascii 
     */
    public String hexToasciiz(String hexStr){
        return ""+(char)Integer.parseInt(hexStr, 16);
    }

    public String hexToasciizs(String hex){
        String res="";
        for(int i=0;i<hex.length();i+=2){
            res+=hexToasciiz(""+hex.charAt(i)+hex.charAt(i+1));
        }
        return res;
    }
    public String get(int index){
        return octets.get(index);
    }

    public String get(int firstIndex,int endIndex){
        String s="";
        for(int i=firstIndex;i<endIndex;i++){
            s+=octets.get(i);
        }
        return s;
    }

    public String toString(){
        return "Protocole : "+name + "\n\n";
    }

    public int size(){
        return octets.size();
    }
}
