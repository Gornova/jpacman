package it.marte.games.pacman.brains;

import it.marte.games.pacman.actors.Brain;
import it.marte.games.pacman.map.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * Running Ghost
 * 
 * Escape from player, go to corners!
 * 
 * @author AM
 * @project PacMan
 */
public class GoToBaseGhostBrain implements Brain {

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
	
	private boolean cannotFindPath;
	
	/**
	 * Start RedGhost logic based on a map and a start position
	 * @param map
	 * @param start
	 * @throws SlickException 
	 */
	public GoToBaseGhostBrain(Map map, Vector2f start) throws SlickException{
		this.map = map;
		this.current = start;
		init();
	}
	
	/**
	 * Init brain Logic
	 * 
	 * @throws SlickException 
	 */
	private void init() throws SlickException {
		updateThinkingTime = 0;
		currentStepIndex = 0;
		
		cannotFindPath = false;
		dot = new Image("data/dot.gif");
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
			} else {
				//currentStep = path.getStep(currentStepIndex);
				//doMovement(step, delta);
			}
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
		Vector2f base = map.getBase();
		try {
			path = map.getUpdatedPath((int) current.getX() / 32, (int) current.getY() / 32,
					(int)base.getX(), (int) base.getY());
		} catch (NullPointerException e){
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

	/**
	 * @return the cannotFindPath
	 */
	public boolean isCannotFindPath() {
		return cannotFindPath;
	}

}
