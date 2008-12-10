package it.marte.games.pacman;

import it.marte.games.pacman.state.EndOfGame;
import it.marte.games.pacman.state.Game;
import it.marte.games.pacman.state.LevelLose;
import it.marte.games.pacman.state.LevelWin;
import it.marte.games.pacman.state.Menu;
import it.marte.games.pacman.state.Pause;
import it.marte.games.pacman.state.ScoreTable;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class PacManGame extends StateBasedGame {

    public PacManGame() {
	super("PacMan game");
    }

    public static void main(String[] arguments) {
	try {
	    Log.setVerbose(true);
	    try {
		Log.out = new PrintStream("jpacman.log");
	    } catch (FileNotFoundException e) {
		Log.error(e);
	    }

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
	addState(new ScoreTable());
	addState(new EndOfGame());
    }

}