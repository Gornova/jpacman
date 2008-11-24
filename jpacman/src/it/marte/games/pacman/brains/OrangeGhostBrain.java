package it.marte.games.pacman.brains;

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
 * Orange ghost (Machibuse): 
 *
 * Go random, when see player, follow him for some time
 *  
 * @author AM
 * @project PacMan
 */
public class OrangeGhostBrain implements Brain {

	/** Internal thinking delta **/
	private int updateThinkingTime;
	
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
	
	private int followTimer;

	private boolean cannotFindPath; 
	
	/**
	 * Start RedGhost logic based on a map and a start position
	 * @param map
	 * @param start
	 * @throws SlickException 
	 */
	public OrangeGhostBrain(Map map, Vector2f start) throws SlickException{
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
		
		path = null;
		updateThinkingTime = 0;
		followTimer = 0;
		currentStepIndex = 0;
		cannotFindPath = false;
		
		try {
			dot = new Image("data/orangedot.gif");
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
		// update logic of player follow
		followTimer = followTimer + delta;
		if (followTimer > 20000){
			updatePathToPlayer();
			followTimer = 0;
		}
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

		
		Vector2f target = map.getRandomPoint();

		if (Map.getPlayer().getPosition().distance(current)< 8*32){
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
