package com.skyfire.brustyourenemy;

import com.badlogic.gdx.graphics.Color;

public class Transition {
	public float Fade_Speed;
	public int Fade_Counts;
	public Plane Related_Obj_Plane;
	public Enemy_Plane Related_Obj_Enemy_Plane;
	public Enemy_Ground Related_Obj_Enemy_Ground;
	public Transition_Object_Type Related_Obj_Type;
	public float Current_Fadiness;
	public float Current_Fadiness_Count;
	public Boolean Update_Transition;
	public Boolean On_Back_Transition;
	public Transition(Plane Plane_Obj)
	{
		Fade_Speed = 5f;
		Fade_Counts = 2;
		Related_Obj_Plane = Plane_Obj;
		Related_Obj_Enemy_Plane = null;
		Related_Obj_Enemy_Ground = null;
		Current_Fadiness = 0;
		Current_Fadiness_Count = 0;
		Update_Transition = false;
		On_Back_Transition = false;
		Related_Obj_Type = Transition_Object_Type.Plane;
	}
	public Transition(Enemy_Plane Plane_Obj)
	{
		Fade_Speed = 5f;
		Fade_Counts = 2;
		Related_Obj_Enemy_Plane = Plane_Obj;
		Related_Obj_Plane = null;
		Related_Obj_Enemy_Ground = null;
		Current_Fadiness = 0;
		Current_Fadiness_Count = 0;
		Update_Transition = false;
		On_Back_Transition = false;
		Related_Obj_Type = Transition_Object_Type.Enemy_Plane;
	}
	public Transition(Enemy_Ground Ground_Obj)
	{
		Fade_Speed = 5f;
		Fade_Counts = 2;
		Related_Obj_Enemy_Ground = Ground_Obj;
		Related_Obj_Plane = null;
		Related_Obj_Enemy_Plane = null;
		Current_Fadiness = 0;
		Current_Fadiness_Count = 0;
		Update_Transition = false;
		On_Back_Transition = false;
		Related_Obj_Type = Transition_Object_Type.Enemy_Ground;
	}
	public void Dispose()
	{
		Current_Fadiness = 0;
		Update_Transitions();
	}
	
	public void Update_Transitions()
	{
		if (Related_Obj_Type == Transition_Object_Type.Plane)
			Related_Obj_Plane.Update_Transition(this);
		else if (Related_Obj_Type == Transition_Object_Type.Enemy_Plane)
			Related_Obj_Enemy_Plane.Update_Transition(this);
		else if (Related_Obj_Type == Transition_Object_Type.Enemy_Ground)
			Related_Obj_Enemy_Ground.Update_Transition(this);
	}
	
	public Boolean Has_Object_Attached()
	{
		if (Related_Obj_Type == Transition_Object_Type.Plane)
		{
			if (Related_Obj_Plane != null) return true;
		}
		if (Related_Obj_Type == Transition_Object_Type.Enemy_Plane)
		{
			if (Related_Obj_Enemy_Plane != null) return true;
		}
		if (Related_Obj_Type == Transition_Object_Type.Enemy_Ground)
		{
			if (Related_Obj_Enemy_Ground != null) return true;
		}
		return false;
	}
	
	public void Play_Transition()
	{
		if (Update_Transition == false)
		{
			if (Current_Fadiness_Count == 0)
			{
				Current_Fadiness = 0;
				Current_Fadiness_Count = 0;
				Update_Transition = true;
				On_Back_Transition = false;
				Update_Transitions();
				return;
			}
		}
		if (Current_Fadiness_Count >= Fade_Counts) {
			Current_Fadiness = 0;
			Current_Fadiness_Count = 0;
			Update_Transition = true;
			On_Back_Transition = false;
			Update_Transitions();
		}
	}
}
