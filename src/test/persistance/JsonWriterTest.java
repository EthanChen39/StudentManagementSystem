package persistance;

import model.*;
import model.enums.Faculty;
import model.enums.Gender;
import model.enums.YearLevel;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// This is class is largely based on work:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
class JsonWriterTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        try {
            ManagementSystem managementSystem = new ManagementSystem();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyManagementSystem() {
        try {
            ManagementSystem managementSystem = new ManagementSystem();
            JsonWriter writer = new JsonWriter("./data/testManagementSystemEmpty.json");
            writer.open();
            writer.write(managementSystem);
            writer.close();

            JsonReader reader = new JsonReader("./data/testManagementSystemEmpty.json");
            managementSystem = reader.read();
            assertEquals(managementSystem.getCourseList().size(), 0);
            assertEquals(managementSystem.getTeachingAssistanceList().size(), 0);
            assertEquals(managementSystem.getStudentList().size(), 0);
            assertEquals(managementSystem.getInstructorList().size(), 0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            ManagementSystem managementSystem = new ManagementSystem();
            int age = 21;
            String name = "Ethan";
            Gender gender = Gender.MALE;
            int id = 123456;
            Faculty faculty = Faculty.SCIENCE;
            YearLevel yearLevel = YearLevel.JUNIOR;

            Student s1 = new Student(age, name, gender, id, false, faculty, yearLevel);
            Instructor instructor = Util.generateRandomInstructor();
            TeachingAssistance teachingAssistance = Util.generateRandomTA();
            Course course = Util.generateRandomCourse();
            course.addStudents(s1);
            course.addInstructor(instructor);
            course.addTeachingAssistance(teachingAssistance);
            s1.addCourse(course);
            s1.addGrade(course, 55);
            managementSystem.addStudentToSystem(s1);
            managementSystem.addInstructorToSystem(instructor);
            managementSystem.addCourseToSystem(course);
            managementSystem.addTAToSystem(teachingAssistance);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralManagementSystem.json");
            writer.open();
            writer.write(managementSystem);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralManagementSystem.json");
            managementSystem = reader.read();
            List<Student> studentList = managementSystem.getStudentList();
            List<Instructor> instructorList = managementSystem.getInstructorList();
            List<TeachingAssistance> taList = managementSystem.getTeachingAssistanceList();
            List<Course> courses = managementSystem.getCourseList();

            assertEquals(1, studentList.size());
            assertEquals(1, instructorList.size());
            assertEquals(1, taList.size());
            assertEquals(1, courses.size());

            checkPerson(id, studentList.get(0));
            checkPerson(instructor.getId(), instructorList.get(0));
            checkPerson(teachingAssistance.getId(), taList.get(0));
            checkCourse(course.getSubjectCode(), course.getSection(), courses.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


}
