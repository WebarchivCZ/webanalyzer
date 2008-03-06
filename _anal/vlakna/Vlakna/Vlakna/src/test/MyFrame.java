/*
 * MyFrame.java
 *
 * Created on 10. duben 2007, 11:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author xadamek2
 */
public class MyFrame extends JFrame {
    
    private JButton myButton = new JButton("Hello");
    
    private static class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.err.println(e);
        }
    }
    
    public MyFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(myButton);
        pack();
        
        myButton.addActionListener(new MyActionListener());
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyFrame().setVisible(true);
            }
        });
    }
}
