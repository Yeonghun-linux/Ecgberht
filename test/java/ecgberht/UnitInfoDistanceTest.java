package ecgberht;



import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.unit.PlayerUnit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;




import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UnitInfoDistanceTest {

    private UnitInfo unitInfo;
    private UnitInfoDistance unitInfoDistance;

    @BeforeEach
    void setUp() {
        PlayerUnit unit = mock(PlayerUnit.class);
        when(unit.getPosition()).thenReturn(new Position(10, 10));

        unitInfo = new UnitInfo(unit);
        unitInfoDistance = new UnitInfoDistance(unitInfo);
    }




    @Test
    void testGetDistanceUnitAndDiStance(){
        int expectedDistance = 13;
        int expectedDistance2 = 40;
        assertEquals(expectedDistance, unitInfoDistance.getDistance(new Position(20, 20)));;
        assertEquals(expectedDistance2, unitInfoDistance.getDistance(new Position(40, 40)));
    }





    @Test
    void testGetDistanceUnitAndUnit(){
        PlayerUnit unit1 = mock(PlayerUnit.class);
        when(unit1.getPosition()).thenReturn(new Position(20, 20));

        PlayerUnit unit2 = mock(PlayerUnit.class);
        when(unit2.getPosition()).thenReturn(new Position(40, 40));

        int expectedDistance = 13;
        int expectedDistance2 = 40;
        assertEquals(expectedDistance, unitInfoDistance.getDistance(unit1));
        assertEquals(expectedDistance2, unitInfoDistance.getDistance(unit2));
    }


    @Test
    void testGetDistanceUnitAndUnitInfo(){
        PlayerUnit target = mock(PlayerUnit.class);
        when(target.getPosition()).thenReturn(new Position(20, 20));

        UnitInfo targetInfo = new UnitInfo(target);

        int expectedDistance1 = 13;
        assertEquals(expectedDistance1, unitInfoDistance.getDistance(targetInfo));

        targetInfo.visible = true;

        int expectedDistance2 = 0;
        assertEquals(expectedDistance2, unitInfoDistance.getDistance(targetInfo));
    }


    @Test
    void testGetPredictedDistanceUnitAndUnitInfoWithoutFrame(){
        PlayerUnit target = mock(PlayerUnit.class);
        when(target.getPosition()).thenReturn(new Position(20, 20));
        when(target.getVelocityX()).thenReturn(5.0);
        when(target.getVelocityY()).thenReturn(5.0);

        UnitInfo targetInfo = new UnitInfo(target);
        targetInfo.speed = 1.0;

        int expectedDistance1 = 20;
        assertEquals(expectedDistance1, unitInfoDistance.getPredictedDistance(targetInfo));
    }

    @Test
    void testGetPredictedDistanceUnitAndUnitInfoWithFrame(){
        PlayerUnit target = mock(PlayerUnit.class);
        when(target.getPosition()).thenReturn(new Position(20, 20));
        when(target.getVelocityX()).thenReturn(5.0);
        when(target.getVelocityY()).thenReturn(5.0);

        UnitInfo targetInfo = new UnitInfo(target);
        targetInfo.speed = 1.0;

        int frame = 2;
        int expectedDistance1 = 26;
        assertEquals(expectedDistance1, unitInfoDistance.getPredictedDistance(targetInfo, frame));
    }

}