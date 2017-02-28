package main;

/**
 * Created by Oisín on 3/1/2017.
 */

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 *
 * This is the dialog for Employee search by ID
 *
 * */


public class SearchDialog extends JDialog implements ActionListener {
    private EmployeeDetails parent;
    private JButton search, cancel;
    private JTextField searchField;
    private String toSearch;
    // constructor for SearchDialog
    public SearchDialog(EmployeeDetails parent,String toSearch) {
        setTitle("Search by "+toSearch);
        setModal(true);
        this.parent = parent;
        this.toSearch=toSearch;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(searchPane());
        setContentPane(scrollPane);

        getRootPane().setDefaultButton(search);

        setSize(500, 190);
        setLocation(350, 250);
        setVisible(true);
    }// end SearchDialog

    // initialize search container
    public Container searchPane() {
        JPanel searchPanel = new JPanel(new GridLayout(3, 1));
        JPanel textPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JLabel searchLabel;

        searchPanel.add(new JLabel("Search by "+this.toSearch));

        textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        textPanel.add(searchLabel = new JLabel("Enter "+toSearch+":"));
        searchLabel.setFont(this.parent.font1);
        textPanel.add(searchField = new JTextField(20));
        searchField.setFont(this.parent.font1);
        searchField.setDocument(new JTextFieldLimit(20));

        buttonPanel.add(search = new JButton("Search"));
        search.addActionListener(this);
        search.requestFocus();

        buttonPanel.add(cancel = new JButton("Cancel"));
        cancel.addActionListener(this);

        searchPanel.add(textPanel);
        searchPanel.add(buttonPanel);

        return searchPanel;
    }// end searchPane

    // action listener for save and cancel button
    public void actionPerformed(ActionEvent e) {
        // if option search, search for Employee
        if (e.getSource() == search) {
            // try get correct valus from text field
            try {
                Double.parseDouble(searchField.getText());
                this.parent.searchByIdField.setText(searchField.getText());
                // search Employee by ID
                if(this.toSearch.equals("ID")){
                    this.parent.searchEmployeeById();
                }
                else {
                    this.parent.searchEmployeeBySurname();
                }
                dispose();// dispose dialog
            }// end try
            catch (NumberFormatException num) {
                // display message and set colour to text field if entry is wrong
                searchField.setBackground(new Color(255, 150, 150));
                JOptionPane.showMessageDialog(null, "Wrong "+this.toSearch+" format!");
            }// end catch
        }// end if
        // else dispose dialog
        else if (e.getSource() == cancel)
            dispose();
    }// end actionPerformed
}// end class searchByIdDialog
