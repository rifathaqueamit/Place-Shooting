package com.skyfire.brustyourenemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Texture_Manager {
	public TextureRegion[] Get_Regions(Texture Main_Texture, int X,int Y,int Width, int Height, int Num_Rows,int Num_Cols)
	{
		//Our texture
		TextureRegion Region = new TextureRegion(Main_Texture,X,Y,Width,Height);
		//Sub divide it to some more regions
		TextureRegion[][] Regions_Array = Region.split(Region.getRegionWidth() / Num_Cols, Region.getRegionHeight() / Num_Rows);
		//Put them in a row
		TextureRegion[] Regions_Final = new TextureRegion[Num_Cols * Num_Rows];
		int Index = 0;
		int I, J;
		for (I = 0; I < Num_Rows; I++)
		{
			for (J = 0; J < Num_Cols; J++)
			{
				Regions_Final[Index++] = Regions_Array[I][J];
			}
		}
		return Regions_Final;
	}
}
