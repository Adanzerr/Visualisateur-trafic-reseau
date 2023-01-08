package protocole;

import java.util.ArrayList;
import java.util.List;

import exception.OctetInvalidException;

//Classe de representation d'une entete IPV4

public class IPv4 extends Protocole{

   private String src;
   private String des;
   private int version;
   private int headerLength;
   private int lengthTotal;
   private int ttl;
   private String  protocole;
   private String protocoleNom;
   private List<String> options = new ArrayList<>();

   public IPv4(String o) throws OctetInvalidException {
      super(o,"IPv4");
      String p1[]= getBytes(get(0));
      //4 premier octets
      version =  hexToDec(p1[0]);
      headerLength = hexToDec(p1[1])*4;

      lengthTotal = hexToDec(get(2)+get(3));
      //4 a 8eme octet
      
      //8 a 12 octet
      ttl = hexToDec(get(8));
      protocole = get(9);
      setProtocoleNom();
      src = getIP(12);
      des = getIP(16);
      if(lengthTotal>20 && size()>20){
         setOption();
      }
   }

   /*
    * Permet de savoir quel est le nom du prochain protocole
    */
   private void setProtocoleNom() throws OctetInvalidException{
      if(protocole.equalsIgnoreCase("01")) {
    	  protocoleNom="ICMP";
      }
      if(protocole.equalsIgnoreCase("02")) {
    	  protocoleNom="IGMP";
      }
      if(protocole.equalsIgnoreCase("06")) {
    	  protocoleNom="TCP";
      }
      if(protocole.equalsIgnoreCase("08")) {
    	  protocoleNom="EGP";
      }
      if(protocole.equalsIgnoreCase("09")) {
    	  protocoleNom="IGP";
      }
      if(protocole.equalsIgnoreCase("11")) {
    	  protocoleNom="UDP";
      }
      if(protocole.equalsIgnoreCase("24")) {
    	  protocoleNom="XTP";
      }
      if(protocole.equalsIgnoreCase("2e")) {
    	  protocoleNom="RSVP";
      }
      if(protocoleNom==null){
         protocoleNom="???";
         throw new OctetInvalidException("Le type du protocol apres la couche 3(ipv4) n'existe probablement pas!");
      }
   }

   /*
   * Verifie si la prochaine couche est TCP
   */
   public boolean nextIsTCP(){
      return protocoleNom.equalsIgnoreCase("TCP");
   }
   
   public String getNextProtocol(){
      return protocoleNom;
   }

   public int getLength(){
      return headerLength;
   }

   private String getIP(int begin){
      return  hexToDec(get(begin++))+"."+hexToDec(get(begin++))+"."+hexToDec(get(begin++))+"."+hexToDec(get(begin++));
   }

   public void setOption(){
      int lengthOption=lengthTotal-20;
      int i=0;
      while(i<lengthOption){
         // 1001
         String write="\t\t";
         String type = get(20+i);
         if(type.equalsIgnoreCase("01"))write+="NOP : ";
         if(type.equalsIgnoreCase("44"))write+="TS : ";
         if(type.equalsIgnoreCase("83"))write+="LSR : ";
         if(type.equalsIgnoreCase("89"))write+="SSR : ";
         i++;
         int length = hexToDec(get(20+i));
         i++;
         if(type.equalsIgnoreCase("07")){
            write+="RR : \n";
            int j=1;
            while(4*j<length){
               write+= "\t\t\t"+j+" : "+getIP(i+(j*4)+2);
               if(4*(j+1)<length){
                  write+="\n";
               }
               j++;
            }
         }
         else{
            if(type.equalsIgnoreCase("00")){
               write+="EOOL\n" ;
               options.add(write);
               break;
            }
            write+="0x"+get(20+i,20+length-2);
         }
         i+=length-2;
         write+="\n";
         options.add(write);
      }

   }

   public String getSrc(){
      return ""+src;
   }

   public String getDest(){
      return ""+des;
   }
}
