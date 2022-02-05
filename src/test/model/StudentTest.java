package model;

import model.enums.Faculty;
import model.enums.Gender;
import model.enums.SubjectCode;
import model.enums.YearLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    Student s1;
    Student s2;

    Instructor i1;

    Course csCourse;
    Course course2;

    @BeforeEach
    void init() {
        int age = 21;
        String name = "Ethan";
        Gender gender = Gender.MALE;
        int id = 123456;
        Faculty faculty = Faculty.SCIENCE;
        YearLevel yearLevel = YearLevel.JUNIOR;

        csCourse = new Course(SubjectCode.CPSC, 3, 3);
        course2 = new Course(SubjectCode.AFST, 3, 2);
        s1 = new Student(age, name,
                gender, id, false,
                faculty, yearLevel);

        i1 = Util.generateRandomInstructor();
        s2 = Util.generateRandomStudent();
    }

    @Test
    void testAddCourse(){
        s1.addCourse(csCourse);
        List<Course> courses = s1.getCourses();
        assertEquals(courses.get(0), csCourse);

        s1.addCourse(null);
        //Make sure the null course is not added to the list
        assertEquals(s1.getNumOfCourses(), 1);
    }


    @Test
    void testAddGrade(){
        s1.addCourse(csCourse);
        assertEquals(s1.getGrade(csCourse), 0);
//        s1.addCourse(course2);
        // try to add a null course with grade
        assertFalse(s1.addGrade(null, 100));

        // try to add a course with negative numbers
        assertFalse(s1.addGrade(csCourse, -80));

        assertFalse(s1.addGrade(csCourse, 120));

        //try to add a course that student doesn't enroll in
        assertFalse(s1.addGrade(course2, 80));

        //successfully add a course
        assertTrue(s1.addGrade(csCourse, 90));
    }

    @Test
    void testGetGPA() {
        s1.addCourse(csCourse);
        s1.addCourse(course2);
        s1.addGrade(csCourse, 95);
        assertEquals(s1.getGPA(), 4.33);

        s1.addGrade(course2, 80);
        assertEquals(s1.getGPA(), 4);
    }

    @Test
    void testChangeGrade() {
        s1.addCourse(csCourse);
        s1.addGrade(csCourse, 95);
        assertFalse(s1.changeGrade(null, 50));
        assertFalse(s1.changeGrade(csCourse, -2));
        assertFalse(s1.changeGrade(course2, 50));
        assertFalse(s1.changeGrade(csCourse, 110));
        assertTrue(s1.changeGrade(csCourse, 80));
        assertEquals(s1.getGrade(csCourse), 80);
    }

    @Test
    void testIsGraded() {
        assertFalse(s1.isGraded(csCourse));
        s1.addGrade(csCourse, 80);
//        assertTrue(s1.isGraded(csCourse));
    }

    @Test
    void testToString() {
        String expectedStr = "studentID=123456{Name=Ethan, age=21, GPA=0.0, yearLevel=JUNIOR, faculty=SCIENCE, number of courses=0, isCovidPositive=false}";
        assertEquals(s1.toString(), expectedStr);
    }

    @Test
    void testGetAge() {
        assertEquals(s1.getAge(), 21);
    }

    @Test
    void testGetGender() {
        assertEquals(s1.getGender(), Gender.MALE);
    }

    @Test
    void testGetId() {
        assertEquals(s1.getId(), 123456);
    }

    @Test
    void testGetName() {
        assertEquals(s1.getName(), "Ethan");
    }

    @Test
    void testSetName() {
        String newName = "James";
        s1.setName(newName);
        assertEquals(s1.getName(), newName);
    }


    @Test
    void testPersonEquals() {
        assertNotEquals(s1, csCourse);
        assertNotEquals(s1, s2);
        assertEquals(s1, s1);
    }

    @Test
    void testPersonIsCovidPositive() {
        s1.declarePositive();
        assertTrue(s1.getCovidStatus());
        s1.declarePositive();
        assertTrue(s1.getCovidStatus());
    }

    @Test
    void testGetGrade() {
        s1.addCourse(csCourse);
        s1.addGrade(csCourse, 99);
        assertEquals(s1.getGrade(csCourse), 99);
        assertEquals(s1.getGrade(course2), 0);
    }

    @Test
    void testGetFaculty() {
        assertEquals(s1.getFaculty(), Faculty.SCIENCE);
    }

    @Test
    void testGetYearLevel() {
        assertEquals(s1.getYearLevel(), YearLevel.JUNIOR);
    }

    @Test
    void testWithdrawFromCourse() {
        assertFalse(s1.withdrawFromCourse(null));
        assertFalse(s1.withdrawFromCourse(csCourse));
        s1.addCourse(csCourse);
        s1.addGrade(csCourse, 100);
        assertEquals(s1.getGPA(), 4.33);
        assertTrue(s1.withdrawFromCourse(csCourse));
        assertEquals(s1.getGPA(), 0.00);
    }

    @Test
    void testGetGrades() {
        s1.addCourse(csCourse);
        s1.addGrade(csCourse, 99);
        assertEquals(s1.getGrades().size(), 1);
    }
}