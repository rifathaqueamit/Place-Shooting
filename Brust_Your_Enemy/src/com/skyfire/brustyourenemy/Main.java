package com.skyfire.brustyourenemy;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Peripheral;
//import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode;
import com.sun.prism.paint.Color;

//﻿

public class Main implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Texture All_Planes;
	
	private Texture_Manager Tex_Manager;
	
	private Plane Our_Plane;
	private float Our_Plane_Extra_Y;
	private float Our_Plane_Max_Y;
	private float Our_Plane_Max_Y_Speed = 100;
	
	private TextureRegion[] Bullet_Texture_Regions;
	private Bullet Our_Bullet;
	
	private Bullet_Manager Bullet_Man;
	
	float Elapsed_Time = 0;
	
	private Timer Bullet_Creator;
	private Task Bullet_Creating_Task;
	
	private Map Our_Map;
	private float Scrolling_Speed = 50;
	
	private float Screen_Left_To_Go;
	
	private Enemy_Manager Enemy_Central;
	
	private Screen_Text_Writer Text_Writer;
	
	private Transitions_Manager Transitions;
	
	private Collision_Manager Collision_Man;
	
	private Texture Burst_Texture;
	
	private Burst_Manager Burst_Man;
	
	private TextureRegion[] Burst_Regions;
	
	private Burst Type_1_Burst;
	
	private Music_Manager Music_Man;
	
	//Menu
	private Texture Background;
	private Texture New_Game;
	private Texture Exit_Game;
	private Texture You_Win;
	private Texture Game_Over;
	
	private Sprite Background_Sprite;
	private Sprite New_Game_Sprite;
	private Sprite Exit_Game_Sprite;
	private Sprite Game_Over_Sprite;
	private Sprite You_Win_Sprite;
	
	//Health Bar
	private Pixmap Health_Bar_Map;
	private Texture Health_Bar_Texture;
	private Pixmap Health_Map;
	private Texture Health_Texture;
	private Sprite Health_Bar_Sprite;
	private Sprite Health_Sprite;
	private float Max_Health;
	private float Current_Health;
	
	//Game States
	private int State; //0 Menu, 1 Game
	private boolean Game_Started;
	
	//Fixed Screen
	float w = 352;
	float h = 608;
	
	float Starting_Health = 250;
	
	@Override
	public void create() {		
		
		//Menu State
		State = 0;
		Game_Started = false;
		

		
		//Initialization
		camera = new OrthographicCamera(w,h);
		batch = new SpriteBatch();
		Tex_Manager = new Texture_Manager();
		
		//GUI
		Background = new Texture("data/Menu_1_8Bit.png");
		Background_Sprite = new Sprite(Background);
		
		New_Game = new Texture("data/Menu_New_Game.png");
		Exit_Game = new Texture("data/Menu_Exit_Game.png");
		New_Game_Sprite = new Sprite(New_Game);
		Exit_Game_Sprite = new Sprite(Exit_Game);
		New_Game_Sprite.setPosition(-New_Game_Sprite.getWidth()/2, -120);
		Exit_Game_Sprite.setPosition(-Exit_Game_Sprite.getWidth()/2, -250);
		Background_Sprite.setPosition(-w/2, - h/2); 
		Game_Over = new Texture("data/Game_Over.png");
		Game_Over_Sprite = new Sprite(Game_Over);
		You_Win = new Texture("data/You_Win.png");
		You_Win_Sprite = new Sprite(You_Win);
		
		//Text Writer
		Text_Writer = new Screen_Text_Writer(batch, com.badlogic.gdx.graphics.Color.WHITE, new Vector2(2, 2));

		//Music Manager
		Music_Man = new Music_Manager();
		Music_Man.Load_Music("data/Level_1_Music.mp3");
		Music_Man.Set_Looping(true);
		Music_Man.Play_Music();
		
		//Set Input Processor
		Gdx.input.setInputProcessor(this);
	}
	
	public void Renew_Game()
	{
		//Disposal
		//if (You_Win != null) You_Win.dispose();
		
		//if (Game_Over != null) Game_Over.dispose();
		
		//if (New_Game != null) New_Game.dispose();
		
		//if (Exit_Game != null) Exit_Game.dispose();
		
		//if (Health_Bar_Texture != null) Health_Bar_Texture.dispose();
		
		//if (Health_Texture != null) Health_Texture.dispose();
		
		//if (Music_Man != null) Music_Man.Dispose();
		
		if (Burst_Man != null) Burst_Man.Dispose();
		
		if (Collision_Man != null) Collision_Man.Dispose();
		
		if (Transitions != null) Transitions.Dispose();
		
		//if (Text_Writer != null) Text_Writer.Dispose();
		
	    if (Our_Map != null)	Our_Map.Dispose();
		
		if (Enemy_Central != null) Enemy_Central.Dispose();
		
	    if (Bullet_Creator != null)	Bullet_Creator.stop();
		
		if (All_Planes != null) All_Planes.dispose();
		
		//if (Background != null) Background.dispose();
		
		//if (batch != null ) batch.dispose();
	
		
		//Menu State
		State = 0;

		//Set Screen
		Screen_Left_To_Go = (190-19)*32; //32 per tiles, 190 tiles in height, 19 tiles in screen
		
		camera = new OrthographicCamera(w,h);
		//camera.translate(11*32/2 , 19*32 /2);
		camera.update();
		
		//Our plane
		Max_Health = Starting_Health;
		Current_Health = Starting_Health;
		
		
		Our_Plane_Extra_Y = 0;
		Our_Plane.Set_Position(11*32/2, Our_Plane.Get_Height() + Our_Plane_Extra_Y);
		
		Screen_Left_To_Go = Our_Map.Map_Total_Height-19*32;

		Game_Started = false;
		
		Music_Man.Play_Music();
		
		New_Game_Sprite.setPosition(-New_Game_Sprite.getWidth()/2, -120);
		Exit_Game_Sprite.setPosition(-Exit_Game_Sprite.getWidth()/2, -250);
		Background_Sprite.setPosition(-w/2, - h/2); 
	}
	
	public void New_Game()
	{
		//Menu State
		State = 1;

		//Set Screen
		Screen_Left_To_Go = (190-19)*32; //32 per tiles, 190 tiles in height, 19 tiles in screen
		
		camera.translate(11*32/2 , 19*32 /2);
		camera.update();
		
		//Load Textures
		//if (All_Planes == null)
		All_Planes = new Texture("data/ImgGameComm_H1.png");
		
		TextureRegion[] Plane_Regions = Tex_Manager.Get_Regions(All_Planes, 0, 620, 414, 49, 1, 9);
		TextureRegion[] Shadow_Regions = Tex_Manager.Get_Regions(All_Planes, 415,620,414,49,1,9);
		
		//Transitions
		//if (Transitions == null)
		Transitions = new Transitions_Manager();
		
		//Bursts
		//if (Burst_Texture == null)
		Burst_Texture = new Texture("data/Fire_01.png");
		//if (Burst_Man == null)
		Burst_Man = new Burst_Manager();
		
		Burst_Regions = Tex_Manager.Get_Regions(Burst_Texture,0,0,288,231,4,5 );
		
		//if (Type_1_Burst == null)
		Type_1_Burst = new Burst(Burst_Regions, 0.025f, Vector2.Zero, Burst_Man);
		
		//Our plane
		Max_Health = Starting_Health;
		Current_Health = Starting_Health;
		
		//if (Our_Plane != null) Our_Plane = null;
		Our_Plane = new Plane(Plane_Regions, 4, Transitions, null,40, Max_Health);
		
		Our_Plane_Extra_Y = 0;
		Our_Plane.Set_Position(11*32/2, Our_Plane.Get_Height() + Our_Plane_Extra_Y);
		Our_Plane.Set_Left_Animation(0, 3);
		Our_Plane.Set_Right_Animation(5, 8);
		
		Our_Plane.Set_Shadow_Regions(Shadow_Regions);
		Our_Plane.Add_Shooting_Position(new Shooting_Style(10, 0, 400, 3.1416f/2));
		Our_Plane.Add_Shooting_Position(new Shooting_Style(-10, 0, 400, 3.1416f/2));
		Our_Plane_Extra_Y = 0;
		Our_Plane_Max_Y = 18*32 - Our_Plane.Get_Height();
		
		//Bullets
		Bullet_Texture_Regions = new TextureRegion[1];
		Bullet_Texture_Regions[0] = new TextureRegion(All_Planes, 451,911,5,11);
		
		//if (Bullet_Man == null)
		Bullet_Man = new Bullet_Manager();
		
		Bullet_Creator = new Timer();
		Bullet_Creating_Task = new Task() {
			@Override
			public void run() {
				Our_Plane.Shoot(Bullet_Man, Bullet_Texture_Regions, 3.1416f/2);
				System.out.println(Bullet_Man.All_Bullets.size());
			}
		};
		Bullet_Creator.scheduleTask( Bullet_Creating_Task, 0, 0.25f);
		Bullet_Creator.start();
		
		//Map
		//if (Our_Map == null)
		Our_Map = new Map("data/Level_1_Final_3.tide", camera);
		Our_Map.Map_Width = 11*32;
		Our_Map.Map_Height = 19*32;
		Our_Map.Tile_Pixel_Width = 32;
		Our_Map.Tile_Pixel_Height = 32;
		Our_Map.Map_Total_Width = 11*32;
		Our_Map.Map_Total_Height = 190*32;
		
		Screen_Left_To_Go = Our_Map.Map_Total_Height-19*32;

		//Enemies
		TextureRegion[] Enemy_Regions_1 = Tex_Manager.Get_Regions(All_Planes, 337, 717, 308, 43, 1, 7);
		TextureRegion[] Enemy_Regions_Shadows_1 = Tex_Manager.Get_Regions(All_Planes,646 ,717 ,308 ,43, 1, 7);
		
		TextureRegion[] Enemy_Regions_2 = Tex_Manager.Get_Regions(All_Planes,0,761,308, 43, 1, 7);
		TextureRegion[] Enemy_Regions_Shadows_2 = Tex_Manager.Get_Regions(All_Planes,309,761,308,43, 1, 7);
		
		TextureRegion[] Enemy_Regions_3 = Tex_Manager.Get_Regions(All_Planes,618,670,336,43, 1, 7);
		TextureRegion[] Enemy_Regions_Shadows_3 = Tex_Manager.Get_Regions(All_Planes,0,717,336, 43, 1, 7);
		//"0" y="520" width="697" height="49"
		//x="0" y="570" width="697" height="49"
		TextureRegion[] Enemy_Regions_4 = Tex_Manager.Get_Regions(All_Planes, 0,469, 350,50, 1, 7);
		TextureRegion[] Enemy_Regions_Shadows_4 = Tex_Manager.Get_Regions(All_Planes,351,469,350,50, 1,7);
		
		//if (Enemy_Central != null) Enemy_Central.Dispose();
		Enemy_Central = new Enemy_Manager(Bullet_Man, Transitions);
		Enemy_Central.Set_Burst(Type_1_Burst, Type_1_Burst);
		
		
		//Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*2, 17*32), null,100,5);
		//Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 - 32*2, 18*32), null,100,5);
		
		//1st Wave
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*2, 23*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*3, 22*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*4, 21*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 - 32*4, 21*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 - 32*3, 22*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 - 32*2, 23*32), null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*1, 26*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*3, 27*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*5, 28*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*7, 29*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*10, 30*32), null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*1, 30*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*3, 31*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*5, 32*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*7, 31*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*10, 30*32), null,100,5);
		
		//Enemy_Central.All_Planes.get(0).Move_Style.Increase_Angle_Right(1f);
		//Enemy_Central.All_Planes.get(1).Move_Style.Increase_Angle_Left(1f);
		
		//2nd Wave
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(32*2, (18+19)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 32*2, (18+19)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5.5f*32, (18+19)*32),null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(32*1, (18+19+1)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 32*1, (18+19+1)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5.5f*32, (18+19+1)*32),null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(32*2, (18+19+3)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(32*4, (18+19+3)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(32*6, (18+19+3)*32),null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 32*2, (18+19+1+6)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 32*4, (18+19+1+6)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 32*6, (18+19+1+6)*32),null,100,5);
		
		//3rd Wave
		//One another type plane
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5.5f*32, (18+19+19)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(2*32, (18+19+19)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 2*32, (18+19+19)*32), Our_Plane,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5.5f*32, (18+19+19+4)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Move_Style.Increase_Angle_Left(0.1f);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(2*32, (18+19+19+6)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Move_Style.Increase_Angle_Left(0.1f);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 2*32, (18+19+19+8)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Move_Style.Increase_Angle_Left(0.1f);
		
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5.5f*32, (18+19+19+8)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Move_Style.Increase_Angle_Right(0.1f);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(2*32, (18+19+19+4)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Move_Style.Increase_Angle_Right(0.1f);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 2*32, (18+19+19+6)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Move_Style.Increase_Angle_Right(0.1f);
		
		//Speedy fighter
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(5.5f*32, (18+19+19+19)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(32*2, (18+19+19+19+1)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(32*3, (18+19+19+19+2)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(11*32- 32*2, (18+19+19+19+3)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(11*32 - 32*3, (18+19+19+19+4)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(32*1, (18+19+19+19)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(32*3, (18+19+19+19+1)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(32*5, (18+19+19+19+2)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(11*32- 32*1, (18+19+19+19+3)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(11*32 - 32*3, (18+19+19+19+4)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		Enemy_Central.Add_Plane(Enemy_Regions_3[3], Enemy_Regions_Shadows_3[3], Bullet_Texture_Regions,3.1416f/2, 150, new Vector2(11*32 - 32*5, (18+19+19+19+5)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Shooting.clear();
		
		
		//4th wave
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*2, (18+19+19+19+5+10)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*3, (18+19+19+19+5+9)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*4, (18+19+19+19+5+8)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*5, (18+19+19+19+5+9)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(32*6, (18+19+19+19+5+10)*32), null,100,5);
	
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32- 32*2, (18+19+19+19+5+19)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 -32*3, (18+19+19+19+5+18)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 -32*4, (18+19+19+19+5+17)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 -32*5, (18+19+19+19+5+18)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(11*32 - 32*6, (18+19+19+19+5+19)*32), null,100,5);
	
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(5*32- 32*2, (18+19+19+19+5+19+7)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(5*32 -32, (18+19+19+19+5+18+6)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(5*32, (18+19+19+19+5+17+5)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(5*32 +32, (18+19+19+19+5+18+6)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 125, new Vector2(5*32 + 32*2, (18+19+19+19+5+19+7)*32), null,100,5);
	
		//5th Wave
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(2*32, (18+19+19+19+5+19+14)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(4*32, (18+19+19+19+5+19+14)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(6*32, (18+19+19+19+5+19+14)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(8*32, (18+19+19+19+5+19+14)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32, (18+19+19+19+5+19+17)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 2*32, (18+19+19+19+5+19+17)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 4*32, (18+19+19+19+5+19+17)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 6*32, (18+19+19+19+5+19+17)*32), Our_Plane,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_2[3], Enemy_Regions_Shadows_2[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 8*32, (18+19+19+19+5+19+17)*32), Our_Plane,100,5);
		
		//6th Wave
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(2*32, (18+19+19+19+5+19+19+7)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(4*32, (18+19+19+19+5+19+19+6)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(6*32, (18+19+19+19+5+19+19+8)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32, (18+19+19+19+5+19+19+12)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 2*32, (18+19+19+19+5+19+19+14)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 4*32, (18+19+19+19+5+19+19+16)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 6*32, (18+19+19+19+5+19+19+15)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(11*32 - 8*32, (18+19+19+19+5+19+19+16)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32, (18+19+19+19+5+19+19+19)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		
		//7th Wave
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32-64, (18+19+19+19+5+19+19+7+7)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, Our_Plane);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32, (18+19+19+19+5+19+19+6+7)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, Our_Plane);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32+32, (18+19+19+19+5+19+19+8+7)*32), Our_Plane,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, Our_Plane);
		
		//8th Wave
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*2, (18+19+19+19+5+19+19+14+8)*32), null,200,15);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*3, (18+19+19+19+5+19+19+14+8)*32), null,200,15);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*4, (18+19+19+19+5+19+19+14+8)*32), null,200,15);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(11*32 - 32*4, (18+19+19+19+5+19+19+14+8)*32), null,200,15);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(11*32 - 32*3, (18+19+19+19+5+19+19+14+8)*32), null,200,15);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(11*32 - 32*2, (18+19+19+19+5+19+19+14+8)*32), null,200,15);
		
		//9th Wave
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 - 32*4, (18+19+19+19+5+19+19+14+19)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 - 32*3, (18+19+19+19+5+19+19+14+18)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 - 32*2, (18+19+19+19+5+19+19+14+17)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 - 32*1, (18+19+19+19+5+19+19+14+16)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5, (18+19+19+19+5+19+19+14+15)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 + 32*1, (18+19+19+19+5+19+19+14+16)*32), null,200,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 + 32*2, (18+19+19+19+5+19+19+14+17)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 + 32*3, (18+19+19+19+5+19+19+14+18)*32), null,200,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5 + 32*4, (18+19+19+19+5+19+19+14+19)*32), null,200,5);

		//10th Wave
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*2, (18+19+19+19+5+19+19+14+19+10)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*3, (18+19+19+19+5+19+19+14+19+11)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*4, (18+19+19+19+5+19+19+14+19+12)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(11*32 - 32*4, (18+19+19+19+5+19+19+14+19+12)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(11*32 - 32*3, (18+19+19+19+5+19+19+14+19+11)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(11*32 - 32*2, (18+19+19+19+5+19+19+14+19+10)*32), null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*1, (18+19+19+19+5+19+19+14+19+16)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*3, (18+19+19+19+5+19+19+14+19+17)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5, (18+19+19+19+5+19+19+14+19+18)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*7, (18+19+19+19+5+19+19+14+19+17)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*10, (18+19+19+19+5+19+19+14+19+16)*32), null,100,5);
		
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*1, (18+19+19+19+5+19+19+14+19+19+5)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*3, (18+19+19+19+5+19+19+14+19+19+6)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*5, (18+19+19+19+5+19+19+14+19+7)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*7, (18+19+19+19+5+19+19+14+19+6)*32), null,100,5);
		Enemy_Central.Add_Plane(Enemy_Regions_1[3], Enemy_Regions_Shadows_1[3], Bullet_Texture_Regions,3.1416f/2, 225, new Vector2(32*10, (18+19+19+19+5+19+19+14+19+5)*32), null,100,5);
		
		//11th Wave
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32-96, (18+19+19+19+5+19+19+19+19+15)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32-64, (18+19+19+19+5+19+19+19+19+14)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32, (18+19+19+19+5+19+19+19+19+13)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32+32, (18+19+19+19+5+19+19+19+19+14)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		Enemy_Central.Add_Plane(Enemy_Regions_4[3], Enemy_Regions_Shadows_4[3], Bullet_Texture_Regions,3.1416f/2, 25, new Vector2(5*32+96, (18+19+19+19+5+19+19+19+19+15)*32), null,100,5);
		Enemy_Central.All_Planes.get(Enemy_Central.All_Planes.size()-1).Add_Shooting_Position(0,-2, 300, 3.1416f/2, null);
		
		//TextureRegion Tank = Tex_Manager.Get_Regions(All_Planes,702,469,30,50, 1, 1)[0];
		//Enemy_Central.Add_Ground_Enemy(Tank, Bullet_Texture_Regions, 3.1416f/2, new Vector2(11*32-32, 10*32), Our_Plane,100,5);
		
		//Collision
		//if (Collision_Man != null) Collision_Man.Dispose();
		Collision_Man = new Collision_Manager(Bullet_Man, Enemy_Central);
		Collision_Man.Add_Plane(Our_Plane);
		
		//Health
		//if (Health_Bar_Map == null)
		Health_Bar_Map = new Pixmap(100,25,Pixmap.Format.RGBA8888);
		Health_Bar_Map.setColor(com.badlogic.gdx.graphics.Color.DARK_GRAY);
		
		int I;
		for (I = 0; I < 5; I++)
		Health_Bar_Map.drawRectangle(I,I, 100-I*2, 25-I*2);
		
		//if (Health_Bar_Texture == null)
		Health_Bar_Texture = new Texture(Health_Bar_Map);
		Health_Bar_Map.dispose();
		
		//if (Health_Map == null)
		Health_Map = new Pixmap(1,20,Pixmap.Format.RGBA8888);
		Health_Map.setColor(com.badlogic.gdx.graphics.Color.GREEN);
		Health_Map.fill();
		
		//if (Health_Texture == null)
		Health_Texture = new Texture(Health_Map);
		Health_Map.dispose();
		
		Health_Bar_Sprite = null;
		Health_Sprite = null;
		
		Health_Bar_Sprite = new Sprite(Health_Bar_Texture);
		Health_Sprite = new Sprite(Health_Texture);
				
	}

	@Override
	public void dispose() {	
		//Dispose all
		if (You_Win != null) You_Win.dispose();
		
		if (Game_Over != null) Game_Over.dispose();
		
		if (New_Game != null) New_Game.dispose();
		
		if (Exit_Game != null) Exit_Game.dispose();
		
		if (Health_Bar_Texture != null) Health_Bar_Texture.dispose();
		
		if (Health_Texture != null) Health_Texture.dispose();
		
		if (Music_Man != null) Music_Man.Dispose();
		
		if (Burst_Man != null) Burst_Man.Dispose();
		
		if (Collision_Man != null) Collision_Man.Dispose();
		
		if (Transitions != null) Transitions.Dispose();
		
		if (Text_Writer != null) Text_Writer.Dispose();
		
	    if (Our_Map != null)	Our_Map.Dispose();
		
		if (Enemy_Central != null) Enemy_Central.Dispose();
		
	    if (Bullet_Creator != null)	Bullet_Creator.stop();
		
		if (All_Planes != null) All_Planes.dispose();
		
		if (Background != null) Background.dispose();
		
		if (batch != null ) batch.dispose();
	
	}

	public void Process_Inputs()
	{
		if (State == 1) {
		if (Gdx.input.isTouched(0) == true)
		{
			//Our_Plane.Hurt();
			//Check whether the point is on the left, or on the right
			int Mouse_X = Gdx.input.getX(0);
			int Mouse_Y = Gdx.input.getY(0);
			Vector3 Mouse_Pos = new Vector3(Mouse_X, Mouse_Y, 0);
			camera.unproject(Mouse_Pos);
			
			//X Axis
			Vector2 Plane_Positon = Our_Plane.Get_Position();
			float Distance = 1;
			if (Plane_Positon.x + Our_Plane.Get_Width() / 2 < Mouse_Pos.x)
			{
				if (Plane_Positon.x + Our_Plane.Get_Width() /2 < Our_Map.Map_Width) {
				//Move right
				Our_Plane.Animation_Right();
				Distance = Mouse_Pos.x - Plane_Positon.x;
				Our_Plane.Translate_X(Distance);
				}
				else
					Our_Plane.Animation_Stay();
			}
			else if (Plane_Positon.x - Our_Plane.Get_Width()/ 2 > Mouse_Pos.x)
			{
				//Move left
				if (Plane_Positon.x - Our_Plane.Get_Width() /2 > 0) {
				Our_Plane.Animation_Left();
				Distance = Mouse_Pos.x - Plane_Positon.x;
				Our_Plane.Translate_X(Distance);
				}
				else
					Our_Plane.Animation_Stay();
			}
			else
			{
				//Stay
				Our_Plane.Animation_Stay();
			}
			
			//Y Axis
			if (Plane_Positon.y + Our_Plane.Get_Height() / 2 < Mouse_Pos.y)
			{
				//Move up
				Distance = Mouse_Pos.y - Plane_Positon.y;
				if (Distance > Our_Plane_Max_Y_Speed) Distance = Our_Plane_Max_Y_Speed;
				Our_Plane_Extra_Y = Our_Plane_Extra_Y + Distance * 2 * Gdx.graphics.getDeltaTime();
				if (Our_Plane_Extra_Y >= Our_Plane_Max_Y) Our_Plane_Extra_Y = Our_Plane_Max_Y;
			}
			else if (Plane_Positon.y - Our_Plane.Get_Height()/ 2 > Mouse_Pos.y)
			{
				//Move down
				Distance = Mouse_Pos.y - Plane_Positon.y;
				if (Distance < -Our_Plane_Max_Y_Speed) Distance = -Our_Plane_Max_Y_Speed;
				Our_Plane_Extra_Y = Our_Plane_Extra_Y + Distance * 2 * Gdx.graphics.getDeltaTime();
				if (Our_Plane_Extra_Y <= 0) Our_Plane_Extra_Y = 0;
			}
			else
			{

			}
		}
		else
		{
			if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer) == true) {
			//Accelerometer (if input not touched)
			//X Axis
			float A_X = Gdx.input.getAccelerometerX();
			if (A_X > 1f)
			{
				if (Our_Plane.Get_Position().x - Our_Plane.Get_Width()/2 > camera.position.x - Our_Map.Map_Width / 2) {
				Our_Plane.Animation_Left();
				Our_Plane.Translate_X(-A_X*10);
				}
				else
				{
					Our_Plane.Animation_Stay();
				}
			}
			else if (A_X < -1f)
			{
				if (Our_Plane.Get_Position().x + Our_Plane.Get_Width() / 2 < camera.position.x + Our_Map.Map_Width / 2)
				{
				Our_Plane.Animation_Right();
				Our_Plane.Translate_X(-A_X*10);
				}
				else
				{
					Our_Plane.Animation_Stay();
				}
			}
			else
			{
				Our_Plane.Animation_Stay();
			}
			
			//Y Axis
			float A_Y = Gdx.input.getAccelerometerY();
			if (A_Y > 5)
			{
				Our_Plane_Extra_Y = Our_Plane_Extra_Y - ((A_Y-5) * 100 * Gdx.graphics.getDeltaTime());
				if (Our_Plane_Extra_Y <= 0) Our_Plane_Extra_Y = 0;
				
			}
			else if (A_Y < 2)
			{
				Our_Plane_Extra_Y = Our_Plane_Extra_Y - ((A_Y-2) * 40 * Gdx.graphics.getDeltaTime());
				if (Our_Plane_Extra_Y >= Our_Plane_Max_Y) Our_Plane_Extra_Y = Our_Plane_Max_Y;
			}
			else
			{
				
			}
			}
			else
			{
				Our_Plane.Animation_Stay();
			}
		}
		}
		else if (State == 0)
		{
			//Menu
			
		}
		else if (State == 2)
		{
	
			//Game Over
			if (Gdx.input.isTouched(0) == true)
			{
				State = 0;
				Renew_Game();
			}

		}
		else if (State == 3)
		{
			//You Win
			if (Gdx.input.isTouched(0) == true)
			{
				State = 0;
				Renew_Game();
			}

		}
		
	}

	@Override
	public void render() {	
		Elapsed_Time = Elapsed_Time + Gdx.graphics.getDeltaTime();
		
		//Process inputs
		Process_Inputs();
		
		//Screen
		if (State == 1) {
		if (Screen_Left_To_Go > 0)
		{
			Screen_Left_To_Go = Screen_Left_To_Go - Scrolling_Speed * Gdx.graphics.getDeltaTime();
			camera.translate(0, Scrolling_Speed * Gdx.graphics.getDeltaTime());
			camera.update();
			//Our_Plane.Set_Position(Our_Plane.Get_Position().x, Our_Plane.Get_Position().y + Scrolling_Speed * Gdx.graphics.getDeltaTime());
		}
		Our_Plane.Set_Position(Our_Plane.Get_Position().x, Our_Plane.Get_Height() + Our_Plane_Extra_Y + camera.position.y - Our_Map.Map_Height/2);
		
		//Health Bar
		Health_Bar_Sprite.setPosition(camera.position.x - Our_Map.Map_Width/2, camera.position.y - Our_Map.Map_Height/2);
		Health_Sprite.setPosition(camera.position.x - Our_Map.Map_Width/2 + 5, camera.position.y-Our_Map.Map_Height/2+5);
		
		Current_Health = Our_Plane.Hit;
		float Converted_Health = 90*Current_Health/Max_Health;
		if (Converted_Health < 0)
		{
			Converted_Health = 0;
			State = 2;
			Music_Man.Stop_Music();
			Bullet_Creating_Task.cancel();
		}
		Health_Sprite.setSize(Converted_Health, 20-5);
		Enemy_Central.Set_Screen_Left(Screen_Left_To_Go, Our_Map.Map_Total_Height - Our_Map.Map_Height, Our_Map.Map_Height);

		//Process updates
		Bullet_Man.Update_Bullets(camera);
		//Collision updates
		Collision_Man.Update();
		Burst_Man.Update();
		
		Our_Plane.Update_Animation();
		Enemy_Central.Update_Ground_Enemies(camera);
		Enemy_Central.Update_Planes(camera);
		
		//Check whether all enemies gone
		if (Game_Started == true) {
		if (Enemy_Central.All_Planes.size() == 0 && Enemy_Central.All_Ground_Enemies.size() == 0)
		{
			State = 3;
			Bullet_Creating_Task.cancel();
		}
		}
		//Transitions
		Transitions.Update();
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //(Gl10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		if (State == 1 || State == 2 || State == 3)
			Our_Map.Draw();
		
		batch.begin();
	
		if (State == 1) {
		Enemy_Central.Draw(batch);
		Bullet_Man.Draw_Bullets(batch);
		Our_Plane.Draw(batch);
		Burst_Man.Draw_Bursts(batch);
		}
		else if (State == 0) 
		{
		Background_Sprite.draw(batch);
		New_Game_Sprite.draw(batch);
		Exit_Game_Sprite.draw(batch);
		}
		else if (State == 2)
		{
			Game_Over_Sprite.setPosition(camera.position.x - Game_Over_Sprite.getWidth()/2, camera.position.y);
			Game_Over_Sprite.draw(batch);
		}
		else if (State == 3)
		{
			You_Win_Sprite.setPosition(camera.position.x - Game_Over_Sprite.getWidth()/2, camera.position.y);
			You_Win_Sprite.draw(batch);
		}
	
		//Clear Screen Texts
		Text_Writer.Clear();
		if (State == 1) {
		//Text_Writer.Draw_Line(Float.toString(Gdx.input.getAccelerometerY()),camera);
		//Text_Writer.Draw_Line(Float.toString(Our_Plane.Plane_Transition.Current_Fadiness_Count), camera);
			Text_Writer.Draw_Line("Score : " + Long.toString(Collision_Man.Score_User), camera);
			Health_Bar_Sprite.draw(batch);
			Health_Sprite.draw(batch);
		}
		
	
		batch.end();
	
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (pointer == 0)
		{
			if (State == 0)
			{
				int Mouse_X = screenX;
				int Mouse_Y = screenY;
				Vector3 Mouse_Pos = new Vector3(Mouse_X, Mouse_Y, 0);
				camera.unproject(Mouse_Pos);
				
				if (New_Game_Sprite.getBoundingRectangle().contains(Mouse_Pos.x, Mouse_Pos.y) == true) {
				if (Game_Started == false)
				{
					Game_Started = true;
					State = 1;
					New_Game();
				}
				}
				
				if (Exit_Game_Sprite.getBoundingRectangle().contains(Mouse_Pos.x, Mouse_Pos.y) == true) {
					
					if (You_Win != null) You_Win.dispose();
					
					if (Game_Over != null) Game_Over.dispose();
					
					if (New_Game != null) New_Game.dispose();
					
					if (Exit_Game != null) Exit_Game.dispose();
					
					if (Health_Bar_Texture != null) Health_Bar_Texture.dispose();
					
					if (Health_Texture != null) Health_Texture.dispose();
					
					if (Music_Man != null) Music_Man.Dispose();
					
					if (Burst_Man != null) Burst_Man.Dispose();
					
					if (Collision_Man != null) Collision_Man.Dispose();
					
					if (Transitions != null) Transitions.Dispose();
					
					if (Text_Writer != null) Text_Writer.Dispose();
					
				    if (Our_Map != null)	Our_Map.Dispose();
					
					if (Enemy_Central != null) Enemy_Central.Dispose();
					
				    if (Bullet_Creator != null)	Bullet_Creator.stop();
					
					if (All_Planes != null) All_Planes.dispose();
					
					if (Background != null) Background.dispose();
					
					//if (batch != null ) batch.dispose();
					
					Gdx.app.exit();
				}
			}

			
			}
				
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
