package it.marte.games.pacman.actors;

import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;

/**
 * An object that player cannot move on. It's only a logical object used for
 * collision without a graphic
 * 
 * @author AM
 * @project PacMan
 */
public class Block extends Body {

	public Block(Shape s) {
		this.shape = s;
	}

	public void addToLevel(Level l) {
		l.add(this);
	}

	public Role getRole() {
		return Role.BLOCK;
	}

	public void onCollision(Entity obstacle) {
		System.out.println("collisione con blocco semplice");
	}

	public void removeFromLevel(Level l) {
		l.remove(this);
	}


	public void render(BasicGameState game, Graphics g) {
		//g.drawImage(this.image, getX(), getY());
		//g.draw(shape);
	}

	public void update(GameContainer game, int delta) {
	}

	public boolean isToRemove() {
		return false;
	}

}
