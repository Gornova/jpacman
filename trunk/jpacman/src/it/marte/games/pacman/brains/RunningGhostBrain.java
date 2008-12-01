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
 * Running Ghost
 * 
 * Escape from player, go to corners!
 * 
 * @author AM
 * @project PacMan
 */
public class RunningGhostBrain implements Brain {

    /** Internal thinking time * */
    private static final int THINKINGTIME = 50;

    /** Internal thinking delta * */
    private int updateThinkingTime;

    /** Current step index into path * */
    private int currentStepIndex;

    /** Current path * */
    private Path path;

    /** Used to render brain thinking * */
    private Image dot;

    /** Current position of Ghost * */
    private Vector2f current;

    /** Game Map * */
    private Map map;

    private boolean cannotFindPath;

    /** Current corner of map * */
    private Vector2f corner;

    /**
     * Start RedGhost logic based on a map and a start position
     * 
     * @param map
     * @param start
     * @throws SlickException
     */
    public RunningGhostBrain(Map map, Vector2f start) throws SlickException {
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
	cannotFindPath = false;
	path = null;
	updateThinkingTime = 0;
	currentStepIndex = 0;

	try {
	    dot = new Image("data/dot.gif");
	} catch (SlickException e) {
	    Log.error(e);
	}
	corner = map.getRandomCorner();
	if (corner == null) {
	    Log.error("corners null!");
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
	if (updateThinkingTime > THINKINGTIME) {
	    updateThinkingTime = 0;

	    if (currentStepIndex > path.getLength() - 1) {
		reThink(current, map, path);
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
    private void reThink(Vector2f current, Map map, Path path) {
	currentStepIndex = 0;
	path = null;
	updatePathToPlayer();
    }

    /**
     * Update path for the ghost relative to the player position
     */
    private void updatePathToPlayer() {
	try {
	    path = map.getUpdatedPath((int) current.getX() / map.getTileSize(),
		    (int) current.getY() / map.getTileSize(), (int) corner
			    .getX(), (int) corner.getY());
	} catch (NullPointerException e) {
	    Log.error(e);
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
		dot.draw(a.getX() * map.getTileSize(), a.getY()
			* map.getTileSize());
	    }
	}
    }

    /**
     * Return current Step
     */
    public Step getCurrentStep() {
	if (path == null) {
	    updatePathToPlayer();
	}
	return path.getStep(currentStepIndex);
    }

    /**
     * Goto next step, based on current position
     * 
     * @param position
     */
    public void goToNextStep(Vector2f position) {
	this.currentStepIndex++;
	this.current = position;
    }

    public boolean isCannotFindPath() {
	return cannotFindPath;
    }

    public void setCurrent(Vector2f current) {
	this.current = current;
    }

}
