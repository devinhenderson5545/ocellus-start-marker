package com.docsultan.ocellusstart;

import net.runelite.api.coords.WorldPoint;

final class StartPosition
{
    static final int REGION=16472;
    // Template centre: region tile (31,41), plane 1. Validate live before release.
    static final WorldPoint CENTER=new WorldPoint(4127,5673,1);

    static WorldPoint opposite(WorldPoint censer, int offset)
    {
        if(censer==null || censer.getRegionID()!=REGION || censer.getPlane()!=1) return null;
        int dx=Integer.compare(censer.getX(),CENTER.getX());
        int dy=Integer.compare(censer.getY(),CENTER.getY());
        if(dx==0 || dy==0) return null; // Ambiguous quadrant: do not guess.
        int distance=Math.max(2,Math.min(8,offset));
        return new WorldPoint(CENTER.getX()-dx*distance,CENTER.getY()-dy*distance,1);
    }
}
