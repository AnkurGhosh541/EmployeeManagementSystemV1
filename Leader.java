import java.util.ArrayList;

public class Leader extends Employee {

    private final ArrayList<Employee> subordinates = new ArrayList<>();

    public Leader(String id, String name, String designation, String dept, int salary) {
        super(id, name, designation, dept, salary);
    }


    public void addSubordinate(Employee e) {
        subordinates.add(e);
    }

    public void removeSubordinate(Employee e) {
        subordinates.remove(e);
    }

    public ArrayList<Employee> getSubordinates() {
        return subordinates;
    }

    @Override
    public int getTotalEmployees() {
        int totalEmployees = 1;

        for (Employee subordinate : subordinates) {
            totalEmployees += subordinate.getTotalEmployees();
        }

        return totalEmployees;
    }

    @Override
    public int getTotalSalary() {
        int totalSalary = getSalary();

        for (Employee subordinate : subordinates) {
            totalSalary += subordinate.getTotalSalary();
        }

        return totalSalary;
    }
}
