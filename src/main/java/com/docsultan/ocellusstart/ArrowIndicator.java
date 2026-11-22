package com.docsultan.ocellusstart;

import java.awt.*;

/** Screen-space pointer; never changes the game's shared hint-arrow state. */
final class ArrowIndicator
{
    static boolean visible(boolean enabled, boolean flash, int periodMs, long nowMs)
    {
        int period=Math.max(250,Math.min(2000,periodMs));
        return enabled && (!flash || Math.floorMod(nowMs,period)<period/2);
    }

    static void draw(Graphics2D graphics,int centerX,int tipY,int requestedSize,Color color)
    {
        int size=Math.max(16,Math.min(64,requestedSize));
        int half=size/2, shaft=Math.max(3,size/6), shoulder=tipY-size/2, top=tipY-size;
        Polygon arrow=new Polygon(
            new int[]{centerX-shaft,centerX+shaft,centerX+shaft,centerX+half,centerX,centerX-half,centerX-shaft},
            new int[]{top,top,shoulder,shoulder,tipY,shoulder,shoulder},7);
        Graphics2D copy=(Graphics2D)graphics.create();
        try
        {
            copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            copy.setColor(color); copy.fillPolygon(arrow);
            copy.setColor(new Color(0,0,0,color.getAlpha())); copy.setStroke(new BasicStroke(2)); copy.drawPolygon(arrow);
        }
        finally { copy.dispose(); }
    }
}
