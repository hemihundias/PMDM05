package com.mygdx.mijuego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mijuego.pantallas.BaseScreen;
import com.mygdx.mijuego.pantallas.AyudaScreen;
import com.mygdx.mijuego.pantallas.GameOverScreen;
import com.mygdx.mijuego.pantallas.GameScreen;
import com.mygdx.mijuego.pantallas.LoadingScreen;
import com.mygdx.mijuego.pantallas.MenuScreen;

public class MainGame extends Game {

	private AssetManager manager;

	public BaseScreen loadingScreen, menuScreen, gameScreen, gameOverScreen, creditsScreen;

	@Override
	public void create() {
		manager = new AssetManager();
		manager.load("floor.png", Texture.class);
		manager.load("gameover.png", Texture.class);
		manager.load("overfloor.png", Texture.class);
		manager.load("logo.png", Texture.class);
		manager.load("obstacle.png", Texture.class);
		manager.load("nave.png", Texture.class);
		manager.load("audio/die.ogg", Sound.class);
		manager.load("audio/jump.ogg", Sound.class);
		manager.load("audio/song.mp3", Music.class);

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	public void finishLoading() {
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		creditsScreen = new AyudaScreen(this);
		setScreen(menuScreen);
	}

	public AssetManager getManager() {
		return manager;
	}

}

