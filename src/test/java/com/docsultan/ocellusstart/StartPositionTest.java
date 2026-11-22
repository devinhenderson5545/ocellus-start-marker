package com.docsultan.ocellusstart;

import net.runelite.api.coords.WorldPoint;
import org.junit.Test;
import static org.junit.Assert.*;

public class StartPositionTest
{
    @Test public void allQuadrantsSelectDiagonalOpposite()
    {
        for(int x:new int[]{-1,1}) for(int y:new int[]{-1,1})
            assertEquals(new WorldPoint(4127-4*x,5673-4*y,1),
                StartPosition.opposite(new WorldPoint(4127+12*x,5673+12*y,1),4));
    }
    @Test public void wrongRegionPlaneOrAxisDoesNotGuess()
    {
        assertNull(StartPosition.opposite(null,4));
        assertNull(StartPosition.opposite(new WorldPoint(4135,5680,0),4));
        assertNull(StartPosition.opposite(new WorldPoint(4200,5680,1),4));
        assertNull(StartPosition.opposite(new WorldPoint(4127,5680,1),4));
        assertNull(StartPosition.opposite(new WorldPoint(4135,5673,1),4));
    }
    @Test public void outOfRangeConfigurationIsClamped()
    {
        WorldPoint bandos=new WorldPoint(4138,5684,1);
        assertEquals(new WorldPoint(4125,5671,1),StartPosition.opposite(bandos,-2));
        assertEquals(new WorldPoint(4119,5665,1),StartPosition.opposite(bandos,99));
    }
}
