package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
	public Sprite Bullet_Sprite;
	public TextureRegion[] Bullet_Regions;
	public Vector2 Centre;
	public float Animation_Speed = 0.1f;
	
	private int Default_Frame;
	private int Frame_Now;
	private float Last_Time;
	
	public float Move_Speed = 0;
	public float Direction = 0;
	public float Angle_Shift;
	
	public boolean Owner_User;
	
	public float Attack;
	
	public Bullet(TextureRegion[] Regions,int Current_Frame, float Ang_Shift, boolean From_User, float Attack_Point)
	{
		Bullet_Regions = Regions;
		Default_Frame = 0;
		if (Current_Frame < Bullet_Regions.length)
			Default_Frame = Current_Frame;
		if (Current_Frame < 0)
			Default_Frame = 0;
		Bullet_Sprite = new Sprite(Regions[Current_Frame]);
		Centre = new Vector2(Regions[Current_Frame].getRegionWidth() / 2, Regions[Current_Frame].getRegionHeight() / 2);
		Set_Position(0, 0);
		Frame_Now = Current_Frame;
		Last_Time = Gdx.graphics.getDeltaTime();
		Bullet_Sprite.setOrigin(Bullet_Sprite.getWidth()/2, Bullet_Sprite.getHeight()/2);
		Angle_Shift = Ang_Shift;
		Owner_User = From_User;
		Attack = Attack_Point;
	}
	
	public void Set_Position(float X,float Y)
	{
		Bullet_Sprite.setPosition(X - Centre.x, Y - Centre.y);
	    //if (Shadow_Sprite != null) Shadow_Sprite.setPosition(X - Centre.x + Shadow_Translate.x, Y - Centre.y + Shadow_Translate.y);
	}
	
	public Vector2 Get_Position()
	{
		return new Vector2(Bullet_Sprite.getX() + Centre.x, Bullet_Sprite.getY() + Centre.y);
	}
	
	public void Set_Movement(float Speed, Plane To)
	{
		Move_Speed = Speed;
		Vector2 Plane_Pos = To.Get_Position();
		Vector2 Dir = Plane_Pos.sub(Get_Position());
		float Ang = Dir.angleRad();
		Direction = Ang;
		Bullet_Sprite.setRotation((Direction + Angle_Shift) * MathUtils.radiansToDegrees);
	}
	
	public void Set_Movement(float Speed, float Dir)
	{
		Move_Speed = Speed;
		Direction = Dir;
		Bullet_Sprite.setRotation((Direction + Angle_Shift) * MathUtils.radiansToDegrees);
	}
	
	public void Update_Position()
	{
		float X = Move_Speed * Gdx.graphics.getDeltaTime() * MathUtils.cos(Direction);
		float Y = Move_Speed * Gdx.graphics.getDeltaTime() * MathUtils.sin(Direction);
		Bullet_Sprite.translate(X, Y);
	}
	
	public void Update_Animation()
	{
		Last_Time = Last_Time + Gdx.graphics.getDeltaTime();
		int Frames = (int)(Last_Time / Animation_Speed);
		Last_Time = Last_Time % Animation_Speed;
		
		Frame_Now = Frame_Now + Frames;
		Frame_Now = Frame_Now % Bullet_Regions.length;
	}
	
	public void Update()
	{
		Update_Position();
		Update_Animation();
	}
	
	public boolean Is_Out_Of_Screen(Camera C)
	{
		int W = Gdx.graphics.getWidth();
		int H = Gdx.graphics.getHeight();
		
		float X = Bullet_Sprite.getX();
		float Y = Bullet_Sprite.getY();
		float B_W = Bullet_Sprite.getWidth();
		float B_H = Bullet_Sprite.getHeight();
		
		Vector3 Bullet_Pos = new Vector3(X,Y, 0);
		
		C.project(Bullet_Pos);
		
		if (Bullet_Pos.x > W) return true;
		if (Bullet_Pos.x + B_W < 0) return true;
		if (Bullet_Pos.y > H) return true;
		if (Bullet_Pos.y + B_H < 0) return true;
		
		return false;
	}
	
	public void Draw(SpriteBatch B)
	{
		Bullet_Sprite.setRegion(Bullet_Regions[Frame_Now]);
		Bullet_Sprite.draw(B);
	}
}
