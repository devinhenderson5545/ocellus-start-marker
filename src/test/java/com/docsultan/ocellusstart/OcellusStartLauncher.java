package com.docsultan.ocellusstart;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
public final class OcellusStartLauncher
{
    public static void main(String[] args) throws Exception
    { ExternalPluginManager.loadBuiltin(OcellusStartPlugin.class); RuneLite.main(args); }
}
