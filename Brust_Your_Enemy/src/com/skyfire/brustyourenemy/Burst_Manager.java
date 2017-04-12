package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Burst_Manager {
	public ArrayList<Burst> All_Bursts;
	
	public Burst_Manager()
	{
		All_Bursts = new ArrayList<Burst>();
	}
	
	public void Dispose()
	{
		All_Bursts.clear();
	}
	
	public void Perform_Burst(Burst B)
	{
		All_Bursts.add(B);
	}
	
	public void Perform_Burst(TextureRegion[] Regions,float Speed, float X, float Y)
	{
		Burst B = new Burst(Regions, Speed, new Vector2(X,Y), this);
		Perform_Burst(B);
	}
	
	public void Draw_Bursts(SpriteBatch B)
	{
		int I;
		float w,h;
		for (I = 0; I < All_Bursts.size(); I++)
		{
			w = All_Bursts.get(I).Get_Region().getRegionWidth();
			h = All_Bursts.get(I).Get_Region().getRegionHeight();
			B.draw(All_Bursts.get(I).Get_Region(), All_Bursts.get(I).Burst_Pos.x - w/2, All_Bursts.get(I).Burst_Pos.y - h/2);
		}
	}
	
	public void Update()
	{
		int I;
		for (I = 0; I < All_Bursts.size(); I++)
		{
			All_Bursts.get(I).Update();
			if (All_Bursts.get(I).Is_Animation_Completed() == true)
			{
				All_Bursts.remove(I);
			}
			else
			{
				
			}
		}
	}
}
