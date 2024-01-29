import javax.swing.*;
import java.awt.*;

public class EditForm extends JPanel {
    private JTextField idField, nameField, salaryField;
    private JComboBox<String> designationField, deptField;
    public EditForm() {
        setLayout(new GridLayout(0, 1));
        init();
    }

    private void init() {
        JLabel idLabel = new JLabel("ID");
        JLabel nameLabel = new JLabel("Name");
        JLabel designationLabel = new JLabel("Designation");
        JLabel deptLabel = new JLabel("Department");
        JLabel salaryLabel = new JLabel("Salary");

        idField = new JTextField();
        nameField = new JTextField();
        designationField = new JComboBox<>(new String[]{"CEO", "Product Manager", "Sales Manager", "Team Leader", "Developer", "Worker"});
        deptField = new JComboBox<>(new String[]{"Executive", "Production", "Sales", "HR", "IT", "General"});
        salaryField = new JTextField();

        idField.setEditable(false);

        add(idLabel);
        add(idField);
        add(nameLabel);
        add(nameField);
        add(designationLabel);
        add(designationField);
        add(deptLabel);
        add(deptField);
        add(salaryLabel);
        add(salaryField);
    }

    public String[] getFields() {
        String[] val = new String[5];

        val[0] = idField.getText();
        val[1] = nameField.getText();
        val[2] = (String) designationField.getSelectedItem();
        val[3] = (String) deptField.getSelectedItem();
        val[4] = salaryField.getText();

        return val;
    }

    public void setFields(Employee e) {
        idField.setText(e.getId());
        nameField.setText(e.getName());
        designationField.setSelectedItem(e.getDesignation());
        deptField.setSelectedItem(e.getDept());
        salaryField.setText(String.valueOf(e.getSalary()));
    }
}
