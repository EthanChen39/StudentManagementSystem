package ui;

import model.*;
import model.enums.Faculty;
import model.enums.Gender;
import model.enums.SubjectCode;
import model.enums.YearLevel;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// This class contains all the functionality of the system
// Including adding, removing, updating and displaying information in the system
public class ManagementSystemApp {
    private ManagementSystem managementSystem;
    private Scanner scanner;

    // Convention of indices of a list that contains a person's info
    private static final int ID_INDEX = 0;
    private static final int AGE_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int GENDER_INDEX = 3;
    private static final int FACULTY_INDEX = 4;
    private static final int EXPERIENCE_INDEX = 5;

    private static final int MAX_COURSE_TO_TAKE = 7;

    // Convention that uses in asking users for a choice
    private static final int COURSE_CHOICE = 1;

    private static final String JSON_STORE = "./data/managementSystem.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: run the system
    public ManagementSystemApp() {
        runSystem();
    }

    // EFFECTS: Display menu and read information from user, it quits until users enter "q"
    private void runSystem() {
        System.out.println("Welcome to Student Management System!");
        boolean isAppAlive = true;
        String query;
        //Initialize the system with some random People and Courses
        init();

        while (isAppAlive) {
            displayMenu();
            query = scanner.nextLine().toLowerCase();

            if (query.equals("q")) {
                isAppAlive = false;
            } else {
                processCommand(query);
            }
        }
        System.out.println("Thanks for using Student Management System.");
        System.out.println("Don't have a good day. Have a great day!");
    }

    // EFFECTS: Initialize the system with some random people and courses
    //          assign people to different courses
    private void init() {
        scanner = new Scanner(System.in);
        managementSystem = new ManagementSystem();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
//        loadPeopleAndCoursesToSystem();
//        assignRandomStudentsToCourses();
//        assignGradesToStudents();
//        assignRandomInstructorsToCourses();
//        assignRandomTAsToCourses();

    }

    // ADD(Course/People),
    // DEL,
    // UPDATE,
    // VIEW -> (Course/Student/Instructor),
    // MODIFY(Grade),
    // SEARCH(Course/Student/Instructor)
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void processCommand(String command) {
        switch (command) {
            case "add":
                processAddQuery();
                break;
            case "remove":
                processRemoveQuery();
                break;
            case "update":
                processUpdateQuery();
                break;
            case "info":
                processInfoQuery();
                break;
            case "covid":
                processCovidQuery();
                break;
            case "save":
                saveManagementSystem();
                break;
            case "load":
                loadManagementSystem();
                break;
            default:
                System.out.println("Illegal command. Please enter again.");
        }
    }

