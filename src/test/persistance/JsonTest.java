package persistance;

import model.Course;
import model.Person;
import model.enums.SubjectCode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkPerson(int id, Person person) {
        assertEquals(id, person.getId());
    }

    protected void checkCourse(SubjectCode subjectCode, int section, Course course) {
        assertEquals(subjectCode, course.getSubjectCode());
        assertEquals(section, course.getSection());
    }
}
