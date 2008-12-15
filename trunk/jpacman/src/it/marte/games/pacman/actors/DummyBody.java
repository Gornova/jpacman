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

    public DummyBody(Role role, Shape shape) {
	super(role, shape);
    }

    public void addToLevel(Level l) {
    }

    public Role getRole() {
	return Role.DUMMY;
    }

    public void onCollision(Entity obstacle) {
    }

    public void removeFromLevel(Level l) {
    }

    public void render(BasicGameState game, Graphics g) {
    }

    public void update(GameContainer game, int delta) {
    }

    public boolean isToRemove() {
	return false;
    }

}
