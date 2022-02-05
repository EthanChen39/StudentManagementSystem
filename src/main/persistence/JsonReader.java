package persistence;

import model.*;
import model.enums.Faculty;
import model.enums.Gender;
import model.enums.SubjectCode;
import model.enums.YearLevel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


//The class methods and fields are derived from this given demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
//Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonReader.java#L20
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads management system from file and returns it;
    // throws IOException if an error occurs reading data from file
    // This method is cited here
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonReader.java#L26
    public ManagementSystem read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseManagementSystem(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonReader.java#L33
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses management system from JSON object and returns it
    // Citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/d31979d8a993d63c3a8c13c8add7f9d1753777b6/src/main/persistence/JsonReader.java#L44
    private ManagementSystem parseManagementSystem(JSONObject jsonObject) {
        ManagementSystem managementSystem = new ManagementSystem();
        addStudentList(managementSystem, jsonObject);
        addTaList(managementSystem, jsonObject);
        addInstructorList(managementSystem, jsonObject);
        addCourseList(managementSystem, jsonObject);
        assignStudentGrades(managementSystem, jsonObject);
        return managementSystem;
    }

    // MODIFIES: management system
    // EFFECTS: parses instructorList from JSON object and adds them to management system
    private void addInstructorList(ManagementSystem managementSystem, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("instructorList");
        for (Object json: jsonArray) {
            JSONObject nextInstructor = (JSONObject) json;
            addInstructor(managementSystem, nextInstructor);
        }
    }

    // MODIFIES: management system
    // EFFECTS: parses instructor from JSON object and adds them to management system
    private void addInstructor(ManagementSystem managementSystem, JSONObject nextInstructor) {
        Gender gender = Gender.valueOf(nextInstructor.getString("gender"));
        int yearsOfExperience = nextInstructor.getInt("yearsOfExperience");
        boolean isCovidPositive = nextInstructor.getBoolean("isCovidPositive");
        String name = nextInstructor.getString("name");
        int id = nextInstructor.getInt("id");
        int age = nextInstructor.getInt("age");
        Faculty faculty = Faculty.valueOf(nextInstructor.getString("faculty"));
        Instructor instructor = new Instructor(age, name,
                gender, id, isCovidPositive,
                faculty, yearsOfExperience);
        managementSystem.addInstructorToSystem(instructor);
    }

    // MODIFIES: management system
    // EFFECTS: parses taList from JSON object and adds them to management system
    private void addTaList(ManagementSystem managementSystem, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("taList");
        for (Object json: jsonArray) {
            JSONObject nextTA = (JSONObject) json;
            addTA(managementSystem, nextTA);
        }
    }

    // MODIFIES: management system
    // EFFECTS: parses TA from JSON object and adds them to management system
    private void addTA(ManagementSystem managementSystem, JSONObject nextTA) {
        Gender gender = Gender.valueOf(nextTA.getString("gender"));
        int yearsOfExperience = nextTA.getInt("yearsOfExperience");
        double hourlyWages = nextTA.getDouble("wage");
        boolean isCovidPositive = nextTA.getBoolean("isCovidPositive");
        String name = nextTA.getString("name");
        int id = nextTA.getInt("id");
        int age = nextTA.getInt("age");
        Faculty faculty = Faculty.valueOf(nextTA.getString("faculty"));
        TeachingAssistance teachingAssistance = new TeachingAssistance(age, name,
                gender, id, isCovidPositive,
                faculty, yearsOfExperience, hourlyWages);
        managementSystem.addTAToSystem(teachingAssistance);
    }


    // MODIFIES: management system
    // EFFECTS: parses courseList from JSON object and adds them to management system
    private void addCourseList(ManagementSystem managementSystem, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("courseList");
        for (Object json: jsonArray) {
            JSONObject nextCourse = (JSONObject) json;
            addCourse(managementSystem, nextCourse);
        }

    }

    // MODIFIES: management system
    // EFFECTS: parses Course from JSON object and adds them to management system
    private void addCourse(ManagementSystem managementSystem, JSONObject nextCourse) {
        int credit = nextCourse.getInt("Credit");
        SubjectCode subjectCode = SubjectCode.valueOf(nextCourse.getString("SubjectCode"));
        int section = nextCourse.getInt("section");
        JSONArray instructorJsonArray = nextCourse.getJSONArray("instructorList");
        JSONArray studentJsonArray = nextCourse.getJSONArray("studentList");
        JSONArray taJsonArray = nextCourse.getJSONArray("taList");
        Course course = new Course(subjectCode, credit, section);
        addInstructorsToCourse(course, managementSystem, instructorJsonArray);
        addStudentsToCourse(course, managementSystem, studentJsonArray);
        addTaToCourse(course, managementSystem, taJsonArray);
        managementSystem.addCourseToSystem(course);
    }

    // MODIFIES: management system
    // EFFECTS: parses students jsonObject and loads their grades back
    private void assignStudentGrades(ManagementSystem managementSystem, JSONObject jsonObject) {
        JSONArray studentJsonArray = jsonObject.getJSONArray("studentList");
        // For each student
        for (Object json: studentJsonArray) {
            JSONObject studentJson = (JSONObject) json;
            JSONArray grades = studentJson.getJSONArray("Grades");
            Student student = (Student) managementSystem.findPersonById(studentJson.getInt("id"));
            // For each grade they have
            for (int i = 0; i < grades.length(); i++) {
                JSONObject gradeJson = (JSONObject) grades.get(i);
                String courseDescription = gradeJson.keys().next();
                int grade = gradeJson.getInt(courseDescription);
                String subjectCodeStr = courseDescription.substring(courseDescription.indexOf("[") + 1,
                        courseDescription.indexOf("]"));
                int section = Integer.parseInt(courseDescription.substring(courseDescription.indexOf("(") + 1,
                        courseDescription.indexOf(")")));
                Course course = managementSystem.findCourseByNameAndSection(subjectCodeStr, section);
                student.addGrade(course, grade);
            }


        }
    }

    // MODIFIES: management system and courses
    // EFFECTS: parses ta from JSON object and adds them to courses
    private void addTaToCourse(Course course, ManagementSystem managementSystem, JSONArray instructorJsonArray) {
        for (Object json: instructorJsonArray) {
            JSONObject taJson = (JSONObject) json;
            TeachingAssistance teachingAssistance =
                    (TeachingAssistance) managementSystem.findPersonById(taJson.getInt("id"));
            course.addTeachingAssistance(teachingAssistance);
        }

    }

    // MODIFIES: management system and courses
    // EFFECTS: parses students from JSON object and adds them to courses
    private void addStudentsToCourse(Course course, ManagementSystem managementSystem, JSONArray studentJsonArray) {
        for (Object json: studentJsonArray) {
            JSONObject studentJson = (JSONObject) json;
            Student student = (Student) managementSystem.findPersonById(studentJson.getInt("id"));
            course.addStudents(student);
        }
    }

    // MODIFIES: management system and courses
    // EFFECTS: parses instructors from JSON objects and adds them to courses
    private void addInstructorsToCourse(Course course,
                                        ManagementSystem managementSystem,
                                        JSONArray instructorJsonArray) {
        for (Object json: instructorJsonArray) {
            JSONObject instructorJson = (JSONObject) json;
            Instructor instructor = (Instructor) managementSystem.findPersonById(instructorJson.getInt("id"));
            course.addInstructor(instructor);
        }
    }

    // MODIFIES: management system
    // EFFECTS: parses studentList from JSON object and adds them to management system
    private void addStudentList(ManagementSystem managementSystem, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("studentList");
        for (Object json: jsonArray) {
            JSONObject nextStudent = (JSONObject) json;
            addStudent(managementSystem, nextStudent);
        }
    }

    // MODIFIES: management system
    // EFFECTS: parses student from JSON object and adds them to management system
    private void addStudent(ManagementSystem managementSystem, JSONObject jsonObject) {
        YearLevel yearLevel = YearLevel.valueOf(jsonObject.getString("YearLevel"));
        Gender gender = Gender.valueOf(jsonObject.getString("gender"));
        boolean isCovidPositive = jsonObject.getBoolean("isCovidPositive");
        String name = jsonObject.getString("name");
        int id = jsonObject.getInt("id");
        int age = jsonObject.getInt("age");
        Faculty faculty = Faculty.valueOf(jsonObject.getString("faculty"));
        Student student = new Student(age, name, gender, id, isCovidPositive, faculty, yearLevel);
        managementSystem.addStudentToSystem(student);
    }
}
