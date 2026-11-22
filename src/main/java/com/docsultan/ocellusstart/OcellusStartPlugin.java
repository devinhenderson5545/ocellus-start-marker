package com.docsultan.ocellusstart;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.coords.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.*;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(name="Ocellus Start Marker", description="Marks a position opposite the Bandosian Censer until phase two begins", tags={"ocellus","bandos","censer","tfa"})
public class OcellusStartPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private OcellusStartConfig config;
    @Inject private OcellusStartOverlay overlay;
    @Inject private OverlayManager overlays;
    private final PhaseGate gate=new PhaseGate();
    private WorldView targetWorld;
    private Scene targetScene;
    private WorldPoint target;

    @Provides OcellusStartConfig config(ConfigManager manager) { return manager.getConfig(OcellusStartConfig.class); }
    @Override protected void startUp() { clear(); overlays.add(overlay); }
    @Override protected void shutDown() { overlays.remove(overlay); clear(); }
    private void clear() { gate.reset(); target=null; targetWorld=null; targetScene=null; }

    @Subscribe public void onGameStateChanged(GameStateChanged event)
    { if(event.getGameState()!=GameState.LOGGED_IN) clear(); }

    @Subscribe public void onGameTick(GameTick event)
    {
        WorldView world=OcellusScene.activeWorld(client);
        if(world==null || world.getScene()==null) { clear(); return; }
        if(world!=targetWorld || world.getScene()!=targetScene) clear();
        targetWorld=world; targetScene=world.getScene();
        OcellusScene.Snapshot snapshot=OcellusScene.read(client,world);
        target=gate.update(snapshot.intro,snapshot.p1,snapshot.later,snapshot.portal)
            ? StartPosition.opposite(snapshot.censer(),config.offset()) : null;
    }

    @Subscribe public void onGameObjectSpawned(GameObjectSpawned event)
    {
        // Hide immediately when a portal spawns; the tick scan also handles morphs.
        if(OcellusScene.portal(event.getGameObject().getId()) && OcellusScene.activeWorld(client)!=null)
        { gate.portalSpawned(); target=null; }
    }

    LocalPoint marker()
    {
        WorldView world=OcellusScene.activeWorld(client);
        if(world==null || world!=targetWorld || world.getScene()!=targetScene) return null;
        return OcellusScene.projectTarget(client,world,target);
    }
}
