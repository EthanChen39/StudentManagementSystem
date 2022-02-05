package model;

import model.enums.Faculty;
import model.enums.Gender;
import model.enums.SubjectCode;
import model.enums.YearLevel;

import java.util.Random;

// This is a utility class
public class Util {

    //REQUIRES: a student's course grade that is in the range of 0 - 100
    //EFFECTS: convert a number grade to a GPA
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public static double convertGradeToGPA(int courseGrade) {
        double result;
        if (courseGrade >= 90) {
            result = 4.33;
        } else if (courseGrade >= 85) {
            result = 4.00;
        } else if (courseGrade >= 80) {
            result = 3.67;
        } else if (courseGrade >= 77) {
            result = 3.33;
        } else if (courseGrade >= 73) {
            result = 3.00;
        } else if (courseGrade >= 70) {
            result = 2.67;
        } else if (courseGrade >= 67) {
            result = 2.33;
        } else if (courseGrade >= 63) {
            result = 2.00;
        } else if (courseGrade >= 60) {
            result = 1.67;
        } else if (courseGrade >= 55) {
            result = 1.33;
        } else if (courseGrade >= 50) {
            result = 1.00;
        } else {
            result = 0.00;
        }
        return result;
    }

    // EFFECTS: return a random faculty enum value
    private static Faculty generateRandomFaculty() {
        Random random = new Random();
        Faculty[] faculties = Faculty.values();
        int randFacultyIndex = random.nextInt(faculties.length);
        return faculties[randFacultyIndex];
    }

    // EFFECTS: return a random gender enum value
    private static Gender generateRandomGender() {
        Random random = new Random();
        Gender[] genders = Gender.values();
        int randGenderIndex = random.nextInt(genders.length);
        return genders[randGenderIndex];
    }

    public static int generateRandomID() {
        Random random = new Random();
        return 10000 + random.nextInt(9999);
    }

    // EFFECTS: a student with random properties is returned
    public static Student generateRandomStudent() {
        Random random = new Random();
        int randAge = 18 + random.nextInt(10);
        String randName = "StudentTestName:" + random.nextInt(999);
        int randomID = generateRandomID();

        YearLevel[] yearLevels = YearLevel.values();
        int randYearLevelIndex = random.nextInt(yearLevels.length);
        YearLevel randYearLevel = yearLevels[randYearLevelIndex];

        Faculty randFaculty = generateRandomFaculty();
        Gender randGender = generateRandomGender();

        return new Student(randAge, randName, randGender,
                randomID, false, randFaculty, randYearLevel);
    }

    // EFFECTS: a TA with random properties is returned
    public static TeachingAssistance generateRandomTA() {
        Random random = new Random();
        int randAge = random.nextInt(50);
        String randName = "TA TestName:" + random.nextInt(999);
        int randomID = generateRandomID();
        double randHourlyWages = 15.2 + random.nextDouble() * 15;
        int randExperience = 1 + random.nextInt(4);
        Gender randGender = generateRandomGender();
        Faculty randFaculty = generateRandomFaculty();
        return new TeachingAssistance(randAge, randName,
                randGender, randomID,
                false, randFaculty,
                randExperience, randHourlyWages);
    }

    // EFFECTS: an instructor with random properties is returned
    public static Instructor generateRandomInstructor() {
        Random random = new Random();
        int randAge = random.nextInt(50);
        String randName = "InstructorTestName:" + random.nextInt(999);
        int randomID = generateRandomID();
        int randExperience = 1 + random.nextInt(4);

        Faculty randFaculty = generateRandomFaculty();
        Gender randGender = generateRandomGender();

        return new Instructor(randAge, randName,
                randGender, randomID,
                false, randFaculty, randExperience);
    }

    // EFFECTS: a course with random subject code, credit and section is returned
    public static Course generateRandomCourse() {
        Random random = new Random();
        int randCredit = 3 + random.nextInt(2);
        int randSection = random.nextInt(10);
        //Get a random Subject Code
        SubjectCode[] subjectCodes = SubjectCode.values();
        int randSubjectCodeIndex = random.nextInt(subjectCodes.length);
        SubjectCode randSubjectCode = subjectCodes[randSubjectCodeIndex];
        return new Course(randSubjectCode, randCredit, randSection);
    }
}
