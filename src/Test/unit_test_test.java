package Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
 
public class unit_test_test {
    @Test

    void twoPlusTwoShouldEqualFour(){
        var calulator = new unit_test();
        assertEquals(4, calulator.add(2, 2));
    }
    @Test

    void threePlusSevenShouldEqualTen(){
        var calulator = new unit_test();
        assertEquals(10, calulator.add(3, 7));
    }
}
