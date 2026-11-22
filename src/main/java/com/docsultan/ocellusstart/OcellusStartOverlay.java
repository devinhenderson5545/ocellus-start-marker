package com.docsultan.ocellusstart;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ImageUtil;

public class OcellusStartOverlay extends Overlay
{
    private final Client client;
    private final OcellusStartPlugin plugin;
    private final OcellusStartConfig config;
    private final BufferedImage source=ImageUtil.loadImageResource(getClass(),"bandos.png");
    private BufferedImage icon;
    private int height;

    @Inject public OcellusStartOverlay(Client client,OcellusStartPlugin plugin,OcellusStartConfig config)
    {
        this.client=client; this.plugin=plugin; this.config=config;
        setPosition(OverlayPosition.DYNAMIC); setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override public Dimension render(Graphics2D graphics)
    {
        LocalPoint point=plugin.marker();
        if(point==null) return null;
        int wanted=Math.max(16,Math.min(64,config.iconHeight()));
        if(icon==null || wanted!=height)
        {
            height=wanted;
            icon=ImageUtil.resizeImage(source,Math.max(1,source.getWidth()*height/source.getHeight()),height);
        }
        Graphics2D copy=(Graphics2D)graphics.create();
        try
        {
            if(config.tileOutline())
            {
                Polygon poly=Perspective.getCanvasTilePoly(client,point);
                if(poly!=null) { copy.setColor(config.tileColor()); copy.setStroke(new BasicStroke(2)); copy.drawPolygon(poly); }
            }
            net.runelite.api.Point canvas=Perspective.localToCanvas(client,point,client.getTopLevelWorldView().getPlane());
            if(canvas!=null)
            {
                if(config.showIcon()) copy.drawImage(icon,canvas.getX()-icon.getWidth()/2,canvas.getY()-icon.getHeight()/2,null);
                if(ArrowIndicator.visible(config.showArrow(),config.flashArrow(),config.flashPeriod(),System.nanoTime()/1_000_000L))
                {
                    int gap=config.showIcon() ? icon.getHeight()/2+6 : 6;
                    ArrowIndicator.draw(copy,canvas.getX(),canvas.getY()-gap,config.arrowSize(),config.arrowColor());
                }
            }
        }
        finally { copy.dispose(); }
        return null;
    }
}
