package com.docsultan.ocellusstart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowIndicatorTest
{
    @Test public void flashesOnAndOffAtConfiguredRate()
    {
        assertTrue(ArrowIndicator.visible(true,true,1000,0));
        assertTrue(ArrowIndicator.visible(true,true,1000,499));
        assertFalse(ArrowIndicator.visible(true,true,1000,500));
        assertFalse(ArrowIndicator.visible(true,true,1000,999));
        assertTrue(ArrowIndicator.visible(true,true,1000,1000));
        assertFalse(ArrowIndicator.visible(true,true,600,300));
        assertTrue(ArrowIndicator.visible(true,true,600,600));
    }
    @Test public void disabledAndSteadyModesIgnoreFlashPhase()
    {
        for(long time:new long[]{0,500,1000,Long.MAX_VALUE,-500})
        {
            assertFalse(ArrowIndicator.visible(false,true,1000,time));
            assertTrue(ArrowIndicator.visible(true,false,1000,time));
        }
    }
    @Test public void invalidPeriodsAreClampedWithoutDivisionByZero()
    {
        assertFalse(ArrowIndicator.visible(true,true,0,125));
        assertTrue(ArrowIndicator.visible(true,true,0,250));
        assertFalse(ArrowIndicator.visible(true,true,Integer.MAX_VALUE,1000));
    }
    @Test public void rendersConfiguredColorAndSizeWithoutChangingGraphics() throws Exception
    {
        BufferedImage image=new BufferedImage(320,150,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics=image.createGraphics();
        try
        {
            graphics.setColor(Color.MAGENTA);
            Stroke stroke=graphics.getStroke();
            ArrowIndicator.draw(graphics,60,100,28,Color.YELLOW);
            ArrowIndicator.draw(graphics,160,100,64,Color.CYAN);
            ArrowIndicator.draw(graphics,260,100,16,new Color(255,100,0,128));
            assertEquals(Color.MAGENTA,graphics.getColor()); assertSame(stroke,graphics.getStroke());
            assertEquals(Color.YELLOW.getRGB(),image.getRGB(60,78));
            assertEquals(Color.CYAN.getRGB(),image.getRGB(160,45));
            assertEquals(128,image.getRGB(260,88)>>>24);
            assertEquals(0,image.getRGB(60,60));
        }
        finally { graphics.dispose(); }
        File file=new File("build/arrow-preview.png"); file.getParentFile().mkdirs(); ImageIO.write(image,"png",file);
    }
}
