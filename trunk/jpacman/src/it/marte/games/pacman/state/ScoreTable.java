package it.marte.games.pacman.state;

import it.marte.games.pacman.util.ScoreRecord;
import it.marte.games.pacman.util.ScoreTableLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

public class ScoreTable extends BasicGameState {

    public static final int ID = 5;

    /** The game holding this state */
    private StateBasedGame game;

    private ArrayList<ScoreRecord> scores;

    private int updateTimer;

    private GameContainer container;

    @Override
    public int getID() {
	return ID;
    }

    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	this.game = game;
	this.container = container;
	updateTimer = 0;
	try {
	    ScoreTableLoader stl = new ScoreTableLoader("scoretable.properties");
	    scores = new ArrayList<ScoreRecord>(stl.loadScoreTable());

	} catch (FileNotFoundException e) {
	    Log.error(e);
	} catch (IOException e) {
	    Log.error(e);
	}
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	g.drawString("Pacman Scoretable", 100, 50);

	// render only first 20 score
	g.setColor(Color.red);
	int counter = 0;
	// ordering scores, best first
	Collections.sort(scores);

	for (ScoreRecord score : scores) {
	    if (counter >= 20)
		break;
	    g.drawString(counter + " - " + score.getName(), 100,
		    90 + counter * 15);
	    g.drawString(score.getPoints().toString(), 300, 90 + counter * 15);

	    counter++;
	}

	g.setColor(Color.white);

	g.drawString("Press enter to continue", 100, 500);

    }

    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
	updateTimer = updateTimer + delta;
	if (updateTimer > 5000) {
	    try {
		ScoreTableLoader stl = new ScoreTableLoader(
			"scoretable.properties");
		scores = new ArrayList<ScoreRecord>(stl.loadScoreTable());

	    } catch (FileNotFoundException e) {
		Log.error(e);
	    } catch (IOException e) {
		Log.error(e);
	    }
	}
    }

    public void keyReleased(int key, char c) {
	if (key == Input.KEY_ENTER) {
	    game.enterState(Menu.ID, new FadeOutTransition(Color.black),
		    new FadeInTransition(Color.black));
	}
	if (key == Input.KEY_F2) {
	    try {
		if (!container.isFullscreen()) {
		    container.setFullscreen(true);
		} else {
		    container.setFullscreen(false);
		}
	    } catch (SlickException e) {
		Log.error(e);	    }
	}
    }

}
