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
 * Pink ghost (Machibuse):
 * 
 * Input is four tiles in front of Pac-Man. Therefore direction Pac-Man is
 * facing as well as actual location of Pac-Man determine Pinky's "target."
 * Pinky attempts to home in on this target using the same logic as the Red
 * ghost uses to home in on Pac-Man.
 * 
 * @author AM
 * @project PacMan
 */
public class PinkGhostBrain implements Brain {

    /** Internal thinking time * */
    private static final int THINKINGTIME = 50;

    /** How much tile pink target in front of pacman * */
    private static final int SHIFTFACTOR = 4;

    /** Internal thinking delta * */
    private int updateThinkingTime;

    /** Timer for rethinkin Path based on player position * */
    private int updatePlayerPositionTime;

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

    /**
     * Start RedGhost logic based on a map and a start position
     * 
     * @param map
     * @param start
     * @throws SlickException
     */
    public PinkGhostBrain(Map map, Vector2f start) throws SlickException {
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
	updatePlayerPositionTime = 0;
	currentStepIndex = 0;
	cannotFindPath = false;

	try {
	    dot = new Image("data/pinkdot.gif");
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
	if (updateThinkingTime > THINKINGTIME) {
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
    private void reThink(Vector2f current, Map map, Path path) {
	currentStepIndex = 0;
	path = null;
	updatePathToPlayer();
    }

    /**
     * Update path for the ghost relative to the player position
     */
    private void updatePathToPlayer() {

	Player pl = Map.getPlayer();
	Vector2f pos = pl.getPosition();

	String dir = pl.getLastDir();

	if (dir.equals("up")) {
	    pos.y = pos.y - SHIFTFACTOR * map.getTileSize();
	}
	if (dir.equals("down")) {
	    pos.y = pos.y + SHIFTFACTOR * map.getTileSize();
	}
	if (dir.equals("left")) {
	    pos.x = pos.x - SHIFTFACTOR * map.getTileSize();
	}
	if (dir.equals("right")) {
	    pos.x = pos.x + SHIFTFACTOR * map.getTileSize();
	}

	try {
	    if (!map.blocked(null, (int) pos.x / map.getTileSize(), (int) pos.y
		    / map.getTileSize())) {
		path = map.getUpdatedPath((int) current.getX()
			/ map.getTileSize(), (int) current.getY()
			/ map.getTileSize(), (int) pos.getX()
			/ map.getTileSize(), (int) pos.getY()
			/ map.getTileSize());
	    } else {
		path = map.getUpdatedPath((int) current.getX()
			/ map.getTileSize(), (int) current.getY()
			/ map.getTileSize(), (int) pl.getX()
			/ map.getTileSize(), (int) pl.getY()
			/ map.getTileSize());
	    }
	} catch (NullPointerException e) {
	    path = map.getUpdatedPath((int) current.getX() / map.getTileSize(),
		    (int) current.getY() / map.getTileSize(), (int) pl.getX()
			    / map.getTileSize(), (int) pl.getY()
			    / map.getTileSize());
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

    /**
     * Set starting point
     */
    public void setCurrent(Vector2f current) {
	this.current = current;
    }

}
