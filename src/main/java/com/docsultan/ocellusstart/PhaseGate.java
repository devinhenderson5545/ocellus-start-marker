package com.docsultan.ocellusstart;

/** Once portals appear, gaps between portal objects cannot briefly restore the marker. */
final class PhaseGate
{
    private boolean blocked, wasP1;
    void reset() { blocked=false; wasP1=false; }
    void portalSpawned() { blocked=true; }
    boolean update(boolean intro, boolean p1, boolean later, boolean portal)
    {
        if(later || portal) blocked=true;
        else if(intro || (p1 && !wasP1)) blocked=false;
        wasP1=p1;
        return !blocked && !later && !portal && (intro || p1);
    }
}
