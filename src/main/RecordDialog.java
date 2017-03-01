package main;

/**
 * Created by Oisín on 3/1/2017.
 */

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;/*
 *
 * This is a dialog for adding new Employees and saving records to file
 *
 * */

public abstract class RecordDialog extends JDialog implements ActionListener {
    JTextField idField2, ppsField2, surnameField2, firstNameField2, salaryField2;
    JComboBox<String> genderCombo2, departmentCombo2, fullTimeCombo2;
    JButton save, cancel;
    EmployeeDetails parent;

    // constructor for add record dialog
    public RecordDialog(EmployeeDetails parent,String title) {
        setTitle(title);
        setModal(true);

        this.parent = parent;
        this.parent.setEnabled(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(dialogPane());
        setContentPane(scrollPane);

    }// end AddRecordDialog


    public Employee getFromFields(){
          return new Employee(Integer.parseInt(idField2.getText()), ppsField2.getText().toUpperCase(), surnameField2.getText().toUpperCase(),
                           firstNameField2.getText().toUpperCase(), genderCombo2.getSelectedItem().toString().charAt(0),
                                departmentCombo2.getSelectedItem().toString(), Double.parseDouble(salaryField2.getText()), false);
            }


    public  abstract  void setUpFields();

    // initialize dialog container
    public Container dialogPane() {
        JPanel empDetails, buttonPanel;
        empDetails = new JPanel(new MigLayout());
        buttonPanel = new JPanel();
        JTextField field;

        empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        empDetails.add(new JLabel("ID:"), "growx, pushx");
        empDetails.add(idField2 = new JTextField(20), "growx, pushx, wrap");
        idField2.setEditable(false);


        empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
        empDetails.add(ppsField2 = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("Surname:"), "growx, pushx");
        empDetails.add(surnameField2 = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("First Name:"), "growx, pushx");
        empDetails.add(firstNameField2 = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("Gender:"), "growx, pushx");
        empDetails.add(genderCombo2 = new JComboBox<String>(this.parent.gender), "growx, pushx, wrap");

        empDetails.add(new JLabel("Department:"), "growx, pushx");
        empDetails.add(departmentCombo2 = new JComboBox<String>(this.parent.department), "growx, pushx, wrap");

        empDetails.add(new JLabel("Salary:"), "growx, pushx");
        empDetails.add(salaryField2 = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("Full Time:"), "growx, pushx");
        empDetails.add(fullTimeCombo2 = new JComboBox<String>(this.parent.fullTime), "growx, pushx, wrap");

        buttonPanel.add(save = new JButton("Save"));
        save.addActionListener(this);
        save.requestFocus();
        buttonPanel.add(cancel = new JButton("Cancel"));
        cancel.addActionListener(this);

        empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");
        // loop through all panel components and add fonts and listeners
        for (int i = 0; i < empDetails.getComponentCount(); i++) {
            empDetails.getComponent(i).setFont(this.parent.font1);
            if (empDetails.getComponent(i) instanceof JComboBox) {
                empDetails.getComponent(i).setBackground(Color.WHITE);
            }// end if
            else if(empDetails.getComponent(i) instanceof JTextField){
                field = (JTextField) empDetails.getComponent(i);
                if(field == ppsField2)
                    field.setDocument(new JTextFieldLimit(9));
                else
                    field.setDocument(new JTextFieldLimit(20));
            }// end else if
        }// end for
        return empDetails;
    }

    // check for input in text fields
    public boolean checkInput() {
        boolean valid = true;
        // if any of inputs are in wrong format, colour text field and display message
        if (ppsField2.getText().equals("")) {
            ppsField2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        if (this.parent.correctPps(this.ppsField2.getText().trim(), -1)) {
            ppsField2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        if (surnameField2.getText().isEmpty()) {
            surnameField2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        if (firstNameField2.getText().isEmpty()) {
            firstNameField2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        if (genderCombo2.getSelectedIndex() == 0) {
            genderCombo2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        if (departmentCombo2.getSelectedIndex() == 0) {
            departmentCombo2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        try {// try to get values from text field
            Double.parseDouble(salaryField2.getText());
            // check if salary is greater than 0
            if (Double.parseDouble(salaryField2.getText()) < 0) {
                salaryField2.setBackground(new Color(255, 150, 150));
                valid = false;
            }// end if
        }// end try
        catch (NumberFormatException num) {
            salaryField2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end catch
        if (fullTimeCombo2.getSelectedIndex() == 0) {
            fullTimeCombo2.setBackground(new Color(255, 150, 150));
            valid = false;
        }// end if
        return valid;
    }// end checkInput

    // set text field to white colour
    public void setToWhite() {
        ppsField2.setBackground(Color.WHITE);
        surnameField2.setBackground(Color.WHITE);
        firstNameField2.setBackground(Color.WHITE);
        salaryField2.setBackground(Color.WHITE);
        genderCombo2.setBackground(Color.WHITE);
        departmentCombo2.setBackground(Color.WHITE);
        fullTimeCombo2.setBackground(Color.WHITE);
    }// end setToWhite

    // action performed
    public void actionPerformed(ActionEvent e) {
        // if chosen option save, save record to file
        if (e.getSource() == save) {
            addOrUpdate();
        }
        else if (e.getSource() == cancel)
            dispose();// dispose dialog
    }// end actionPerformed

    public abstract void addOrUpdate();


}// end class AddRecordDialog