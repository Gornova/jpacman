package it.marte.games.pacman.base;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A physical body which can collide with other bodies. Bodies are checked
 * against each other for intersection and notified when intersection occurs.
 */
public abstract class Body implements Entity {

	/** The underlying shape of this body, used to perform intersection tests. */
	protected Shape shape;
	
	public float getCenterX() {
		return shape.getCenterX();
	}

	public float getCenterY() {
		return shape.getCenterY();
	}

	public float getX() {
		return shape.getX();
	}

	public float getY() {
		return shape.getY();
	}

	public float getWidth() {
		return shape.getMaxX() - shape.getX();
	}

	public float getHeight() {
		return shape.getMaxY() - shape.getY();
	}

	@Override
	public String toString() {
		return "Body[" + getCenterX() + "," + getCenterY() + "]";
	}

	public Vector2f getPosition(){
		Vector2f pos = new Vector2f();
		pos.set(shape.getX(), shape.getY());
		return pos;
	}
}
