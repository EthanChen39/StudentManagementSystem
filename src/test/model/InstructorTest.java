package model;

import model.enums.Faculty;
import model.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstructorTest {
    Instructor i1;
    Instructor i2;
    Instructor i3;

    Course c1;
    Course c2;
    @BeforeEach
    void init() {
        int age = 51;
        String name = "James";
        Gender gender = Gender.MALE;
        int id = 654321;
        Faculty faculty = Faculty.SCIENCE;

        i1 = Util.generateRandomInstructor();
        i2 = Util.generateRandomInstructor();
        i3 = new Instructor(age, name, gender, id, false, faculty, 20);

        c1 = Util.generateRandomCourse();
        c2 = Util.generateRandomCourse();
    }

    @Test
    void testAddCourseToTeach() {
        assertFalse(i1.addCourseToTeach(null));

        assertTrue(i1.addCourseToTeach(c1));
        assertFalse(i1.addCourseToTeach(c1));
    }

    @Test
    void testGetTeachingCourseList() {
        i1.addCourseToTeach(c1);
        i1.addCourseToTeach(c2);

        List<Course> courses = i1.getTeachingCourseList();
        assertEquals(courses.get(0), c1);
        assertEquals(courses.get(1), c2);
    }

    @Test
    void testPersonToString() {
        String expectedStr = "Instructor{Name=James, id=654321, age=51, isCovidPositive=false}";
        assertEquals(expectedStr, i3.toString());
    }
}
