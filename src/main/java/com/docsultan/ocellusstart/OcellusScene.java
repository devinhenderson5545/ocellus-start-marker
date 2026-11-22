package com.docsultan.ocellusstart;

import java.util.*;
import net.runelite.api.*;
import net.runelite.api.coords.*;

final class OcellusScene
{
    static boolean portal(int id) { return id==62684 || id==62685; }
    static boolean bandos(int id) { return id==62693 || id==62694; }

    static WorldPoint template(Client client, WorldView world, LocalPoint point, int plane)
    {
        if(point==null || client.getWorldView(point.getWorldView())!=world) return null;
        if(world.isInstance())
        {
            int[][][] chunks=world.getInstanceTemplateChunks();
            int x=point.getSceneX()/8, y=point.getSceneY()/8;
            if(chunks==null || plane<0 || plane>=chunks.length || chunks[plane]==null
                || x<0 || x>=chunks[plane].length || chunks[plane][x]==null
                || y<0 || y>=chunks[plane][x].length || chunks[plane][x][y]==-1) return null;
        }
        return WorldPoint.fromLocalInstance(client,point,plane);
    }

    static WorldView activeWorld(Client client)
    {
        if(client.getGameState()!=GameState.LOGGED_IN) return null;
        WorldView world=client.getTopLevelWorldView();
        Player player=client.getLocalPlayer();
        if(world==null || player==null || player.getWorldView()!=world) return null;
        WorldPoint tile=player.getWorldLocation();
        if(tile==null || tile.getPlane()!=world.getPlane()) return null;
        WorldPoint point=template(client,world,LocalPoint.fromWorld(world,tile),tile.getPlane());
        return point!=null && point.getRegionID()==StartPosition.REGION && point.getPlane()==1 ? world : null;
    }

    static LocalPoint projectTarget(Client client, WorldView world, WorldPoint target)
    {
        if(target==null) return null;
        if(world.isInstance())
        {
            int[][][] chunks=world.getInstanceTemplateChunks();
            if(chunks==null) return null;
            for(int[][] plane:chunks)
            {
                if(plane==null) return null;
                for(int[] row:plane) if(row==null) return null;
            }
        }
        LocalPoint result=null;
        for(WorldPoint occurrence:WorldPoint.toLocalInstance(world,target))
        {
            if(occurrence.getPlane()!=world.getPlane()) continue;
            LocalPoint point=LocalPoint.fromWorld(world,occurrence);
            if(point==null || !target.equals(template(client,world,point,world.getPlane()))) continue;
            if(result!=null) return null; // Multiple copies of a room: no arbitrary selection.
            result=point;
        }
        return result;
    }

    static Snapshot read(Client client, WorldView world)
    {
        Snapshot result=new Snapshot();
        for(NPC npc:world.npcs())
        {
            int id=npc.getId();
            result.intro |= id>=16671 && id<=16673;
            result.p1 |= id==16674;
            result.later |= id>=16675 && id<=16677;
        }
        Tile[][][] tiles=world.getScene().getTiles();
        int plane=world.getPlane();
        if(tiles==null || plane<0 || plane>=tiles.length || tiles[plane]==null) return result;
        Set<TileObject> seen=Collections.newSetFromMap(new IdentityHashMap<>());
        for(Tile[] column:tiles[plane])
        {
            if(column==null) continue;
            for(Tile tile:column)
            {
                if(tile==null) continue;
                inspect(client,world,tile.getWallObject(),seen,result);
                inspect(client,world,tile.getDecorativeObject(),seen,result);
                inspect(client,world,tile.getGroundObject(),seen,result);
                if(tile.getGameObjects()!=null)
                    for(GameObject object:tile.getGameObjects()) inspect(client,world,object,seen,result);
            }
        }
        return result;
    }

    private static void inspect(Client client, WorldView world, TileObject object, Set<TileObject> seen, Snapshot result)
    {
        if(object==null || !seen.add(object)) return;
        int id=object.getId();
        ObjectComposition definition=client.getObjectDefinition(id);
        if(definition!=null && definition.getImpostorIds()!=null)
        {
            definition=definition.getImpostor();
            if(definition==null) return;
            id=definition.getId();
        }
        if(!portal(id) && !bandos(id)) return;
        WorldPoint point=template(client,world,object.getLocalLocation(),world.getPlane());
        if(point==null || point.getRegionID()!=StartPosition.REGION || point.getPlane()!=1) return;
        result.portal |= portal(id);
        if(bandos(id)) result.censers.add(point);
    }

    static final class Snapshot
    {
        boolean intro, p1, later, portal;
        final Set<WorldPoint> censers=new HashSet<>();
        WorldPoint censer() { return censers.size()==1 ? censers.iterator().next() : null; }
    }
}
