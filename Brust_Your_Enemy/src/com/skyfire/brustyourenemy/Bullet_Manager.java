package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet_Manager {
	public ArrayList<Bullet> All_Bullets;
	
	public Bullet_Manager()
	{
		All_Bullets = new ArrayList<Bullet>();
	}
	
	public void Add_Bullet(Bullet B)
	{
		All_Bullets.add(B);
	}
	
	public void Update_Bullets(Camera C)
	{
		int I;
		for (I = 0; I < All_Bullets.size(); I++)
		{
			All_Bullets.get(I).Update();
			if (All_Bullets.get(I).Is_Out_Of_Screen(C) == true)
			{
				All_Bullets.remove(I);
			}
		}
	}
	
	public Bullet_Collision_Result Check_Collision(Plane P)
	{
		int I;
		float Attack;
		for (I = 0; I < All_Bullets.size(); I++)
		{
			if (All_Bullets.get(I).Owner_User == false) {
			if (All_Bullets.get(I).Bullet_Sprite.getBoundingRectangle().overlaps(P.Plane_Sprite.getBoundingRectangle()) == true)
			{
				Attack = All_Bullets.get(I).Attack;
				All_Bullets.remove(I);
				return new Bullet_Collision_Result(true, Attack);
			}
			}
		}
		return new Bullet_Collision_Result(false, 0);
	}
	
	public Bullet_Collision_Result Check_Collision(Enemy_Plane P)
	{
		int I;
		float Attack;
		for (I = 0; I < All_Bullets.size(); I++)
		{
			if (All_Bullets.get(I).Owner_User == true) {
			if (All_Bullets.get(I).Bullet_Sprite.getBoundingRectangle().overlaps(P.Plane_Sprite.getBoundingRectangle()) == true)
			{
				Attack = All_Bullets.get(I).Attack;
				All_Bullets.remove(I);
				return new Bullet_Collision_Result(true, Attack);
			}
			}
		}
		return new Bullet_Collision_Result(false, 0);
	}
	
	public Bullet_Collision_Result Check_Collision(Enemy_Ground P)
	{
		int I;
		float Attack;
		for (I = 0; I < All_Bullets.size(); I++)
		{
			if (All_Bullets.get(I).Owner_User == true) {
			if (All_Bullets.get(I).Bullet_Sprite.getBoundingRectangle().overlaps(P.Enemy_Sprite.getBoundingRectangle()) == true)
			{
				Attack = All_Bullets.get(I).Attack;
				All_Bullets.remove(I);
				return new Bullet_Collision_Result(true, Attack);
			}
			}
		}
		return new Bullet_Collision_Result(false, 0);
	}
	
	public void Draw_Bullets(SpriteBatch B)
	{
		int I;
		for (I = 0; I < All_Bullets.size(); I++)
		{
			All_Bullets.get(I).Draw(B);
		}
	}
}
