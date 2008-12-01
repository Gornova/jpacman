package it.marte.games.pacman.state;

import it.marte.games.pacman.level.LevelLoader;
import it.marte.games.pacman.level.SimpleLevel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Game extends BasicGameState {

    private SimpleLevel currentLevel;

    private Hashtable<String, String> levelChain = new Hashtable<String, String>();

    private StateBasedGame game;

    private Integer levelNumber=1;

    public static final int ID = 1;

    @Override
    public int getID() {
	return ID;
    }

    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	this.game = game;
	// load levels definitions
	try {
	    LevelLoader ll = new LevelLoader("data/levels.properties");
	    levelChain = ll.getLevelChain();
	    // set current level
	    // this is a little, horrid hack to solve a simple problem:
	    // how to determine what state are calling GameState?
	    if (levelNumber > levelChain.size()+900){
		levelNumber = 1;
	    }	    
	    if (levelNumber <= levelChain.size()) {
		currentLevel = new SimpleLevel(levelChain.get(levelNumber
			.toString()));
	    } else {
		// end of current level definition
		levelNumber = levelNumber + 1000;
		game.enterState(EndOfGame.ID, new FadeOutTransition(Color.black),
			new FadeInTransition(Color.black));
		return;
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	currentLevel.render(this, g);
    }

    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
	currentLevel.update(container, delta);
	// checking end level logic
	if (currentLevel.isLevelLose()) {
	    game.enterState(LevelLose.ID);
	    init(container, game);
	    return;
	}
	if (currentLevel.isLevelWin()) {
	    levelNumber++;
	    game.enterState(LevelWin.ID);
	    init(container, game);
	    return;
	}
    }

    public void keyReleased(int key, char c) {
	if (key == Input.KEY_ESCAPE) {
	    game.enterState(Pause.ID, new FadeOutTransition(Color.black),
		    new FadeInTransition(Color.black));
	}
    }
    
    

}
