package ui.frames;

import model.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A frame that contains four buttons. Add a student, course, TA, instructor
public class AddPersonAndCourseFrame extends JFrame {
    private JPanel buttonPanel;
    private JButton addStudentButton;
    private JButton addTaButton;
    private JButton addCourseButton;
    private JButton addInstructorButton;

    private ManagementSystem system;

    AddInstructorFrame addInstructorFrame;
    AddCourseFrame addCourseFrame;
    AddTeachingAssistanceFrame taFrame;
    AddStudentFrame addStudentFrame;

    // Constructs an add person and course frame with 4 buttons in it
    public AddPersonAndCourseFrame(ManagementSystem system) {
        super("Add a person/course");
        this.system = system;
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));
        centreOnScreen();
        setSize(new Dimension(300, 260));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initButton();
        // Set up button listeners for all four buttons
        setAddStudentButtonListener();
        setAddTaButtonListener();
        setAddCourseButtonListener();
        setAddInstructorButtonListener();
        buttonPanel.add(addStudentButton);
        buttonPanel.add(addTaButton);
        buttonPanel.add(addInstructorButton);
        buttonPanel.add(addCourseButton);

        add(buttonPanel);
    }

    // MODIFIES: this.addInstructorButton
    // EFFECTS: when user clicks addInstructorButton, an instructor object will be created and added to the system
    private void setAddInstructorButtonListener() {
        addInstructorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInstructorFrame = new AddInstructorFrame(system);
                setVisible(false);
                addInstructorFrame.setVisible(true);
            }
        });
    }

    // MODIFIES: this.addCourseButton
    // EFFECTS: when user clicks on addCourseButton, an addCourseFrame will be displayed
    private void setAddCourseButtonListener() {
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourseFrame = new AddCourseFrame(system);
                setVisible(false);
                addCourseFrame.setVisible(true);
            }
        });
    }

    // MODIFIES: this.addTaButton
    // EFFECTS: when user clicks on addTaButton, an addTaFrame will be displayed
    private void setAddTaButtonListener() {
        addTaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taFrame = new AddTeachingAssistanceFrame(system);
                setVisible(false);
                taFrame.setVisible(true);
            }
        });
    }

    private void setAddStudentButtonListener() {
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentFrame = new AddStudentFrame(system);
                setVisible(false);
                addStudentFrame.setVisible(true);
            }
        });
    }

    //EFFECTS: Set up the new buttons
    private void initButton() {
        addStudentButton = new JButton("Add Student");
        addTaButton = new JButton("Add TA");
        addInstructorButton = new JButton("Add Instructor");
        addCourseButton = new JButton("Add Course");
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

}
