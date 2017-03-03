package main;
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

public class EmployeeDetails extends JFrame implements ItemListener, WindowListener {
	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// hold object start position in file
	private long currentByteStart = 0;
	private RandomFile application = new RandomFile();
	// display files in File Chooser only with extension .dat
	private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
	// hold file name and path for current file in use
	public File file;
	private JMenuItem open, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private ActionListener openAction, saveAsAction, createAction, modifyAction, deleteAction, firstItemAction, lastItemAction, nextItemAction, prevItemAction,
			searchByIdAction,searchBySurnameAction,searchByIdDialogAction,searchBySurnameDialogAction, listAllAction, closeAppAction;

	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	private static EmployeeDetails frame = new EmployeeDetails();
	// font for labels, text fields and combo boxes
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	// holds automatically generated file name
	String generatedFileName;
	// holds current Employee object
	Employee currentEmployee;
	JTextField searchByIdField, searchBySurnameField;
	// gender combo box values
	String[] gender = { "", "M", "F" };
	// department combo box values
	String[] department = { "", "Administration", "Production", "Transport", "Management" };
	// full time combo box values
	String[] fullTime = { "", "Yes", "No" };

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open"));
		open.setAccelerator(KeyStroke.getKeyStroke("control O"));

		fileMenu.add(saveAs = new JMenuItem("Save As"));
		saveAs.setAccelerator(KeyStroke.getKeyStroke("control F2"));

		recordMenu.add(create = new JMenuItem("Create new Record"));
		create.setAccelerator(KeyStroke.getKeyStroke("control N"));

		recordMenu.add(modify = new JMenuItem("Modify Record"));
		modify.setAccelerator(KeyStroke.getKeyStroke("control E"));
		recordMenu.add(delete = new JMenuItem("Delete Record"));

		navigateMenu.add(firstItem = new JMenuItem("First"));
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID"));
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname"));
		navigateMenu.add(listAll = new JMenuItem("List all Records"));