    // EFFECTS: display messages to users, and ask them do they want to raise a COVID-19 alert in the system
    private void processCovidQuery() {
        System.out.println("Do you want to alert a course(1) or a person(others)?");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == COURSE_CHOICE) {
            covidAlertCourses();

        } else {
            covidAlertPerson();
        }
        displayInfectionRates();
    }

    // EFFECTS: raise COVID-19 alert in user selected course
    private void covidAlertPerson() {
        List<Person> people = new ArrayList<>(managementSystem.getTeachingAssistanceList());
        people.addAll(managementSystem.getInstructorList());
        people.addAll(managementSystem.getStudentList());
        System.out.println("Which Person do you want to notify?");
        int index = 1;
        for (Person person: people) {
            System.out.print(index++ + ": ");
            System.out.println(person);
        }
        System.out.println("Please enter the index: (-1 to quit)");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == -1) {
            return;
        }
        people.get(userChoice - 1).declarePositive();
        System.out.println("POSITIVE ALERT SENT!");
    }

    // EFFECTS: raise COVID-19 alert in user selected person
    private void covidAlertCourses() {
        int index = 1;
        for (Course course: managementSystem.getCourseList()) {
            System.out.print(index++ + ": ");
            System.out.println(course);
        }
        System.out.println("Please enter the index: (-1 to quit)");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == -1) {
            return;
        }
        managementSystem.getCourseList().get(userChoice - 1).declarePositive();
        System.out.println("POSITIVE ALERT SENT!");
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void displayInfectionRates() {
        double countCoursesPositive = 0;
        double countStudentPositive = 0;
        double countInstructorPositive = 0;
        double countTaPositive = 0;
        List<Student> studentList = managementSystem.getStudentList();
        List<Instructor> instructorList = managementSystem.getInstructorList();
        List<TeachingAssistance> teachingAssistanceList = managementSystem.getTeachingAssistanceList();
        List<Course> courseList = managementSystem.getCourseList();
        List<Traceable> traceables = new ArrayList<>(courseList);
        traceables.addAll(studentList);
        traceables.addAll(instructorList);
        traceables.addAll(teachingAssistanceList);
        for (Traceable traceable: traceables) {
            if (traceable.getCovidStatus()) {
                if (traceable instanceof Course) {
                    countCoursesPositive++;
                } else if (traceable instanceof Student) {
                    countStudentPositive++;
                } else if (traceable instanceof TeachingAssistance) {
                    countTaPositive++;
                } else {
                    countInstructorPositive++;
                }
            }
        }
        int studentInfectionInfectionRates = (int) (countStudentPositive * 100 / studentList.size());
        int taInfectionRates = (int) (countTaPositive * 100 / teachingAssistanceList.size());
        int instructorInfectionRates = (int) (countInstructorPositive * 100 / instructorList.size());
        int coursesInfectionRates = (int) (countCoursesPositive * 100 / courseList.size());
        System.out.printf("Infection rates: courses=%d%%, students=%d%%, instructors=%d%%, TAs=%d%%\n",
                coursesInfectionRates, studentInfectionInfectionRates, instructorInfectionRates, taInfectionRates);
    }

    // EFFECTS: Display menu/available commands to user
    private void displayMenu() {
        System.out.println("Commands Available: ");

        System.out.print("ADD: ");
        System.out.println("Add a [Course / Person] to the system/course");

        System.out.print("REMOVE: ");
        System.out.println("Remove a [Student / TA] from a course");

        System.out.print("UPDATE: ");
        System.out.println("Update information of a [Course / Person]");

        System.out.print("INFO: ");
        System.out.println("Display information of a course or a student");

        System.out.print("COVID: ");
        System.out.println("Send out COVID-19 alert to a course or a person");

        System.out.print("SAVE: ");
        System.out.println("Save management system to a JSON file.");

        System.out.print("LOAD: ");
        System.out.println("Load system.");

        System.out.println("Enter \"q\" to quit.");
    }

    // EFFECTS: process add query (add a person/ a course)
    private void processAddQuery() {
        System.out.println("What do you want to add? Please enter(a digit): Course(1) People(other)");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == COURSE_CHOICE) {
            System.out.println("Please enter the course code: (CPSC, MATH, LATN...)");
            String courseCodeStr = scanner.nextLine().toUpperCase();
            SubjectCode subjectCode = SubjectCode.valueOf(courseCodeStr);

            System.out.println("Please enter the credits of this course: ");
            int credit = Integer.parseInt(scanner.nextLine());

            System.out.println("What's the section of this course? ");
            int section = Integer.parseInt(scanner.nextLine());
            Course newCourse = new Course(subjectCode, credit, section);
            boolean isAdded = managementSystem.addCourseToSystem(newCourse);
            Instructor instructor = assignInstructorToCourse();
            boolean instructorAdded = newCourse.addInstructor(instructor);
            if (isAdded && instructorAdded) {
                System.out.println("Course has been added successfully");
            } else {
                System.out.println(courseCodeStr + ": " + credit + " can't be added.");
            }
        } else {
            // Add people to the system
            processAddPersonToSystem();
        }

    }

    // EFFECTS: process remove query (remove a person/ a course)
    private void processRemoveQuery() {
        System.out.println("What do you want to remove?(enter a digit) (Course(1) or Person(others))");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == COURSE_CHOICE) {
            processRemoveCourseFromSystem();
        } else {
            processRemovePersonFromSystem();
        }

    }

    // EFFECTS: display information in the system (Courses and People)
    private void processInfoQuery() {
        System.out.println("Display courses(1) or people(others) in the system");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == COURSE_CHOICE) {
            List<Course> courses = managementSystem.getCourseList();
            for (Course course: courses) {
                System.out.println(course);
            }
        } else {
            System.out.println("Do you want to view students' GPA in descending(1) or ascending(other) order?");
            boolean isDescendingOrder = Integer.parseInt(scanner.nextLine()) == 1;
            List<Student> students = managementSystem.getStudentListInGpaOrder(isDescendingOrder);
            for (Student student: students) {
                System.out.println(student);
            }
        }

    }

    //EFFECTS: process update query (assign an instructor/student to a course or update a student's grade)
    private void processUpdateQuery() {
        // Update student's grade
        // employee wage
        // Ta course
        // Course, withdraw student,
        System.out.println("What do you want to update? Course(1) or Student(others)");
        int userChoice = Integer.parseInt(scanner.nextLine());
        if (userChoice == COURSE_CHOICE) {
            updateCourse();
        } else {
            processUpdateStudent();
        }
    }

    // EFFECTS: update a student's grade / assign a course to this student
    private void processUpdateStudent() {
        // Display all the students
        List<Student> studentList = managementSystem.getStudentList();
        int index = 1;
        for (Student student: studentList) {
            System.out.print(index++ + ": ");
            System.out.println(student);
        }
        System.out.println("Please enter the index of that student: ");
        int studentIndex = Integer.parseInt(scanner.nextLine());
        // Ask for grade or course
        System.out.println("Do you want to update this student's grade(1) or add a course(others)?");
        int userChoice = Integer.parseInt(scanner.nextLine());
        int isUpdateGrade = 1;

        Student targetStudent = studentList.get(studentIndex - 1);

        if (userChoice == isUpdateGrade) {
            //Update grade
            updateStudentGrade(targetStudent);
        } else {
            //add course
            addCourseToStudent(targetStudent);
        }
    }

    // EFFECTS: assign a course to a student
    private void addCourseToStudent(Student student) {
        List<Course> coursesTaking = student.getCourses();
        List<Course> allCourses = managementSystem.getCourseList();
        List<Course> coursesNotTaking = new ArrayList<>();
        // get a list of courses that this student is not taking
        int index = 1;
        for (Course course: allCourses) {
            if (!coursesTaking.contains(course)) {
                coursesNotTaking.add(course);
                System.out.print(index++ + ": ");
                System.out.println(course);
            }
        }
        if (coursesNotTaking.size() == 0) {
            System.out.println("No course can be added to this student");
            return;
        }
        System.out.println("Please enter the index of the course: ");
        int courseIndex = Integer.parseInt(scanner.nextLine());
        student.addCourse(coursesNotTaking.get(courseIndex - 1));
        System.out.println("Course added successfully");
    }

    //EFFECTS: update student's grade in a given course
    public void updateStudentGrade(Student student) {
        List<Course> courses = student.getCourses();
        if (courses.size() == 0) {
            System.out.println("This student isn't taking any courses yet. No grade can be updated.");
            return;
        }
        System.out.println("Here are the course(s) that this student is taking.");
        System.out.println("Which course do yo want to update the grade?");
        int index = 1;
        for (Course course: courses) {
            System.out.print(index++ + ": ");
            System.out.println(course);
        }
        int userChoice = Integer.parseInt(scanner.nextLine());
        Course course = courses.get(userChoice - 1);
        System.out.println("Please enter the grade (0-100): ");
        int grade = Integer.parseInt(scanner.nextLine());
        if (student.isGraded(course) && student.changeGrade(course, grade)) {
            System.out.println("Grade updated successfully.");
        } else if (student.addGrade(course, grade)) {
            System.out.println("Grade added successfully.");
        } else {
            System.out.println("Grade can not be modified.");
        }
    }

    //EFFECTS: update a course (withdraw student from this course)
    public void updateCourse() {
        System.out.println("Which course do you you want to update?");
        List<Course> courses = managementSystem.getCourseList();
        int index = 1;
        // Display a list of courses to users
        for (Course course: courses) {
            System.out.print(index++ + ": ");
            System.out.println(course);
        }
        int userIndex = Integer.parseInt(scanner.nextLine());
        index = 1;
        System.out.println("Which student do you want to withdraw from this course? (enter -1 to quit)");
        //Get the selected student
        List<Student> studentList = courses.get(userIndex - 1).getStudentsList();
        for (Student student: studentList) {
            System.out.print(index++ + ": ");
            System.out.println(student);
        }
        int studentIndex = Integer.parseInt(scanner.nextLine());
        if (studentIndex != -1) {
            courses.get(userIndex - 1).removeStudent(studentList.get(studentIndex - 1));
            System.out.println("Removed successfully!");
        }

    }

    //EFFECTS:  assign an instructor to a course that specified by the user, return user selected instructor
    private Instructor assignInstructorToCourse() {
        List<Instructor> instructors = managementSystem.getInstructorList();
        if (instructors.size() == 0) {
            System.out.println("Sorry, we don't have instructors available at this moment.");
            return null;
        }
        System.out.println("Which instructor do you want to assign to this course?");
        int index = 1;
        for (Instructor instructor: instructors) {
            System.out.print(index + ": ");
            System.out.println(instructor);
            index++;
        }
        System.out.println("Please enter the index of that instructor: ");
        int userIndex = Integer.parseInt(scanner.nextLine());
        return instructors.get(userIndex - 1);
    }

    //MODIFIES: this.managementSystem
    //EFFECTS: add a person to this.managementSystem
    private void processAddPersonToSystem() {
        System.out.println("What type of person do you want to add? Student(1), TA(2), Instructor(other)");
        int userChoice = Integer.parseInt(scanner.nextLine());
        int studentChoice = 1;
        int taChoice = 2;
        if (userChoice == studentChoice) {
            processAddStudentToSystem();
        } else if (userChoice == taChoice) {
            processAddTaToSystem();
        } else {
            processAddInstructorToSystem();
        }
    }

    // MODIFIES: this.managementSystem
    //EFFECTS: remove a person from this.managementSystem
    private void processRemovePersonFromSystem() {
        System.out.println("Please enter this person's id");
        int personID = Integer.parseInt(scanner.nextLine());
        // Check which type of person is it
        for (Student student: managementSystem.getStudentList()) {
            if (student.getId() == personID) {
                // Remove this student from the system
                managementSystem.removeStudentFromSystem(student);
                break;
            }
        }
        for (Instructor instructor: managementSystem.getInstructorList()) {
            if (instructor.getId() == personID) {
                if (!managementSystem.removeInstructorFromSystem(instructor)) {
                    System.out.println("This instructor is still teaching some courses. Remove failed.");
                }
            }
        }
        for (TeachingAssistance teachingAssistance: managementSystem.getTeachingAssistanceList()) {
            if (teachingAssistance.getId() == personID) {
                if (!managementSystem.removeTaFromSystem(teachingAssistance)) {
                    System.out.println("This TA can't be removed.");
                }
            }
        }
        System.out.println("Removed Successfully.");
    }

    // MODIFIES: this.managementSystem
    //EFFECTS: remove a course from this.managementSystem
    private void processRemoveCourseFromSystem() {
        System.out.println("Please enter the Subject Code: (CPSC, MATH,...)");
        SubjectCode subjectCode;
        try {
            subjectCode = SubjectCode.valueOf(scanner.nextLine());
        } catch (IllegalArgumentException ex) {
            System.out.println("This subject code does not exist! Back to main menu.");
            return;
        }
        System.out.println("Please enter the section of this course: ");
        int section = Integer.parseInt(scanner.nextLine());
        Course course = managementSystem.searchCourse(subjectCode, section);
        if (course == null) {
            System.out.println("This course is not found");
        } else {
            managementSystem.removeCourseFromSystem(course);
            System.out.println(course + " is removed successfully.");
        }
    }

    // MODIFIES: this.managementSystem
    //EFFECTS: add a new student to this.managementSystem
    private void processAddStudentToSystem() {
        List<Object> personAttributes = getGeneralPersonInfo();

        int id = (int) personAttributes.get(ID_INDEX);
        int age = (int) personAttributes.get(AGE_INDEX);
        String name = (String) personAttributes.get(NAME_INDEX);
        Gender gender = (Gender) personAttributes.get(GENDER_INDEX);


        System.out.println("Which faculty does this person belong to? (SCIENCE, ARTS, APPLIED_SCIENCE...)");
        String facultyStr = scanner.nextLine();
        Faculty faculty = Faculty.valueOf(facultyStr.toUpperCase());

        System.out.println("What's the year level of this student? (FRESHMAN, SOPHOMORE, GRAD_MASTER...)");
        String yearLevelStr = scanner.nextLine().toUpperCase();
        YearLevel yearLevel = YearLevel.valueOf(yearLevelStr);

        Student student = new Student(age, name, gender, id, false, faculty, yearLevel);
        managementSystem.addStudentToSystem(student);
        System.out.println("Student added successfully!");
    }

    // MODIFIES: this.managementSystem
    //EFFECTS: add a new TA to this.managementSystem
    private void processAddTaToSystem() {
        List<Object> employeeAttributes = getEmployeeInfo();
        int id = (int) employeeAttributes.get(ID_INDEX);
        int age = (int) employeeAttributes.get(AGE_INDEX);
        String name = (String) employeeAttributes.get(NAME_INDEX);
        Gender gender = (Gender) employeeAttributes.get(GENDER_INDEX);
        Faculty faculty = (Faculty) employeeAttributes.get(FACULTY_INDEX);
        int yearsOfExperience = (int) employeeAttributes.get(EXPERIENCE_INDEX);

        System.out.println("Please enter this employee's hourly wages: ");
        double hourlyWages = scanner.nextDouble();

        TeachingAssistance teachingAssistance = new TeachingAssistance(age, name,
                gender, id,
                false,faculty,
                yearsOfExperience, 0.00);
        if (!teachingAssistance.setHourlyWages(hourlyWages)) {
            System.out.println("Illegal hourly wages. Fail to add this TA.");
        }
        managementSystem.addTAToSystem(teachingAssistance);
        System.out.println("TA added successfully");
    }

    // MODIFIES: this.managementSystem
    //EFFECTS: add a new instructor to this.managementSystem
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void processAddInstructorToSystem() {
        List<Object> employeeAttributes = getEmployeeInfo();
        int id = (int) employeeAttributes.get(ID_INDEX);
        int age = (int) employeeAttributes.get(AGE_INDEX);
        String name = (String) employeeAttributes.get(NAME_INDEX);
        Gender gender = (Gender) employeeAttributes.get(GENDER_INDEX);
        Faculty faculty = (Faculty) employeeAttributes.get(FACULTY_INDEX);
        int yearsOfExperience = (int) employeeAttributes.get(EXPERIENCE_INDEX);
        Instructor instructor = new Instructor(age, name, gender, id,
                false, faculty, yearsOfExperience);
        managementSystem.addInstructorToSystem(instructor);
        System.out.println("How many courses do this instructor teach?");
        int numOfCourseTeach = Integer.parseInt(scanner.nextLine());
        for (int i = 1; i <= numOfCourseTeach; i++) {
            System.out.println(i + ": Please enter the course code: (CPSC, MATH, LATN...)");
            String courseCodeStr = scanner.nextLine().toUpperCase();
            SubjectCode subjectCode = SubjectCode.valueOf(courseCodeStr);
            System.out.println("Credits of this course: ");
            int credit = Integer.parseInt(scanner.nextLine());
            System.out.println("Which section is this course in? ");
            int section = Integer.parseInt(scanner.nextLine());
            Course course = new Course(subjectCode, credit, section);
            instructor.addCourseToTeach(course);
        }
        System.out.println("Instructor added successfully");
    }

    // EFFECTS: return a list of objects contains basic info of a person
    private List<Object> getGeneralPersonInfo() {
        List<Object> info = new ArrayList<>();
        System.out.println("Please enter id: ");
        Integer id = Integer.parseInt(scanner.nextLine());

        System.out.println("Please enter this person's age: ");
        Integer age = Integer.parseInt(scanner.nextLine());
        scanner.nextLine();
        System.out.println("Please enter name: ");
        String name = scanner.nextLine();

        System.out.println("Please enter gender: (MALE, FEMALE, NEUTRAL, TRANS, OTHERS)");
        String genderStr = scanner.nextLine().toUpperCase();
        Gender gender = Gender.valueOf(genderStr);

        info.add(id);
        info.add(age);
        info.add(name);
        info.add(gender);
        return info;
    }

    //EFFECTS: read input from user on employee attributes
    private List<Object> getEmployeeInfo() {
        List<Object> employeeInfo = getGeneralPersonInfo();

        System.out.println("How many years of experience does this employee have?");
        Integer yearsOfExperience = Integer.parseInt(scanner.nextLine());


        System.out.println("Which faculty does this person belong to? (SCIENCE, ARTS, APPLIED_SCIENCE...)");
        String facultyStr = scanner.nextLine().toUpperCase();
        Faculty faculty = Faculty.valueOf(facultyStr);

        employeeInfo.add(faculty);
        employeeInfo.add(yearsOfExperience);
        return employeeInfo;
    }

    //EFFECTS: during initialization, load people and courses to the system
    private void loadPeopleAndCoursesToSystem() {
        //Generates some Students, Instructors, TAs and Courses
        int numOfStudents = 1;
        int numOfCourses = 1;
        int numOfInstructors = 1;
        int numOfTAs = 1;
        // Add some random students
        for (int i = 0; i < numOfStudents; i++) {
            managementSystem.addStudentToSystem(Util.generateRandomStudent());
        }
        //Add some random Courses
        for (int i = 0; i < numOfCourses; i++) {
            managementSystem.addCourseToSystem(Util.generateRandomCourse());
        }
        //Add some random instructors
        for (int i = 0; i < numOfInstructors; i++) {
            managementSystem.addInstructorToSystem(Util.generateRandomInstructor());
        }

        for (int i = 0; i < numOfTAs; i++) {
            managementSystem.addTAToSystem(Util.generateRandomTA());
        }
    }

    //EFFECTS: during initialization, assign random students to courses
    private void assignRandomStudentsToCourses() {
        Random random = new Random();
        List<Student> students = managementSystem.getStudentList();
        List<Course> courses = managementSystem.getCourseList();
        for (Course course: courses) {
            //For each course assign random number of students
            int numOfStudentInCourse = 1 + random.nextInt(80);
            for (int i = 0; i < numOfStudentInCourse; i++) {
                Student student = students.get(random.nextInt(students.size()));
                if (student.getNumOfCourses() <= MAX_COURSE_TO_TAKE) {
                    course.addStudents(student);
                }

            }
        }
    }

    //EFFECTS: during initialization, assign random grades to students
    private void assignGradesToStudents() {
        Random random = new Random();
        int randGrade = 50;
        int minGrade = 50;
        List<Student> students = managementSystem.getStudentList();
        for (Student student: students) {
            //Assign random grades to all the courses this student is taking
            List<Course> courses = student.getCourses();
            for (Course course: courses) {
                student.addGrade(course, minGrade + random.nextInt(randGrade));
            }
        }
    }

    //EFFECTS: during initialization, assign random instructors to courses
    private void assignRandomInstructorsToCourses() {
        Random random = new Random();
        List<Instructor> instructors = managementSystem.getInstructorList();
        List<Course> courses = managementSystem.getCourseList();
        // For each course, assign 2 instructors
        for (Course course: courses) {
            int randInstructorIndex1 = random.nextInt(instructors.size());
            int randInstructorIndex2 = random.nextInt(instructors.size());
            course.addInstructor(instructors.get(randInstructorIndex1));
            course.addInstructor(instructors.get(randInstructorIndex2));
        }
    }

    //EFFECTS: during initialization, assign random TAs to courses
    private void assignRandomTAsToCourses() {
        Random random = new Random();
        List<TeachingAssistance> teachingAssistanceList = managementSystem.getTeachingAssistanceList();
        List<Course> courses = managementSystem.getCourseList();
        // For each Course, assign 1-3 TAs to that class
        for (Course course: courses) {
            // Get the random num of TA
            int numOfTAs = 1 + random.nextInt(2);
            for (int i = 0; i < numOfTAs; i++) {
                TeachingAssistance randTA = teachingAssistanceList.get(random.nextInt(teachingAssistanceList.size()));
                course.addTeachingAssistance(randTA);
            }
        }
    }

    private void saveManagementSystem() {
        try {
            jsonWriter.open();
            jsonWriter.write(managementSystem);
            jsonWriter.close();
            System.out.println("Saved Management System to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private void loadManagementSystem() {
        try {
            managementSystem = jsonReader.read();
            System.out.println("Loaded ManagementSystem from " + JSON_STORE);
        } catch (IOException ex) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
