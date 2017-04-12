package com.skyfire.brustyourenemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Music_Manager {
	
	public Music Current_Music;
	
	public Music_Manager()
	{
		
	}
	
	public void Dispose()
	{
		Unload_Music();
	}
	
	public void Stop_Music()
	{
		if (Current_Music != null)
			Current_Music.stop();
	}
	
	public void Unload_Music()
	{
		if (Current_Music != null)
		{
			Stop_Music();
			Current_Music.dispose();
		}
	}
	
	public void Load_Music(String File)
	{
		Unload_Music();
		Current_Music = Gdx.audio.newMusic(Gdx.files.internal(File));
	}
	
	public void Play_Music()
	{
		if (Current_Music != null)
		{
			Current_Music.play();
		}
	}
	
	public void Set_Looping(boolean Loop)
	{
		if (Current_Music != null)
		{
			Current_Music.setLooping(Loop);
		}
	}
	
}
