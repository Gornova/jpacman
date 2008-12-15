package it.marte.games.pacman.state;

import it.marte.games.pacman.base.Score;

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

public class LevelWin extends BasicGameState {

    public static final int ID = 4;
    private StateBasedGame game;
    private GameContainer container;

    @Override
    public int getID() {
	return ID;
    }

    public void init(GameContainer container, StateBasedGame game)
	    throws SlickException {
	this.game = game;
	this.container = container;
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g)
	    throws SlickException {
	g.drawString("Score :" + Score.getFinalScore(), 20, 20);
	g.drawString("Level WIN! Press ENTER to continue", 100, 100);
    }

    public void update(GameContainer container, StateBasedGame game, int delta)
	    throws SlickException {
    }

    /**
     * @see org.newdawn.slick.state.BasicGameState#keyReleased(int, char)
     */
    public void keyReleased(int key, char c) {
	if (key == Input.KEY_ENTER) {
	    game.enterState(Game.ID, new FadeOutTransition(Color.black),
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
		Log.error(e);
	    }
	}

    }

}
