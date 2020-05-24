package com.zppycode.jumpandgetcoin.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class JumpAndGetCoin extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manpos = 0;
	int pause = 0;
	int pause1 = 0;
	float gravity = 0.3f;
	float velocity = 0;
	int manY = 0;
	int wait=0;


	Rectangle manRectangle;

	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	Texture coin;
	int coinCount=0;
	Random random;
	Texture bomb;
	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer> bombY = new ArrayList<>();
    ArrayList<Rectangle> bombRectangles = new ArrayList<>();
	int bombCount=0;

	int score=0;
	int gameState=0;

	BitmapFont font;
	Texture dizzy;

	private Sound Jump;
	private Sound Coin;
	private Sound Bomb;
	private Sound Diamond;

	Texture clouds;
	ArrayList<Integer> cloudX = new ArrayList<>();
	ArrayList<Integer> cloudY = new ArrayList<>();
	int cloudCount=0;

	Texture diamonds;
	ArrayList<Integer> diamondX = new ArrayList<>();
	ArrayList<Integer> diamondY = new ArrayList<>();
	int diamondCount=0;

	ArrayList<Rectangle> diamondRectangles = new ArrayList<>();


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		manY = Gdx.graphics.getHeight()/2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		dizzy = new Texture("dizzy-1.png");

		Jump = (Sound) Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
		Coin = (Sound) Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
		Bomb = (Sound) Gdx.audio.newSound(Gdx.files.internal("bomb.mp3"));
		Diamond = (Sound) Gdx.audio.newSound(Gdx.files.internal("diamond.mp3"));

		clouds = new Texture("clouds.png");
		diamonds = new Texture("diamond.png");

	}

	public void makeCoin()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
	}

	public void makeBomb()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}

	public void makeClouds()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		cloudY.add((int)height);
		cloudX.add(Gdx.graphics.getWidth());
	}

	public void makeDiamond()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		diamondX.add((int)height);
		diamondY.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {


		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState == 1)
		{
			//LIVE
			if(coinCount < 100)
			{
				coinCount++;
			}
			else
			{
				coinCount = 0;
				makeCoin();
			}

			if(bombCount < 250)
			{
				bombCount++;
			}
			else
			{
				bombCount = 0;
				makeBomb();
			}

			if(cloudCount < 70)
			{
				cloudCount++;
			}
			else
			{
				cloudCount = 0;
				makeClouds();
			}

			if(diamondCount < 250)
			{
				diamondCount++;
			}
			else
			{
				diamondCount = 0;
				makeDiamond();
			}

			for(int i = 0;i<cloudY.size();i++)
			{
				batch.draw(clouds,cloudX.get(i),cloudY.get(i));
				cloudX.set(i,cloudX.get(i)-5);

			}

			diamondRectangles.clear();
			for(int i = 0;i<diamondY.size();i++)
			{
				batch.draw(diamonds,diamondX.get(i),diamondY.get(i));
				diamondX.set(i,diamondX.get(i)-5);
				diamondRectangles.add(new Rectangle(diamondX.get(i),diamondY.get(i),diamonds.getWidth(),diamonds.getHeight()));
			}

			coinRectangles.clear();

			for(int i = 0;i<coinY.size();i++)
			{
				batch.draw(coin,coinX.get(i),coinY.get(i));
				coinX.set(i,coinX.get(i)-5);
				coinRectangles.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));
			}



			bombRectangles.clear();
			for(int i = 0;i<bombY.size();i++)
			{
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-8);
				bombRectangles.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(Gdx.input.justTouched()) {
				velocity = -10;
				Jump.setVolume(1,5f);
				Jump.play();
			}


			if(pause < 5)
			{
				pause++;
			}
			else {
				pause = 0;

				if (manpos < 3) {
					manpos++;
				} else {
					manpos = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if(manY <= 0){
				manY=0;
			}
		}
		else if(gameState == 0)
		{
			if(Gdx.input.justTouched())
				gameState=1;
		}
		else if(gameState == 2)
		{
			if(Gdx.input.justTouched()) {

				if(wait<3)
				{
					wait++;
				}
				else {
					gameState = 1;
				}
			}

			score = 0;
			velocity=0;
			coinX.clear();
			coinY.clear();
			coinRectangles.clear();
			coinCount=0;
			bombX.clear();
			bombY.clear();
			bombRectangles.clear();
			bombCount=0;

			//Game Over
		}



		if(gameState==2){

			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manpos].getWidth() / 2, manY);

		}
		else {
			batch.draw(man[manpos], Gdx.graphics.getWidth() / 2 - man[manpos].getWidth() / 2, manY);
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manpos].getWidth()/2,manY,man[manpos].getWidth(),man[manpos].getHeight());

		for(int i = 0 ; i<coinRectangles.size() ; i++)
        {
            if(Intersector.overlaps(manRectangle,coinRectangles.get(i))) {
				Coin.setVolume(3,5f);
				Coin.play();
                score++;
                coinRectangles.remove(i);
                coinX.remove(i);
                coinY.remove(i);
                break;
            }

        }

		for(int i = 0 ; i<diamondRectangles.size() ; i++)
		{
			if(Intersector.overlaps(manRectangle,diamondRectangles.get(i))) {
				Diamond.setVolume(4,5f);
				Diamond.play();
				score+=2;
				diamondRectangles.remove(i);
				diamondX.remove(i);
				diamondY.remove(i);
				break;
			}
		}

        for(int i = 0 ; i<bombRectangles.size() ; i++)
        {
            if(Intersector.overlaps(manRectangle,bombRectangles.get(i))) {
				Bomb.setVolume(2,5f);
				Bomb.play();

                gameState = 2;
            }
        }

        font.draw(batch,String.valueOf(score),100,120);
		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		Jump.dispose();
		Bomb.dispose();
		Coin.dispose();
	}
}
