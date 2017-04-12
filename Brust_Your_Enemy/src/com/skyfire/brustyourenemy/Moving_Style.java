package com.skyfire.brustyourenemy;

import com.badlogic.gdx.math.Vector2;


public class Moving_Style {
	public Move_Types Type;
	public Vector2 Direction;
	public float Speed;
	
	public float Increase_Angle;
	
	public Moving_Style(boolean Move_Up, float Move_Speed)
	{
		if (Move_Up == true) Direction = new Vector2(0,1); else Direction = new Vector2(0,-1);
		Speed=  Move_Speed;
		Increase_Angle = 0;
	}
	
	public void Increase_Angle_Left(float Ammount)
	{
		Increase_Angle = -Ammount;
	}
	
	public void Increase_Angle_Right(float Ammount)
	{
		Increase_Angle = Ammount;
	}
	
}
