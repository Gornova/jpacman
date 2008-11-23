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

public class LevelWin extends BasicGameState {

	public static final int ID = 4;
	private StateBasedGame game;
	
	@Override
	public int getID() {
		return ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//TODO: add a better interface here!
		
		g.drawString("Score :"+ Score.getFinalScore(), 20, 20);
		g.drawString("Level WIN! To return to the menu, press SPACE", 100, 100);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}
	
	/**
	 * @see org.newdawn.slick.state.BasicGameState#keyReleased(int, char)
	 */
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_SPACE) {
			game.enterState(Game.ID,new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
	}	


}
