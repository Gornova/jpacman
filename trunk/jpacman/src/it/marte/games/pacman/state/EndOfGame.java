package it.marte.games.pacman.state;

import it.marte.games.pacman.base.Score;
import it.marte.games.pacman.util.ScoreRecord;
import it.marte.games.pacman.util.ScoreTableLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

public class EndOfGame extends BasicGameState {

    public static final int ID = 6;
    private StateBasedGame game;

    private String playerName;
    private GameContainer container;

    @Override
    public int getID() {
	return ID;
    }

    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	this.game = game;
	this.container = container;
	playerName = new String();
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	g.drawString("Congratulations! You have completed Jpacman!", 100, 100);
	g.drawString("Please enter your name, hero!", 100, 150);

	g.setColor(Color.red);
	g.drawString(playerName, 100, 300);
	g.setColor(Color.white);

	g.drawString("Press enter to write your name into scoretable!", 100, 450);
    }

    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
    }

    /**
     * Handle player input
     */
    public void keyReleased(int key, char c) {
	if (key == Input.KEY_ENTER) {
	    writeScore();
	    game.enterState(ScoreTable.ID, new FadeOutTransition(Color.black),
		    new FadeInTransition(Color.black));
	}
	if (key == Input.KEY_BACK) {
	    if (playerName.length() > 0) {
		playerName = playerName.substring(0, playerName.length() - 1);
	    }
	}
	if (playerName.length() < 20) {
	    if (key == Input.KEY_A) {
		playerName = playerName + "A";
	    }
	    if (key == Input.KEY_B) {
		playerName = playerName + "B";
	    }
	    if (key == Input.KEY_C) {
		playerName = playerName + "C";
	    }
	    if (key == Input.KEY_D) {
		playerName = playerName + "D";
	    }
	    if (key == Input.KEY_E) {
		playerName = playerName + "E";
	    }
	    if (key == Input.KEY_F) {
		playerName = playerName + "F";
	    }
	    if (key == Input.KEY_G) {
		playerName = playerName + "G";
	    }
	    if (key == Input.KEY_H) {
		playerName = playerName + "H";
	    }
	    if (key == Input.KEY_I) {
		playerName = playerName + "I";
	    }
	    if (key == Input.KEY_J) {
		playerName = playerName + "J";
	    }
	    if (key == Input.KEY_K) {
		playerName = playerName + "K";
	    }
	    if (key == Input.KEY_L) {
		playerName = playerName + "L";
	    }
	    if (key == Input.KEY_M) {
		playerName = playerName + "M";
	    }
	    if (key == Input.KEY_N) {
		playerName = playerName + "N";
	    }
	    if (key == Input.KEY_O) {
		playerName = playerName + "O";
	    }
	    if (key == Input.KEY_P) {
		playerName = playerName + "P";
	    }
	    if (key == Input.KEY_Q) {
		playerName = playerName + "Q";
	    }
	    if (key == Input.KEY_R) {
		playerName = playerName + "R";
	    }
	    if (key == Input.KEY_S) {
		playerName = playerName + "S";
	    }
	    if (key == Input.KEY_T) {
		playerName = playerName + "T";
	    }
	    if (key == Input.KEY_U) {
		playerName = playerName + "U";
	    }
	    if (key == Input.KEY_V) {
		playerName = playerName + "V";
	    }
	    if (key == Input.KEY_W) {
		playerName = playerName + "W";
	    }
	    if (key == Input.KEY_X) {
		playerName = playerName + "X";
	    }
	    if (key == Input.KEY_Y) {
		playerName = playerName + "Y";
	    }
	    if (key == Input.KEY_Z) {
		playerName = playerName + "Z";
	    }
	    if (key == Input.KEY_SPACE) {
		playerName = playerName + " ";
	    }
	    
	}
	if (key == Input.KEY_F2) {
	    try {
		if (!container.isFullscreen()) {
		    container.setFullscreen(true);
		} else {
		    container.setFullscreen(false);
		}
	    } catch (SlickException e) {
		//TODO: what? 
	    }

	}
    }

    /** 
     * Save scoretable to disk
     */
    private void writeScore() {
	try {
	    ScoreTableLoader stl = new ScoreTableLoader("scoretable.properties");
	    ArrayList<ScoreRecord> scores =  stl.loadScoreTable();
	    ScoreRecord sr = new ScoreRecord(playerName,Score.getFinalScore());
	    scores.add(sr);
	    stl.saveScoreTable(scores);
	    stl = null;
	} catch (FileNotFoundException e) {
	    Log.error(e);
	} catch (IOException e) {
	    Log.error(e);
	}
    }

}
