package com.docsultan.ocellusstart;

import java.awt.Color;
import net.runelite.client.config.*;

@ConfigGroup("ocellusstartmarker")
public interface OcellusStartConfig extends Config
{
    @Range(min=2, max=8)
    @ConfigItem(keyName="offset", name="Distance from centre", description="Tiles along EACH axis from the room centre, in the quadrant opposite Bandos", position=0)
    default int offset() { return 4; }

    @Range(min=16, max=64)
    @ConfigItem(keyName="iconHeight", name="Icon height", description="Bandos icon height in pixels", position=1)
    default int iconHeight() { return 32; }

    @ConfigItem(keyName="tileOutline", name="Outline target tile", description="Also outline the marked tile", position=2)
    default boolean tileOutline() { return true; }

    @Alpha
    @ConfigItem(keyName="tileColor", name="Tile colour", description="Colour of the optional tile outline", position=3)
    default Color tileColor() { return new Color(180, 135, 65, 220); }

    @ConfigItem(keyName="showIcon", name="Show Bandos icon", description="Show the Bandos symbol on the target tile", position=4)
    default boolean showIcon() { return true; }

    @ConfigItem(keyName="showArrow", name="Show arrow", description="Draw a downward arrow above the target tile", position=5)
    default boolean showArrow() { return true; }

    @ConfigItem(keyName="flashArrow", name="Flash arrow", description="Blink the arrow on and off; disable for a steady arrow", position=6)
    default boolean flashArrow() { return true; }

    @Range(min=250, max=2000)
    @ConfigItem(keyName="flashPeriod", name="Flash cycle (ms)", description="Time for one complete on/off cycle; the arrow is visible for half", position=7)
    default int flashPeriod() { return 1000; }

    @Range(min=16, max=64)
    @ConfigItem(keyName="arrowSize", name="Arrow size", description="Arrow height in pixels", position=8)
    default int arrowSize() { return 28; }

    @Alpha
    @ConfigItem(keyName="arrowColor", name="Arrow colour", description="Fill colour of the arrow", position=9)
    default Color arrowColor() { return Color.YELLOW; }
}
