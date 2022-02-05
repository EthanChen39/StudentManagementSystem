package persistence;

import org.json.JSONObject;

// This interface is largely based on the work:
// Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/Writable.java
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
