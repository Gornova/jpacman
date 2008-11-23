package it.marte.games.pacman;

import it.marte.games.pacman.state.Game;
import it.marte.games.pacman.state.LevelLose;
import it.marte.games.pacman.state.LevelWin;
import it.marte.games.pacman.state.Menu;
import it.marte.games.pacman.state.Pause;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PacManGame extends StateBasedGame{

	public PacManGame() {
		super("PacMan game");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new PacManGame());
			app.setDisplayMode(800, 600, false);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new Menu());		
		addState(new Game());
		addState(new Pause());
		addState(new LevelLose());
		addState(new LevelWin());
	}

}