package viewer;

import java.awt.event.ActionEvent;

import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import traitement.LectureFichier;
import protocole.Encapsulation;
import protocole.MultiTrame;


/* Créer le tableau de l'interface graphique contenant les informations des differentes trames comme les adresses IP , 
 * le protocole ainsi que les informations contenu dans l'entete de ce protocole
 */

public class Tableau{
    
    private JTable table,tableFiltrer;
    private Fenetre fenetre;
    private LectureFichier file;
    private String[][] data;
    private JTextArea txt;
    JScrollPane scrollPane ,scroll2;
    private double w;
    private double h;
    private int cpt;
    private ArrayList<String[]> arrayfiltre;
    private String[] title = {"No","IP Source","IP Destination","Protocol","Length","Info"}; ;
    public Tableau(LectureFichier file, Fenetre fenetre){
        this.fenetre=fenetre;
        w=fenetre.getW();
        h=fenetre.getH();
        this.file=file;
        table=fenetre.getTable();
        txt = new JTextArea();
        initTable();
        fenetre.setResult(txt.getText());
    }

    /*
     * Initialise un tableau lorsque le bouton fenetre.btnDetail est en mode essential
     */
    private void initTable(){
        try{
            fenetre.setResult("");
            int i=1;
            MultiTrame trames = new MultiTrame(file);
            data= new String[trames.size()][];
            for(Encapsulation ft : trames.getTrames()){
                data[i-1]=ft.getData(i);
                txt.append(ft.getEssential(i));
                i++;
            } 
            table = new JTable(data,title);
            table.setEnabled(false);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(130);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.setRowHeight(35);
           
            scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBounds((int)(22*w), (int)(2*60*h), (int)(556*w), (int)((240*h)/1.5));
            fenetre.setScrollPane(scrollPane);
            cpt=0;
            for(int j=0;j<title.length-1;j++){
                cpt+=table.getColumnModel().getColumn(j).getWidth();
            }
            table.getColumnModel().getColumn(title.length-1).setPreferredWidth((int)(600*w-(w*22+cpt)));
         }
        catch (Exception e) {
            fenetre.createError(e.getMessage());
        }
    }

    private void defiltrer(){
        scroll2.setVisible(false);
        scrollPane.setVisible(true);
    }

    public boolean accept(String filter,String[] data) throws Exception{
        if(!filter.contains("&&") && !filter.contains("||")){
            return data[3].equalsIgnoreCase(filter) || data[1].equalsIgnoreCase(filter);
        }
        else{
            String saveFilter=filter;
            String tab[] = saveFilter.split(" && ");
            if(tab.length>1){
                String s="";
                for(int i=1;i<tab.length;i++){
                    s+=tab[i];
                }
                boolean b = accept(tab[0], data) && accept(s, data);
                return b;
            }
            else{
                System.out.println("aAAAA   " + filter);
                String tab2[] = filter.split(" \\|\\| ");
                if(tab2.length>1){
                    String s="";
                    for(int i=1;i<tab2.length;i++){
                        s+=tab2[i];
                    }
                    return accept(tab2[0], data) || accept(s, data);
                }
                return false;
            }
        }
    }

    public void filtreButton(ActionEvent e) {
        String s = fenetre.getFilter();
       
        if(s.length()==0){
            defiltrer();
            return;
        }
        if(scroll2!=null){
            defiltrer();
        }
        arrayfiltre = new ArrayList<>();
        try{
            for(int i=0;i<data.length;i++){
                if(accept(s,data[i])){
                    arrayfiltre.add(data[i]);
                }
            }
            if(arrayfiltre.size()==0){
                throw new Exception();
            }
            String[][] datafiltre= new String[arrayfiltre.size()][];
            int j=0;
            for(String[] data : arrayfiltre){
                datafiltre[j]=data;
                j++;
            }
            tableFiltrer= new JTable(datafiltre,title);
            tableFiltrer.setEnabled(false);
            tableFiltrer.setRowHeight(30);
            tableFiltrer.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableFiltrer.getColumnModel().getColumn(1).setPreferredWidth(130);
            tableFiltrer.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableFiltrer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tableFiltrer.getColumnModel().getColumn(title.length-1).setPreferredWidth((int)(556*w-(w*22+cpt)));
            scrollPane.setVisible(false);
            scroll2 = new JScrollPane(tableFiltrer,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll2.setBounds((int)(22*w), (int)(2*60*h), (int)(556*w), (int)((240*h)/1.5));
            fenetre.add(scroll2);
        }
        catch(Exception ex){
            fenetre.createError("Ce filtre ne marche pas!");
        }
    }

    public JTextArea getTxt(){
        return txt;
    }
}
