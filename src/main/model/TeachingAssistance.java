package model;

import model.enums.Faculty;
import model.enums.Gender;
import org.json.JSONObject;

// Teaching assistance has TA course as a distinct attribute
public class TeachingAssistance extends Employee {
    private double hourlyWages;
    private Course taCourse;

    // REQUIRES: 1 <= age <= 150, name not null, id >= 1, yearsOfExperience >= 0, hourlyWages>= 0
    // EFFECTS: create a teaching assistance with the following attributes
    public TeachingAssistance(int age, String name, Gender gender,
                              int id, boolean isCovidPositive,
                              Faculty faculty, int yearsOfExperience,
                              double hourlyWages) {
        super(age, name, gender, id, isCovidPositive, faculty, yearsOfExperience);
        setHourlyWages(hourlyWages);
    }

    public double getHourlyWages() {
        return hourlyWages;
    }

    // REQUIRES: Make sure the TA is paying >= to the BC minimum wage
    // MODIFIES: this
    // EFFECTS: set
    public boolean setHourlyWages(double hourlyWages) {
        if (hourlyWages >= MINIMUM_WAGE) {
            this.hourlyWages = hourlyWages;
            return true;
        }
        return false;
    }

    public void assignTaCourse(Course course) {
        this.taCourse = course;
    }

    // Remove the course that this TA is teaching
    public boolean removeTaCourse() {
        if (taCourse == null) {
            return false;
        } else {
            taCourse = null;
            return true;
        }
    }

    // EFFECTS: get the course that is TA is teaching
    public Course getTaCourse() {
        return taCourse;
    }

    public JSONObject toJson() {
        JSONObject teachingAssistanceJson = super.toJson();
        teachingAssistanceJson.put("TA_Course", taCourse);
        return teachingAssistanceJson;
    }

}
