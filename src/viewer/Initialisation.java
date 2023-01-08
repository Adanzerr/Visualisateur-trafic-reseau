package viewer;

import javax.swing.*;

import java.awt.Dimension;

//crée l'ensemble des boutons et zones de texte de l'interface graphique lors de son demarrage 

@SuppressWarnings("serial")
public class Initialisation extends JFrame{
    public JPanel container;
    public JButton btnFind;
    private JTextField txtPath;
    protected JTextField txtFilter;
    private JTextArea txtOutput;
    public String save="";
    public JButton btnFiltre, btnStart, btnSave, btnSaveDetail;
    public JTable table;
    private JScrollPane scrollPane;
    private Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private final double WIDTH = dimension.getWidth()/1.25;
    private final double HEIGHT = dimension.getHeight()/1.25;
    private double w=WIDTH/600.0;
    private double h=HEIGHT/400.0;
    

    public Initialisation(){
        super();
        initWindow();
        initInputTextFiled();
        initBtnPath();
        initBtnFiltrer();
        initBtnStart();
        initBtnSave();
    }

    /*
     * Creer la fenetre
     */
    private void initWindow(){
        setSize((int)WIDTH,(int)HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(true); 
        this.setTitle("AnalyseurTrafic"); 
        container = new JPanel();
        this.setContentPane(container);
        container.setLayout(null);
    }

    /*
     * Creer le champ où il faut mettre le champ path 
     */
    private void initInputTextFiled(){
        txtPath = new JTextField();
        txtFilter = new JTextField();
        txtPath.setBounds((int)(30*w), (int)(20*h), (int)(440*w), (int)(30*h));
        txtFilter.setBounds((int)(100*w), (int)(78*h), (int)(300*w), (int)(20*h));
        txtFilter.setEditable(false);
        container.add(txtPath);
        container.add(txtFilter);
    }

    /*
     * Creer le champ où il faut appuyer le bouton path 
     */
    private void initBtnPath(){
        btnFind = new JButton();
        btnFind.setBounds((int)(480*w), (int)(17*h), (int)(100*w), (int)(35*h));
        btnFind.setText("Path");
        container.add(btnFind);
    }

    private void initBtnFiltrer(){
        btnFiltre = new JButton();
        btnFiltre.setBounds((int)(480*w), (int)(70*h), (int)(100*w), (int)(35*h));
        btnFiltre.setText("Filtrer");
        container.add(btnFiltre);
    }

    private void initBtnStart(){
        btnStart = new JButton();
        btnStart.setBounds((int)(100*w), (int)(310*h), (int)(100*w), (int)(35*h));
        btnStart.setText("Decoder");
        container.add(btnStart);
    }

    private void initBtnSave() {
        btnSave = new JButton();
        btnSave.setBounds((int)(250*w), (int)(310*h), (int)(100*w), (int)(35*h));
        btnSave.setText("Sauvegarder");
        container.add(btnSave);
    }

    public void createError(String message){
        JOptionPane.showMessageDialog(this,
        message,
        "Erreur",
        JOptionPane.ERROR_MESSAGE);
    }

    /*
     * Enleve le chaine de caractere du path
     */
    public void clearOutput(){
        this.txtOutput.setText("");
    }

    public Initialisation appendOutput(String text){
        this.txtOutput.append(text);
        return this;
    }

    public void setPath(String path){
        this.txtPath.setText(path);
    }

    public String getPath(){
        return this.txtPath.getText();
    }

    public String getFilter(){
        return this.txtFilter.getText();
    }

    public JTable getTable(){
        return table;
    }

   
    public void setScrollPane(JScrollPane scrollPane2) {
        if(scrollPane!=null){
            container.remove(scrollPane);
        }
        scrollPane=scrollPane2;
        container.add(scrollPane);
    }

    public JTextArea getTxtOutput(){
        return txtOutput;
    }

    public void setTxtOutput(JTextArea t){
        txtOutput=t;
    }

    public double getW(){
        return w;
    }

    public double getH(){
        return h;
    }

    public void refreshScreen(){
        btnSave.setBounds((int)(250*w), (int)(310*h), (int)(100*w), (int)(35*h));
        btnFiltre.setBounds((int)(480*w), (int)(70*h), (int)(100*w), (int)(35*h));
        btnFind.setBounds((int)(480*w), (int)(17*h), (int)(100*w), (int)(35*h));
        btnSaveDetail.setBounds((int)(400*w), (int)(310*h), (int)(100*w), (int)(35*h));
        txtPath.setBounds((int)(30*w), (int)(20*h), (int)(440*w), (int)(30*h));
        txtFilter.setBounds((int)(100*w), (int)(78*h), (int)(300*w), (int)(20*h));
    }

}