package ui;

import model.ManagementSystem;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// This class
public class StudentFrame extends JFrame {
    private final int width = 300;
    private final int height = 500;

    private Student student;
    private ManagementSystem system;


    private JPanel infoPanel;
    private JPanel buttonPanel;
    private JButton manageCoursesButton;
    private JButton declarePositiveButton;
    private JLabel[] studentInfoLabels;
    private JTextField[] textFields;
    private final String[] labels = {"Name: ", "ID: ", "Age: ",
            "GPA: ", "Covid Status: ", "Faculty: ", "YearLevel: "};
    private final int columnSize = labels.length;

    // Construct a student frame that takes in a system and a student
    public StudentFrame(ManagementSystem system, Student student) {
        super(student.getName());
        this.student = student;
        this.system = system;
        infoPanel = new JPanel();
        buttonPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        centreOnScreen();

        setResizable(false);
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loadLabels();
        loadButtons();
        add(infoPanel);
        add(buttonPanel);
    }

    //EFFECTS: init all the buttons and add them to buttonPanel
    private void loadButtons() {
        manageCoursesButton = new JButton("Manage Courses and Grades");
        declarePositiveButton = new JButton("Declare COVID Positive");

        buttonPanel.setBounds(10, 300, 250, 100);
        setManageCoursesButtonListener();
        setDeclarePositiveButtonListener();
        buttonPanel.add(manageCoursesButton);
        buttonPanel.add(declarePositiveButton);

        manageCoursesButton.setSize(new Dimension(150, 20));
    }

    // EFFECTS: set up declare positive button listener
    //          when user clicks on the button, it will update the covid status of others
    private void setDeclarePositiveButtonListener() {
        declarePositiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.declarePositive();
                setVisible(false);
                JOptionPane.showMessageDialog(new JFrame("Message sent"), "Declared successfully.");
            }
        });
    }

    private void setManageCoursesButtonListener() {
        manageCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageCoursesFrame coursesFrame = new ManageCoursesFrame(system, student);
                coursesFrame.setVisible(true);
            }
        });
    }

    // EFFECTS: load labels of the frame
    private void loadLabels() {
        setLayout(null);
        infoPanel.setBounds(30, 20, 200, 250);
        studentInfoLabels = new JLabel[labels.length];
        textFields = new JTextField[labels.length];

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        String[] studentInfo = getStudentInfo();
        // display student basic info
        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel();
            SpringLayout layout = new SpringLayout();
            studentInfoLabels[i] = new JLabel(labels[i], JLabel.TRAILING);
            JLabel l = studentInfoLabels[i];
            textFields[i] = new JTextField();
            JTextField textField = textFields[i];
            textField.setText(studentInfo[i]);
            textField.setEditable(false);
            textField.setSize(new Dimension(40, 3));
            layout.putConstraint(SpringLayout.WEST, textField, 5, SpringLayout.EAST, l);
            row.setLayout(layout);
            row.add(l);
            row.add(textField);

            infoPanel.add(row);
        }

    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    private String[] getStudentInfo() {
        String[] result = new String[7];
        result[0] = student.getName();
        result[1] = student.getId() + "";
        result[2] = student.getAge() + "";
        result[3] = student.getGPA() + "";
        result[4] = student.getCovidStatus() ? "true" : "false";
        result[5] = student.getFaculty().toString().toLowerCase();
        result[6] = student.getYearLevel().toString().toLowerCase();

        return result;
    }

}
