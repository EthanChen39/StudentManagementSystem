package ui.frames;

import model.ManagementSystem;
import model.enums.Gender;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


// This class is a frame that asks user to enter basic information of a person and add him/her to the system
public abstract class AddPersonFrame extends JFrame {
    protected JPanel infoPanel;
    protected JTextField nameField;

    protected JSpinner ageSpinner;

    protected JComboBox genderBox;

    private JPanel covidRadioPanel;
    private ButtonGroup covidRadioButtonGroup;

    protected JRadioButton positiveButton;
    protected JRadioButton negativeButton;

    protected JButton submitButton;

    protected ManagementSystem system;

    private static final int INITIAL_AGE = 18;
    private static final int MIN_AGE = 6;
    private static final int MAX_AGE = 110;

    private String[] genders;

    public AddPersonFrame(ManagementSystem system) {
        super();
        this.system = system;
        setFrameDefaultBehaviour();
        infoPanel = new JPanel();
        submitButton = new JButton("Submit");
        setSubmitButtonListener();
        infoPanel.setLayout(new GridLayout(0, 1));
        infoPanel.setPreferredSize(new Dimension(300, 450));
        // init name field
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(100, 30));
        // init age spinner
        SpinnerModel model = new SpinnerNumberModel(INITIAL_AGE, MIN_AGE, MAX_AGE, 1);
        ageSpinner = new JSpinner(model);
        // init gender combo box
        loadGenders();
        genderBox = new JComboBox(genders);
        // init radio button group
        covidRadioButtonGroup = new ButtonGroup();
        covidRadioPanel = new JPanel();
        positiveButton = new JRadioButton("Positive");
        negativeButton = new JRadioButton("Negative");
        covidRadioButtonGroup.add(positiveButton);
        covidRadioButtonGroup.add(negativeButton);
        covidRadioPanel.add(negativeButton);
        covidRadioPanel.add(positiveButton);
        addLabelsAndFields();
    }

    private void setFrameDefaultBehaviour() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 500));
    }

    protected abstract void setSubmitButtonListener();

    private void addLabelsAndFields() {
        JPanel namePanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Name: ");
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        infoPanel.add(namePanel);

        JPanel agePanel = new JPanel();
        JLabel ageLabel = new JLabel("age: ");
        agePanel.add(ageLabel);
        agePanel.add(ageSpinner);
        infoPanel.add(agePanel);

        JPanel genderPanel = new JPanel();
        JLabel genderLabel = new JLabel("Gender: ");
        genderPanel.add(genderLabel);
        genderPanel.add(genderBox);
        infoPanel.add(genderPanel);

        JPanel covidPanel = new JPanel();
        JLabel covidLabel = new JLabel("Covid Status: ");
        covidPanel.add(covidLabel);
        covidPanel.add(covidRadioPanel);
        infoPanel.add(covidPanel);

    }

    private void loadGenders() {
        Gender[] allGenders = Gender.values();
        genders = new String[allGenders.length];
        for (int i = 0; i < genders.length; i++) {
            genders[i] = allGenders[i].toString();
        }
    }

    protected ArrayList<Object> getPersonInfo() {
        ArrayList<Object> personInfo = new ArrayList<>();
        String name = nameField.getText();
        int age = (Integer) ageSpinner.getValue();
        String genderStr = (String) genderBox.getSelectedItem();
        assert genderStr != null;
        Gender gender = Gender.valueOf(genderStr.toUpperCase());
        boolean covidStatus = positiveButton.isSelected();
        personInfo.add(name);
        personInfo.add(age);
        personInfo.add(gender);
        personInfo.add(covidStatus);
        return personInfo;
    }
}
