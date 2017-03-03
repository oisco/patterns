package main;

/**
 * Created by Oisín on 3/1/2017.
 */

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public abstract class RecordDialog extends JDialog implements ActionListener {
    JTextField idField2, ppsField2, surnameField2, firstNameField2, salaryField2;
    JComboBox<String> genderCombo2, departmentCombo2, fullTimeCombo2;
    JButton save, cancel;
    EmployeeDetails parent;
    File file;
    long currentByteStart;
    private RandomFile application = new RandomFile();

    // constructor for add record dialog
    public RecordDialog(EmployeeDetails parent, String title, long currentByteStart, File file) {
        setTitle(title);
        setModal(true);

        this.currentByteStart=currentByteStart;
        this.file=file;
        this.parent = parent;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(dialogPane());
        setContentPane(scrollPane);
    }// end AddRecordDialog


    public Employee getFromFields() {
        return new Employee(Integer.parseInt(idField2.getText()), ppsField2.getText().toUpperCase(), surnameField2.getText().toUpperCase(),
                firstNameField2.getText().toUpperCase(), genderCombo2.getSelectedItem().toString().charAt(0),
                departmentCombo2.getSelectedItem().toString(), Double.parseDouble(salaryField2.getText()), false);
    }

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

    // check for correct PPS format and look if PPS already in use
    public boolean correctPps(String pps, long currentByte) {
        boolean ppsExist = false;
        // check for correct PPS format based on assignment description
        if (pps.matches("[0-9][0-9][0-9][0-9][0-9][0-9][A-Z^a-z]")) {
            // open file for reading
             application.openReadFile(file.getAbsolutePath());
            // look in file is PPS already in use
//            application.openReadFile(this.parent.file.getName());
            if(!application.isPpsExist(pps, currentByte)){
                application.closeReadFile();
                ppsExist =true;
            }
            application.closeReadFile();// close file for reading
        } // end if
        else
            ppsExist = false;
        return ppsExist;
    }// end correctPPS

    // check for input in text fields
    public boolean checkInput() {
        boolean valid = true;
        ArrayList<JTextField> fields=new ArrayList<>();
        fields.add(surnameField2);fields.add(firstNameField2);fields.add(ppsField2); fields.add(salaryField2);
        ArrayList<JComboBox> comboBoxes=new ArrayList<>();
        comboBoxes.add(genderCombo2);comboBoxes.add(departmentCombo2);comboBoxes.add(fullTimeCombo2);

        for(int i=0;i<fields.size();i++){
            if(fields.get(i).isEditable()&& fields.get(i).getText().trim().isEmpty()){
                fields.get(i).setBackground(new Color(255, 150, 150));
                valid = false;
            }
        }
        //check if pps is not alreaddy flagged for being empty
        if (ppsField2.getBackground()!=new Color(255, 150, 150) && ppsField2.isEditable()) {
            if(!correctPps(ppsField2.getText().trim(), currentByteStart)){
                ppsField2.setBackground(new Color(255, 150, 150));
                valid=false;
            }
        }

        for(int i=0;i<comboBoxes.size();i++){
            if(comboBoxes.get(i).getSelectedIndex() == 0 && comboBoxes.get(i).isEnabled()){
                comboBoxes.get(i).setBackground(new Color(255, 150, 150));
                valid = false;
            }
        }

        //if salary is not already flagged for being null
        if(salaryField2.getBackground()!=new Color(255, 150, 150)){
            try {// try to get values from text field
                Double.parseDouble(salaryField2.getText());
                // check if salary is greater than 0
                if (Double.parseDouble(salaryField2.getText()) < 0) {
                    salaryField2.setBackground(new Color(255, 150, 150));
                    valid = false;
                } // end if
            } // end try
            catch (NumberFormatException num) {
                if (salaryField2.isEditable()) {
                    salaryField2.setBackground(new Color(255, 150, 150));
                    valid = false;
                } // end if
            } // end catch
        }

        // display message if any input or format is wrong
        if (!valid)
            JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
        // set text field to white colour if text fields are editable
        if (ppsField2.isEditable())
            setToWhite();

        return valid;
    }

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
             if(checkInput()){
                 addOrUpdate();
             }
         }
         else if (e.getSource() == cancel)
           dispose();// dispose dialog
     }// end actionPerformed

    public abstract void addOrUpdate();
    public abstract void setUpFields();


}// end class AddRecordDialog