package it.marte.games.pacman.actors;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
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
	
	/**
	 * Force internal Brain logic to go to next step
	 * 
	 * @param position - next step where to go
	 */
	public void goToNextStep(Vector2f position);

	/**
	 * Render on screen Brain thinking 
	 * @param game
	 * @param g
	 */
	public void render(BasicGameState game, Graphics g);
	
	/**
	 * @return true if is not possibile to find a path, false otherwise
	 */
	public boolean isCannotFindPath();
	
}
