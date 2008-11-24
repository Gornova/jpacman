package it.marte.games.pacman.brains;

import java.util.Hashtable;

import it.marte.games.pacman.actors.Brain;
import it.marte.games.pacman.map.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * Blue ghost (Machibuse): 
 *
 * Cicle into a circle, but when see pacman, follow him, until last position!
 *  
 * @author AM
 * @project PacMan
 */
public class BlueGhostBrain implements Brain {

	/** Internal thinking delta **/
	private int updateThinkingTime;
	
	/** Timer for rethinkin Path based on player position **/
	private int updatePlayerPositionTime;

	/** Current step index into path **/
	private int currentStepIndex;
	
	/** Current path **/
	private Path path;	
	
	/** Used to render brain thinking **/
	private Image dot;
	
	/** Current position of Ghost **/
	private Vector2f current;

	/** Game Map  **/
	private Map map;

	private boolean cannotFindPath; 
	
	private Vector2f a = null;
	private Vector2f b = null;
	private Vector2f c = null;
	private Vector2f d = null;
	
	
	/**
	 * Start RedGhost logic based on a map and a start position
	 * @param map
	 * @param start
	 * @throws SlickException 
	 */
	public BlueGhostBrain(Map map, Vector2f start) throws SlickException{
		this.map = map;
		this.current = start;
		init();
	}
	
	/**
	 * Init brain Logic
	 * 
	 * @throws SlickException 
	 */
	public void init() {
		
		Hashtable<String, Vector2f> points = map.getBluePoint();
		if (points!=null){
			a = points.get("a");
			b = points.get("b");
			c = points.get("c");
			d = points.get("d");
		}
		
		path = null;
		updateThinkingTime = 0;
		updatePlayerPositionTime = 0;
		currentStepIndex = 0;
		cannotFindPath = false;
		
		try {
			dot = new Image("data/bluedot.gif");
		} catch (SlickException e) {
			Log.error(e);
		}
		updatePathToPlayer();
	}

	/**
	 * Update Brain logic
	 */
	public void update(int delta) {
		// Update path if there is not one
		if (path == null) {
			updatePathToPlayer();
			return;
		}
		// update logic of movement of a ghost
		updateThinkingTime = updateThinkingTime + delta;
		if (updateThinkingTime > 50) {
			updateThinkingTime = 0;

			if (currentStepIndex > path.getLength() - 1) {
				reThink(current, map, path);				
			} 
		}
		// update logic of thinking of a ghost
		updatePlayerPositionTime = updatePlayerPositionTime + delta;
	}
	
	/**
	 * Rethink path  
	 * 
	 * @param current
	 * @param map
	 * @param path
	 */
	private void reThink(Vector2f current, Map map, Path path){
		currentStepIndex = 0;
		path = null;
		updatePathToPlayer();
	}

	/**
	 * Update path for the ghost relative to the player position
	 */
	private void updatePathToPlayer() {

		
		Vector2f target = null;
		
		if (current.distance(a)<1){
			target = b;
		}
		if (current.distance(b)<1){
			target = c;
		}
		if (current.distance(c)<1){
			target = d;
		}
		if (current.distance(d)<1){
			target = a;
		}
		if (target == null){
			target = a;
		}
		if (Map.getPlayer().getPosition().distance(current)< 5*32){
			target = Map.getPlayer().getPosition();
		}
		try {
			path = map.getUpdatedPath((int) current.getX() / 32, (int) current.getY() / 32,
					(int) target.getX() / 32, (int) target.getY() / 32);
		} catch (NullPointerException e){
			path = null;
			cannotFindPath = true;
		}

		
	}

	/**
	 * Render Brain Path
	 * 
	 * @param game
	 * @param g
	 */
	public void render(BasicGameState game, Graphics g) {
		
		if (path != null) {
			for (int i = 0; i < path.getLength(); i++) {
				Step a = path.getStep(i);
				dot.draw(a.getX() * 32, a.getY() * 32);
			}
		}
	}

	/**
	 * Return current Step
	 */
	public Step getCurrentStep() {
		if (path==null){
			updatePathToPlayer();
		}
		return path.getStep(currentStepIndex);
	}

	/**
	 * Goto next step, based on current position
	 * @param position
	 */
	public void goToNextStep(Vector2f position){
		this.currentStepIndex++;
		this.current = position;
	}

	public boolean isCannotFindPath() {
		return cannotFindPath ;
	}

	public void setCurrent(Vector2f current) {
		this.current = current;
	}

}
