package model;
import model.enums.SubjectCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeachingAssistanceTest {
    TeachingAssistance t1;
    TeachingAssistance t2;

    Course c1;
    Course c2;

    @BeforeEach
    void init() {
        t1 = Util.generateRandomTA();
        t2 = Util.generateRandomTA();

        c1 = new Course(SubjectCode.AFST, 3, 1);
        c1 = new Course(SubjectCode.MATH, 2, 2);
    }

    @Test
    void testGetHourlyWages() {
        t1.setHourlyWages(20);
        assertEquals(t1.getHourlyWages(), 20);
    }

    @Test
    void testSetHourlyWages() {
        t1.setHourlyWages(20);
        assertEquals(t1.getHourlyWages(), 20);
        t1.setHourlyWages(5);
        assertEquals(t1.getHourlyWages(), 20);
    }

    @Test
    void testAssignTaCourse() {
        t1.assignTaCourse(c1);
        assertEquals(t1.getTaCourse(), c1);
    }

    @Test
    void testRemoveTaCourse() {
        assertFalse(t1.removeTaCourse());

        t1.assignTaCourse(c1);
        assertTrue(t1.removeTaCourse());
    }

    @Test
    void testSetWage() {
        t1.setWage(2.34);
        assertEquals(t1.getWage(), 15.20);

        t1.setWage(20);
        assertEquals(t1.getWage(), 20);
    }

    @Test
    void testGetWage() {
        t1.setWage(2.34);
        assertEquals(t1.getWage(), 15.20);

        t1.setWage(20);
        assertEquals(t1.getWage(), 20);
    }

}
