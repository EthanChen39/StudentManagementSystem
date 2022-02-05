package ui;

import model.Course;
import model.Event;
import model.EventLog;
import model.ManagementSystem;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// This class represents the window that manage students' courses
public class ManageCoursesFrame extends JFrame {
    private ArrayList<String> columnNames;
    private String[][] data;

    private SearchInformationTablePanel searchInformationTablePanel;

    private ManagementSystem system;
    private JPanel buttonPanel;
    private JButton addCourseButton;
    private JButton removeCourseButton;
    private Student student;
    private Course selectedCourse;
    private DefaultTableModel coursesTableModel;
    private JTable coursesTable;
    private String[][] newCoursesData;

    private static final int COLUMN_SIZE = 6;

    // Default constructor that takes in a management system and a student
    public ManageCoursesFrame(ManagementSystem system, Student student) {
        super("Manage Courses & Grades");
        columnNames = new ArrayList<>();
        data = new String[student.getCourses().size()][5];
        buttonPanel = new JPanel();
        addCourseButton = new JButton("Add");
        removeCourseButton = new JButton("Withdraw");
        this.system = system;
        this.student = student;
        setLayout(new FlowLayout());
        loadCurrentCoursesTable();
        initTable();
        centreOnScreen();
        loadButtons();
        tableAddMouseListener(coursesTable);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(600, 500));
    }

    // EFFECTS: load all the buttons to the button panel and add action listener to each button
    private void loadButtons() {
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(addCourseButton);
        buttonPanel.add(removeCourseButton);
        setAddCourseButtonListener();
        setRemoveCourseButtonListener();
        add(buttonPanel);
    }

    // EFFECTS: set remove course button listener
    private void setRemoveCourseButtonListener() {
        removeCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCourse.removeStudent(student)) {
//                    String description = String.format("[Student %d] is being withdrawn from [Course %s %d]",
//                            student.getId(), selectedCourse.getSubjectCode().name(), selectedCourse.getSection());
//                    EventLog.getInstance().logEvent(new Event(description));
                    coursesTableModel.removeRow(coursesTable.getSelectedRow());
                    coursesTableModel.fireTableDataChanged();
                    JOptionPane.showMessageDialog(new JFrame(), "Changed successfully.");
                } else {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Failed to change!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: load courses that this student is not taking to "newCoursesData"
    private void loadNewCoursesData() {
        // Get all the courses
        List<Course> allCourses = system.getCourseList();
        List<Course> studentCourses = student.getCourses();
        List<Course> coursesStudentNotTaking = new ArrayList<>();
        for (Course course: allCourses) {
            if (!studentCourses.contains(course)) {
                coursesStudentNotTaking.add(course);
            }
        }
        // Populate the data table
        newCoursesData = new String[coursesStudentNotTaking.size()][COLUMN_SIZE];
        for (int i = 0; i < coursesStudentNotTaking.size(); i++) {
            Course course = coursesStudentNotTaking.get(i);
            newCoursesData[i][0] = course.getSubjectCode().name();
            newCoursesData[i][1] = course.getSubjectCode().title;
            newCoursesData[i][2] = course.getSection() + "";
            newCoursesData[i][3] = course.getCredit() + "";
            newCoursesData[i][4] = course.getInstructorsList().get(0).getName();
            newCoursesData[i][5] = student.getGrade(course) + "";
        }
    }

    // EFFECTS: set up add course button listener, when add is clicked
    //          a new course will be added to the system and add a new row to the table
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void setAddCourseButtonListener() {
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Construct a new frame
                JFrame coursesToAddFrame = new JFrame("Add Course");
                coursesToAddFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try {
                    loadNewCoursesData();
                } catch (IndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "No courses can be added at this moment.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Construct the table panel
                SearchInformationTablePanel newCoursesTable
                        = new SearchInformationTablePanel(newCoursesData, columnNames);
                // Add a add button
                JPanel addButtonPanel = new JPanel();
                JButton addCourseButton = new JButton("Add");
                JTable table = newCoursesTable.getTable();
                tableAddMouseListener(table);
                addCourseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        student.addCourse(selectedCourse);
//                        String description
//                                = String.format("[Student %d] is being added to a new [Course %s section %d]",
//                                student.getId(), selectedCourse.getSubjectCode().name(), selectedCourse.getSection());
//                        EventLog.getInstance().logEvent(new Event(description));
                        String[] newCourseRow = new String[COLUMN_SIZE];
                        newCourseRow[0] = selectedCourse.getSubjectCode().name();
                        newCourseRow[1] = selectedCourse.getSubjectCode().title;
                        newCourseRow[2] = selectedCourse.getSection() + "";
                        newCourseRow[3] = selectedCourse.getCredit() + "";
                        newCourseRow[4] = selectedCourse.getInstructorsList().get(0).getName();
                        newCourseRow[5] = student.getGrade(selectedCourse) + "";
                        coursesTableModel.addRow(newCourseRow);
                        coursesTableModel.fireTableDataChanged();
                        coursesToAddFrame.setVisible(false);
                        JOptionPane.showMessageDialog(new JFrame(), "Added successfully.");
                    }
                });
                // Load new data
                loadCurrentCoursesTable();
                coursesTableModel.fireTableDataChanged();
                newCoursesTable.setPreferredSize(new Dimension(350, 400));
                coursesToAddFrame.setSize(new Dimension(400, 500));
                coursesToAddFrame.setLayout(new FlowLayout());
                coursesToAddFrame.add(newCoursesTable);
                coursesToAddFrame.setVisible(true);
                addButtonPanel.add(addCourseButton);
                coursesToAddFrame.add(addButtonPanel);
            }
        });
    }

    // EFFECTS: init table columns, set up the size and add table listener
    private void initTable() {
        columnNames = new ArrayList<>();
        columnNames.add("Subject Code");
        columnNames.add("Subject Title");
        columnNames.add("Section");
        columnNames.add("Credit");
        columnNames.add("Main instructor");
        columnNames.add("Grade");
        searchInformationTablePanel = new SearchInformationTablePanel(data, columnNames);
        searchInformationTablePanel.setPreferredSize(new Dimension(500, 400));
        coursesTableModel = searchInformationTablePanel.getTableModel();
        coursesTable = searchInformationTablePanel.getTable();
        setCoursesTableMouseListener();
        add(searchInformationTablePanel);
    }

    // MODIFIES: this -- data
    // EFFECTS: load data from system into data 2D array
    private void loadCurrentCoursesTable() {
        List<Course> courseList = student.getCourses();

        data = new String[courseList.size()][COLUMN_SIZE];
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            data[i][0] = course.getSubjectCode().name();
            data[i][1] = course.getSubjectCode().title;
            data[i][2] = course.getSection() + "";
            data[i][3] = course.getCredit() + "";
            data[i][4] = course.getInstructorsList().get(0).getName();
            data[i][5] = student.getGrade(course) + "";
        }



    }

    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    private void tableAddMouseListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                String name = (String) table.getValueAt(row, 1);
                int section = Integer.parseInt((String) table.getValueAt(row, 2));
                selectedCourse = system.findCourseByNameAndSection(name, section);
            }
        });
    }

    private void setCoursesTableMouseListener() {
        coursesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = coursesTable.rowAtPoint(e.getPoint());
                if (row != -1) {
                    String name = (String) coursesTable.getValueAt(row, 1);
                    int section = Integer.parseInt((String) coursesTable.getValueAt(row, 2));
                    selectedCourse = system.findCourseByNameAndSection(name, section);
                    if (e.getClickCount() == 2) {
                        processChangeGrade(row);
                    }
                }

            }
        });
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void processChangeGrade(int studentRow) {
        JFrame changeGradeFrame = new JFrame("Modify Grade");
        JPanel changeGradePanel = new JPanel();

        JLabel newGradeLabel = new JLabel("Enter new grade:");
        JTextField newGradeTextField = new JTextField();

        changeGradePanel.setLayout(new BoxLayout(changeGradePanel, BoxLayout.Y_AXIS));
        changeGradeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changeGradeFrame.setSize(new Dimension(200, 123));
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newGradeStr = newGradeTextField.getText();
                try {
                    int newGrade = Integer.parseInt(newGradeStr);
                    boolean isChangeSuccessfully = system.changeStudentGrade(selectedCourse, student, newGrade);
                    // If the grade is not in the range, throw this exception to the catch block
                    if (!isChangeSuccessfully) {
                        throw new NumberFormatException();
                    }
                    loadCurrentCoursesTable();
                    coursesTable.setValueAt(newGrade, studentRow, COLUMN_SIZE - 1);
                    coursesTableModel.fireTableDataChanged();
                    changeGradeFrame.setVisible(false);
                    JOptionPane.showMessageDialog(new JFrame(), "Changed successfully.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Invalid Grade!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        changeGradePanel.add(newGradeLabel);
        changeGradePanel.add(newGradeTextField);
        changeGradePanel.add(submit);
        changeGradeFrame.add(changeGradePanel);
        // Display the frame at the center of the window
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        changeGradeFrame.setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
        changeGradeFrame.setVisible(true);
    }
}
