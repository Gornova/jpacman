package it.marte.games.pacman.brains;

import it.marte.games.pacman.actors.Block;
import it.marte.games.pacman.actors.Brain;
import it.marte.games.pacman.actors.DummyBody;
import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Entity.Role;
import it.marte.games.pacman.map.Map;
import it.marte.games.pacman.util.Collider;
import it.marte.games.pacman.util.Ray;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * Ray Ghost
 * 
 * @author AM
 * @project PacMan
 */
public class GrayGhostBrain implements Brain {

    /** Internal thinking time * */
    private static final int THINKINGTIME = 50;

    /** Update time of path * */
    private static final int UPDATETIME = 5000;

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

    private Body parent;

    private ArrayList<Ray> rays = new ArrayList<Ray>();

    public enum Direction {
	UP, DOWN, LEFT, RIGHT;
    };

    /**
     * Start RedGhost logic based on a map and a start position
     * 
     * @param map
     * @param start
     * @throws SlickException
     */
    public GrayGhostBrain(Map map, Vector2f start) throws SlickException {
	Log.info("gray ghost loaded");
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
	    dot = new Image("data/dot.gif");
	} catch (SlickException e) {
	    Log.error(e);
	}
	// updatePathToPlayer();
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

	/*
	 * // update logic of thinking of a ghost updatePlayerPositionTime =
	 * updatePlayerPositionTime + delta;
	 * 
	 * if (updatePlayerPositionTime > UPDATETIME) { updatePlayerPositionTime =
	 * 0; reThink(current, map, path); }
	 */

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
	Body one = parent;
	Body two = Map.getPlayer();

	Ray ray = new Ray(one, two);

	Ray rayLeft = getRay(Direction.RIGHT);

	rays.clear();
	rays.add(ray);
	rays.add(rayLeft);

	try {
	    path = map.getUpdatedPath((int) current.getX() / map.getTileSize(),
		    (int) current.getY() / map.getTileSize(), (int) rayLeft
			    .getTwo().getX()
			    + 32 / map.getTileSize(), (int) rayLeft.getTwo()
			    .getY()
			    / map.getTileSize());
	} catch (NullPointerException e) {
	    Log.error(e);
	    path = null;
	    cannotFindPath = true;
	}

    }

    private Ray getRay(Direction dir) {

	Body point = null;
	Circle circle = null;
	// traccio una linea infinita nella direzione scelta
	Line line = null;
	switch (dir) {
	case UP:
	    // circle = new
	    // Circle(parent.getCenterX(),parent.getCenterY()-100*32,5);
	    line = new Line(parent.getCenterX(), parent.getCenterY() - 100 * 32);
	    break;
	case DOWN:
	    // circle = new
	    // Circle(parent.getCenterX(),parent.getCenterY()+100*32,5);
	    line = new Line(parent.getCenterX(), parent.getCenterY() + 100 * 32);
	    break;
	case LEFT:
	    // circle = new
	    // Circle(parent.getCenterX()-100*32,parent.getCenterY(),5);
	    line = new Line(0, parent.getCenterY());
	    break;
	case RIGHT:
	    // circle = new
	    // Circle(parent.getCenterX()+100*32,parent.getCenterY(),5);
	    line = new Line(map.getWidthInTiles() * 32, parent.getCenterY());
	    break;
	default:
	    break;
	}
	// ritorno il body più vicino che interseca la linea
	/*
	 * ArrayList<Body> blocking = (ArrayList<Body>)
	 * map.getBlockingEntities(); Body match = null; for (Body body :
	 * blocking) { if () }
	 * 
	 */

	point = new DummyBody(Role.DUMMY, line);
	ArrayList<Body> obstacles = Collider.testAndReturnBodies(point, map
		.getBlockingEntities());
	Body match = null;
	if (obstacles.size() > 0) {
	    match = obstacles.get(0);
	    for (Body body : obstacles) {
		if (body.getCenterX() > match.getCenterX())
		    match = body;
	    }
	} else {
	    match = new Block(Role.BLOCK, circle);
	}

	return new Ray(parent, match);

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

	for (Ray ray : rays) {
	    ray.render(g);
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
     * @param current
     *                the current to set
     */
    public void setCurrent(Vector2f current) {
	this.current = current;
    }

    /**
     * @return the parent
     */
    public Body getParent() {
	return parent;
    }

    /**
     * @param parent
     *                the parent to set
     */
    public void setParent(Body parent) {
	this.parent = parent;
    }

}
