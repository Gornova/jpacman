package it.marte.games.pacman.actors;

import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * An interface for a Ghost's Brain
 * 
 * @author AM
 * @project PacMan
 */
public interface Brain {

	/**
	 * Update Brain thinking relative to a certain delta (in milliseconds) 
	 * @param delta
	 */
	public void update(int delta);
	
	/**
	 * @return next Step to follow
	 */
	public Step getCurrentStep();
	
}
