package model;

import model.enums.Faculty;
import model.enums.Gender;
import model.enums.YearLevel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.*;

// Student class extends from Person class. Students can register/drop courses
// Calculate this student's GPA, get this student's grades
public class Student extends Person {
    private YearLevel yearLevel;
    private Faculty faculty;

    private List<Course> courseList;
    // Each course is associated with a grade
    private HashMap<Course, Integer> grades;

    public Student(int age, String name, Gender gender,
                   int id, boolean isCovidPositive,
                   Faculty faculty, YearLevel yearLevel) {
        super(age, name, gender, id, isCovidPositive);

        this.faculty = faculty;
        this.yearLevel = yearLevel;
        //init the data fields
        courseList = new ArrayList<>();
        grades = new HashMap<>();

    }



    // REQUIRES: course is not a null object
    // EFFECTS: return the grade of the course
    public boolean isGraded(Course course) {
        return grades.containsKey(course);
    }

    // REQUIRES: course is not NULL
    // MODIFIES: this.courseList
    // EFFECTS: adds a new course to the list of courses
    public void addCourse(Course course) {
        if (course != null && !courseList.contains(course)) {
            courseList.add(course);
            addGrade(course, 0);
            course.addStudents(this);
        }
    }

    // REQUIRES: course is not NULL and is in the courseList, grade is 0-100
    // MODIFIES: this.grades
    // EFFECTS: adds a new grade associates with the course to the grade list
    public boolean addGrade(Course course, int grade) {
        if (course != null
                && courseList.contains(course) && grade >= 0 && grade <= 100) {
            grades.put(course, grade);
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: return the list of courses that this student is taking
    public List<Course> getCourses() {
        //Make a deep copy of the list
        return new ArrayList<>(courseList);
    }

    public HashMap<Course, Integer> getGrades() {
        return new HashMap<>(grades);
    }

    // REQUIRES: course is not null
    // MODIFIES: this, courseList
    // EFFECTS: withdraw student from the given course, and also remove their grade
    public boolean withdrawFromCourse(Course course) {
        if (course != null && courseList.contains(course)) {
            courseList.remove(course);
            grades.remove(course);
            course.removeStudent(this);
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: return the number of courses this student is currently taking
    public int getNumOfCourses() {
        return this.courseList.size();
    }

    public YearLevel getYearLevel() {
        return yearLevel;
    }

    public Faculty getFaculty() {
        return faculty;
    }



    // EFFECTS: returns this student's GPA in a 4.33 GPA scale system
    public double getGPA() {
        double gpaSum = 0.00;
        int numOfCourses = grades.size();
        if (numOfCourses > 0) {
            for (Integer grade : grades.values()) {
                gpaSum += Util.convertGradeToGPA(grade);
            }
            //Compute a student's average score and format it in 2 decimals
            DecimalFormat format = new DecimalFormat("#.##");
            return new Double(format.format(gpaSum / numOfCourses));
        } else {
            // If this student is not taking any courses
            // If this student is not taking any courses
            return 0.00;
        }
    }

    // REQUIRES: the course object is not null, course is in the course list
    // and 0 <= grade <= 100
    // MODIFIES: this.grades
    // EFFECTS: change the grade in a given course, return true if it added successfully
    //          otherwise, return false
    public boolean changeGrade(Course course, Integer grade) {
        if (course != null
                && courseList.contains(course)
                && grade >= 0 && grade <= 100) {
            grades.replace(course, grade);
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: return a string that contains basic information of a student
    @Override
    public String toString() {
        return "studentID=" + id + "{"
                + "Name=" + name
                + ", age=" + age
                + ", GPA=" + getGPA()
                + ", yearLevel=" + yearLevel
                + ", faculty=" + faculty
                + ", number of courses=" + getNumOfCourses()
                + ", isCovidPositive=" + isCovidPositive
                + '}';
    }

    // MODIFIES: this.courseList
    // EFFECTS: mark this student as positive, and also mark the courses that this student is taking as positive
    @Override
    public void declarePositive() {
        // If this student has been declared positive, do nothing and return
        if (isCovidPositive) {
            return;
        }
        isCovidPositive = true;
        List<Course> courses = courseList;
        // for every course that this student is taking, declare positive
        for (Course course: courses) {
            course.declarePositive();
        }
    }

    // REQUIRES: course is in grades and course is not null
    // EFFECTS: returns student's grade of a course, if the course is not in grades, returns 0
    public int getGrade(Course course) {
        if (grades.get(course) == null) {
            return 0;
        }
        return grades.get(course);
    }

    @Override
    public JSONObject toJson() {
        JSONObject studentJson = super.toJson();
        studentJson.put("YearLevel", yearLevel);
        studentJson.put("faculty", faculty);
        studentJson.put("Courses", courseListToJson());
        studentJson.put("Grades", gradesToJsonArray());
        return studentJson;
    }

    // EFFECTS: returns courses that this student is taking as a JSON array
    private JSONArray courseListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Course course: courseList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Subject Code", course.getSubjectCode());
            jsonObject.put("Section", course.getSection());
            jsonObject.put("Credits", course.getCredit());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    private JSONArray gradesToJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<Course, Integer> entry: grades.entrySet()) {
            JSONObject gradeJson = new JSONObject();
            Course course = entry.getKey();
            Integer grade = entry.getValue();
            gradeJson.put(course.toString(), grade);
            jsonArray.put(gradeJson);
        }
        return jsonArray;
    }
}
