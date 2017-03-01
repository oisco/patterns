package main;/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AddRecordDialog extends RecordDialog {

	public AddRecordDialog(EmployeeDetails parent, String title, File file, long currentByteStart,RandomFile application) {
		super(parent, title,currentByteStart,file,application);
		setUpFields();
		getRootPane().setDefaultButton(save);
		setSize(500, 370);
		setLocation(350, 250);
		setVisible(true);
	}

	@Override
	public void setUpFields() {
			idField2.setText(Integer.toString(this.parent.getNextFreeId()));
	}

	// add record to file
	public void addRecord() {
		boolean fullTime = false;
		Employee theEmployee;

		if (((String) fullTimeCombo2.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		theEmployee =getFromFields();
		this.parent.currentEmployee = theEmployee;
		// open file for writing
		application.openWriteFile(file.getAbsolutePath());
		// write into a file
		currentByteStart = application.addRecords(theEmployee);
		application.closeWriteFile();// close file for writing

		this.parent.displayRecords(theEmployee);
	}

	@Override
	public void addOrUpdate() {
			addRecord();// add record to file
			dispose();// dispose dialog
			this.parent.changesMade = true;

	}// end if
}
