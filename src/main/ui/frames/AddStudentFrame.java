package ui.frames;

import model.ManagementSystem;
import model.Student;
import model.Util;
import model.enums.Faculty;
import model.enums.Gender;
import model.enums.YearLevel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// This class is a frame that asks user to enter information of a student and add him/her to the system
public class AddStudentFrame extends AddPersonFrame {
    private JComboBox yearLevelBox;
    private JComboBox facultyBox;

    private String[] yearLevels;
    private String[] faculties;

    // Construct a frame - add a new student to the system
    public AddStudentFrame(ManagementSystem system) {
        super(system);
        setTitle("Add a student");
        loadYearLevels();
        loadFaculties();
        yearLevelBox = new JComboBox(yearLevels);
        facultyBox = new JComboBox(faculties);

        JPanel yearLevelPanel = new JPanel();
        JLabel yearLevelLabel = new JLabel("Year Level: ");
        yearLevelPanel.add(yearLevelLabel);
        yearLevelPanel.add(yearLevelBox);
        infoPanel.add(yearLevelPanel);


        JPanel facultyPanel = new JPanel();
        JLabel facultyLabel = new JLabel("Faculty: ");
        facultyPanel.add(facultyLabel);
        facultyPanel.add(facultyBox);
        infoPanel.add(facultyPanel);
        JPanel submitButtonPanel = new JPanel();
        submitButtonPanel.add(submitButton);
        infoPanel.add(submitButtonPanel);
        add(infoPanel);
    }

    // EFFECTS: returns an array of objects that contains all the field information of a student
    private ArrayList<Object> getStudentInfo() {
        ArrayList<Object> studentInfo = super.getPersonInfo();
        String yearLevelStr = (String) yearLevelBox.getSelectedItem();
        assert yearLevelStr != null;
        YearLevel yearLevel = YearLevel.valueOf(yearLevelStr.toUpperCase());
        String facultyStr = (String) facultyBox.getSelectedItem();
        assert facultyStr != null;
        Faculty faculty = Faculty.valueOf(facultyStr.toUpperCase());

        studentInfo.add(yearLevel);
        studentInfo.add(faculty);
        return studentInfo;
    }

    // EFFECTS: set up submit button listener, when user clicks submit, a new student object will be created
    //          and added to the system
    @Override
    protected void setSubmitButtonListener() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Object> studentInfo = getStudentInfo();
                int randomID = Util.generateRandomID();
                String name = (String) studentInfo.get(0);
                int age = (Integer) studentInfo.get(1);
                Gender gender = (Gender) studentInfo.get(2);
                boolean covidStatus = (boolean) studentInfo.get(3);
                YearLevel yearLevel = (YearLevel) studentInfo.get(4);
                Faculty faculty = (Faculty) studentInfo.get(5);
                Student newStudent = new Student(age, name, gender,
                        randomID, covidStatus, faculty, yearLevel);
                system.addStudentToSystem(newStudent);
                setVisible(false);
                JOptionPane.showMessageDialog(new JFrame("Successful"), "Student Added Successfully.");
            }
        });
    }

    // MODIFIES: this.faculties
    // EFFECTS: load faculties from system to this.faculties
    private void loadFaculties() {
        Faculty[] allFaculty = Faculty.values();
        faculties = new String[allFaculty.length];
        for (int i = 0; i < allFaculty.length; i++) {
            faculties[i] = allFaculty[i].name();
        }
    }

    // MODIFIES: this.yearLevels
    // EFFECTS: laod all the year levels to this.yearLevels
    private void loadYearLevels() {
        YearLevel[] allYearLevel = YearLevel.values();
        yearLevels = new String[allYearLevel.length];
        for (int i = 0; i < allYearLevel.length; i++) {
            yearLevels[i] = allYearLevel[i].toString();
        }
    }

}
