package model;

import model.enums.SubjectCode;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// The management system stores all people and courses and
// perform certain tasks like (adding/removing/searching people/courses)
public class ManagementSystem implements Writable {
    private List<Student> studentList;
    private List<Instructor> instructorList;
    private List<TeachingAssistance> teachingAssistanceList;

    private List<Course> courseList;

    //Default constructor that initializes all the lists in the system
    public ManagementSystem() {
        //Initialize data members
        studentList = new ArrayList<>();
        instructorList = new ArrayList<>();
        teachingAssistanceList = new ArrayList<>();
        courseList = new ArrayList<>();
    }

    //Getter Methods for all the lists
    // EFFECTS: return a deep copy of this.studentList
    public List<Student> getStudentList() {
        return new ArrayList<>(this.studentList);
    }

    // EFFECTS: return a deep copy of this.instructorList
    public List<Instructor> getInstructorList() {
        return new ArrayList<>(this.instructorList);
    }

    // EFFECTS: return a deep copy of this.teachingAssistanceList
    public List<TeachingAssistance> getTeachingAssistanceList() {
        return new ArrayList<>(this.teachingAssistanceList);
    }

    // EFFECTS: return a deep copy of this.courseList
    public List<Course> getCourseList() {
        return new ArrayList<>(this.courseList);
    }

    // REQUIRES: student is not a null object
    // EFFECTS: add student to the student list
    public boolean addStudentToSystem(Student student) {
        if (student != null && !studentList.contains(student)) {
            studentList.add(student);
            EventLog.getInstance().logEvent(new Event("[Student "
                    + student.getId() + "] has been added to the system"));
            return true;
        } else {
            return false;
        }

    }

