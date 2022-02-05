package ui.frames;

import model.Course;
import model.Instructor;
import model.ManagementSystem;
import model.TeachingAssistance;
import model.enums.SubjectCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// This class displays a frame that asks user to enter information of a course and add it to the system
public class AddCourseFrame extends JFrame {
    private JList subjectCodeList;
    private JScrollPane subjectListScroller;

    private JSpinner creditSpinner;
    private JTextField sectionTextField;
    private ManagementSystem system;

    private JButton submitButton;

    private String[] subjects;

    private JList instructorList;
    private JScrollPane instructorScrollPanel;

    private String[] instructorNames;

    private JList taList;
    private JScrollPane taScrollPanel;

    private String[] taNames;


    // Construct an addCourseFrame that takes in a system object
    public AddCourseFrame(ManagementSystem system) {
        super("Add a Course");
        this.system = system;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 700));
        setLayout(new GridLayout(0, 1));
        loadSubjectCodes();
        loadTaNames();
        loadInstructorNames();
        initComponents();
        renderFrame();
        setSubmitButtonListener();
        centreOnScreen();
    }

    // EFFECTS: when user hits submit button, grab all the data and create a new course object
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void setSubmitButtonListener() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Instructor> instructors = system.getInstructorList();
                List<TeachingAssistance> tas = system.getTeachingAssistanceList();
                int instructorIndex = instructorList.getSelectedIndex();
                int taIndex = taList.getSelectedIndex();
                Instructor selectedInstructor = null;
                TeachingAssistance selectedTA = null;
                if (instructorIndex != -1 && taIndex != -1) {
                    selectedInstructor = instructors.get(instructorIndex);
                    selectedTA = tas.get(taIndex);
                }

                SubjectCode subjectCode = SubjectCode.getEnum((String) subjectCodeList.getSelectedValue());
                int credit = (int) creditSpinner.getValue();
                int section = Integer.parseInt(sectionTextField.getText());

                Course newCourse = new Course(subjectCode, credit, section);
                newCourse.addTeachingAssistance(selectedTA);
                newCourse.addInstructor(selectedInstructor);
                system.addCourseToSystem(newCourse);
                setVisible(false);
                JOptionPane.showMessageDialog(new JFrame("Successful"), "Added Successfully");
            }
        });
    }

    // MODIFIES: this.taNames
    // EFFECTS: load all the TA's names from the system to this.taNames
    private void loadTaNames() {
        List<TeachingAssistance> tas = system.getTeachingAssistanceList();
        taNames = new String[tas.size()];
        for (int i = 0; i < tas.size(); i++) {
            taNames[i] = tas.get(i).getName();
        }
    }

    // MODIFIES: this.instructorNames
    // EFFECTS: load instructors' names from system to this.instructorNames
    private void loadInstructorNames() {
        List<Instructor> instructors = system.getInstructorList();
        instructorNames = new String[instructors.size()];
        for (int i = 0; i < instructors.size(); i++) {
            instructorNames[i] = instructors.get(i).getName();
        }
    }

    // MODIFIES: this.subjects
    // EFFECTS: load subject code from system to this.subjects
    private void loadSubjectCodes() {
        SubjectCode[] subjectCodes = SubjectCode.values();
        subjects = new String[subjectCodes.length];
        for (int i = 0; i < subjects.length; i++) {
            subjects[i] = subjectCodes[i].toString();
        }
    }

    
    // EFFECTS: initialize all the components that this frame has
    private void initComponents() {
        // set up subject code list
        subjectCodeList = new JList(subjects);
        subjectCodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectCodeList.setLayoutOrientation(JList.VERTICAL);
        subjectListScroller = new JScrollPane(subjectCodeList);
        subjectListScroller.setPreferredSize(new Dimension(250, 80));

        SpinnerModel model = new SpinnerNumberModel(3, 0, 50, 1);
        creditSpinner = new JSpinner(model);

        sectionTextField = new JTextField();

        instructorList = new JList(instructorNames);
        instructorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instructorList.setLayoutOrientation(JList.VERTICAL);
        instructorScrollPanel = new JScrollPane(instructorList);
        instructorScrollPanel.setPreferredSize(new Dimension(250, 80));

        taList = new JList(taNames);
        taList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taList.setLayoutOrientation(JList.VERTICAL);
        taScrollPanel = new JScrollPane(taList);
        taScrollPanel.setPreferredSize(new Dimension(250, 80));

        submitButton = new JButton("Submit");
    }

    // MODIFIES: this
    // EFFECTS: create panels that each contains subject, credit, section, "add an instructor" and "button panel"
    //          then add these panels to this frame
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void renderFrame() {
        JPanel subjectCodePanel = new JPanel();
        subjectCodePanel.add(new JLabel("Subject: "));
        subjectCodePanel.add(subjectListScroller);

        JPanel creditPanel = new JPanel();
        creditPanel.add(new JLabel("Credit: "));
        creditPanel.add(creditSpinner);

        JPanel sectionPanel = new JPanel();
        sectionPanel.add(new JLabel("Section: "));
        sectionTextField.setPreferredSize(new Dimension(80, 20));
        sectionPanel.add(sectionTextField);

        JPanel instructorPanel = new JPanel();
        instructorPanel.add(new JLabel("Add an instructor"));
        instructorPanel.add(instructorScrollPanel);
        instructorPanel.setSize(new Dimension(270, 100));

        JPanel taPanel = new JPanel();
        taPanel.add(new JLabel("Add a TA"));
        taPanel.add(taScrollPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        add(subjectCodePanel);
        add(creditPanel);
        add(sectionPanel);
        add(instructorPanel);
        add(taPanel);
        add(buttonPanel);
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
