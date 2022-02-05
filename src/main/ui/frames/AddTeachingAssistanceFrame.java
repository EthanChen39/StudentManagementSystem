package ui.frames;

import model.Course;
import model.ManagementSystem;
import model.TeachingAssistance;
import model.Util;
import model.enums.Faculty;
import model.enums.Gender;
import ui.SearchInformationTablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// This class is a frame that asks user to enter information of a Teaching Assistance and add him/her to the system
public class AddTeachingAssistanceFrame extends AddPersonFrame {
    private JTextField hourlyWagesField;
    private JSpinner yearsOfExperienceSpinner;
    private JButton addTaCourseButton;
    private JLabel currentTaCourseLabel;
    private JComboBox facultyBox;
    private String[] faculties;


    private Course selectedCourseToTa;

    private ArrayList<String> columnNames;
    private String[][] data;
    private static final int COLUMN_SIZE = 3;

    public AddTeachingAssistanceFrame(ManagementSystem system) {
        super(system);
        setTitle("Add a TA");
        setSize(new Dimension(400, 600));
        columnNames = new ArrayList<>();
        loadFaculties();
        facultyBox = new JComboBox(faculties);
        initColumnNames();
        addTaCourseButton = new JButton("Add TA Course");
        addTaCourseButton.setSize(new Dimension(100, 30));
        loadCourses();
        setAddTaCourseButtonListener();

        JPanel wagePanel = initWagePanel();
        JPanel taCoursePanel = initTaCoursePanel();
        initFacultyPanel();
        initExperiencePanel();
        infoPanel.add(wagePanel);


        taCoursePanel.add(addTaCourseButton);
        infoPanel.add(taCoursePanel);
        JPanel submitButtonPanel = new JPanel();
        submitButtonPanel.add(submitButton);
        infoPanel.add(submitButtonPanel);
        add(infoPanel);
    }

    private void initColumnNames() {
        columnNames.add("Subject");
        columnNames.add("Section");
        columnNames.add("Main Instructor");
    }

    private void initExperiencePanel() {
        JPanel experiencePanel = new JPanel();
        int initialVal = 1;
        int minVal = 0;
        int maxVal = 90;
        SpinnerModel model = new SpinnerNumberModel(initialVal, minVal, maxVal, 1);
        yearsOfExperienceSpinner = new JSpinner(model);
        JLabel experienceLabel = new JLabel("Years of Experience: ");
        experiencePanel.add(experienceLabel);
        experiencePanel.add(yearsOfExperienceSpinner);
        infoPanel.add(experiencePanel);
    }

    private void initFacultyPanel() {
        // Init faculty panel -> label + text field
        JPanel facultyPanel = new JPanel();
        JLabel facultyLabel = new JLabel("Faculty: ");
        facultyPanel.add(facultyLabel);
        facultyPanel.add(facultyBox);
        infoPanel.add(facultyPanel);
    }

    private void loadFaculties() {
        Faculty[] allFaculty = Faculty.values();
        faculties = new String[allFaculty.length];
        for (int i = 0; i < allFaculty.length; i++) {
            faculties[i] = allFaculty[i].name();
        }
    }

    private JPanel initWagePanel() {
        JPanel wagePanel = new JPanel();
        JLabel wageLabel = new JLabel("Wage($): ");
        hourlyWagesField = new JTextField();
        hourlyWagesField.setPreferredSize(new Dimension(60, 20));
        wagePanel.add(wageLabel);
        wagePanel.add(hourlyWagesField);
        return wagePanel;
    }

    private JPanel initTaCoursePanel() {
        JPanel taCoursePanel = new JPanel();
        JLabel taCourseLabel = new JLabel("Current TA Course: ");
        currentTaCourseLabel = new JLabel("None");
        taCoursePanel.add(taCourseLabel);
        taCoursePanel.add(currentTaCourseLabel);
        return taCoursePanel;

    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void setAddTaCourseButtonListener() {
        addTaCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame taCourseFrame = new JFrame("Add a TA course");
                SearchInformationTablePanel taCourseTablePanel
                        = new SearchInformationTablePanel(data, columnNames);
                JTable coursesTable = taCourseTablePanel.getTable();
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int row = coursesTable.getSelectedRow();
                        if (row != -1) {
                            String courseName = coursesTable.getValueAt(row, 0).toString();
                            int section = Integer.parseInt(coursesTable.getValueAt(row, 1).toString());
                            selectedCourseToTa = system.findCourseByNameAndSection(courseName, section);
                            currentTaCourseLabel.setText("Title: " + selectedCourseToTa.getSubjectCode().title
                                    + " | Section: " + selectedCourseToTa.getSection());
                            taCourseFrame.setVisible(false);
                            JOptionPane.showMessageDialog(new JFrame(), "Added Successfully!");
                        }
                    }
                });

                taCourseTablePanel.add(submitButton);
                taCourseFrame.add(taCourseTablePanel);
                taCourseFrame.setSize(new Dimension(500, 450));
                taCourseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



                taCourseFrame.setVisible(true);
            }
        });
    }

    private void loadCourses() {
        List<Course> courses = system.getCourseList();
        data = new String[courses.size()][COLUMN_SIZE];
        for (int i = 0; i < courses.size(); i++) {
            Course currentCourse = courses.get(i);
            data[i][0] = currentCourse.getSubjectCode().toString();
            data[i][1] = currentCourse.getSection() + "";
            data[i][2] = currentCourse.getInstructorsList().get(0).getName();
        }
    }



    @Override
    protected void setSubmitButtonListener() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Object> personInfo = getPersonInfo();
                int randomID = Util.generateRandomID();
                String name = (String) personInfo.get(0);
                int age = (Integer) personInfo.get(1);
                Gender gender = (Gender) personInfo.get(2);
                boolean covidStatus = (boolean) personInfo.get(3);

                String facultyStr = (String) facultyBox.getSelectedItem();
                assert facultyStr != null;
                Faculty faculty = Faculty.valueOf(facultyStr.toUpperCase());

                String hourlyWageStr = hourlyWagesField.getText();
                double wage = Double.parseDouble(hourlyWageStr);
                int yearsOfExperience = (Integer) yearsOfExperienceSpinner.getValue();
                TeachingAssistance newTa = new TeachingAssistance(age, name, gender,
                        randomID, covidStatus,
                        faculty, yearsOfExperience, wage);
                newTa.assignTaCourse(selectedCourseToTa);
                JOptionPane.showMessageDialog(new JFrame(), "Added Successfully.");
                setVisible(false);
            }
        });
    }
}
