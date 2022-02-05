package persistance;

import model.*;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// The class is largely based on the work:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java
class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ManagementSystem managementSystem = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyManagementSystem() {
        JsonReader reader = new JsonReader("./data/testManagementSystemEmpty.json");
        try {
            ManagementSystem managementSystem = reader.read();

            assertEquals(0, managementSystem.getCourseList().size());
            assertEquals(0, managementSystem.getStudentList().size());
            assertEquals(0, managementSystem.getTeachingAssistanceList().size());
            assertEquals(0, managementSystem.getInstructorList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testWriterGeneralManagementSystem.json");
        try {
            ManagementSystem managementSystem = reader.read();

            List<Student> studentList = managementSystem.getStudentList();
            List<Instructor> instructorList = managementSystem.getInstructorList();
            List<TeachingAssistance> taList = managementSystem.getTeachingAssistanceList();
            List<Course> courses = managementSystem.getCourseList();

            assertEquals(1, studentList.size());
            assertEquals(1, instructorList.size());
            assertEquals(1, taList.size());
            assertEquals(1, courses.size());
            assertEquals(1, studentList.get(0).getNumOfCourses());
            assertEquals(1, instructorList.get(0).getTeachingCourseList().size());
            assertEquals(55, studentList.get(0).getGrade(courses.get(0)));

//            checkPerson(id, studentList.get(0));
//            checkPerson(instructor.getId(), instructorList.get(0));
//            checkPerson(teachingAssistance.getId(), taList.get(0));
//            checkCourse(course.getSubjectCode(), course.getSection(), courses.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
