package main;

/**
 * Created by Oisín on 3/1/2017.
 */

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class SearchDialog extends JDialog implements ActionListener {
    public EmployeeDetails parent;
    public JButton search;
    public JButton cancel;
    public JTextField searchField;
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


    public abstract void search();
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

    public void actionPerformed(ActionEvent e) {
        // if option search, search for Employee
        if (e.getSource() == search) {
            search();
            dispose();// dispose dialog
        }// end if
        // else dispose dialog
        else if (e.getSource() == cancel)
            dispose();
    }// end actionPerformed


}// end class searchByIdDialog
