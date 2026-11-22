package com.docsultan.ocellusstart;

import java.util.*;
import net.runelite.api.*;
import net.runelite.api.coords.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OcellusSceneTest
{
    private Client client;
    private WorldView world;
    private Player player;
    private Scene scene;
    private Tile[][][] tiles;
    @Before public void setup()
    {
        client=mock(Client.class); world=mock(WorldView.class); player=mock(Player.class); scene=mock(Scene.class);
        when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
        when(client.getTopLevelWorldView()).thenReturn(world); when(client.getLocalPlayer()).thenReturn(player);
        when(client.getWorldView(WorldView.TOPLEVEL)).thenReturn(world);
        when(world.getId()).thenReturn(WorldView.TOPLEVEL); when(world.isTopLevel()).thenReturn(true);
        when(world.getBaseX()).thenReturn(4096); when(world.getBaseY()).thenReturn(5632);
        when(world.getSizeX()).thenReturn(104); when(world.getSizeY()).thenReturn(104);
        when(world.getPlane()).thenReturn(1); when(world.contains(any(WorldPoint.class))).thenReturn(true);
        when(player.getWorldView()).thenReturn(world);
        when(player.getWorldLocation()).thenReturn(StartPosition.CENTER);
        when(world.getScene()).thenReturn(scene);
        tiles=new Tile[4][104][104]; when(scene.getTiles()).thenReturn(tiles);
        npcs(16674);
    }
    private void npcs(int... ids)
    {
        List<NPC> list=new ArrayList<>();
        for(int id:ids) { NPC npc=mock(NPC.class); when(npc.getId()).thenReturn(id); list.add(npc); }
        IndexedObjectSet<NPC> set=mock(IndexedObjectSet.class);
        doReturn(set).when(world).npcs(); when(set.iterator()).thenAnswer(a->list.iterator());
    }
    private GameObject object(int id,int x,int y)
    {
        GameObject object=mock(GameObject.class);
        when(object.getId()).thenReturn(id);
        LocalPoint point=LocalPoint.fromWorld(world,new WorldPoint(x,y,1));
        when(object.getLocalLocation()).thenReturn(point);
        return object;
    }
    private void tile(int x,int y,GameObject... objects)
    {
        Tile tile=mock(Tile.class); when(tile.getGameObjects()).thenReturn(objects); tiles[1][x][y]=tile;
    }
    @Test public void activeWorldRejectsLogoutWrongRoomAndMissingPlayer()
    {
        assertSame(world,OcellusScene.activeWorld(client));
        for(GameState state:new GameState[]{GameState.LOADING,GameState.HOPPING,GameState.LOGIN_SCREEN,GameState.CONNECTION_LOST})
        { when(client.getGameState()).thenReturn(state); assertNull(OcellusScene.activeWorld(client)); }
        when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
        when(player.getWorldLocation()).thenReturn(new WorldPoint(4200,5673,1));
        assertNull(OcellusScene.activeWorld(client));
        when(client.getLocalPlayer()).thenReturn(null); assertNull(OcellusScene.activeWorld(client));
    }
    @Test public void deduplicatesMultiTileCenserAndFindsBothPortalIds()
    {
        for(int censerId:new int[]{62693,62694}) for(int portalId:new int[]{62684,62685})
        {
            GameObject censer=object(censerId,4138,5684);
            tile(42,52,censer); tile(43,52,censer); tile(30,40,object(portalId,4126,5672));
            OcellusScene.Snapshot s=OcellusScene.read(client,world);
            assertTrue(s.p1); assertTrue(s.portal); assertFalse(s.later);
            assertEquals(new WorldPoint(4138,5684,1),s.censer());
        }
    }
    @Test public void ambiguousOrMissingCensersDoNotGiveTarget()
    {
        assertNull(OcellusScene.read(client,world).censer());
        tile(42,52,object(62693,4138,5684)); tile(20,30,object(62694,4116,5662));
        assertNull(OcellusScene.read(client,world).censer());
    }
    @Test public void transformedCenserDefinitionIsRecognized()
    {
        ObjectComposition base=mock(ObjectComposition.class), transformed=mock(ObjectComposition.class);
        when(client.getObjectDefinition(90000)).thenReturn(base);
        when(base.getImpostorIds()).thenReturn(new int[]{62693}); when(base.getImpostor()).thenReturn(transformed);
        when(transformed.getId()).thenReturn(62693); tile(42,52,object(90000,4138,5684));
        assertEquals(new WorldPoint(4138,5684,1),OcellusScene.read(client,world).censer());
    }
    @Test public void laterNpcFormsHideEvenWithoutPortals()
    {
        for(int id:new int[]{16675,16676,16677})
        { npcs(id); assertTrue(OcellusScene.read(client,world).later); assertFalse(OcellusScene.read(client,world).p1); }
    }
    @Test public void targetProjectsBackToSameTemplateForAllRotations()
    {
        when(world.isInstance()).thenReturn(true);
        when(world.getBaseX()).thenReturn(8000); when(world.getBaseY()).thenReturn(8000);
        WorldPoint target=new WorldPoint(4123,5669,1);
        for(int rotation=0;rotation<4;rotation++)
        {
            int[][][] chunks=new int[4][13][13];
            for(int[][] plane:chunks) for(int[] row:plane) Arrays.fill(row,-1);
            chunks[1][3][3]=(1<<24)|((target.getX()/8)<<14)|((target.getY()/8)<<3)|(rotation<<1);
            when(world.getInstanceTemplateChunks()).thenReturn(chunks);
            LocalPoint point=OcellusScene.projectTarget(client,world,target);
            assertNotNull("rotation "+rotation,point);
            assertEquals(target,OcellusScene.template(client,world,point,1));
            when(player.getWorldLocation()).thenReturn(new WorldPoint(8000+point.getSceneX(),8000+point.getSceneY(),1));
            assertSame(world,OcellusScene.activeWorld(client));
            chunks[1][4][4]=chunks[1][3][3];
            assertNull("duplicate instance chunk",OcellusScene.projectTarget(client,world,target));
        }
    }
    @Test public void incompleteInstanceChunksDoNotThrowOrDraw()
    {
        when(world.isInstance()).thenReturn(true);
        assertNull(OcellusScene.activeWorld(client));
        assertNull(OcellusScene.projectTarget(client,world,StartPosition.CENTER));
        when(world.getInstanceTemplateChunks()).thenReturn(new int[4][][]);
        assertNull(OcellusScene.activeWorld(client));
        assertNull(OcellusScene.projectTarget(client,world,StartPosition.CENTER));
    }

    private OcellusStartPlugin plugin() throws Exception
    {
        OcellusStartPlugin plugin=new OcellusStartPlugin();
        Object[][] fields={
            {"client",client},
            {"config",mock(OcellusStartConfig.class,CALLS_REAL_METHODS)},
            {"overlays",mock(net.runelite.client.ui.overlay.OverlayManager.class)},
            {"overlay",mock(OcellusStartOverlay.class)}
        };
        for(Object[] pair:fields)
        {
            java.lang.reflect.Field field=OcellusStartPlugin.class.getDeclaredField((String)pair[0]);
            field.setAccessible(true); field.set(plugin,pair[1]);
        }
        plugin.startUp(); return plugin;
    }
    @Test public void cachedMarkerHidesImmediatelyOnExitAndShutdown() throws Exception
    {
        tile(42,52,object(62693,4138,5684));
        OcellusStartPlugin plugin=plugin();
        plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNotNull(plugin.marker());
        when(client.getGameState()).thenReturn(GameState.LOADING); assertNull(plugin.marker());
        when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
        when(player.getWorldLocation()).thenReturn(new WorldPoint(4200,5673,1)); assertNull(plugin.marker());
        when(player.getWorldLocation()).thenReturn(StartPosition.CENTER);
        plugin.shutDown(); assertNull(plugin.marker());
    }
    @Test public void portalEventAndPhaseTransitionHideTheActualPluginMarker() throws Exception
    {
        tile(42,52,object(62693,4138,5684));
        OcellusStartPlugin plugin=plugin();
        plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNotNull(plugin.marker());
        net.runelite.api.events.GameObjectSpawned event=new net.runelite.api.events.GameObjectSpawned();
        event.setGameObject(object(62684,4126,5672)); plugin.onGameObjectSpawned(event);
        assertNull(plugin.marker());
        plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNull(plugin.marker());
        plugin.shutDown(); npcs(16675); plugin.startUp();
        plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNull(plugin.marker());
        npcs(16674); plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNotNull(plugin.marker());
    }
    @Test public void replacementSceneDoesNotReusePreviousTarget() throws Exception
    {
        tile(42,52,object(62693,4138,5684));
        OcellusStartPlugin plugin=plugin();
        plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNotNull(plugin.marker());
        when(world.getScene()).thenReturn(mock(Scene.class)); assertNull(plugin.marker());
        plugin.onGameTick(new net.runelite.api.events.GameTick()); assertNull(plugin.marker());
    }
}
