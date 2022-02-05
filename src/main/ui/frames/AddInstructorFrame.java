package ui.frames;

import model.Instructor;
import model.ManagementSystem;
import model.Util;
import model.enums.Faculty;
import model.enums.Gender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// This class is a frame that asks user to enter information of an instructor and add him/her to the system
public class AddInstructorFrame extends AddPersonFrame {
    private JComboBox facultyBox;
    private String[] faculties;

    private JSpinner yearsOfExperienceSpinner;

    // Construct an  add instructor frame
    public AddInstructorFrame(ManagementSystem system) {
        super(system);
        setTitle("Add an Instructor");
        loadFaculties();
        facultyBox = new JComboBox(faculties);
        JPanel facultyPanel = new JPanel();
        JLabel facultyLabel = new JLabel("Faculty: ");
        facultyPanel.add(facultyLabel);
        facultyPanel.add(facultyBox);
        infoPanel.add(facultyPanel);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 100, 1);
        yearsOfExperienceSpinner = new JSpinner(model);

        JPanel yearsOfExperiencePanel = new JPanel();
        yearsOfExperiencePanel.add(new JLabel("Years of Experience: "));
        yearsOfExperiencePanel.add(yearsOfExperienceSpinner);

        infoPanel.add(yearsOfExperiencePanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        infoPanel.add(buttonPanel);
        add(infoPanel);
    }

    // MODIFIES: faculties
    // EFFECTS: load all the Faculties to this. faculties
    private void loadFaculties() {
        Faculty[] allFaculty = Faculty.values();
        faculties = new String[allFaculty.length];
        for (int i = 0; i < allFaculty.length; i++) {
            faculties[i] = allFaculty[i].name();
        }
    }

    // EFFECTS: when use clicks on submit button,
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
                Faculty faculty = Faculty.valueOf((String) facultyBox.getSelectedItem());
                int yearsOfExperience = (int) yearsOfExperienceSpinner.getValue();
                Instructor newInstructor = new Instructor(age, name, gender,
                        randomID, covidStatus,
                        faculty, yearsOfExperience);
                system.addInstructorToSystem(newInstructor);

                setVisible(false);
                JOptionPane.showMessageDialog(new JFrame("Successful"), "Added Successfully!");

            }
        });
    }
}
