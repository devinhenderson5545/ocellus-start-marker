package com.docsultan.ocellusstart;

import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseGateTest
{
    @Test public void showsBeforeP2AndHidesOnPortals()
    {
        PhaseGate gate=new PhaseGate();
        assertTrue(gate.update(true,false,false,false));
        assertTrue(gate.update(false,true,false,false));
        assertFalse(gate.update(false,true,false,true));
        assertFalse(gate.update(false,true,false,false));
    }
    @Test public void startupInLaterPhasesNeverShowsMarker()
    {
        PhaseGate gate=new PhaseGate();
        assertFalse(gate.update(false,false,true,false));
        assertFalse(gate.update(false,false,false,false));
        assertFalse(gate.update(true,true,true,false));
    }
    @Test public void newFightAndResetCanShowAgain()
    {
        PhaseGate gate=new PhaseGate();
        gate.update(false,false,true,true);
        assertTrue(gate.update(false,true,false,false));
        gate.portalSpawned();
        assertFalse(gate.update(false,true,false,false));
        gate.reset();
        assertTrue(gate.update(true,false,false,false));
    }
}
