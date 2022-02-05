package model;

import model.enums.SubjectCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseTest {
    Course c1;
    Course c2;
    Course c3;

    Student s1;
    Student s2;

    TeachingAssistance ta1;
    TeachingAssistance ta2;

    Instructor i1;
    Instructor i2;


    @BeforeEach
    void init() {
        c1 = new Course(SubjectCode.CPSC, 5, 3);
        c2 = new Course(SubjectCode.AGEC, 3, 2);
        s1 = Util.generateRandomStudent();
        s2 = Util.generateRandomStudent();

        ta1 = Util.generateRandomTA();
        ta2 = Util.generateRandomTA();

        i1 = Util.generateRandomInstructor();
        i2 = Util.generateRandomInstructor();

    }

    @Test
    void testAddInstructor() {
        assertFalse(c1.addInstructor(null));
        assertTrue(c1.addInstructor(i1));
        assertFalse(c1.addInstructor(i1));
    }

    @Test
    void testAddStudent() {
        c1.addStudents(s1);
        c1.addStudents(s2);
        List<Student> studentList = c1.getStudentsList();
        assertEquals(studentList.get(0), s1);
        assertEquals(studentList.get(1), s2);

        c1.addStudents(null);
        assertEquals(c1.getClassSize(), 2);
    }

    @Test
    void testAddTeachingAssistance() {
        assertFalse(c1.addTeachingAssistance(null));
        assertTrue(c1.addTeachingAssistance(ta1));
        assertTrue(c1.addTeachingAssistance(ta2));
        assertFalse(c1.addTeachingAssistance(ta1));

    }

    @Test
    void testRemoveStudent() {
        c1.addStudents(s1);
        // try to remove a null object
        c1.removeStudent(null);
        assertEquals(c1.getClassSize(), 1);

        //Try to remove a student that doesn't exist
        c1.removeStudent(s2);
        assertEquals(c1.getClassSize(), 1);

        c1.removeStudent(s1);
        assertEquals(c1.getClassSize(), 0);

    }

    @Test
    void testRemoveInstructor() {
        assertFalse(c1.removeInstructor(i1));
        // A course should have more than 1 instructor
        c1.addInstructor(i1);
        assertFalse(c1.removeInstructor(i1));

        c1.addInstructor(i2);
        assertTrue(c1.removeInstructor(i1));
    }

    @Test
    void testContainsStudent() {
        assertFalse(c1.containsStudent(s1));
        c1.addStudents(s1);
        assertTrue(c1.containsStudent(s1));
    }

    @Test
    void testEquals() {
        assertNotEquals(c1, s1);
        assertNotEquals(null, c1);
        Course sameCourse = new Course(SubjectCode.CPSC, 5, 3);
        Course diffSectionCourse = new Course(SubjectCode.CPSC, 4, 2);
        assertEquals(c1, sameCourse);
        assertNotEquals(c1, c2);
        assertNotEquals(c1, diffSectionCourse);
    }

    @Test
    void testToString() {
        i1.setName("ABC");
        c1.addInstructor(i1);
        String c1Str = c1.toString();
        String expected = "[Computer Science] section (3) with credits {5} has 0 student(s). Main Instructor: ABC.";
        assertEquals(expected, c1Str);
    }

    @Test
    void testDeclarePositive() {
        c1.addStudents(s1);
        c1.addInstructor(i1);
        c1.addTeachingAssistance(ta1);
        c1.addTeachingAssistance(ta2);

        c1.declarePositive();
        assertTrue(c1.getCovidStatus());

        for (Student student: c1.getStudentsList()) {
            assertTrue(student.getCovidStatus());
        }
        for (Instructor instructor: c1.getInstructorsList()) {
            assertTrue(instructor.getCovidStatus());
        }

        for (TeachingAssistance teachingAssistance: c1.getTeachingAssistanceList()) {
            assertTrue(teachingAssistance.getCovidStatus());
        }
    }

    @Test
    void testDeclareNegative() {
        c1.addStudents(s1);
        c1.addInstructor(i1);

        c1.declarePositive();
        assertTrue(c1.getCovidStatus());
        c1.declareNegative();
        assertFalse(c1.getCovidStatus());
    }

    @Test
    void testGetSubjectCode() {
        assertEquals(c1.getSubjectCode(), SubjectCode.CPSC);
        assertEquals(c2.getSubjectCode(), SubjectCode.AGEC);
    }

    @Test
    void testGGetSection() {
        assertEquals(c1.getSection(), 3);
        assertEquals(c2.getSection(), 2);
    }

    @Test
    void testGetTeachingAssistanceList() {
        c1.addTeachingAssistance(ta1);
        c1.addTeachingAssistance(ta2);
        List<TeachingAssistance> teachingAssistanceList = c1.getTeachingAssistanceList();
        assertEquals(teachingAssistanceList.get(0), ta1);
        assertEquals(teachingAssistanceList.get(1), ta2);
    }

    @Test
    void testGetCredit() {
        assertEquals(c1.getCredit(), 5);
        assertEquals(c2.getCredit(), 3);
    }
}
