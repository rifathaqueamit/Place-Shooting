package com.skyfire.brustyourenemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Burst {
	public TextureRegion[] Burst_Regions;
	public Animation Burst_Animation;
	public float Current_Time;
	public Vector2 Burst_Pos;
	public Burst_Manager Manager;
	public float Animation_Speed;
	public Burst(TextureRegion[] Regions, float Speed, Vector2 Pos, Burst_Manager M)
	{
		Burst_Regions = Regions;
		Burst_Animation = new Animation(Speed, Burst_Regions);
		Current_Time = 0;
		Burst_Pos = Pos;
		Manager = M;
		Animation_Speed = Speed;
	}
	
	public Burst(Burst B, Vector2 Pos)
	{
		Burst_Regions = B.Burst_Regions;
		Burst_Animation = new Animation(B.Animation_Speed, B.Burst_Regions);
		Current_Time = 0;
		Burst_Pos = Pos;
		Manager = B.Manager;
	}
	
	public TextureRegion Get_Region()
	{
		return Burst_Animation.getKeyFrame(Current_Time);
	}
	
	public Boolean Is_Animation_Completed()
	{
		return Burst_Animation.isAnimationFinished(Current_Time);
	}
	
	public void Update()
	{
		Current_Time = Current_Time + Gdx.graphics.getDeltaTime();
	}
}
