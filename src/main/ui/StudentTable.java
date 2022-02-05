package ui;

import model.Event;
import model.EventLog;
import model.ManagementSystem;
import model.Student;
import ui.frames.AddPersonAndCourseFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// This class is a Panel that contains a SearchInformationTable,
// a search text field, an add, a remove and a refresh button
public class StudentTable extends JPanel {

    private ArrayList<String> columnNames;
    private ManagementSystem system;
    private Object[][] data;

    private JPanel buttonPanel;

    private SearchInformationTablePanel tablePanel;
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton addFrame;
    private JButton removeStudentButton;
    private JButton refreshButton;


    private Student selectedStudent;
    private int numOfRows;

    private static final int COLUMN_SIZE = 5;

    // Construct a student table Panel that takes in a system object
    public StudentTable(ManagementSystem system) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.system = system;
        buttonPanel = new JPanel(new FlowLayout());
        columnNames = new ArrayList<>();
        data = new Object[system.getStudentList().size()][5];
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Age");
        columnNames.add("Covid Status");
        columnNames.add("GPA");
        loadData();

        initTable();

        add(tablePanel);
        addButtons();
        tablePanel.add(buttonPanel);
        setAddStudentButtonListener();
        setRemoveStudentButtonListener();
        setRefreshButtonListener();
    }

    // MODIFIES: this.system
    // EFFECTS: set this.system to the given system
    public void setSystem(ManagementSystem system) {
        this.system = system;
    }

    // MODIFIES: this
    // EFFECTS: initialize SearchInformationTablePanel, and add table listener
    private void initTable() {
        tablePanel = new SearchInformationTablePanel(data, columnNames);
        table = tablePanel.getTable();
        tableModel = tablePanel.getTableModel();
        setTableMouseListener(table);
        numOfRows = table.getRowCount();
    }

    // MODIFIES: this.data
    // EFFECTS: load all the students from the system to this.data
    public void loadData() {
        List<Student> studentList = system.getStudentList();
        data = new Object[studentList.size()][COLUMN_SIZE];
        for (int row = 0; row < studentList.size(); row++) {
            data[row][0] = studentList.get(row).getId();
            data[row][1] = studentList.get(row).getName();
            data[row][2] = studentList.get(row).getAge();
            data[row][3] = studentList.get(row).getCovidStatus();
            data[row][4] = studentList.get(row).getGPA();
        }
    }

    // EFFECTS: add mouse listener to student table, when user clicks on the row
    //          set selectedStudent to the corresponding
    private void setTableMouseListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                // if user clicks within the data rows
                if (row != -1) {
                    int id = (Integer) table.getValueAt(row, 0);
                    selectedStudent = (Student) system.findPersonById(id);
                    if (e.getClickCount() == 2) {
                        StudentFrame studentFrame = new StudentFrame(system, selectedStudent);
                        studentFrame.setVisible(true);
                    }
                }

            }
        }
        );
    }

    private void addButtons() {
        addFrame = new JButton("Add");
        removeStudentButton = new JButton("Remove");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addFrame);
        buttonPanel.add(removeStudentButton);
        buttonPanel.add(refreshButton);
    }

    private void setAddStudentButtonListener() {
        addFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddPersonAndCourseFrame addPersonAndCourseFrame = new AddPersonAndCourseFrame(system);
                addPersonAndCourseFrame.setVisible(true);
            }
        });
    }

    private void setRemoveStudentButtonListener() {
        removeStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    boolean isAdded = system.removeStudentFromSystem(selectedStudent);
                    if (isAdded) {
//                        String description =
//                                String.format("[Student %d] has been removed from the system", selectedStudent.getId());
//                        EventLog.getInstance().logEvent(new Event(description));
                        tableModel.removeRow(table.getSelectedRow());
                        tableModel.fireTableDataChanged();
                        JOptionPane.showMessageDialog(new JFrame(), "Removed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Failed to remove.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
    }

    public void refreshTable() {
        refreshButton.doClick();
    }

    public DefaultTableModel getTableModel() {
        return tablePanel.getTableModel();
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void setRefreshButtonListener() {
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                List<Student> students = system.getStudentList();

                // Add those newly added students to the table
                if (numOfRows != students.size()) {
                    for (int i = numOfRows; i < students.size(); i++) {
                        Student newStudent = students.get(i);
                        Object[] s = {newStudent.getId(),
                                newStudent.getName(),
                                newStudent.getAge(),
                                newStudent.getCovidStatus(),
                                newStudent.getGPA()};
                        tableModel.addRow(s);
                    }
                    numOfRows = students.size();
                }

                for (int i = 0; i < students.size(); i++) {
                    double newGrade = students.get(i).getGPA();
                    boolean newCovidStatus = students.get(i).getCovidStatus();
                    //Modify grades
                    table.setValueAt(newGrade, i, COLUMN_SIZE - 1);
                    // Modify COVID-19 status
                    table.setValueAt(newCovidStatus, i, COLUMN_SIZE - 2);
                }
                tableModel.fireTableDataChanged();
            }
        });
    }

}
