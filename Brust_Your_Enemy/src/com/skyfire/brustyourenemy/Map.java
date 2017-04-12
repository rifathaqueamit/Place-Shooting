package com.skyfire.brustyourenemy;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map {
	public TiledMap Map;
	public OrthogonalTiledMapRenderer Renderer;
	
	private OrthographicCamera Cam;
	public int Map_Width;
	public int Map_Height;
	public int Tile_Pixel_Width;
	public int Tile_Pixel_Height;
	public int Map_Total_Width;
	public int Map_Total_Height;
	
	public Map(String Map_File, OrthographicCamera C)
	{
		Map = new TideMapLoader().load(Map_File);
		Renderer = new OrthogonalTiledMapRenderer(Map);
		Cam = C;
		Renderer.setView(C);
	
		/* Load them from map
		Map_Width = prop.get("map_width", Integer.class);
		Map_Height = prop.get("map_height", Integer.class);
		Tile_Pixel_Width = prop.get("tile_width", Integer.class);
		Tile_Pixel_Height = prop.get("tile_height", Integer.class);

	    Map_Total_Width = Map_Width * Tile_Pixel_Width;
		Map_Total_Height = Map_Height * Tile_Pixel_Height;
		*/
	}
	
	public void Draw()
	{
		Renderer.setView(Cam);
		Renderer.render();
	}
	
	public void Dispose()
	{
		try {
		Renderer.dispose();
		Map.dispose();
		}
		catch (Exception e)
		{
			
		}
	}

}
