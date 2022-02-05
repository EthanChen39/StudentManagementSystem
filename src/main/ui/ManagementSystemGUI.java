package ui;

import model.Event;
import model.EventLog;
import model.ManagementSystem;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// This class contains all the main component of system GUI
public class ManagementSystemGUI extends JFrame {
    private static final int PREFERRED_WIDTH = 500;
    private static final int PREFERRED_HEIGHT = 600;

    private ManagementSystem system;
    private StudentTable tablePanel;

    private static final String JSON_STORE = "./data/managementSystem.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JButton loadButton;
    private JButton saveButton;


    // EFFECTS: Construct a frame, and initialize json reader and writer
    public ManagementSystemGUI() {
        super("Student Management System");
        setLayout(null);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        init();
        setLoadButtonListener();
        setSaveButtonListener();
        addWindowListener();
    }

    // EFFECTS: this method will print all the log entries to the console when the window is closed
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void addWindowListener() {
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                boolean isEmptyLog = true;
                for (Event event : EventLog.getInstance()) {
                    isEmptyLog = false;
                    System.out.println(event.getDescription());
                }
                if (isEmptyLog) {
                    System.out.println("No new log entries.");
                }

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    // EFFECTS: when user clicks save, it saves the current system to a json file
    private void setSaveButtonListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSystem();
            }
        });
    }

    // MODIFIES: this - data
    // EFFECTS: when user clicks load, load saved system data to the table
    private void setLoadButtonListener() {
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSystem();
                tablePanel.setSystem(system);
                tablePanel.loadData();
                tablePanel.refreshTable();
                DefaultTableModel tableModel = tablePanel.getTableModel();
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(new JFrame("Successfully"), "File loaded successfully");
            }
        });
    }

    // EFFECTS: save the current system to JSON_STORE
    private void saveSystem() {
        try {
            jsonWriter.open();
            jsonWriter.write(system);
            jsonWriter.close();
            JOptionPane.showMessageDialog(new JFrame(), "Saved Management System to " + JSON_STORE);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Warning",
                    "Unable to write to file: " + JSON_STORE, JOptionPane.WARNING_MESSAGE);
        }
    }

    // EFFECTS: load data to this system
    private void loadSystem() {
        try {
            system = jsonReader.read();
            System.out.println("Loaded ManagementSystem from " + JSON_STORE);
        } catch (IOException ex) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // init the system, set size of the frame, load buttons and set up listeners
    private void init() {
        system = new ManagementSystem();

//        system.loadPeopleAndCoursesToSystem();
//        system.assignRandomStudentsToCourses();
//        system.assignGradesToStudents();
//        system.assignRandomInstructorsToCourses();
//        system.assignRandomTAsToCourses();

        setSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tablePanel = new StudentTable(system);
        add(tablePanel);
        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);

        add(buttonPanel);

//        setVisible(true);
        setResizable(true);
        centreOnScreen();
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    public static void main(String[] args) {
        ManagementSystemGUI managementSystemGUI = new ManagementSystemGUI();
        managementSystemGUI.setVisible(true);
    }
}
