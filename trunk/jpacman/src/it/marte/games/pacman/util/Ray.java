package it.marte.games.pacman.util;

import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;

public class Ray extends Body {

    private Body one;
    private Body two;

    public Ray(Body one, Body two) {
	super(Role.RAY, new Line(one.getCenterX(), one.getCenterY(), two
		.getCenterX(), two.getCenterY()));
	this.one = one;
	this.two = two;
    }

    public Ray(Body one, float dx, float dy) {
	super(Role.RAY, new Line(one.getCenterX(), one.getCenterY(), dx, dy,
		false));
    }

    public Vector2f getDirection() {
	Vector2f d = new Vector2f();
	d.x = ((Line) shape).getDX();
	d.y = ((Line) shape).getDY();
	d.normalise();
	return d;
    }

    public void render(Graphics g) {
	g.draw(shape);
    }

    public void translateX(float x) {
	shape.setCenterX(getCenterX() + x);
    }

    public void translateY(float y) {
	shape.setCenterY(getCenterY() + y);
    }

    public void addToLevel(Level l) {
    }

    public Role getRole() {
	return Role.RAY;
    }

    public boolean isToRemove() {
	return false;
    }

    public void onCollision(Entity obstacle) {
    }

    public void removeFromLevel(Level l) {
    }

    public void render(BasicGameState game, Graphics g) {
	g.draw(shape);
    }

    public void update(GameContainer game, int delta) {
    }

    /**
     * @return the one
     */
    public Body getOne() {
	return one;
    }

    /**
     * @return the two
     */
    public Body getTwo() {
	return two;
    }
}
