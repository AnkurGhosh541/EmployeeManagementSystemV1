import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MyFrame extends JFrame implements ActionListener {

    private boolean ifCEOcreated = false;
    private HashMap<String, Employee> employees;
    private JTree employeeTree;
    private JPanel mainTab;
    private JScrollPane empPane;
    private final CreateForm createForm = new CreateForm();
    private final EditForm editForm = new EditForm();

    public MyFrame() {
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        employees = new HashMap<>();
        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        JTabbedPane mainTab = new JTabbedPane();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 50, 20);
        JPanel empTab = new JPanel(fl);
        JPanel optTab = new JPanel(fl);

        JButton createBtn = new JButton("CREATE");
        JButton editBtn = new JButton("EDIT");
        JButton deleteBtn = new JButton("DELETE");

        JButton showDtlsBtn = new JButton("Show Details");
        JButton showTotalSalaryBtn = new JButton("Show Total Salary");
        JButton showTotalEmpBtn = new JButton("Show Total Number of Employees");

        createBtn.addActionListener(this);
        editBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        showDtlsBtn.addActionListener(this);
        showTotalEmpBtn.addActionListener(this);
        showTotalSalaryBtn.addActionListener(this);

        empTab.add(createBtn);
        empTab.add(editBtn);
        empTab.add(deleteBtn);

        optTab.add(showDtlsBtn);
        optTab.add(showTotalEmpBtn);
        optTab.add(showTotalSalaryBtn);

        mainTab.addTab("Employee", empTab);
        mainTab.addTab("Operations", optTab);

        empPane = new JScrollPane();
        empPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        empPane.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.55;
        c.weighty = 0.8;
        mainPanel.add(mainTab, c);

        Component b = Box.createRigidArea(new Dimension(20, -1));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.025;
        c.weighty = 0.8;
        mainPanel.add(b, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.425;
        c.weighty = 0.8;
        mainPanel.add(empPane, c);

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("create")) {
            if (ifCEOcreated && (employees.isEmpty() || employeeTree.getLastSelectedPathComponent() == null)) {
                JOptionPane.showMessageDialog(this, "Select an employee to add its subordinate.", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int opt = JOptionPane.showConfirmDialog(mainTab, createForm, "Create Employee", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                String[] params = createForm.getFields();
                if (params[2].equalsIgnoreCase("worker") || params[2].equalsIgnoreCase("developer")) {
                    Worker w = new Worker(params[0], params[1], params[2], params[3], Integer.parseInt(params[4]));

                    Employee emp = employees.putIfAbsent(w.getId(), w);
                    if (emp == null) {
                        JOptionPane.showMessageDialog(this, "Employee with the same ID already exists.", "ID Conflict", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }

                    if (employeeTree == null) {
                        DefaultMutableTreeNode root = new DefaultMutableTreeNode(w, true);
                        employeeTree = new JTree(root);
                    } else {
                        DefaultTreeModel model = (DefaultTreeModel) employeeTree.getModel();
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(w, true);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                    }

                } else {
                    Leader l = new Leader(params[0], params[1], params[2], params[3], Integer.parseInt(params[4]));
                    if (params[2].equalsIgnoreCase("ceo")) {
                        createForm.disableCEOField();
                        ifCEOcreated = true;
                    }
                    Employee emp = employees.putIfAbsent(l.getId(), l);
                    if (emp != null) {
                        JOptionPane.showMessageDialog(this, "Employee with the same ID already exists.", "ID Conflict", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }

                    if (employeeTree == null) {
                        DefaultMutableTreeNode root = new DefaultMutableTreeNode(l, true);
                        employeeTree = new JTree(root);
                        empPane.setViewportView(employeeTree);
                    } else {
                        DefaultTreeModel model = (DefaultTreeModel) employeeTree.getModel();
                        if (params[2].equalsIgnoreCase("ceo")) {
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode) model.getRoot();
                            DefaultMutableTreeNode root = new DefaultMutableTreeNode(l, true);
                            l.addSubordinate((Employee) child.getUserObject());
                            model.setRoot(root);
                            model.insertNodeInto(child, root, root.getSiblingCount() - 1);
                        } else {
                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
                            Leader em = (Leader) parent.getUserObject();
                            em.addSubordinate(l);
                            DefaultMutableTreeNode child = new DefaultMutableTreeNode(l, true);
                            model.insertNodeInto(child, parent, parent.getChildCount());
                        }

                    }
                }
            }
            employeeTree.revalidate();
            employeeTree.repaint();

            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }
        } else if (e.getActionCommand().equalsIgnoreCase("edit")) {
            if (employees.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected to edit", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode empNode = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) empNode.getUserObject();

            editForm.setFields(emp);

            int opt = JOptionPane.showConfirmDialog(mainTab, editForm, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                String[] params = editForm.getFields();

                String id = params[0];
                emp = employees.get(id);
                emp.setName(params[1]);
                emp.setDesignation(params[2]);
                emp.setDept(params[3]);
                emp.setSalary(Integer.parseInt(params[4]));

                empNode.setUserObject(emp);
            }
            employeeTree.revalidate();
            employeeTree.repaint();
        } else if (e.getActionCommand().equalsIgnoreCase("Delete")) {
            if (employees.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            employees.remove(emp.getId());
            if (emp.getDesignation().equalsIgnoreCase("ceo")) {
                ifCEOcreated = false;
            }
            if (!node.isRoot()) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                Leader l = (Leader) parent.getUserObject();
                if (!node.isLeaf()) {
                    for (int i = 0; i < node.getChildCount(); i++) {
                        DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                        l.addSubordinate((Employee) child.getUserObject());
                        parent.add(child);
                    }
                }
                node.removeFromParent();
                DefaultTreeModel model = (DefaultTreeModel) employeeTree.getModel();
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                model.reload(root);
                employeeTree.revalidate();
                employeeTree.repaint();
            } else if (employees.isEmpty()) {
                empPane.setViewportView(null);
                employeeTree = null;
                empPane.revalidate();
                empPane.repaint();
            } else if (node.isRoot()) {
                DefaultTreeModel model = (DefaultTreeModel) employeeTree.getModel();
                if (node.getChildCount() == 1) {
                    DefaultMutableTreeNode newRoot = (DefaultMutableTreeNode) node.getFirstChild();
                    newRoot.removeFromParent();
                    model.setRoot(newRoot);
                }
                employeeTree.revalidate();
                employeeTree.repaint();
            }
        } else if (e.getActionCommand().equalsIgnoreCase("Show Details")) {
            if (employees.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            ViewDetails details = new ViewDetails(emp);
            JOptionPane.showMessageDialog(this, details);
        } else if (e.getActionCommand().equalsIgnoreCase("Show Total Salary")) {
            if (employees.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            String msg = "Total Salary payable: " + emp.getTotalSalary();
            JOptionPane.showMessageDialog(this, msg);
        } else if (e.getActionCommand().equalsIgnoreCase("Show Total Number of Employees")) {
            if (employees.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            String msg = "Total Number of employees: " + emp.getTotalEmployees();
            JOptionPane.showMessageDialog(this, msg);
        }
    }
}
