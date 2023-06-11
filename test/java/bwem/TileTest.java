package bwem;

import bwem.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    private Tile tile;
    private Asserter asserter;

    @BeforeEach
    public void setUp() {
        asserter = new Asserter();
        tile = new Tile(asserter);
    }

    @Test
    public void testIsBuildable() {
        assertFalse(tile.isBuildable());

        tile.setBuildable();
        assertTrue(tile.isBuildable());
    }

    @Test
    public void testGetAreaId() {
        assertEquals(AreaId.ZERO, tile.getAreaId());

        tile.setAreaId(new AreaId(1));
        assertEquals(new AreaId(1), tile.getAreaId());
    }

    @Test
    public void testGetLowestAltitude() {
        assertEquals(Altitude.ZERO, tile.getLowestAltitude());

        tile.setLowestAltitude(new Altitude(3));
        assertEquals(new Altitude(3), tile.getLowestAltitude());
    }

    @Test
    public void testIsWalkable() {
        assertFalse(tile.isWalkable());

        tile.setAreaId(new AreaId(1));
        assertTrue(tile.isWalkable());
    }

    @Test
    public void testIsTerrain() {
        assertFalse(tile.isTerrain());

        tile.setAreaId(new AreaId(1));
        assertTrue(tile.isTerrain());
    }

    @Test
    public void testGetGroundHeight() {
        assertEquals(Tile.GroundHeight.LOW_GROUND, tile.getGroundHeight());

        tile.setGroundHeight(1);
        assertEquals(Tile.GroundHeight.HIGH_GROUND, tile.getGroundHeight());
    }

    @Test
    public void testIsDoodad() {
        assertFalse(tile.isDoodad());

        tile.setDoodad();
        assertTrue(tile.isDoodad());
    }

    @Test
    public void testGetInternalData() {
        assertEquals(0, tile.getInternalData());

        tile.setInternalData(42);
        assertEquals(42, tile.getInternalData());
    }
}
