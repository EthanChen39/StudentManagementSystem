package model;

import model.enums.SubjectCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ManagementSystemTest {
    Student s1;
    Student s2;

    TeachingAssistance t1;
    TeachingAssistance t2;

    Instructor i1;
    Instructor i2;

    Course c1;
    Course c2;

    ManagementSystem managementSystem;

    @BeforeEach
    void init() {
        s1 = Util.generateRandomStudent();
        s2 = Util.generateRandomStudent();

        t1 = Util.generateRandomTA();
        t2 = Util.generateRandomTA();

        i1 = Util.generateRandomInstructor();
        i2 = Util.generateRandomInstructor();

        c1 = new Course(SubjectCode.ASTU, 3, 1);
        c2 = new Course(SubjectCode.CPSC, 4, 2);

        managementSystem = new ManagementSystem();
    }

    @Test
    void testAddStudentToSystem() {
        assertTrue(managementSystem.addStudentToSystem(s1));

        //Try to add a null object to the system
        assertFalse(managementSystem.addStudentToSystem(null));
        assertFalse(managementSystem.addStudentToSystem(s1));
    }

    @Test
    void testGetStudentTest() {
        managementSystem.addStudentToSystem(s1);
        List<Student> students = managementSystem.getStudentList();
        assertEquals(students.get(0), s1);
    }

    @Test
    void testAddTeachingAssistanceToSystem() {
        assertTrue(managementSystem.addTAToSystem(t1));
        assertFalse(managementSystem.addTAToSystem(t1));
        //Try to add a null object to the system
        assertFalse(managementSystem.addTAToSystem(null));
        assertTrue(managementSystem.addTAToSystem(t2));

    }

    @Test
    void testAddInstructorToSystem() {
       assertTrue(managementSystem.addInstructorToSystem(i1));
       assertFalse(managementSystem.addInstructorToSystem(i1));
        //Try to add a null object to the system
        assertFalse(managementSystem.addInstructorToSystem(null));

        assertTrue(managementSystem.addInstructorToSystem(i2));
    }

    @Test
    void testAddCourseToSystem() {
        assertTrue(managementSystem.addCourseToSystem(c1));
        assertFalse(managementSystem.addCourseToSystem(c1));
        //Try to add a null object to the system
        assertFalse(managementSystem.addCourseToSystem(null));
    }

    @Test
    void testIsStudentInSystem() {
        managementSystem.addStudentToSystem(s1);
        assertTrue(managementSystem.isStudentInSystem(s1));
        assertFalse(managementSystem.isStudentInSystem(s2));
        assertFalse(managementSystem.isStudentInSystem(null));
    }

    @Test
    void testFindStudentByName() {
        managementSystem.addStudentToSystem(s1);
        String studentName = s1.getName();
        Student studentFound = managementSystem.findStudentByName(studentName);
        assertEquals(studentFound, s1);
        Student studentNotFound = managementSystem.findStudentByName("NotExist");
        assertNull(studentNotFound);
    }

    @Test
    void testWithdrawStudentFromCourse() {
        s1.addCourse(c1);
        s1.addCourse(c2);
        // Withdraw a student/course that doesn't exist
        assertFalse(managementSystem.withdrawStudentFromCourse(s1, c1));
        managementSystem.addStudentToSystem(s1);
        assertFalse(managementSystem.withdrawStudentFromCourse(s1, c1));

        managementSystem.addCourseToSystem(c1);
        managementSystem.addCourseToSystem(c2);
        assertTrue(managementSystem.withdrawStudentFromCourse(s1, c1));
        // Make sure the course is removed
        assertFalse(managementSystem.withdrawStudentFromCourse(s1, c1));
        assertTrue(managementSystem.withdrawStudentFromCourse(s1, c2));
    }

    @Test
    void testAddStudentToCourse() {
        managementSystem.addStudentToSystem(s1);
        managementSystem.addCourseToSystem(c1);

        assertTrue(managementSystem.addStudentToCourse(s1, c1));
        //Make sure we cannot add a student two times to the same course
        assertFalse(managementSystem.addStudentToCourse(s1, c1));
        //Not exits c2 in courseList
        assertFalse(managementSystem.addStudentToCourse(s1, c2));
        //Not exits c1 in courseList
        assertFalse(managementSystem.addStudentToCourse(s2, c1));
    }

    @Test
    void testGetStudentListInGradesOrder() {
        managementSystem.addStudentToSystem(s1);
        managementSystem.addStudentToSystem(s2);
        managementSystem.addCourseToSystem(c1);
        managementSystem.addCourseToSystem(c2);

        s1.addCourse(c1);
        s1.addCourse(c2);

        s2.addCourse(c1);
        s2.addCourse(c2);

        s1.addGrade(c1, 100);
        s1.addGrade(c2, 90);

        s2.addGrade(c1, 49);
        s2.addGrade(c2, 98);
        List<Student> ascendingGrades = managementSystem.getStudentListInGpaOrder(false);
        assertEquals(ascendingGrades.get(0), s1);
        assertEquals(ascendingGrades.get(1), s2);

        List<Student> descendingGrades = managementSystem.getStudentListInGpaOrder(true);
        assertEquals(descendingGrades.get(0), s2);
        assertEquals(descendingGrades.get(1), s1);
    }

    @Test
    void testChangeStudentGrade() {
        managementSystem.addStudentToSystem(s1);
        managementSystem.addCourseToSystem(c1);
        s1.addCourse(c1);
        s1.addGrade(c1, 50);

        // Test when the course is a null object
        assertFalse(managementSystem.changeStudentGrade(null, s1, 100));
        //When student is a null object
        assertFalse(managementSystem.changeStudentGrade(c1, null, 100));
        //When grade is negative
        assertFalse(managementSystem.changeStudentGrade(c1, s1, -60));
        //When grade is over 100
        assertFalse(managementSystem.changeStudentGrade(c1, s1, 120));

        //Everything is good
        assertTrue(managementSystem.changeStudentGrade(c1, s1, 90));
    }

    @Test
    void testRemoveStudentFromSystem() {
        managementSystem.addStudentToSystem(s1);
        managementSystem.addCourseToSystem(c1);
        managementSystem.addCourseToSystem(c2);
        s1.addCourse(c1);
        s1.addCourse(c2);
        assertFalse(managementSystem.removeStudentFromSystem(null));
        assertFalse(managementSystem.removeStudentFromSystem(s2));

        assertTrue(managementSystem.removeStudentFromSystem(s1));
    }

    @Test
    void testRemoveInstructorFromSystem() {
        managementSystem.addCourseToSystem(c1);
        managementSystem.addCourseToSystem(c2);
        i1.addCourseToTeach(c1);
        i1.addCourseToTeach(c2);
        managementSystem.addInstructorToSystem(i1);

        assertFalse(managementSystem.removeInstructorFromSystem(null));
        assertFalse(managementSystem.removeInstructorFromSystem(i2));
        assertFalse(managementSystem.removeInstructorFromSystem(i1));
        managementSystem.addInstructorToSystem(i2);
        i2.addCourseToTeach(c1);
        i2.addCourseToTeach(c2);
        assertTrue(managementSystem.removeInstructorFromSystem(i2));
    }

    @Test
    void testRemoveTaFromSystem() {
        managementSystem.addTAToSystem(t1);

        assertFalse(managementSystem.removeTaFromSystem(null));
        assertFalse(managementSystem.removeTaFromSystem(t2));

        assertTrue(managementSystem.removeTaFromSystem(t1));
    }

    @Test
    void testRemoveCourseFromSystem() {
        managementSystem.addCourseToSystem(c1);
        c1.addStudents(s1);
        c1.addStudents(s2);
        assertFalse(managementSystem.removeCourseFromSystem(null));
        assertFalse(managementSystem.removeCourseFromSystem(c2));

        assertTrue(managementSystem.removeCourseFromSystem(c1));
    }

    @Test
    void testSearchCourse() {
        managementSystem.addCourseToSystem(c1);
        managementSystem.addCourseToSystem(c2);
        SubjectCode subjectCodeFound = SubjectCode.ASTU;
        int sectionFound = 1;

        SubjectCode subjectCodeNotFound = SubjectCode.CPSC;
        int sectionNotFound = 100;

        Course courseFound = managementSystem.searchCourse(subjectCodeFound, sectionFound);
        Course courseNotFound = managementSystem.searchCourse(subjectCodeNotFound, sectionNotFound);

        assertEquals(courseFound, c1);
        assertNull(courseNotFound);
    }

    @Test
    void testGetInstructorList() {
        managementSystem.addInstructorToSystem(i1);
        managementSystem.addInstructorToSystem(i2);

        List<Instructor> instructorList = managementSystem.getInstructorList();

        assertEquals(instructorList.get(0), i1);
        assertEquals(instructorList.get(1), i2);
    }

    @Test
    void testGetTeachingAssistanceList() {
        managementSystem.addTAToSystem(t1);
        managementSystem.addTAToSystem(t2);

        List<TeachingAssistance> teachingAssistanceList = managementSystem.getTeachingAssistanceList();

        assertEquals(teachingAssistanceList.get(0), t1);
        assertEquals(teachingAssistanceList.get(1), t2);
    }

    @Test
    void testGetCourseList() {
        managementSystem.addCourseToSystem(c1);
        managementSystem.addCourseToSystem(c2);

        List<Course> courses = managementSystem.getCourseList();

        assertEquals(courses.get(0), c1);
        assertEquals(courses.get(1), c2);
    }

    @Test
    void testRaiseCovidAlert() {
        managementSystem.addStudentToSystem(s1);
        managementSystem.addStudentToSystem(s2);
        managementSystem.addCourseToSystem(c1);
        c1.addStudents(s1);
        c1.addStudents(s2);

        // Alert a course
        managementSystem.raiseCovidAlert(c1);
        assertTrue(s1.getCovidStatus());
        assertTrue(s2.getCovidStatus());
        assertTrue(c1.getCovidStatus());
    }

    @Test
    void testClearCovidAlert() {
        managementSystem.addStudentToSystem(s1);
        managementSystem.addStudentToSystem(s2);
        managementSystem.addCourseToSystem(c1);
        c1.addStudents(s1);
        c1.addStudents(s2);

        // Alert a course
        managementSystem.raiseCovidAlert(c1);

        managementSystem.clearCovidAlert(c1);
        assertFalse(c1.getCovidStatus());

        managementSystem.clearCovidAlert(s1);
        assertFalse(s1.getCovidStatus());

        managementSystem.clearCovidAlert(s2);
        assertFalse(s2.getCovidStatus());

    }

    @Test
    public void testFindStudentById() {
        managementSystem.addStudentToSystem(s1);
        assertEquals(managementSystem.findPersonById(s1.getId()), s1);
        assertNull(managementSystem.findPersonById(-1));
    }

    @Test
    public void testFindCourseByNameAndSection() {
        c1.addInstructor(i1);
        managementSystem.addCourseToSystem(c1);
        assertEquals(managementSystem.findCourseByNameAndSection("Arts Studies", 1), c1);
        assertNull(managementSystem.findCourseByNameAndSection("Arts Studies", 2));
        assertNull(managementSystem.findCourseByNameAndSection("Computer Science", 2));
    }
}
