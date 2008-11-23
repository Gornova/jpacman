package it.marte.games.pacman.state;

import org.newdawn.slick.AngelCodeFont;
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
	/** Font used **/
	private AngelCodeFont font;
	
	private FadeOutTransition fot;
	
	private FadeInTransition fit;
	
	@Override
	public int getID() {
		return ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		fot = new FadeOutTransition(Color.black);
		fit = new FadeInTransition(Color.black);
		try {
			font = new AngelCodeFont("data/demo2.fnt","data/demo2_00.tga");
		} catch (SlickException e1) {
			Log.error(e1);
		}		
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setFont(font);
		g.drawString("Pacman Scoretable", 340, 50);
		//TODO: put scoretable here!
		
		g.drawString("Press enter to continue", 340, 200);
		
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			game.enterState(Menu.ID,fot,fit);
		}
	}
	
}
