package com.skyfire.brustyourenemy;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class Transitions_Manager {
	public ArrayList<Transition> All_Transitions;
	
	public Transitions_Manager()
	{
		All_Transitions = new ArrayList<Transition>();
	}
	public void Dispose()
	{
		int I;
		for (I = 0; I < All_Transitions.size(); I++)
		{
			All_Transitions.get(I).Dispose();
		}
	}

	public Transition Transition_3_Times_White_Fade(Plane Plane_Obj)
	{
		Transition New_Transition = new Transition(Plane_Obj);
		All_Transitions.add(New_Transition);
		return New_Transition;
	}
	
	public Transition Transition_3_Times_White_Fade(Enemy_Plane Plane_Obj)
	{
		Transition New_Transition = new Transition(Plane_Obj);
		All_Transitions.add(New_Transition);
		return New_Transition;
	}
	
	public Transition Transition_3_Times_White_Fade(Enemy_Ground Ground_Obj)
	{
		Transition New_Transition = new Transition(Ground_Obj);
		All_Transitions.add(New_Transition);
		return New_Transition;
	}
	
	public void Update()
	{
		int I;
		for (I = 0; I < All_Transitions.size(); I++)
		{
			if (All_Transitions.get(I).Has_Object_Attached() == false)
			{
				All_Transitions.get(I).Dispose();
				All_Transitions.remove(I);
			}
			else
			{
				Transition T = All_Transitions.get(I);
				if (T.Update_Transition == true)
				{
					if (T.Current_Fadiness_Count < T.Fade_Counts)
					{
						if (T.On_Back_Transition == false)
						{
							if (T.Current_Fadiness < 1)
							{
								T.Current_Fadiness = T.Current_Fadiness + T.Fade_Speed * Gdx.graphics.getDeltaTime();
								T.Update_Transitions();
								if (T.Current_Fadiness >= 1)
								{
									T.Current_Fadiness = 1;
									T.Update_Transitions();
									T.On_Back_Transition = true;
								}
							}
							else
							{
								T.Current_Fadiness = 1;
								T.Update_Transitions();
								T.On_Back_Transition = true;
							}
						}
						else
						{
							if (T.Current_Fadiness > 0)
							{
								T.Current_Fadiness = T.Current_Fadiness - T.Fade_Speed * Gdx.graphics.getDeltaTime();
								T.Update_Transitions();
								if (T.Current_Fadiness  < 0)
								{
									T.Current_Fadiness = 0;
									T.Update_Transitions();
									T.On_Back_Transition = false;
									T.Current_Fadiness_Count = T.Current_Fadiness_Count + 1;
									if (T.Current_Fadiness_Count >= T.Fade_Counts) {
										T.Current_Fadiness_Count = T.Fade_Counts;
										T.Update_Transition = false;
									}
								
								}
							}
							else
							{
								T.Current_Fadiness = 0;
								T.Update_Transitions();
								T.On_Back_Transition = false;
								T.Current_Fadiness_Count = T.Current_Fadiness_Count + 1;
								if (T.Current_Fadiness_Count >= T.Fade_Counts) {
									T.Current_Fadiness_Count = T.Fade_Counts;
									T.Update_Transition = false;
								}
							}
						}
					}
					else
					{
						T.Current_Fadiness_Count = T.Fade_Counts;
						T.Update_Transition = false;
					}
				}
				else
				{
					
				}
				
			}
		}
	}
}
