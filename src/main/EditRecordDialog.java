package main;

import javax.swing.*;
import java.io.File;

/**
 * Created by Oisín on 3/1/2017.
 */
public class EditRecordDialog extends RecordDialog{
    private RandomFile application = new RandomFile();
    Employee currentEmployee;
    long currentByteStart;

    public EditRecordDialog(EmployeeDetails parent, String title, File file, long currentByteStart,Employee currentEmployee) {
        super(parent, title);
        this.currentByteStart=currentByteStart;
        this.currentEmployee=currentEmployee;

        setUpFields();
        getRootPane().setDefaultButton(save);
        setSize(500, 370);
        setLocation(350, 250);
        setVisible(true);

    }

    @Override
    public void addOrUpdate() {
            int returnVal = JOptionPane.showOptionDialog(this, "Do you want to save changes to current Employee?", "Save",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if user choose to save changes, save changes
            if (returnVal == JOptionPane.YES_OPTION) {
                // open file for writing
                application.openWriteFile(this.parent.file.getAbsolutePath());
                // get changes for current Employee
                currentEmployee = getChangedDetails();
                // write changes to file for corresponding Employee record
                application.changeRecords(currentEmployee, currentByteStart);
                application.closeWriteFile();// close file for writing
                this.parent.displayRecords(currentEmployee);
                dispose();// dispose dialog
//                changesMade = false;// state that all changes has bee saved
            } // end if
            setEnabled(false);
        }// end saveChanges

    private Employee getChangedDetails() {
        boolean fullTime = false;
        Employee theEmployee;
        if (((String) fullTimeCombo2.getSelectedItem()).equalsIgnoreCase("Yes"))
            fullTime = true;
        theEmployee = new Employee(Integer.parseInt(idField2.getText()), ppsField2.getText().toUpperCase(),
                surnameField2.getText().toUpperCase(), firstNameField2.getText().toUpperCase(),
                genderCombo2.getSelectedItem().toString().charAt(0), departmentCombo2.getSelectedItem().toString(),
                Double.parseDouble(salaryField2.getText()), fullTime);
        return theEmployee;
    }// end getChangedDetails

    public void setUpFields(){
        //NB --update combos
        idField2.setText(String.valueOf(this.currentEmployee.getEmployeeId()));
        firstNameField2.setText(this.currentEmployee.getFirstName().trim());
        surnameField2.setText(this.currentEmployee.getSurname().trim());
        salaryField2.setText(String.valueOf(this.currentEmployee.getSalary()).trim());
        ppsField2.setText(this.currentEmployee.getPps().trim());
        departmentCombo2.setSelectedIndex(1);////come back to chenge dept and gender to be numeric
        genderCombo2.setSelectedIndex(2);
        fullTimeCombo2.setSelectedIndex(2);
    }

}
