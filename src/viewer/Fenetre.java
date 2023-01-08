package viewer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import exception.FormatInvalidException;
import protocole.Encapsulation;
import protocole.MultiTrame;
import traitement.LectureFichier;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.Label;
import java.awt.Toolkit;

/**
 *  gere les interractions de l'utilsateurs avec les boutons filtre , decoder , sauvegarder ainsi que les differentes zone de texte 

 */
public class Fenetre extends Initialisation {

	
	private static final long serialVersionUID = 1L;
	private LectureFichier filereader;
    private Tableau table;
    
    public Fenetre() {
        super();
        setIconImage(Toolkit.getDefaultToolkit().getImage(Fenetre.class.getResource("/viewer/sorbonne.png")));
        setForeground(new Color(0, 0, 0));
        btnStart.setBounds(288, 550, 295, 72);
        btnSave.setBounds(761, 550, 295, 72);
        getContentPane().setBackground(new Color(128, 128, 128));
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0, 0, 0));
        separator.setBackground(new Color(0, 0, 0));
        separator.setBounds(181, 523, 924, 26);
        getContentPane().add(separator);
        
        JLabel label = new JLabel("");
        label.setBounds(0, 507, 92, 155);
        ImageIcon img = new ImageIcon(this.getClass().getResource("sorbonne.png"));
        label.setIcon(img);
        getContentPane().add(label);
        
        
        
        btnSave.setBackground(new Color(192, 192, 192));
        btnStart.setBackground(new Color(192, 192, 192));
        btnFiltre.setBackground(new Color(192, 192, 192));
        btnSave.setForeground(new Color(0, 0, 0));
        btnSave.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
        btnFiltre.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
        btnFind.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
        btnStart.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
        btnFind.setBackground(new Color(192, 192, 192));
        btnFind.setText("Fichier TXT");
        txtFilter.setBounds(64, 134, 645, 34);
        btnStart.setForeground(new Color(0, 0, 0));
        btnFiltre.setForeground(new Color(0, 0, 0));
        btnFind.setForeground(new Color(0, 0, 0));
        txtFilter.setForeground(new Color(0, 0, 0));
        txtFilter.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
        txtFilter.setBackground(new Color(255, 255, 255));

        try{
            addAction();
        }
        catch(Exception e){
            createError("Erreur de fichier?");
        }
    }

    public void addAction(){
        btnFind.addActionListener(e -> {
                try {
                    pathButton(e);
                } catch (IOException | FormatInvalidException e1) {
                    createError(e1.getMessage());
                }
        });
        btnStart.addActionListener(e ->{
            try {
                startButton(e);
            } catch (IOException | FormatInvalidException e1) {
                createError(e1.getMessage());
            }});
        btnSave.addActionListener(this::saveButton);
        btnFiltre.addActionListener(e -> {filtreButton(e);});
    }

    private void pathButton(ActionEvent e) throws IOException, FormatInvalidException  {
        //JOptionPane.showMessageDialog(null, "Ton message");
        JFileChooser fileChooser = new JFileChooser();
        //file name filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file","txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            setPath(fileChooser.getSelectedFile().getAbsolutePath());
            startButton(e);
        }
    }

    private void startButton(ActionEvent e) throws IOException, FormatInvalidException {
        String path = getPath();
        if(!path.isEmpty()){
            filereader = new LectureFichier(path);
            if(table!=null){
                table.getTxt().setText("");
            }
            table = new Tableau(filereader, this);
            txtFilter.setEditable(true);
        }
        else{
            createError("File path - Error");
        }
}

    private void filtreButton(ActionEvent e){
        if(table!=null){
            table.filtreButton(e);
        }
    }

    private void saveButton(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        //file name filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file","txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            path = getPathWithExtensionName(path, "txt");
            try{
                FileOutputStream file = new FileOutputStream(path, false);
                PrintStream out = new PrintStream(file, false, "UTF-8");
                String[] s = getResult().split("\n");
                for(String str: s){
                    out.println(str);
                }
                out.close();
                file.close();
                JOptionPane.showMessageDialog(this, "Success");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Can't write in the file");
            }
        }
    }

    public void setResult(String res){
        save=res;
    }

    public String getResult(){
        return save;
    }

    public LectureFichier getFile() {
        return filereader;
    } 

    private String getPathWithExtensionName(String path, String extension){
        String[] pathSplit = path.split("\\.");
        if(pathSplit[pathSplit.length-1].trim().equals(extension)){
            return path;
        }
        return path+"."+extension;
    }
    
    public void start(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}