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
        super(parent, title,currentByteStart,file);
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
                currentEmployee = getFromFields();
                // write changes to file for corresponding Employee record
                application.changeRecords(currentEmployee, currentByteStart);
                application.closeWriteFile();// close file for writing
                this.parent.displayRecords(currentEmployee);
                dispose();// dispose dialog
            } // end if
            setEnabled(false);
        }// end saveChanges

    public void setUpFields(){
        //NB --update combos
        idField2.setText(String.valueOf(this.currentEmployee.getEmployeeId()));
        firstNameField2.setText(this.currentEmployee.getFirstName().trim());
        surnameField2.setText(this.currentEmployee.getSurname().trim());
        salaryField2.setText(String.valueOf(this.currentEmployee.getSalary()).trim());
        ppsField2.setText(this.currentEmployee.getPps().trim());
        departmentCombo2.setSelectedIndex(this.parent.toDepartment(this.currentEmployee.getDepartment().trim()));////come back to chenge dept and gender to be numeric
        genderCombo2.setSelectedIndex(this.parent.toGender(this.currentEmployee.getGender()));
        fullTimeCombo2.setSelectedIndex(this.parent.toFulltime(this.currentEmployee.getFullTime()));
    }

}
