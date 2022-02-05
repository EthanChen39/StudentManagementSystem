package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest {
    Util util;
    @BeforeEach
    void init() {
        util = new Util();
    }

    @Test
    void testConvertGradeToGPA() {
        assertEquals(Util.convertGradeToGPA(100), 4.33);
        assertEquals(Util.convertGradeToGPA(85), 4.00);
        assertEquals(Util.convertGradeToGPA(80), 3.67);
        assertEquals(Util.convertGradeToGPA(77), 3.33);
        assertEquals(Util.convertGradeToGPA(73), 3.00);
        assertEquals(Util.convertGradeToGPA(70), 2.67);
        assertEquals(Util.convertGradeToGPA(67), 2.33);
        assertEquals(Util.convertGradeToGPA(63), 2.00);
        assertEquals(Util.convertGradeToGPA(60), 1.67);
        assertEquals(Util.convertGradeToGPA(55), 1.33);
        assertEquals(Util.convertGradeToGPA(50), 1.00);
        assertEquals(Util.convertGradeToGPA(40), 0.00);

    }
}
