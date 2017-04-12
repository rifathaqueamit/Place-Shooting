package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sun.webkit.ContextMenu.ShowContext;

public class Plane {
	public TextureRegion[] All_Regions;
	public TextureRegion[] Shadow_Regions;
	public boolean Shadow_On = false;
	public Sprite Plane_Sprite;
	public Sprite Shadow_Sprite;
	public Vector2 Shadow_Translate = new Vector2(20,15);
	public Vector2 Centre;
	public float Move_Speed = 5;
	
	private int Default_Frame;
	private int Frame_Now;
	private int Left_Start;
	private int Left_End;
	
	private int Right_Start;
	private int Right_End;
	
	public boolean Continuous_Animation = true;
	public float Animation_Speed = 0.1f; //10 frame in 1 seconds
	
	private float Last_Time = 0;
	private int State = 0;
	
	public ArrayList<Shooting_Style> Shooting;
	
	public Transition Plane_Transition;
	
	public Burst Burst_Animation;
	
	public Boolean Burst_On;
	
	public float Attack;
	public float Hit;
	
	public boolean Perform_Deletion = false;
	
	public Plane(TextureRegion[] Regions, int Current_Frame, Transitions_Manager T, Burst B, float Attack_Points, float Hit_Points)
	{
		All_Regions = Regions;
		Default_Frame = 0;
		if (Current_Frame < All_Regions.length)
			Default_Frame = Current_Frame;
		if (Current_Frame < 0)
			Default_Frame = 0;
		Plane_Sprite = new Sprite(Regions[Current_Frame]);
		Centre = new Vector2(Regions[Current_Frame].getRegionWidth() / 2, Regions[Current_Frame].getRegionHeight() / 2);
		Set_Position(0, 0);
		Left_Start = Left_End = Right_Start = Right_End = Current_Frame;
		Frame_Now = Current_Frame;
		Last_Time = Gdx.graphics.getDeltaTime();
		Shooting = new ArrayList<Shooting_Style>();
		Plane_Transition = T.Transition_3_Times_White_Fade(this);
		Burst_Animation = B;
		if (B==null) Burst_On = false; else Burst_On = true;
		Attack = Attack_Points;
		Hit = Hit_Points;
	}
	
	public void Add_Shooting_Position(Shooting_Style S)
	{
		Shooting.add(S);
	}
	
	public void Set_Shadow_Regions(TextureRegion[] Regions)
	{
		Shadow_Regions = Regions;
		Shadow_On = true;
		Shadow_Sprite = new Sprite(Regions[Default_Frame]);
	}
	
	public void Set_Left_Animation(int Start, int End)
	{
		Left_Start = Start;
		Left_End = End;
	}
	
	public void Set_Right_Animation(int Start, int End)
	{
		Right_Start = Start;
		Right_End = End;
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
		Plane_Sprite.translate(Multiply * Move_Speed * Gdx.graphics.getDeltaTime() , 0);
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
	
	public void Hurt(float Attack_Points)
	{
		Plane_Transition.Play_Transition();
	    if (Burst_On == true)	Burst_Animation.Manager.Perform_Burst(new Burst(Burst_Animation, Get_Position()));
	    Hit = Hit - Attack_Points;
	    if (Hit < 0)
	    	Perform_Deletion = true;
	}
	
	public void Update_Animation()
	{
		Last_Time = Last_Time + Gdx.graphics.getDeltaTime();
		
		int Frames = (int)(Last_Time / Animation_Speed);
		
		Last_Time = Last_Time % Animation_Speed;
		
		if (Continuous_Animation == true) {
			
		if (State == 0)
		{
			if (Frame_Now > Default_Frame)
			{
				Frame_Now = Frame_Now - Frames;
				if (Frame_Now < Default_Frame) Frame_Now = Default_Frame;
			}
			else if (Frame_Now < Default_Frame)
			{
				Frame_Now = Frame_Now + Frames;
				if (Frame_Now > Default_Frame) Frame_Now = Default_Frame;
			}
			else
			{
				Frame_Now = Default_Frame;
			}
		}
		else if (State == 1)
		{
			if (Frame_Now > Left_Start)
			{
				Frame_Now = Frame_Now - Frames;
				if (Frame_Now < Left_Start) Frame_Now = Left_Start;
			}
		}
		else
		{
			if (Frame_Now < Right_End)
			{
				Frame_Now = Frame_Now + Frames;
				if (Frame_Now > Right_End) Frame_Now = Right_End;
			}
		}
		
		}
	}
	
	public void Animation_Left()
	{
		State = 1;
	}
	
	public void Animation_Right()
	{
		State = 2;
	}
	
	public void Animation_Stay()
	{
		State = 0;
	}
	
	public void Shoot(Bullet_Manager B_M, TextureRegion[] Bullet_Regions, float Angle_Shift)
	{
		int I;
		for (I = 0; I < Shooting.size(); I++) 
		{
		Bullet New_Bullet;
		New_Bullet = new Bullet(Bullet_Regions, 0, Angle_Shift,true, Attack);
		New_Bullet.Set_Position(Get_Position().x + Shooting.get(I).Offset_X, Get_Position().y - Shooting.get(I).Offset_Y);
		New_Bullet.Set_Movement(Shooting.get(I).Speed, Shooting.get(I).Direction);
		B_M.Add_Bullet(New_Bullet);
		}
	}
	
	public void Draw(SpriteBatch B)
	{
		if (Shadow_On == true)
		{
		    if (Shadow_Sprite != null) Shadow_Sprite.setPosition(Plane_Sprite.getX() + Shadow_Translate.x , Plane_Sprite.getY() - Shadow_Translate.y);
			Shadow_Sprite.setRegion(Shadow_Regions[Frame_Now]);
			Shadow_Sprite.draw(B);
		}
		
		Plane_Sprite.setRegion(All_Regions[Frame_Now]);
		Plane_Sprite.draw(B);
	}
	
}
