package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Enemy_Plane {
	public TextureRegion Plane_Region;
	public TextureRegion Shadow_Region;
	public boolean Shadow_On = false;
	public Sprite Plane_Sprite;
	public Sprite Shadow_Sprite;
	public Vector2 Shadow_Translate = new Vector2(20,15);
	public Vector2 Centre;
	
	public boolean Move_Auto;
	public Moving_Style Move_Style;
	
	public boolean Shoot_Auto = false;
	public Task Bullet_Creating_Task;
	public Enemy_Manager Current_Manager;
	public ArrayList<Shooting_Style> Shooting;
	public boolean Flipped_Y = false;
	
	public Transition Plane_Transition;
	
	public Burst Burst_Animation;
	
	public Boolean Burst_On;
	
	public float Hit_Points;
	public float Attack_Points;
	
	public Boolean Perform_Deletion = false;
	
	public Enemy_Plane(Enemy_Manager E_M, TextureRegion Region, boolean Auto_Move, float Moving_Speed, Transitions_Manager T, Burst B, float Hit, float Attack)
	{
		Current_Manager = E_M;
		
		Plane_Region = Region;

		Plane_Sprite = new Sprite(Region);
		Centre = new Vector2(Region.getRegionWidth() / 2, Region.getRegionHeight() / 2);
		Set_Position(0, 0);
		
		Shooting = new ArrayList<Shooting_Style>();
		Move_Auto = Auto_Move;
		
		Move_Style = new Moving_Style(false, Moving_Speed);
		
		Plane_Sprite.setOrigin(Plane_Sprite.getWidth()/2, Plane_Sprite.getHeight()/2);
		
		Plane_Transition = T.Transition_3_Times_White_Fade(this);
		
		Burst_Animation = B;
		if (B==null) Burst_On = false; else Burst_On = true;
		
		Hit_Points = Hit;
		Attack_Points = Attack;
	}
	
	public void Update()
	{
		if (Move_Auto == true)
		{
			
			Plane_Sprite.rotate(Move_Style.Increase_Angle);
			if (Shadow_Sprite != null) Shadow_Sprite.rotate(Move_Style.Increase_Angle);
			
			Vector2 Old_Dir = Move_Style.Direction;
			float Get_Ang = Old_Dir.angle();
			Get_Ang = Get_Ang + Move_Style.Increase_Angle;
			Get_Ang = Get_Ang % 360;
			Vector2 New_Dir = new Vector2();
			New_Dir.x = MathUtils.cos(Get_Ang * MathUtils.degreesToRadians);
			New_Dir.y = MathUtils.sin(Get_Ang * MathUtils.degreesToRadians);
			Move_Style.Direction = New_Dir;
			
			Plane_Sprite.translate(Move_Style.Direction.x * Move_Style.Speed *  Gdx.graphics.getDeltaTime(),Move_Style.Direction.y * Move_Style.Speed * Gdx.graphics.getDeltaTime());
			//Shadow_Sprite.setPosition(Plane_Sprite.getX(), Plane_Sprite.getY());
			//Shadow_Sprite.setRotation(Plane_Sprite.getRotation());
		}
	}
	
	public float Get_Rotation_Radians()
	{
		return Plane_Sprite.getRotation() * 3.1416f / 180;
	}
	
	public float Get_Rotation_Degree()
	{
		return Plane_Sprite.getRotation();
	}
	
	public void Set_Rotation(float Angle)
	{
		Plane_Sprite.setRotation(Angle);
		if (Shadow_Sprite != null) Shadow_Sprite.setRotation(Angle);
	}
	
	public void Add_Shooting_Position(Shooting_Style S)
	{
		Shooting.add(S);
	}
	
	public void Set_Auto_Shoot(final TextureRegion[] Bullet_Texture_Regions, final float Angle_Shift)
	{
		Bullet_Creating_Task = new Task() {
			@Override
			public void run() {
				Shoot(Bullet_Texture_Regions, Angle_Shift);
			}
		};
		Current_Manager.Bullet_Timer.scheduleTask( Bullet_Creating_Task, 0, 1);
	}
	
	public void Dispose()
	{
		Bullet_Creating_Task.cancel();
		Bullet_Creating_Task = null;
	}
	
	public void Set_Shadow_Regions(TextureRegion Region)
	{
		Shadow_Region = Region;
		Shadow_On = true;
		Shadow_Sprite = new Sprite(Region);
		Shadow_Sprite.setOrigin(Shadow_Sprite.getWidth()/2, Shadow_Sprite.getHeight()/2);
	}
	
	public float Get_Height()
	{
		return Plane_Sprite.getHeight();
	}
	
	public float Get_Width()
	{
		return Plane_Sprite.getWidth();
	}
	
	public Vector2 Get_Position()
	{
		return new Vector2(Plane_Sprite.getX() + Centre.x, Plane_Sprite.getY() + Centre.y);
	}
	
	public void Translate_X(float Multiply)
	{
		Plane_Sprite.translate(Multiply * Move_Style.Speed * Gdx.graphics.getDeltaTime() , 0);
		//Shadow_Sprite.translate(Multiply * Move_Speed * Gdx.graphics.getDeltaTime(), 0);
	}
	
	public void Set_Position(float X,float Y)
	{
		Plane_Sprite.setPosition(X - Centre.x, Y - Centre.y);
	    //if (Shadow_Sprite != null) Shadow_Sprite.setPosition(X - Centre.x + Shadow_Translate.x, Y - Centre.y + Shadow_Translate.y);
	}
	
	public void Update_Transition(Transition T)
	{
		Plane_Sprite.setColor(1, 1-T.Current_Fadiness, 1-T.Current_Fadiness, 1);
	}
	
	public void Hurt(float Attack)
	{
		Plane_Transition.Play_Transition();
	    if (Burst_On == true) Burst_Animation.Manager.Perform_Burst(new Burst(Burst_Animation, Get_Position()));
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
		New_Bullet = new Bullet(Bullet_Regions, 0, Angle_Shift,false, Attack_Points);
		New_Bullet.Set_Position(Get_Position().x + Shooting.get(I).Offset_X, Get_Position().y - Shooting.get(I).Offset_Y);
		if (Shooting.get(I).Shoot_To_Plane == false) {
		if (Flipped_Y == false) 
			New_Bullet.Set_Movement(Shooting.get(I).Speed, Shooting.get(I).Direction + Get_Rotation_Radians());
		else
			New_Bullet.Set_Movement(Shooting.get(I).Speed, Shooting.get(I).Direction + 3.1416f + Get_Rotation_Radians());
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
		
		float X = Plane_Sprite.getX();
		float Y = Plane_Sprite.getY();
		float B_W = Plane_Sprite.getWidth();
		float B_H = Plane_Sprite.getHeight();
		
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
		if (Shadow_On == true)
		{
		    if (Shadow_Sprite != null) Shadow_Sprite.setPosition(Plane_Sprite.getX() + Shadow_Translate.x , Plane_Sprite.getY() - Shadow_Translate.y);
			Shadow_Sprite.setRegion(Shadow_Region);
			
			if (Flipped_Y) Shadow_Sprite.flip(false, true);
			Shadow_Sprite.draw(B);
		}
		
		Plane_Sprite.setRegion(Plane_Region);
		if (Flipped_Y) Plane_Sprite.flip(false, true);
		Plane_Sprite.draw(B);
	}
	
	public void Add_Shooting_Position(float x, float y, float speed, float Direction,Plane Shoot_To)
	{
		Shooting_Style S = new Shooting_Style(x,y,speed,Direction);
		if (Shoot_To != null) S.Set_Shoot_To_Plane(Shoot_To);
		Add_Shooting_Position(S);
		
	}
	
	public void Flip_Y()
	{
		Flipped_Y = !Flipped_Y;
	}
	
}
