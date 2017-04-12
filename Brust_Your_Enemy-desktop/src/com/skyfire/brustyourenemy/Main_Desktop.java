package com.skyfire.brustyourenemy;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main_Desktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Brust_Your_Enemy";
		//cfg.useGL20 = true;
		cfg.width = 352;
		cfg.height = 608;
		
		new LwjglApplication(new com.skyfire.brustyourenemy.Main(), cfg);
	}
}
