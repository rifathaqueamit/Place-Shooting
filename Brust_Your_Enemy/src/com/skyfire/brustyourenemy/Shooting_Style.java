package com.skyfire.brustyourenemy;

public class Shooting_Style {
	public float Offset_X;
	public float Offset_Y;
	public float Speed;
	public float Direction;
	public Plane Shoot_To;
	public boolean Shoot_To_Plane;
	public Shooting_Style(float O_X, float O_Y, float S, float D)
	{
		Offset_X = O_X;
		Offset_Y = O_Y;
		Speed = S;
		Direction = D;
	}
	public void Set_Shoot_To_Plane(Plane P)
	{
		Shoot_To_Plane = true;
		Shoot_To = P;
	}
}
