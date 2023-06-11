package ecgberht;

import ecgberht.BehaviourTrees.Training.TrainUnit;
import org.junit.jupiter.api.Test;
import org.openbw.bwapi4j.unit.Unit;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static ecgberht.Ecgberht.getGs;
class UnitStorageTest {
    /*
    purpose : test getter getAllyUnit()

    there are no Unit, so return is same with new  TreeMap<>()
     */
    @Test
    void getAllyUnitTest(){
        UnitStorage p = new UnitStorage();

        assertEquals(p.getAllyUnits(),new TreeMap<>());

    }
    /*
    purpose : test getter getEnemyUnit()

    there are no Unit, so return is same with new  TreeMap<>()
     */
    @Test
    void getEnemyUnitTest(){
        UnitStorage p = new UnitStorage();
        assertEquals(p.getEnemyUnits(), new TreeMap<>());
    }
}