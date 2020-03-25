package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.InversePacman;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = InversePacman.APP_TITLE;
		config.width = InversePacman.APP_WIDTH;
		config.height = InversePacman.APP_HEIGHT;
		config.backgroundFPS = InversePacman.APP_FPS;
		config.foregroundFPS = InversePacman.APP_FPS;
		new LwjglApplication(new InversePacman(), config);
	}
}
