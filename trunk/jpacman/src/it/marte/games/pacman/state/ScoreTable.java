package it.marte.games.pacman.state;

import it.marte.games.pacman.util.ScoreTableLoader;

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
import org.newdawn.slick.util.Log;

public class ScoreTable extends BasicGameState {

    public static final int ID = 5;

    /** The game holding this state */
    private StateBasedGame game;
    /** Font used * */
    //private AngelCodeFont font;

    private FadeOutTransition fot;

    private FadeInTransition fit;

    private Hashtable<String, String> scores;

    @Override
    public int getID() {
	return ID;
    }

    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	this.game = game;
	fot = new FadeOutTransition(Color.black);
	fit = new FadeInTransition(Color.black);
	/*
	try {
	    font = new AngelCodeFont("data/demo2.fnt", "data/demo2_00.tga");
	} catch (SlickException e1) {
	    Log.error(e1);
	}
	*/
	loadFromDisk();
    }

    private void loadFromDisk() {
	// load scores from disk
	try {
	    ScoreTableLoader stl = new ScoreTableLoader("scoretable.properties");
	    scores = stl.loadScoreTable();
	} catch (FileNotFoundException e) {
	    Log.error(e);
	} catch (IOException e) {
	    Log.error(e);
	}
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	// g.setFont(font);
	g.drawString("Pacman Scoretable", 100, 50);

	// render only first 20 score
	g.setColor(Color.red);
	int counter = 0;
	orderScores(scores);
	for (String name : scores.keySet()) {
	    if (counter >= 20)
		break;
	    g.drawString(counter + " - " + name + filler(name, 20)
		    + scores.get(name), 100, 90 + counter * 15);
	    counter++;
	}
	g.setColor(Color.white);

	g.drawString("Press enter to continue", 100, 500);

    }

    /** 
     * Order an hashtable into a vector, based on crescent value order 
     * @param element Hashtable to order
     */
    private void orderScores(Hashtable<String, String> element) {
	//Vector<String> result = new Vector<String>();

	//TODO
    }

    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
	loadFromDisk();
    }

    public void keyReleased(int key, char c) {
	if (key == Input.KEY_ENTER) {
	    game.enterState(Menu.ID, new FadeOutTransition(Color.black),
		    new FadeInTransition(Color.black));
	}
    }

    private String filler(String string, int limit) {
	String a = new String();
	for (int i = 0; i < (Math.abs(limit - string.length())); i++) {
	    a = a + " ";
	}
	return a;
    }

}
