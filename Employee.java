abstract public class Employee {
    private String id;
    private String name;
    private String designation;
    private String dept;
    private int salary;

    public Employee(String id, String name, String designation, String dept, int salary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.dept = dept;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    abstract public int getTotalEmployees();
    abstract public int getTotalSalary();

    @Override
    public String toString() {
        return name + " (" +
                designation + ")";
    }
}
