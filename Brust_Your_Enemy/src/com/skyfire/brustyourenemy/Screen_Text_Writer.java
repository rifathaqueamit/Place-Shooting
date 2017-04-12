package com.skyfire.brustyourenemy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Screen_Text_Writer {
	public BitmapFont Current_Font;
	public int Current_Line;
	public float Text_Height;
	public SpriteBatch Batch;
	public Screen_Text_Writer(SpriteBatch B, Color Font_Color, Vector2 Scale)
	{
		Current_Font = new BitmapFont();
		Current_Font.setColor(Font_Color);
		Current_Font.setScale(Scale.x, Scale.y);
		Current_Line = 0;
		Text_Height = Current_Font.getCapHeight();
		Batch = B;
	}
	
	public void Dispose()
	{
		Current_Font.dispose();
	}
	
	public void Draw_Line(String Text, Camera C)
	{
		float Y_Value = Current_Line * Text_Height + 5;
		Current_Font.draw(Batch, Text, C.position.x - C.viewportWidth/2, C.position.y + C.viewportHeight/2 - Y_Value);
		Current_Line++;
	}
	
	public void Clear()
	{
		Current_Line = 0;
	}

}
