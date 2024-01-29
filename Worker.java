public class Worker extends Employee {

    public Worker(String id, String name, String designation, String dept, int salary) {
        super(id, name, designation, dept, salary);
    }

    @Override
    public int getTotalEmployees() {
        return 1;
    }

    @Override
    public int getTotalSalary() {
        return getSalary();
    }
}
