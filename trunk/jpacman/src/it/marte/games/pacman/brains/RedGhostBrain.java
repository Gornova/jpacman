package it.marte.games.pacman.brains;

import it.marte.games.pacman.actors.Brain;
import it.marte.games.pacman.actors.Player;
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
 * Red Ghost
 * 
 * Red ghost (Oikake): Input is Pac-Man's location. 
 * He doesn't consider which direction Pac-Man is going. 
 * He attempts to reduce the distance between himself and Pac-Man. 
 * 
 * @author AM
 * @project PacMan
 */
public class RedGhostBrain implements Brain {

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
	
	/**
	 * Start RedGhost logic based on a map and a start position
	 * @param map
	 * @param start
	 * @throws SlickException 
	 */
	public RedGhostBrain(Map map, Vector2f start) throws SlickException{
		this.map = map;
		this.current = start;
		init();
	}
	
	/**
	 * Init brain Logic
	 * 
	 * @throws SlickException 
	 */
	public void init()  {
		path = null;
		updateThinkingTime = 0;
		updatePlayerPositionTime = 0;
		currentStepIndex = 0;
		cannotFindPath = false;
		
		try {
			dot = new Image("data/dot.gif");
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
		
		if (updatePlayerPositionTime > 5000) {
			updatePlayerPositionTime = 0;
			reThink(current, map, path);
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
		Path tempPAth = path;
		int tempCurrent = currentStepIndex;
		// rethink
		currentStepIndex = 0;
		path = null;
		updatePathToPlayer();
		
		//TODO: remove this
		if (path!=null){
			Step oldLast = tempPAth.getStep(tempCurrent);
			Step newFirst = path.getStep(currentStepIndex);
			
			Log.debug("RED Rethink");
			Log.debug("X diff : "+ Math.abs(oldLast.getX()-newFirst.getX() )  );
			Log.debug("Y diff : "+ Math.abs(oldLast.getY()-newFirst.getY() )  );
		}
	}

	/**
	 * Update path for the ghost relative to the player position
	 */
	private void updatePathToPlayer() {

		Player pl = Map.getPlayer();
		try {
			path = map.getUpdatedPath((int) current.getX() / 32, (int) current.getY() / 32,
					(int) pl.getX() / 32, (int) pl.getY() / 32);
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
		return cannotFindPath;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(Vector2f current) {
		this.current = current;
	}

}
