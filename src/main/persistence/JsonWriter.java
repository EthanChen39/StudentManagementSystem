package persistence;

import model.ManagementSystem;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of workroom to file
// This class is largely based on the work:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonWriter.java
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonWriter.java#L15
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonWriter.java#L22
    public void write(ManagementSystem managementSystem) {
        JSONObject json = managementSystem.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonWriter.java#L35
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonWriter.java#L41
    private void saveToFile(String json) {
        writer.print(json);
    }
}