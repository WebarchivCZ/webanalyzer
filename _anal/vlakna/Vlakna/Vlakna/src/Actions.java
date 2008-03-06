import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
/*
 * Actions.java
 *
 * Created on 24. duben 2007, 11:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author xadamek2
 */
public class Actions extends JFrame {
    
    private static class ExitAction extends AbstractAction {
        private ExitAction() {
            super("Exit 2");
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
        
    }

    Action action2 = new ExitAction();
 
    private class DisableAction extends AbstractAction {
        private DisableAction() {
            super("Disable");
        }

        public void actionPerformed(ActionEvent e) {
            action2.setEnabled(false);
        }
        
    }
    
    /** Creates a new instance of Actions */
    public Actions() {
        JMenuBar menubar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem menuItemExit = new JMenuItem();
        menubar.add(menuFile);
        menuFile.add(menuItemExit);
        setJMenuBar(menubar);
        
        ActionListener exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        menuItemExit.setText("Exit");
        menuItemExit.setToolTipText("Ukonci aplikaci");
        menuItemExit.addActionListener(exitListener);
        
        JToolBar toolbar = new JToolBar();
        
        JButton exitButton = new JButton();
        exitButton.setText("Exit");
        exitButton.setToolTipText("Ukonci Aplikaci");
        exitButton.addActionListener(exitListener);
        toolbar.add(exitButton);
        
        add(toolbar,BorderLayout.NORTH);
        
        
        menuFile.add(action2);
        toolbar.add(action2);
        toolbar.add(new DisableAction());
        
        pack();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Actions().setVisible(true);
            }
        });
    }
    
}
