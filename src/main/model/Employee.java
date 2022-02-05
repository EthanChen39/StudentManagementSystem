package model;

import model.enums.Faculty;
import model.enums.Gender;
import org.json.JSONObject;

// This is an abstract class that represents some general information of an employee.
public abstract class Employee extends Person {
    protected static final double MINIMUM_WAGE = 15.20;
    protected int yearsOfExperience;
    protected double wage;
    protected Faculty faculty;

    // REQUIRES: init an employee, it needs a person's info and yearsOfExperience
    public Employee(int age, String name, Gender gender,
                    int id, boolean isCovidPositive,
                    Faculty faculty, int yearsOfExperience) {
        super(age, name, gender, id, isCovidPositive);
        this.faculty = faculty;
        this.yearsOfExperience = yearsOfExperience;
        this.wage = MINIMUM_WAGE;

    }

    // EFFECTS: Set the wage of this employee to the specified wage
    public void setWage(double wage) {
        this.wage = Math.max(wage, MINIMUM_WAGE);
    }

    // EFFECTS: return the wage of this employee
    public double getWage() {
        return this.wage;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("faculty", faculty);
        jsonObject.put("yearsOfExperience", yearsOfExperience);
        jsonObject.put("wage", wage);
        return jsonObject;
    }

}