		closeMenu.add(closeApp = new JMenuItem("Close"));
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
		return menuBar;
	}// end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchId = new JButton(new ImageIcon(
				new ImageIcon("imgres.png").getImage().getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(
				searchSurname = new JButton(new ImageIcon(new ImageIcon("imgres.png").getImage()
						.getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(new ImageIcon("previous.png").getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.setToolTipText("Display last Record");

		return navigPanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(add = new JButton("Add Record"), "growx, pushx");
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), "growx, pushx");
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), "growx, pushx, wrap");
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), "growx, pushx");
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel empDetails = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel("ID:"), "growx, pushx");
		empDetails.add(idField = new JTextField(20), "growx, pushx, wrap");
		idField.setEditable(false);

		empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
		empDetails.add(ppsField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Surname:"), "growx, pushx");
		empDetails.add(surnameField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("First Name:"), "growx, pushx");
		empDetails.add(firstNameField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Gender:"), "growx, pushx");
		empDetails.add(genderCombo = new JComboBox<>(gender), "growx, pushx, wrap");

		empDetails.add(new JLabel("Department:"), "growx, pushx");
		empDetails.add(departmentCombo = new JComboBox<>(department), "growx, pushx, wrap");

		empDetails.add(new JLabel("Salary:"), "growx, pushx");
		empDetails.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Full Time:"), "growx, pushx");
		empDetails.add(fullTimeCombo = new JComboBox<String>(fullTime), "growx, pushx, wrap");

		empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");

		// loop through panel components and add listeners and format
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(font1);
			if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				field.setEditable(false);
			} // end if
			else if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setEnabled(false);
				((JComboBox<String>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}// end paint
				});
			} // end else if
		} // end for
		return empDetails;
	}// end detailsPanel

	public void initActionListeners(){
		closeAppAction= e -> {
			exitApp();
        };
        closeApp.addActionListener(closeAppAction);
		openAction= e -> {
			openFile();
		};
		open.addActionListener(openAction);
		saveAsAction= e -> {
			if(isSomeoneToDisplay())
				saveFileAs();
		};
		saveAs.addActionListener(saveAsAction);
		searchBySurnameDialogAction = e -> {
			exitApp();
		};
		deleteAction= e -> {
			deleteRecord();
		};
		delete.addActionListener(deleteAction); deleteButton.addActionListener(deleteAction);
		searchBySurnameDialogAction = e -> {
			new SearchBySurnameDialog(EmployeeDetails.this,"Surname");
		};
		searchBySurname.addActionListener(searchBySurnameDialogAction);
		searchByIdDialogAction = e -> {
			new SearchByIdDialog(EmployeeDetails.this,"ID");
		};
		searchById.addActionListener(searchByIdDialogAction);
		createAction= e -> {
				new AddRecordDialog(EmployeeDetails.this,"Add Record",file,currentByteStart);
		};
		add.addActionListener(createAction);
		modifyAction=e -> {
			if(isSomeoneToDisplay())
				new EditRecordDialog(EmployeeDetails.this,"Edit Record",file,currentByteStart,currentEmployee);
		};
		modify.addActionListener(modifyAction); edit.addActionListener(modifyAction);
		firstItemAction= e -> {
			firstRecord();
			displayRecords(currentEmployee);
		};
		firstItem.addActionListener(firstItemAction); first.addActionListener(firstItemAction);
		nextItemAction= e -> {
			nextRecord();
			displayRecords(currentEmployee);
		};
		nextItem.addActionListener(nextItemAction); next.addActionListener(nextItemAction);
		prevItemAction= e -> {
			previousRecord();
			displayRecords(currentEmployee);
		};
		prevItem.addActionListener(prevItemAction); previous.addActionListener(prevItemAction);
		lastItemAction= e -> {
			lastRecord();
			displayRecords(currentEmployee);
		};
		last.addActionListener(lastItemAction); lastItem.addActionListener(lastItemAction);
		listAllAction= e -> {
			if (isSomeoneToDisplay())
				displayEmployeeSummaryDialog();
		};
		listAll.addActionListener(listAllAction); displayAll.addActionListener(listAllAction);
		searchBySurnameAction= e -> {
			search("Surname");
		};
		searchSurname.addActionListener(searchBySurnameAction);
		searchByIdAction= e -> {
			search("ID");
		};
		searchId.addActionListener(searchByIdAction);


	}

	public int toFulltime(boolean fullTime){
		if (fullTime== true)
			return 1;
		else
			return 2;
	}

	public int toDepartment(String department){
		switch (department){
			case "Administration":return 1;
			case "Production":return 2;
			case "Transport":return 3;
			case "Management":return 4;
		}
		return 0;
	}

	public int toGender(char gender){
		switch (gender){
			case 'M': return 1;
			case 'F': return 2;
		}
		return 0;
	}

	// display current Employee details
	public void displayRecords(Employee thisEmployee) {
		searchByIdField.setText("");
		searchBySurnameField.setText("");
		// details
		idField.setText(Integer.toString(thisEmployee.getEmployeeId()));
		ppsField.setText(thisEmployee.getPps().trim());
		surnameField.setText(thisEmployee.getSurname().trim());
		firstNameField.setText(thisEmployee.getFirstName());
		genderCombo.setSelectedIndex(toGender(thisEmployee.getGender()));
		departmentCombo.setSelectedIndex(toDepartment(thisEmployee.getDepartment().trim()));
		salaryField.setText(format.format(thisEmployee.getSalary()));
		fullTimeCombo.setSelectedIndex(toFulltime(thisEmployee.getFullTime()));
	}// end display records


	// find byte start in file for first active record
	private void firstRecord() {
		// if any active record in file look for first record
		if (isSomeoneToDisplay()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for first record
			currentByteStart = application.getFirst();
			// assign current Employee to first record in file
			currentEmployee = application.readRecords(currentByteStart);
			application.closeReadFile();// close file for reading
			// if first record is inactive look for next record
			if (currentEmployee.getEmployeeId() == 0)
				nextRecord();// look for next record
		} // end if
	}// end firstRecord

	// find byte start in file for previous active record
	private void previousRecord() {
		// if any active record in file look for first record
		if (isSomeoneToDisplay()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for previous record
			currentByteStart = application.getPrevious(currentByteStart);
			// assign current Employee to previous record in file
			currentEmployee = application.readRecords(currentByteStart);
			// loop to previous record until Employee is active - ID is not 0
			while (currentEmployee.getEmployeeId() == 0) {
				// get byte start in file for previous record
				currentByteStart = application.getPrevious(currentByteStart);
				// assign current Employee to previous record in file
				currentEmployee = application.readRecords(currentByteStart);
			} // end while
			application.closeReadFile();// close file for reading
		}
	}// end previousRecord

	public void search(String toSearch){
		int i=0;
		application.openReadFile(file.getAbsolutePath());
		long max=application.getLast();
		application.closeReadFile();// close file for reading
		if (isSomeoneToDisplay()) {
			firstRecord();// look for first record
				if (toSearch.equals("ID")) {
					do {
					if (searchByIdField.getText().trim().equals(String.valueOf(currentEmployee.getEmployeeId()))) {
						displayRecords(currentEmployee);
						return;
					}
						nextRecord();
						i++;
					}while (i*RandomAccessEmployeeRecord.SIZE!=max);
					searchByIdField.setText("");
				} else {
					do {
					if (searchBySurnameField.getText().toString().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
						displayRecords(currentEmployee);
						return;
					}
						nextRecord();
						i++;
					}while (i*RandomAccessEmployeeRecord.SIZE!=max);
					searchBySurnameField.setText("");
				}
			 firstRecord();
			JOptionPane.showMessageDialog(null, "Employee not found!");
		}
	}

	// find byte start in file for next active record
	private void nextRecord() {
		// if any active record in file look for first record
		if (isSomeoneToDisplay()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for next record
			currentByteStart = application.getNext(currentByteStart);
			// assign current Employee to record in file
			currentEmployee = application.readRecords(currentByteStart);
			// loop to previous next until Employee is active - ID is not 0
			while (currentEmployee.getEmployeeId() == 0) {
				// get byte start in file for next record
				currentByteStart = application.getNext(currentByteStart);
				// assign current Employee to next record in file
				currentEmployee = application.readRecords(currentByteStart);
			} // end while
			application.closeReadFile();// close file for reading
		} // end if
	}// end nextRecord

	// find byte start in file for last active record
	private void lastRecord() {
		// if any active record in file look for first record
		if (isSomeoneToDisplay()) {
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			// get byte start in file for last record
			currentByteStart = application.getLast();
			// assign current Employee to first record in file
			currentEmployee = application.readRecords(currentByteStart);
			application.closeReadFile();// close file for reading
			// if last record is inactive look for previous record
			if (currentEmployee.getEmployeeId() == 0)
				previousRecord();// look for previous record
		} // end if
	}// end lastRecord

	// get next free ID from Employees in the file
	public int getNextFreeId() {
		int nextFreeId = 0;
		// if file is empty or all records are empty start with ID 1 else look
		// for last active record
		if (file.length() == 0 || !isSomeoneToDisplay())
			nextFreeId++;
		else {
			lastRecord();// look for last active record
			// add 1 to last active records ID to get next ID
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	}// end getNextFreeId

	// delete (make inactive - empty) record from file
	private void deleteRecord() {
		if (isSomeoneToDisplay()) {// if any active record in file display
									// message and delete record
			// if answer yes delete (make inactive - empty) record
			if (showMessage( "Do you want to delete record?", "Delete")) {
				// open file for writing
				application.openWriteFile(file.getAbsolutePath());
				// delete (make inactive - empty) record in file proper position
				application.deleteRecords(currentByteStart);
				application.closeWriteFile();// close file for writing
				// if any active record in file display next record
				if (isSomeoneToDisplay()) {
					nextRecord();// look for next record
					displayRecords(currentEmployee);
				} // end if
			} // end if
		} // end if
	}// end deleteDecord

	// create vector of vectors with all Employee details
	private void displayEmployeeSummaryDialog() {
		// vector of Employee objects
		Vector<Object> allEmployee = new Vector<Object>();
		Vector<Object> empDetails;// vector of each employee details
		long byteStart = currentByteStart;
		int firstId;

		firstRecord();// look for first record
		firstId = currentEmployee.getEmployeeId();
		// loop until all Employees are added to vector
		do {
			empDetails = new Vector<>();
			empDetails.addElement(new Integer(currentEmployee.getEmployeeId()));
			empDetails.addElement(currentEmployee.getPps());
			empDetails.addElement(currentEmployee.getSurname());
			empDetails.addElement(currentEmployee.getFirstName());
			empDetails.addElement(currentEmployee.getGender());
			empDetails.addElement(currentEmployee.getDepartment());
			empDetails.addElement(new Double(currentEmployee.getSalary()));
			empDetails.addElement(new Boolean(currentEmployee.getFullTime()));

			allEmployee.addElement(empDetails);
			nextRecord();// look for next record
		} while (firstId != currentEmployee.getEmployeeId());// end do - while
		currentByteStart = byteStart;

		if (isSomeoneToDisplay())
			new EmployeeSummaryDialog(allEmployee);
	}// end getAllEmployees

	public boolean showMessage(String question,String option){
		 return JOptionPane.showOptionDialog(frame, question, option,
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null)==JOptionPane.YES_OPTION;
	}

	// check if any of records in file is active - ID is not 0
	private boolean isSomeoneToDisplay() {
		boolean someoneToDisplay = false;
		// open file for reading
		application.openReadFile(file.getAbsolutePath());
		// check if any of records in file is active - ID is not 0
		someoneToDisplay = application.isSomeoneToDisplay();
		application.closeReadFile();// close file for reading
		// if no records found clear all text fields and display message
		if (!someoneToDisplay) {
			currentEmployee = null;
			idField.setText("");
			ppsField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderCombo.setSelectedIndex(0);
			departmentCombo.setSelectedIndex(0);
			fullTimeCombo.setSelectedIndex(0);
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
		return someoneToDisplay;
	}// end isSomeoneToDisplay

	// check if file name has extension .dat
	private boolean checkFileName(File fileName) {
		boolean checkFile = false;
		int length = fileName.toString().length();
		// check if last characters in file name is .dat
		if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
				&& fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
			checkFile = true;
		return checkFile;
	}// end checkFileName

	// open file
	private void openFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open");
		// display files in File Chooser only with extension .dat
		fc.setFileFilter(datfilter);
		File newFile; // holds opened file name and path
		int returnVal = fc.showOpenDialog(EmployeeDetails.this);
		// if file been chosen, open it
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			newFile = fc.getSelectedFile();
			// if old file wasn't saved and its name is generated file name,
			// delete this file
			if (file.getName().equals(generatedFileName))
				file.delete();// delete file
			file = newFile;// assign opened file to file
			// open file for reading
			application.openReadFile(file.getAbsolutePath());
			firstRecord();// look for first record
			displayRecords(currentEmployee);
			application.closeReadFile();// close file for reading
		} // end if
	}// end openFile

	// save file as 'save as'
	private void saveFileAs() {
		final JFileChooser fc = new JFileChooser();
		File newFile;
		String defaultFileName = "new_Employee.dat";
		fc.setDialogTitle("Save As");
		// display files only with .dat extension
		fc.setFileFilter(datfilter);
		fc.setApproveButtonText("Save");
		fc.setSelectedFile(new File(defaultFileName));

		int returnVal = fc.showSaveDialog(EmployeeDetails.this);
		// if file has chosen or written, save old file in new file
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			newFile = fc.getSelectedFile();
			// check for file name
			if (!checkFileName(newFile)) {
				// add .dat extension if it was not there
				newFile = new File(newFile.getAbsolutePath() + ".dat");
				// create new file
				application.createFile(newFile.getAbsolutePath());
			} // end id
			else
				// create new file
				application.createFile(newFile.getAbsolutePath());

			try {// try to copy old file to new file
				Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				// if old file name was generated file name, delete it
				if (file.getName().equals(generatedFileName))
					file.delete();// delete file
				file = newFile;// assign new file to file
			} // end try
			catch (IOException e) {
			} // end catch
		} // end if
	}// end saveFileAs

	// allow to save changes to file when exiting the application
	private void exitApp() {
		if (file.getName().equals(generatedFileName))
			if(showMessage("Do you want to save your changes ?","Yes")){
				saveFileAs();
			}
			file.delete();// delete file
		System.exit(0);// exit application
	}// end exitApp

	// generate 20 character long file name
	private String getFileName() {
		String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
		StringBuilder fileName = new StringBuilder();
		Random rnd = new Random();
		// loop until 20 character long file name is generated
		while (fileName.length() < 20) {
			int index = (int) (rnd.nextFloat() * fileNameChars.length());
			fileName.append(fileNameChars.charAt(index));
		}
		String generatedfileName = fileName.toString();
		return generatedfileName;
	}// end getFileName

	// create file with generated file name when application is opened
	private void createRandomFile() {
		generatedFileName = getFileName() + ".dat";
		// assign generated file name to file
		file = new File(generatedFileName);
		// create file
		application.createFile(file.getName());
	}// end createRandomFile

	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");
		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
		createRandomFile();// create random file name
		initActionListeners();

	}// end createContentPane

	// create and show main dialog
	private static void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI

	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}// end main

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit application
		exitApp();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

	}
}// end class EmployeeDetails