    // REQUIRES: instructor is not a null object
    // EFFECTS: add an instructor to this instructor list
    public boolean addInstructorToSystem(Instructor instructor) {
        if (instructor != null && !instructorList.contains(instructor)) {
            instructorList.add(instructor);
            EventLog.getInstance().logEvent(new Event("[Instructor "
                    + instructor.getId() + "] has been added to the system"));
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: teaching assistance is not a null object
    // EFFECTS: add an instructor to this instructor list
    public boolean addTAToSystem(TeachingAssistance teachingAssistance) {
        if (teachingAssistance != null && !teachingAssistanceList.contains(teachingAssistance)) {
            teachingAssistanceList.add(teachingAssistance);
            EventLog.getInstance().logEvent(new Event("[TA "
                    + teachingAssistance.getId() + "] has been added to the system"));
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: course is not a null object, and course is not in the courseList
    // EFFECTS: add a course to this course list
    public boolean addCourseToSystem(Course course) {
        if (course != null && !courseList.contains(course)) {
            courseList.add(course);
            EventLog.getInstance().logEvent(new Event("Course ["
                    + course.getSubjectCode() + "] section <"
                    + course.getSection() + "> has been added to the system"));
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: student is not a null object
    // EFFECTS: return true if this student is in the system
    //          otherwise, return false
    public boolean isStudentInSystem(Student student) {
        if (student == null) {
            return false;
        }
        return studentList.contains(student);
    }

    // REQUIRES: student's name exists in the list
    // EFFECTS: return a copy of the student object in the list
    public Student findStudentByName(String studentName) {
        Student result = null;
        for (Student student: studentList) {
            if (student.getName().equals(studentName)) {
                // If the student is found, set it to result and break
                result = student;
                break;
            }
        }
        return result;
    }

    // REQUIRES: course's name exists in the list
    // EFFECTS: return the course in the courseList
    public Course findCourseByNameAndSection(String name, int section) {
        for (Course course: courseList) {
            if (Objects.equals(course.getSubjectCode().title, name)
                    && section == course.getSection()) {
                return course;
            }
        }
        return null;
    }

    // REQUIRES: a person's id exists in the list
    // EFFECTS: return a copy of the student object in the list
    public Person findPersonById(int id) {
        Person result = null;
        List<Person> people = new ArrayList<>(studentList);
        people.addAll(instructorList);
        people.addAll(teachingAssistanceList);
        for (Person person: people) {
            if (person.getId() == id) {
                // If the person is found, set it to result and break
                result = person;
                break;
            }
        }
        return result;
    }

    // REQUIRES: student's name exists in the list, course exists in the course list
    // MODIFIES: this.courseList
    // EFFECTS: remove a student in the given course, return true if it removes successfully
    //          return false, otherwise
    public boolean withdrawStudentFromCourse(Student student, Course course) {
        // Make sure both student and the course are in the system
        if (!studentList.contains(student)
                || !courseList.contains(course)
                || !course.containsStudent(student)) {
            return false;
        }
        course.removeStudent(student);
        EventLog.getInstance().logEvent(new Event("[Student: " + student.getId()
                + "] has been withdrew from " + course.getSubjectCode()));
        return true;
    }

    // REQUIRES: student is not a null object && student in the studentList
    // MODIFIES: this.studentList
    // EFFECTS: remove a student from the system, also withdraw the courses he/her is taking and return true
    public boolean removeStudentFromSystem(Student student) {
        if (student != null && studentList.contains(student)) {
            List<Course> courses = student.getCourses();
            studentList.remove(student);
            for (Course course: courses) {
                withdrawStudentFromCourse(student, course);
            }
            return true;
        }
        return false;
    }

    // REQUIRES: instructor is not a null object and instructor is in this.instructorList
    // MODIFIES: this.instructorList
    // EFFECTS: remove instructor from this.instructorList and return true
    public boolean removeInstructorFromSystem(Instructor instructor) {
        if (instructor != null && instructorList.contains(instructor)) {
            List<Course> courses = instructor.getTeachingCourseList();
            // Try to remove courses that this instructor teaches
            for (Course course: courses) {
                if (!course.removeInstructor(instructor)) {
                    return false;
                }
            }
            instructorList.remove(instructor);
            return true;
        }
        return false;
    }

    // REQUIRES: teachingAssistance is not a null object and teachingAssistance is in this.teachingAssistance
    // MODIFIES: this.teachingAssistanceList
    // EFFECTS: remove teachingAssistance from the teachingAssistance list and return true
    public boolean removeTaFromSystem(TeachingAssistance teachingAssistance) {
        if (teachingAssistance != null && teachingAssistanceList.contains(teachingAssistance)) {
            // Remove TA from all the courses
            teachingAssistance.removeTaCourse();
            teachingAssistanceList.remove(teachingAssistance);
            return true;
        }
        return false;
    }

    // REQUIRES: student is not in the course,
    // course exists in the course list,
    // student exists in the student list
    // MODIFIES: this.courseList
    // EFFECTS: remove a student in the given course, return true if it removes successfully
    //          return false, otherwise
    public boolean addStudentToCourse(Student student, Course course) {
        if (studentList.contains(student)
                && courseList.contains(course)
                && !course.containsStudent(student)) {
            course.addStudents(student);
            return true;
        }
        return false;
    }

    // EFFECTS: return a list of student sorted by their GPAs
    public List<Student> getStudentListInGpaOrder(boolean isDescendingOrder) {
        //Sort the student list by their GPAs
        List<Student> resultList = new ArrayList<>(studentList);
        if (isDescendingOrder) {
            resultList.sort(Comparator.comparingDouble(Student::getGPA));
        } else {
            resultList.sort(Comparator.comparingDouble(Student::getGPA));
            Collections.reverse(resultList);
        }

        return resultList;
    }

    // REQUIRES: course and student are not a null object, 0 <= grade <= 100
    // MODIFIES: Change the grade of a student in a course
    public boolean changeStudentGrade(Course course, Student student, int grade) {
        if (course != null
                && student != null
                && grade >= 0
                && grade <= 100) {
            EventLog.getInstance().logEvent(new Event("[Student: "
                    + student.getId()
                    + "] Course: " + course.getSubjectCode().name()
                    + " | Grade changed from (" + student.getGrade(course) + "%) to (" + grade + "%)"));
            return student.changeGrade(course, grade);
        } else {
            return  false;
        }
    }

    // REQUIRES: course is not a null object
    // MODIFIES: this.courseList
    // EFFECTS: remove course in the course list and drop all students in that course
    public boolean removeCourseFromSystem(Course course) {
        if (course != null && courseList.contains(course)) {
            List<Student> students = course.getStudentsList();
            // Remove all the student from this course
            for (Student student: students) {
                course.removeStudent(student);
            }
            courseList.remove(course);
            return true;
        }
        return false;
    }

    // REQUIRES: subjectCode is not null
    // EFFECTS: return the course with the same subjectCode and section
    //          return null if it's not found
    public Course searchCourse(SubjectCode subjectCode, int section) {
        for (Course course: courseList) {
            // if the subject code and section are matched, return it
            if (course.getSubjectCode() == subjectCode
                    && course.getSection() == section) {
                return course;
            }
        }
        return null;
    }

    // EFFECTS: declare COVID-19 Positive for a traceable object
    void raiseCovidAlert(Traceable traceable) {
        traceable.declarePositive();
    }

    // EFFECTS: declare a COVID-19 negative for a traceable object
    void clearCovidAlert(Traceable traceable) {
        traceable.declareNegative();
    }


    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("courseList", courseListToJson());
        jsonObject.put("studentList", studentListToJson());
        jsonObject.put("instructorList", instructorListToJson());
        jsonObject.put("taList", taListToJson());
        return jsonObject;
    }

    private JSONArray courseListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Course course: courseList) {
            jsonArray.put(course.toJson());
        }
        return jsonArray;
    }

    private JSONArray studentListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Student student: studentList) {
            jsonArray.put(student.toJson());
        }
        return jsonArray;
    }

    private JSONArray instructorListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Instructor instructor: instructorList) {
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

//    public void loadPeopleAndCoursesToSystem() {
//        //Generates some Students, Instructors, TAs and Courses
//        int numOfStudents = 10;
//        int numOfCourses = 30;
//        int numOfInstructors = 20;
//        int numOfTAs = 40;
//        // Add some random students
//        for (int i = 0; i < numOfStudents; i++) {
//            addStudentToSystem(Util.generateRandomStudent());
//        }
//        //Add some random Courses
//        for (int i = 0; i < numOfCourses; i++) {
//            addCourseToSystem(Util.generateRandomCourse());
//        }
//        //Add some random instructors
//        for (int i = 0; i < numOfInstructors; i++) {
//            addInstructorToSystem(Util.generateRandomInstructor());
//        }
//
//        for (int i = 0; i < numOfTAs; i++) {
//            addTAToSystem(Util.generateRandomTA());
//        }
//    }
//
//    //EFFECTS: during initialization, assign random students to courses
//    public void assignRandomStudentsToCourses() {
//        Random random = new Random();
//        List<Student> students = getStudentList();
//        List<Course> courses = getCourseList();
//        for (Course course: courses) {
//            //For each course assign random number of students
//            int numOfStudentInCourse = 1 + random.nextInt(80);
//            for (int i = 0; i < numOfStudentInCourse; i++) {
//                Student student = students.get(random.nextInt(students.size()));
//                if (student.getNumOfCourses() <= 6) {
//                    course.addStudents(student);
//                }
//
//            }
//        }
//    }
//
//    //EFFECTS: during initialization, assign random grades to students
//    public void assignGradesToStudents() {
//        Random random = new Random();
//        int randGrade = 50;
//        int minGrade = 50;
//        List<Student> students = getStudentList();
//        for (Student student: students) {
//            //Assign random grades to all the courses this student is taking
//            List<Course> courses = student.getCourses();
//            for (Course course: courses) {
//                student.addGrade(course, minGrade + random.nextInt(randGrade));
//            }
//        }
//    }
//
//    //EFFECTS: during initialization, assign random instructors to courses
//    public void assignRandomInstructorsToCourses() {
//        Random random = new Random();
//        List<Instructor> instructors = getInstructorList();
//        List<Course> courses = getCourseList();
//        // For each course, assign 2 instructors
//        for (Course course: courses) {
//            int randInstructorIndex1 = random.nextInt(instructors.size());
//            int randInstructorIndex2 = random.nextInt(instructors.size());
//            course.addInstructor(instructors.get(randInstructorIndex1));
//            course.addInstructor(instructors.get(randInstructorIndex2));
//        }
//    }
//
//    //EFFECTS: during initialization, assign random TAs to courses
//    public void assignRandomTAsToCourses() {
//        Random random = new Random();
//        List<TeachingAssistance> teachingAssistanceList = getTeachingAssistanceList();
//        List<Course> courses = getCourseList();
//        // For each Course, assign 1-3 TAs to that class
//        for (Course course: courses) {
//            // Get the random num of TA
//            int numOfTAs = 1 + random.nextInt(2);
//            for (int i = 0; i < numOfTAs; i++) {
//                TeachingAssistance randTA = teachingAssistanceList.get(random.nextInt(teachingAssistanceList.size()));
//                course.addTeachingAssistance(randTA);
//            }
//        }
//    }

}
