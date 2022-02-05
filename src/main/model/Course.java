package model;

import model.enums.SubjectCode;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

//The course class can add/remove a person. It also can declare a positive exposure to COVID-19
public class Course implements Traceable, Writable {
    private final SubjectCode subjectCode;
    private boolean covidAlert;
    private int credit;
    private final int section;

    private List<Instructor> instructorsList;
    private List<Student> studentsList;
    private List<TeachingAssistance> teachingAssistanceList;

    // EFFECTS: Construct a course with appropriate subject Code, covidAlert and credit
    public Course(SubjectCode subjectCode, boolean covidAlert, int credit, int section) {
        this.subjectCode = subjectCode;
        this.covidAlert = covidAlert;
        this.credit = credit;
        this.section = section;
        // Initialize data members
        instructorsList = new ArrayList<>();
        studentsList = new ArrayList<>();
        teachingAssistanceList = new ArrayList<>();
    }

    // EFFECTS: Default Constructor that creates a course with covidAlert to be false
    public Course(SubjectCode subjectCode, int credit, int section) {
        this(subjectCode, false, credit, section);
    }

    // EFFECTS: returns the number of students in this course
    int getClassSize() {
        return studentsList.size();
    }

    // EFFECTS: return the subject code of this course
    public SubjectCode getSubjectCode() {
        return subjectCode;
    }

    // EFFECTS: return the section of this course
    public int getSection() {
        return section;
    }

    // EFFECTS: return a deep copy of this instructorsList
    public List<Instructor> getInstructorsList() {
        return new ArrayList<>(this.instructorsList);
    }

    // EFFECTS: return a deep copy of this studentList
    public List<Student> getStudentsList() {
        return new ArrayList<>(studentsList);
    }

    // EFFECTS: return the credit of this course
    public int getCredit() {
        return credit;
    }

    //EFFECTS: return a deep copy of the teaching assistance list
    public List<TeachingAssistance> getTeachingAssistanceList() {
        return new ArrayList<>(this.teachingAssistanceList);
    }

    // REQUIRES: instructor object is not null and instructor is not in the list
    // MODIFIES: this
    // EFFECTS: add an instructor this instructor list
    public boolean addInstructor(Instructor instructor) {
        if (instructor != null && !instructorsList.contains(instructor)) {
            instructorsList.add(instructor);
            instructor.addCourseToTeach(this);
            return true;
        }
        return false;
    }

    // REQUIRES: teaching assistance object is not null
    // MODIFIES: this
    // EFFECTS: add the teaching assistance this TA list
    public boolean addTeachingAssistance(TeachingAssistance teachingAssistance) {
        if (teachingAssistance != null && !teachingAssistanceList.contains(teachingAssistance)) {
            this.teachingAssistanceList.add(teachingAssistance);
            return true;
        }
        return false;

    }

    // REQUIRES: student object is not null
    // MODIFIES: this
    // EFFECTS: add a new student to this students list
    public boolean addStudents(Student student) {
        if (student != null && !studentsList.contains(student)) {
            studentsList.add(student);
            student.addCourse(this);
            return true;
        }
        return false;
    }

    // REQUIRES: student object is not null and student is in this course
    // MODIFIES: this.studentList
    // EFFECTS: remove a student in the student list
    public boolean removeStudent(Student student) {
        if (student != null && studentsList.contains(student)) {
            this.studentsList.remove(student);
            student.withdrawFromCourse(this);
            return true;
        }
        return false;
    }


    // REQUIRES: instructor object is not null and there are more than 1 instructor in this course
    // MODIFIES: this.instructorList
    // EFFECTS: remove an instructor in the instructor list
    public boolean removeInstructor(Instructor instructor) {
        if (instructorsList.contains(instructor)
                && instructorsList.size() > 1) {
            instructorsList.remove(instructor);
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: set this course and students in this course to be COVID-19 positive
    @Override
    public void declarePositive() {
        if (covidAlert) {
            return;
        }
        //Declare positive for this course and all the people in this course
        covidAlert = true;
        for (Student student: studentsList) {
            student.declarePositive();
        }
        for (Instructor instructor: instructorsList) {
            instructor.declarePositive();
        }

        for (TeachingAssistance teachingAssistance: teachingAssistanceList) {
            teachingAssistance.declarePositive();
        }
    }

    // MODIFIES: this.covidAlert
    // EFFECTS: set this course to be COVID-19 negative
    @Override
    public void declareNegative() {
        covidAlert = false;
    }


    @Override
    public boolean getCovidStatus() {
        return covidAlert;
    }

    // EFFECTS: returns true if the student is in this course, otherwise, return false
    public boolean containsStudent(Student student) {
        return studentsList.contains(student);
    }

    // REQUIRES: course object is not null
    // EFFECTS: Make the object that passes in is a Course object, if not return false
    //          return true if another course has the same subject Code and credit
    //          as this one, otherwise, return false
    public boolean equals(Object course) {
        if (!course.getClass().getSimpleName().equals("Course")) {
            return false;
        } else {
            // Cast the Object to a course type
            Course anotherCourse = (Course) course;
            return anotherCourse.subjectCode == this.subjectCode
                    && anotherCourse.credit == this.credit;
        }
    }

    // EFFECTS: return a formatted string that contains information of this course
    public String toString() {
        String mainInstructorName = instructorsList.get(0).getName();
        return  "[" + subjectCode + "] section (" + section + ") with credits {" + credit + "} has "
                + studentsList.size() + " student(s)."
                + " Main Instructor: " + mainInstructorName + ".";
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SubjectCode", subjectCode);
        jsonObject.put("Credit", credit);
        jsonObject.put("section", section);
        jsonObject.put("studentList", studentListToJson());
        jsonObject.put("instructorList", instructorListToJson());
        jsonObject.put("taList", taListToJson());
        return jsonObject;
    }

    private JSONArray studentListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Student student: studentsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Student name", student.getName());
            jsonObject.put("id", student.getId());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    private JSONArray instructorListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Instructor instructor: instructorsList) {
            jsonArray.put(instructor.toJson());
        }
        return jsonArray;
    }

    private JSONArray taListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (TeachingAssistance teachingAssistance: teachingAssistanceList) {
            jsonArray.put(teachingAssistance.toJson());
        }
        return jsonArray;
    }
}
