package it.marte.games.pacman.actors;

import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;
import it.marte.games.pacman.map.Map;
import it.marte.games.pacman.util.Collider;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;

/**
 * An object that player can collect to gain score
 * 
 * @author AM
 * @project PacMan
 */
public class EatGem extends Body {

    private Image image;

    private boolean toRemove = false;

    public EatGem(Role role, Shape shape, Image image) {
	super(role, shape);
	this.image = image;
    }

    public void addToLevel(Level l) {
	l.add(this);
    }

    public Role getRole() {
	return Role.EATGEM;
    }

    public void onCollision(Entity obstacle) {
	if (obstacle.getRole().equals(Entity.Role.PLAYER)) {
	    toRemove = true;
	}
    }

    public void removeFromLevel(Level l) {
	l.remove(this);
    }

    public void render(BasicGameState game, Graphics g) {
	g.drawImage(this.image, getX(), getY());
    }

    public void update(GameContainer game, int delta) {
	if (!toRemove) {
	    Player player = Map.getPlayer();
	    Collider.testAndAlert(player, this);
	}
    }

    /**
     * @return the toRemove
     */

    public boolean isToRemove() {
	return toRemove;
    }

}
