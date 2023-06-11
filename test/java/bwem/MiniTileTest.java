package bwem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MiniTileTest {

    private MiniTile miniTile;

    @BeforeEach
    public void setUp() {
        miniTile = new MiniTile(new Asserter());
    }

    @Test
    public void testIsWalkable() {
        assertTrue(miniTile.isWalkable());

        miniTile.setWalkable(true);
        assertTrue(miniTile.isWalkable());

        miniTile.setWalkable(false);
        assertFalse(miniTile.isWalkable());
    }

    @Test
    public void testGetAltitude() {
        assertEquals(Altitude.UNINITIALIZED, miniTile.getAltitude());

        Altitude altitude = new Altitude(2);
        miniTile.setAltitude(altitude);
        assertEquals(altitude, miniTile.getAltitude());
    }

    @Test
    public void testIsSea() {
        assertFalse(miniTile.isSea());

        miniTile.setAltitude(new Altitude(1));
        assertFalse(miniTile.isSea());
    }

    @Test
    public void testIsLake() {
        assertFalse(miniTile.isLake());

        miniTile.setAltitude(new Altitude(1));
        miniTile.setWalkable(false);

        assertTrue(miniTile.isLake());
    }

    @Test
    public void testIsTerrain() {
        assertTrue(miniTile.isTerrain());

        miniTile.setWalkable(true);
        assertTrue(miniTile.isTerrain());

        miniTile.setWalkable(false);
        assertFalse(miniTile.isTerrain());
    }

    @Test
    public void testGetAreaId() {
        assertEquals(AreaId.UNINITIALIZED, miniTile.getAreaId());

        AreaId areaId = new AreaId(1);
        miniTile.setAreaId(areaId);
        assertEquals(areaId, miniTile.getAreaId());
    }

    @Test
    public void testIsSeaOrLake() {
        assertFalse(miniTile.isSeaOrLake());

        miniTile.setAltitude(new Altitude(1));
        assertTrue(miniTile.isSeaOrLake());

        miniTile.setWalkable(true);
        assertFalse(miniTile.isSeaOrLake());
    }

    @Test
    public void testIsAltitudeMissing() {
        assertTrue(miniTile.isAltitudeMissing());

        miniTile.setAltitude(new Altitude(1));
        assertFalse(miniTile.isAltitudeMissing());
    }

    @Test
    public void testIsAreaIdMissing() {
        assertTrue(miniTile.isAreaIdMissing());

        miniTile.setAreaId(new AreaId(1));
        assertFalse(miniTile.isAreaIdMissing());
    }

    @Test
    public void testReplaceAreaId() {
        miniTile.setAreaId(new AreaId(1));
        miniTile.replaceAreaId(new AreaId(2));
        assertEquals(new AreaId(2), miniTile.getAreaId());
    }

    @Test
    public void testSetBlocked() {
        miniTile.setBlocked();
        assertTrue(miniTile.isBlocked());
    }

    @Test
    public void testReplaceBlockedAreaId() {
        miniTile.setBlocked();
        miniTile.replaceBlockedAreaId(new AreaId(1));
        assertEquals(new AreaId(1), miniTile.getAreaId());
    }
}
