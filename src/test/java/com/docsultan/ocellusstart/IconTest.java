package com.docsultan.ocellusstart;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;

public class IconTest
{
    @Test public void bundledUserIconHasTransparencyAndVisiblePixels() throws Exception
    {
        BufferedImage image=ImageIO.read(getClass().getResource("bandos.png"));
        assertNotNull(image); assertTrue(image.getColorModel().hasAlpha());
        assertEquals(0,image.getRGB(0,0)>>>24);
        int visible=0;
        for(int x=0;x<image.getWidth();x++) for(int y=0;y<image.getHeight();y++)
            if((image.getRGB(x,y)>>>24)>0) visible++;
        assertTrue(visible>100);
    }
}
