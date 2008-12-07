package it.marte.games.pacman.state;

import it.marte.games.pacman.base.Score;
import it.marte.games.pacman.util.Clock;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

public class Pause extends BasicGameState {

    public static final int ID = 2;

    private StateBasedGame game;

    /** Font used * */
    private AngelCodeFont font;

    /** The menu options */
    private String[] options = new String[] { "Continue", "Quit" };

    private Image logo;

    /** The index of the selected option */
    private int selected;

    private FadeOutTransition fot;

    private FadeInTransition fit;

    private GameContainer container;

    @Override
    public int getID() {
	return ID;
    }

    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	this.game = game;
	this.container = container;
	fot = new FadeOutTransition(Color.black);
	fit = new FadeInTransition(Color.black);
	try {
	    font = new AngelCodeFont("data/demo2.fnt", "data/demo2_00.tga");
	    logo = new Image("data/logo.gif");
	} catch (SlickException e1) {
	    Log.error("font non found " + e1.getMessage());
	    e1.printStackTrace();
	}

    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	g.setFont(font);
	g.drawImage(logo, 320, 20);

	g.drawString("Score :" + Score.getScore(), 200, 80);

	g.drawString("Time : " + Clock.getTime(), 200, 120);

	for (int i = 0; i < options.length; i++) {
	    g.drawString(options[i], 400 - (font.getWidth(options[i]) / 2),
		    200 + (i * 50));
	    if (selected == i) {
		g.drawRect(200, 190 + (i * 50), 400, 50);
	    }
	}
    }

    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
    }

    public void keyReleased(int key, char c) {
	if (key == Input.KEY_DOWN) {
	    selected++;
	    if (selected >= options.length) {
		selected = 0;
	    }
	}
	if (key == Input.KEY_UP) {
	    selected--;
	    if (selected < 0) {
		selected = options.length - 1;
	    }
	}
	if (key == Input.KEY_ENTER) {
	    if (selected == 0) {
		game.enterState(Game.ID, fot, fit);
	    }
	    if (selected == 1) {
		game.getContainer().exit();
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

}
