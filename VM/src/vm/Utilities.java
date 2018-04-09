/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vm;

import javax.swing.JOptionPane;

/**
 *
 * @author sebas
 */
public class Utilities {
    
    public boolean stay()
    {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to continue?","",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }
}
