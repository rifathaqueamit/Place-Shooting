package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Enemy_Ground {
	public TextureRegion Enemy_Region;

	
	public Sprite Enemy_Sprite;

	public Vector2 Centre;
	
	public boolean Shoot_Auto = false;
	
	public Task Bullet_Creating_Task;
	public Enemy_Manager Current_Manager;
	public ArrayList<Shooting_Style> Shooting;
	
	public Transition Enemy_Ground_Transition;
	
	public Burst Burst_Animation;
	
	public Boolean Burst_On;
	
	public float Hit_Points;
	public float Attack_Points;
	
	public Boolean Perform_Deletion = false;
	
	public Enemy_Ground(Enemy_Manager E_M, TextureRegion Region, Transitions_Manager T, Burst B, float Hit,float Attack)
	{
		Current_Manager = E_M;
		
		Enemy_Region = Region;

		Enemy_Sprite = new Sprite(Region);
		Centre = new Vector2(Region.getRegionWidth() / 2, Region.getRegionHeight() / 2);
		Set_Position(0, 0);
		
		Shooting = new ArrayList<Shooting_Style>();
		
		Enemy_Ground_Transition = T.Transition_3_Times_White_Fade(this);
		
		Burst_Animation = B;
		if (B==null) Burst_On = false; else Burst_On = true;
		
		Hit_Points = Hit;
		Attack_Points = Attack;
	}
	
	public void Dispose()
	{
		Bullet_Creating_Task.cancel();
		Bullet_Creating_Task = null;
	}
	
	public void Update()
	{

	}
	
	public float Get_Rotation_Radians()
	{
		return Enemy_Sprite.getRotation() * 3.1416f / 180;
	}
	
	public float Get_Rotation_Degree()
	{
		return Enemy_Sprite.getRotation();
	}
	
	public void Set_Rotation(float Angle)
	{
		Enemy_Sprite.setRotation(Angle);
	}
	
	public void Add_Shooting_Position(Shooting_Style S)
	{
		Shooting.add(S);
	}
	
	public void Set_Auto_Shoot(final TextureRegion[] Bullet_Texture_Regions,final float Angle_Shift)
	{
		Bullet_Creating_Task = new Task() {
			@Override
			public void run() {
				Shoot(Bullet_Texture_Regions, Angle_Shift);
			}
		};
		Current_Manager.Bullet_Timer.scheduleTask( Bullet_Creating_Task, 0, 1);
	}
	
	public float Get_Height()
	{
		return Enemy_Sprite.getHeight();
	}
	
	public float Get_Width()
	{
		return Enemy_Sprite.getWidth();
	}
	
	public Vector2 Get_Position()
	{
		return new Vector2(Enemy_Sprite.getX() + Centre.x, Enemy_Sprite.getY() + Centre.y);
	}
	

	public void Set_Position(float X,float Y)
	{
		Enemy_Sprite.setPosition(X - Centre.x, Y - Centre.y);
	    //if (Shadow_Sprite != null) Shadow_Sprite.setPosition(X - Centre.x + Shadow_Translate.x, Y - Centre.y + Shadow_Translate.y);
	}
	
	public void Update_Transition(Transition T)
	{
		Enemy_Sprite.setColor(1, 1-T.Current_Fadiness, 1-T.Current_Fadiness, 1);
	}
	
	public void Hurt(float Attack)
	{
		Enemy_Ground_Transition.Play_Transition();
	    if (Burst_On == true)	Burst_Animation.Manager.Perform_Burst(new Burst(Burst_Animation, Get_Position()));
	    Hit_Points = Hit_Points - Attack;
	    if (Hit_Points < 0)
	    	Perform_Deletion = true;
	}
	
	
  void Shoot( TextureRegion[] Bullet_Regions, float Angle_Shift)
	{
		int I;
		
		for (I = 0; I < Shooting.size(); I++) 
		{
		Bullet New_Bullet;
		New_Bullet = new Bullet(Bullet_Regions, 0,Angle_Shift,false, Attack_Points);
		New_Bullet.Set_Position(Get_Position().x + Shooting.get(I).Offset_X, Get_Position().y - Shooting.get(I).Offset_Y);
		if (Shooting.get(I).Shoot_To_Plane == false) {
			New_Bullet.Set_Movement(Shooting.get(I).Speed, Shooting.get(I).Direction);
		}
		else
		{
			New_Bullet.Set_Movement(Shooting.get(I).Speed, Shooting.get(I).Shoot_To);
		}
		
		Current_Manager.Main_Bullet_Manager.Add_Bullet(New_Bullet);
		}
	}
  
  public boolean Is_Out_Of_Screen(Camera C)
	{
		int W = Gdx.graphics.getWidth();
		int H = Gdx.graphics.getHeight();
		
		float X = Enemy_Sprite.getX();
		float Y = Enemy_Sprite.getY();
		float B_W = Enemy_Sprite.getWidth();
		float B_H = Enemy_Sprite.getHeight();
		
		Vector3 Plane_Pos = new Vector3(X,Y, 0);
		
		C.project(Plane_Pos);
		
		if (Plane_Pos.x > W) return true;
		if (Plane_Pos.x + B_W < 0) return true;
		if (Plane_Pos.y > H) return true;
		if (Plane_Pos.y + B_H < 0) return true;
		
		return false;
	}
	
	public void Draw(SpriteBatch B)
	{
		Enemy_Sprite.setRegion(Enemy_Region);
		Enemy_Sprite.draw(B);
	}
	
	
}