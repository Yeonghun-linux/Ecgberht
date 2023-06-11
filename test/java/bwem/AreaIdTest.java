package bwem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AreaIdTest {
    /**
     * purpose : test intValue
     *    input : val=10
     *    output : 10
     */
    @Test
    void intValueTest() {
        AreaId areaId = new AreaId(10);
        assertEquals(areaId.intValue(),10);
    }

    /**
     * purpose : test compareToSame if same AreaID
     *    input : val=10 , val=10
     *    output : 0 (if they are same)
     */
    @Test
    void compareToSameTest() {
        AreaId areaId = new AreaId(10);
        AreaId areId_compare = new AreaId(10);
        assertEquals(areaId.compareTo(areId_compare),0);
    }
    /**
     * purpose : test compareToSame if different AreaID
     *    input : val=10 , val=20
     *    output : -1 (if they are differnet)
     */
    @Test
    void compareToDifferentTest() {
        AreaId areaId = new AreaId(10);
        AreaId areId_compare = new AreaId(20);
        assertEquals(areaId.compareTo(areId_compare),-1);
    }

    /**
     * purpose : test equals if input Object is not AreaID
     *    input : new Object()
     *    output : false (if input is not AreaID class)
     */
    @Test
    void equalsTest_ElseIf() {
        Object object = new Object();
        AreaId areaId = new AreaId(10);
        assertFalse(areaId.equals(object));
    }
    /**
     * purpose : test equals if input Object is same instance
     *    input : areaID
     *    output : true (if input is same instance)
     */
    @Test
    void equalsTest_True() {
        Object object = new Object();
        AreaId areaId = new AreaId(10);
        assertTrue(areaId.equals(areaId));
    }
    /**
     * purpose : test equals if input Object is AreaID class, and same value
     *    input : new AreaId(10)
     *    output : true (if input is same value)
     */
    @Test
    void equalsTest_Else_Same() {
        AreaId areaId = new AreaId(10);
        AreaId areaId_versus = new AreaId(10);
        assertTrue(areaId.equals(areaId_versus));
    }
    /**
     * purpose : test equals if input Object is AreaID class, and differnet value
     *    input : new AreaId(20)
     *    output : false (if input is different value)
     */
    @Test
    void equalsTest_Else_Different() {
        AreaId areaId = new AreaId(10);
        AreaId areaId_versus = new AreaId(20);
        assertFalse(areaId.equals(areaId_versus));
    }
    /**
     * purpose : test testHashCode
     *    input : x
     *    output : 10 (if new AreaID(10) )
     */
    @Test
    void testHashCode() {
        AreaId areaId = new AreaId(10);
        int hashCode = areaId.hashCode();
        assertEquals(hashCode,10);
    }
}