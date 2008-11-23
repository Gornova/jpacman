package it.marte.games.pacman.actors;

import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;

/**
 * A dummy body for testing
 * 
 * @author AM
 * @project PacMan
 */
public class DummyBody extends Body {

	public DummyBody(Shape shape) {
		this.shape = shape;
	}

	public void addToLevel(Level l) {
		// TODO Auto-generated method stub

	}

	public Role getRole() {
		return Role.DUMMY;
	}

	public void onCollision(Entity obstacle) {
		// TODO Auto-generated method stub

	}

	public void removeFromLevel(Level l) {
		// TODO Auto-generated method stub

	}

	public void render(BasicGameState game, Graphics g) {
		// TODO Auto-generated method stub
	}

	public void update(GameContainer game, int delta) {
		// TODO Auto-generated method stub

	}

	public boolean isToRemove() {
		return false;
	}

}
