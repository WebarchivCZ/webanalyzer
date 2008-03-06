/*
 * JTableExample.java
 *
 * Created on 24. duben 2007, 12:12
 */

package swingexamples;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

class Person {
    
    public Person() {};
    
    public Person(String firstName, String lastName, Date dateOfBirth,
            BigDecimal salary, Color favoritColor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.salary = salary;
        this.favoritColor = favoritColor;
    }
    
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Color favoritColor;
    private BigDecimal salary;
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Color getFavoritColor() {
        return favoritColor;
    }
    
    public void setFavoritColor(Color favoritColor) {
        this.favoritColor = favoritColor;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}

/**
 *
 * @author  xadamek2
 */
public class JTableExample extends javax.swing.JFrame {
    
    /** Creates new form JTableExample */
    public JTableExample() {
        initComponents();
        jTable1.setDefaultRenderer(Color.class,new ColorCellRenderer());
        
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                removeLineAction.setEnabled(jTable1.getSelectedRows().length == 1);
            }
        });
     /*   JFormattedTextField dateField = new JFormattedTextField(
                SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT));
      
        jTable1.setDefaultEditor(Date.class,new DefaultCellEditor(dateField));*/
    }
    
    private Action removeLineAction = new RemoveLineAction();
    
    private class RemoveLineAction extends AbstractAction {
        
        private RemoveLineAction() {
            super("Delete");
        }
        
        public void actionPerformed(ActionEvent e) {
            ((MyModel) jTable1.getModel()).removeLine(jTable1.getSelectedRow());
        }
    }
    
    private class MyModel extends AbstractTableModel {
        
        private List<Person> people = createTestData();
        
        private List<Person> createTestData() {
            SimpleDateFormat format = new SimpleDateFormat("y-M-d");
            List<Person> result = new ArrayList<Person>();
            try {
                result.add(new Person("Petr","Adámek",format.parse("1990-01-01"),
                        new BigDecimal(100000),Color.RED));
                result.add(new Person("Martin","Kuba",format.parse("1950-02-04"),
                        new BigDecimal(10000),Color.BLUE));
                result.add(new Person("Jirka","Kosek",format.parse("1975-05-04"),
                        new BigDecimal(50000),Color.GREEN));
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            return result;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            Person person = people.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    return person.getFirstName();
                case 1:
                    return person.getLastName();
                case 2:
                    return person.getDateOfBirth();
                case 3:
                    return person.getSalary();
                case 4:
                    return person.getFavoritColor();
                default:
                    throw new IllegalArgumentException("Wrong column");
            }
            
            
        }
        
        
        public int getRowCount() {
            return people.size();
        }
        
        public int getColumnCount() {
            return 5;
        }
        
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                case 1:
                    return String.class;
                case 2:
                    return Date.class;
                case 3:
                    return Number.class;
                case 4:
                    return Color.class;
                default:
                    throw new IllegalArgumentException("Wrong column");
            }
        }
        
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "First name";
                case 1:
                    return "Last name";
                case 2:
                    return "Date of birth";
                case 3:
                    return "Salary";
                case 4:
                    return "Favorit color";
                default:
                    throw new IllegalArgumentException("Wrong column");
            }
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex < 2;
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Person person = people.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    person.setFirstName((String) aValue);
                    break;
                case 1:
                    person.setLastName((String) aValue);
                    break;
                case 2:
                    person.setDateOfBirth((Date) aValue);
                    break;
                case 3:
                    person.setSalary((BigDecimal) aValue);
                    break;
                case 4:
                    person.setFavoritColor((Color) aValue);
                    break;
                default:
                    throw new IllegalArgumentException("Wrong column");
            }
            
        }
        
        private void appendLine() {
            people.add(new Person());
            fireTableRowsInserted(people.size(),people.size());
        }
        
        private void removeLine(int index) {
            people.remove(index);
            fireTableRowsDeleted(index,index);
        }
        
    }
    
    private class ColorCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component renderer;
            renderer = super.getTableCellRendererComponent(table, null,
                    isSelected, hasFocus, row, column);
            renderer.setBackground((Color) value);
            return renderer;
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTable1.setModel(new MyModel());
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jToolBar1.add(jButton1);

        jCheckBox1.setText("Appending enabled");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jToolBar1.add(jCheckBox1);

        jButton2.setAction(removeLineAction);
        jToolBar1.add(jButton2);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jMenu2.setText("File");
        jMenuItem1.setText("Add");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem1);

        jMenuItem2.setAction(removeLineAction);
        jMenu2.add(jMenuItem2);

        jMenuBar2.add(jMenu2);

        setJMenuBar(jMenuBar2);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        JCheckBox source = (JCheckBox ) evt.getSource();
        jButton1.setEnabled(source.isSelected());
        jMenuItem1.setEnabled(source.isSelected());
        
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ((MyModel) jTable1.getModel()).appendLine();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ((MyModel) jTable1.getModel()).appendLine();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JTableExample().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    
}
