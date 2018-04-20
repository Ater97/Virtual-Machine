/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vm;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
/**
 *
 * @author sebas
 */
public class VM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        boolean flag = true;
        Utilities U = new Utilities();
        while(flag)
        {
            VMS vms = new VMS();
            /*
            File fileParse = null;
            JFrame parentFrame = new JFrame();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Assembler Files", "vm");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a folder");   
            fileChooser.setFileFilter(filter);
            int userSelection = fileChooser.showSaveDialog(parentFrame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileParse = fileChooser.getSelectedFile();}
            try {
                vms.Read(fileParse);
                flag = U.stay();
            } catch (Exception e) {
                flag = false;
            } 
            */
            
            File fileParse = null;
            JFileChooser chooser = new JFileChooser();
            //chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Virtual Machine");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
              System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            } else {
              System.out.println("No Selection ");
            }
            try {
                fileParse = chooser.getSelectedFile();
                String[] extensions = new String[] { "vm" };
                List<File> files = (List<File>) FileUtils.listFiles(fileParse, extensions, true);
                
                if (JOptionPane.showConfirmDialog(null, "Do you want to add init to your files?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    vms.flaginit = true;
                }


                vms.MergeFiles(files);
                flag = U.stay();
            } catch (Exception e) {
                flag = U.stay();
            }
        }
        System.exit(0);
    }
}
    

