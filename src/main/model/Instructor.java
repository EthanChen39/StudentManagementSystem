package model;

import model.enums.Faculty;
import model.enums.Gender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Instructor class has the ability to teach a course.
public class Instructor extends Employee {
    private List<Course> teachingCourseList;

    // EFFECTS: Construct an instructor with most of the features the same as Employee class.
    //          It has a distinct attribute teachingCourseList
    public Instructor(int age, String name,
                      Gender gender, int id,
                      boolean isCovidPositive,
                      Faculty faculty, int yearsOfExperience) {
        super(age, name, gender, id, isCovidPositive, faculty, yearsOfExperience);
        teachingCourseList = new ArrayList<>();
    }

    // REQUIRES: course is not in the teachingCourseList
    // MODIFIES: this
    // EFFECTS: add course to this teachingCourseList
    public boolean addCourseToTeach(Course course) {
        if (course != null && !teachingCourseList.contains(course)) {
            teachingCourseList.add(course);
            course.addInstructor(this);
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: a deep-copy teachingCourseList is returned
    public List<Course> getTeachingCourseList() {
        return new ArrayList<>(teachingCourseList);
    }

    public JSONObject toJson() {
        JSONObject instructorJson = super.toJson();
        instructorJson.put("TeachingCourses",courseListToJson());
        return instructorJson;
    }

    // EFFECTS: returns courses that this student is taking as a JSON array
    private JSONArray courseListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Course course: teachingCourseList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SubjectCode", course.getSubjectCode());
            jsonObject.put("Section", course.getSection());
            jsonObject.put("Credits", course.getCredit());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
