package main;

/**
 * Created by Oisín on 3/2/2017.
 */
public class SearchBySurnameDialog extends SearchDialog{

    public SearchBySurnameDialog(EmployeeDetails parent, String toSearch) {
        super(parent, toSearch);
    }

    @Override
    public void search() {
        this.parent.searchBySurnameField.setText(searchField.getText());
        this.parent.searchEmployeeBySurname();
    }


}
