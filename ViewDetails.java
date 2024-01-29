import javax.swing.*;
import java.awt.*;

public class ViewDetails extends JPanel {
    private JLabel idL1, idL2, nameL1, nameL2, desiL1, desiL2, deptL1, deptL2, salL1, salL2;

    public ViewDetails(Employee e) {
        setLayout(new GridLayout(5, 2));

        idL1 = new JLabel("ID");
        nameL1 = new JLabel("Name");
        desiL1 = new JLabel("Designation");
        deptL1 = new JLabel("Department");
        salL1 = new JLabel("Salary");

        idL2 = new JLabel(e.getId());
        nameL2 = new JLabel(e.getName());
        desiL2 = new JLabel(e.getDesignation());
        deptL2= new JLabel(e.getDept());
        salL2 = new JLabel(String.valueOf(e.getSalary()));

        add(idL1);
        add(idL2);
        add(nameL1);
        add(nameL2);
        add(desiL1);
        add(desiL2);
        add(deptL1);
        add(deptL2);
        add(salL1);
        add(salL2);
    }
}
