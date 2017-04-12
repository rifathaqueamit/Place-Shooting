package com.skyfire.brustyourenemy;

import java.util.ArrayList;

public class Collision_Manager {
	public Bullet_Manager Bullet_Man;
	
	public ArrayList<Plane> All_Planes;
	public Enemy_Manager Enemy_Man;
	
	public long Score_User;
	public long Per_Bullet;
	
	public Collision_Manager(Bullet_Manager B, Enemy_Manager E)
	{
		Bullet_Man = B;
		Enemy_Man = E;
		All_Planes = new ArrayList<Plane>();
		Per_Bullet = 10;
		Score_User = 0;
	}
	
	public void Reset_Score()
	{
		Score_User = 0;
	}
	
	public void Add_Plane(Plane P)
	{
		All_Planes.add(P);
	}
	
	public void Dispose()
	{
		All_Planes.clear();
	}
	
	public void Update()
	{
		int I;
		Bullet_Collision_Result Result;
		for (I = 0; I < All_Planes.size(); I++)
		{
			Result = Bullet_Man.Check_Collision(All_Planes.get(I));
			if (Result.Collide == true)
			{
				All_Planes.get(I).Hurt(Result.Bullet_Attack);
				//System.out.println("Hurt!");
			
			}
		}
		for (I = 0; I < Enemy_Man.All_Planes.size(); I++)
		{
			Result = Bullet_Man.Check_Collision(Enemy_Man.All_Planes.get(I));
			if (Result.Collide == true)
			{
			    Enemy_Man.All_Planes.get(I).Hurt(Result.Bullet_Attack);
				//System.out.println("Hurt!");
			    Score_User = Score_User + Per_Bullet;
			}
		}
		for (I = 0; I < Enemy_Man.All_Ground_Enemies.size(); I++)
		{
			Result = Bullet_Man.Check_Collision(Enemy_Man.All_Ground_Enemies.get(I));
			if (Result.Collide == true)
			{
			    Enemy_Man.All_Ground_Enemies.get(I).Hurt(Result.Bullet_Attack);
				//System.out.println("Hurt!");
			    Score_User = Score_User + Per_Bullet;
			}
		}
	}
}
