package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class Enemy_Manager {
public ArrayList<Enemy_Plane> All_Planes;
public ArrayList<Enemy_Ground> All_Ground_Enemies;
public Timer Bullet_Timer;
public Bullet_Manager Main_Bullet_Manager;
public Transitions_Manager Transition_Man;
public float Screen_Left_To_Go;
public float Screen_Total_Height;
public float Screen_Height;
public Burst Common_Burst_Plane;
public Burst Common_Burst_Ground;

	public Enemy_Manager(Bullet_Manager B_M, Transitions_Manager T)
	{
		All_Planes = new ArrayList<Enemy_Plane>();
		All_Ground_Enemies = new ArrayList<Enemy_Ground>();
		Bullet_Timer = new Timer();
		Main_Bullet_Manager = B_M;
		Transition_Man = T;
		Common_Burst_Plane = null;
		Common_Burst_Ground = null;
	}
	
	public void Set_Burst(Burst For_Plane, Burst For_Ground)
	{
		Common_Burst_Plane = For_Plane;
		Common_Burst_Ground = For_Ground;
	}
	
	public void Add_Plane(Enemy_Plane P)
	{
		All_Planes.add(P);
	}
	
	public void Add_Ground_Enemy(Enemy_Ground G)
	{
		All_Ground_Enemies.add(G);
	}
	
	public void Set_Screen_Left(float Screen_Left,float Screen_T_Height, float Screen_V_Height)
	{
		Screen_Left_To_Go = Screen_Left;
		Screen_Total_Height = Screen_T_Height;
		Screen_Height = Screen_V_Height;
	}
	
	public void Add_Plane(TextureRegion Texture_Region,TextureRegion Shadow_Region,TextureRegion[] Bullet_Regions,float Ang_Shift, float Speed, Vector2 Position, Plane Shoot_To, float Hit_Points, float Attack_Points)
	{
		Enemy_Plane Our_Plane;
		
		Our_Plane = new Enemy_Plane(this, Texture_Region, true, Speed, Transition_Man, Common_Burst_Plane,Hit_Points, Attack_Points);
		Our_Plane.Set_Position(Position.x, Position.y);
		Our_Plane.Set_Shadow_Regions(Shadow_Region);
		Our_Plane.Flip_Y();
		
		Shooting_Style S = new Shooting_Style(0, 0, 400, 3.1416f/2);
		if (Shoot_To != null) S.Set_Shoot_To_Plane(Shoot_To);
		Our_Plane.Add_Shooting_Position(S);
		
		Our_Plane.Set_Auto_Shoot(Bullet_Regions, Ang_Shift);
		
		Add_Plane(Our_Plane);
	}
	
	public void Add_Ground_Enemy(TextureRegion Texture_Region,TextureRegion[] Bullet_Regions,float Ang_Shift, Vector2 Position, Plane Shoot_To,float Hit_Points,float Attack_Points)
	{
		Enemy_Ground Our_Enemy;
		
		Our_Enemy = new Enemy_Ground(this, Texture_Region, Transition_Man, Common_Burst_Ground,Hit_Points,Attack_Points);
		Our_Enemy.Set_Position(Position.x, Position.y);
				
		Shooting_Style S = new Shooting_Style(0, 0, 400, 3.1416f/2);
		if (Shoot_To != null) S.Set_Shoot_To_Plane(Shoot_To);
		Our_Enemy.Add_Shooting_Position(S);
		
		Our_Enemy.Set_Auto_Shoot(Bullet_Regions, Ang_Shift);
		
		Add_Ground_Enemy(Our_Enemy);
	}
	public void Dispose()
	{
		Bullet_Timer.stop();
	}
	
	public void Update_Ground_Enemies(Camera C)
	{
		int I;
		float Current_Y;
		float View_Y;
		
		for (I = 0; I < All_Ground_Enemies.size(); I++)
		{
			if (All_Ground_Enemies.get(I).Perform_Deletion == true)
			{
				All_Ground_Enemies.get(I).Dispose();
				All_Ground_Enemies.remove(I);
			}	
			else 
			{
			//Plane's will only be updated if on the screen
			View_Y = Screen_Total_Height - Screen_Left_To_Go;
			Current_Y = All_Ground_Enemies.get(I).Get_Position().y+(All_Ground_Enemies.get(I).Get_Height()/2);
			
			if (Current_Y <= View_Y+Screen_Height+(All_Ground_Enemies.get(I).Get_Height()) && Current_Y > View_Y) {
			All_Ground_Enemies.get(I).Update();
			//System.out.println("Updating : " + I);
			}
			else
			{
				if (Current_Y < View_Y) {
				All_Ground_Enemies.get(I).Dispose();
				All_Ground_Enemies.remove(I);
				//System.out.println("Ground enemy is removed : " + I);
				}
				else
				{
					//System.out.println("Out of screen : " + I);
				}
			}
			}
		}
	}
	
	public void Update_Planes(Camera C)
	{
		int I;
		float Plane_Current_Y;
		float View_Y;
		
		for (I = 0; I < All_Planes.size(); I++)
		{
			if (All_Planes.get(I).Perform_Deletion == true)
			{
				All_Planes.get(I).Dispose();
				All_Planes.remove(I);
			}	
			else
			{
			//Plane's will only be updated if on the screen
			View_Y = Screen_Total_Height - Screen_Left_To_Go;
			Plane_Current_Y = All_Planes.get(I).Get_Position().y+(All_Planes.get(I).Get_Height()/2);
			
			if (Plane_Current_Y <= View_Y+Screen_Height+(All_Planes.get(I).Get_Height()) && Plane_Current_Y > View_Y) {
			All_Planes.get(I).Update();
			//System.out.println("Updating : " + I);
			}
			else
			{
				if (Plane_Current_Y < View_Y) {
				All_Planes.get(I).Dispose();
				All_Planes.remove(I);
				//System.out.println("Plane is removed : " + I);
				}
				else
				{
					//System.out.println("Out of screen : " + I);
				}
			}
			}
			
		}
	}
	
	public void Draw(SpriteBatch B)
	{
		int I;
		for (I = 0; I < All_Ground_Enemies.size(); I++)
		{
			All_Ground_Enemies.get(I).Draw(B);
		}
		for (I = 0; I < All_Planes.size(); I++)
		{
			All_Planes.get(I).Draw(B);
		}
	}

}
