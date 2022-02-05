package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

// This class is a panel that has a table and search bar
public class SearchInformationTablePanel extends JPanel {
    private Object[][] data;
    private ArrayList<String> columnNames;

    private JTable table;
    private TableRowSorter sorter;
    private DefaultTableModel tableModel;
    private JPanel searchPanel;
    private JScrollPane scrollPane;

    private JLabel searchLabel;
    private JTextField searchTextField;

    // Construct a table panel that takes in a 2D data and columnNames
    public SearchInformationTablePanel(Object[][] data, ArrayList<String> columnNames) {
        this.data = data;
        this.columnNames = columnNames;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initTable();
    }

    // EFFECTS: initialize the table (Layout, labels and search text field)
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void initTable() {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchTextField = new JTextField();
        searchLabel = new JLabel("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);

        add(searchPanel);

        table = new JTable(data, columnNames.toArray());
        table.setPreferredScrollableViewportSize(new Dimension(400, 430));
        table.setFillsViewportHeight(true);


        // Make sure all the data in the table are not editable
        tableModel = new DefaultTableModel(data, columnNames.toArray()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        //Create the scroll pane and add the table to it.


        table.setModel(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        // User can sort each column
        table.setAutoCreateRowSorter(true);
        table.setRowSorter(sorter);

        scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);

        addDocumentListener();
        add(scrollPane);
    }


    // EFFECTS: enables user to use regex search in the textField
    // Code is based on:
    // https://www.tutorialspoint.com/how-to-implement-the-search-functionality-of-a-jtable-in-java
    private void addDocumentListener() {
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchTextField.getText());
            }

            public void search(String text) {
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(text));
                }
            }
        });
    }

    // EFFECTS: return the JTable that this table panel contains
    public JTable getTable() {
        return table;
    }

    // EFFECTS: return table model of this JTable, so we can fire data change in other places
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
