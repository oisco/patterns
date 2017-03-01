package main;

import java.awt.*;

/**
 * Created by Oisín on 3/2/2017.
 */
public class SearchByIdDialog extends SearchDialog {
    public SearchByIdDialog(EmployeeDetails parent, String toSearch) {
        super(parent, toSearch);
    }

    @Override
    public void search() {
        try {
            Double.parseDouble(searchField.getText());
            this.parent.searchByIdField.setText(searchField.getText());
            // search Employee by ID
                this.parent.searchEmployeeById();
        }// end try
        catch (NumberFormatException num) {
            // display message and set colour to text field if entry is wrong
            searchField.setBackground(new Color(255, 150, 150));
        }// end catch
    }
}
