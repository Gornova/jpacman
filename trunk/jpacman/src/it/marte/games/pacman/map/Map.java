/**
 * 
 */
package it.marte.games.pacman.map;

import it.marte.games.pacman.actors.Block;
import it.marte.games.pacman.actors.Brain;
import it.marte.games.pacman.actors.EatGem;
import it.marte.games.pacman.actors.Gem;
import it.marte.games.pacman.actors.Ghost;
import it.marte.games.pacman.actors.Player;
import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;
import it.marte.games.pacman.brains.BlueGhostBrain;
import it.marte.games.pacman.brains.GrayGhostBrain;
import it.marte.games.pacman.brains.GreenGhostBrain;
import it.marte.games.pacman.brains.OrangeGhostBrain;
import it.marte.games.pacman.brains.PinkGhostBrain;
import it.marte.games.pacman.brains.RedGhostBrain;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * @author AM
 * @project WizardGame
 */
public class Map implements Entity, TileBasedMap {

    private TiledMap map;

    private static Player player;

    public enum LAYER {
	terrain, entity, bonus
    };

    public static final int SIZE = 32;

    /** blocking entities */
    private Iterable<Body> blockingEnt = null;

    private boolean[][] blocked;

    /** collectable entities */
    private Iterable<Body> collectableEnt = null;

    /** collectable entities */
    private Iterable<Body> eatGemEnt = null;

    /** ghost entities */
    private Iterable<Body> ghostEnt;

    /** ghost main base! */
    private Vector2f base;

    private Hashtable<String, Vector2f> bluePoint;

    private Vector2f cornerUpLeft = new Vector2f(1000, 1000);
    private Vector2f cornerUpRight = new Vector2f(0, 0);
    private Vector2f cornerDownLeft = new Vector2f(0, 1000);
    private Vector2f cornerDownRight = new Vector2f(0, 0);

    /**
     * Load map from specified mapPath and blocking entities used to do
     * collision
     * 
     * @param mapPath
     * @throws SlickException
     */
    public Map(String mapPath) throws SlickException {
	map = new TiledMap(mapPath);
	player = loadPlayer();
	blocked = getCollisionMatrix(map, "blocked", "false");
	blockingEnt = loadBlockingEntities(LAYER.terrain, "blocked");
	collectableEnt = loadGemEntities(LAYER.bonus, "gem");
	eatGemEnt = loadEatGemEntities(LAYER.bonus, "eatGem");
	bluePoint = loadBluePoint(LAYER.entity, "position");

	ghostEnt = loadGhostEntities(LAYER.entity, "ghost");
	base = loadBaseEntity(Map.LAYER.entity, "base");

	// init loading of corners from map
	initCorners();
    }

    public void render(BasicGameState game, Graphics g) {
	map.render(0, 0, 0, 0, SIZE, SIZE, LAYER.terrain.ordinal(), false);
    }

    public void update(GameContainer game, int delta) {
    }

    private Player loadPlayer() throws SlickException {
	Player pl = null;
	// adding player to level
	try {
	    pl = new Player(this, Role.PLAYER, getPlayerStart(Map.LAYER.entity,
		    "player"));
	} catch (SlickException e) {
	    throw new SlickException("cannot find player position "
		    + e.getMessage());
	}
	return pl;
    }

    /**
     * A blocking entity is an tile with prop at "true"
     * 
     * for example "blocked" = "true"
     * 
     * @param layer
     * @param prop
     * @return blocking entity
     */
    private Iterable<Body> loadBlockingEntities(LAYER layer, String prop) {
	Vector<Body> ent = new Vector<Body>();

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		// blocked
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    int blockSize = map.getTileHeight();
		    int xrec = xAxis * blockSize;
		    int yrec = yAxis * blockSize;
		    Rectangle rect = new Rectangle(xrec, yrec, blockSize,
			    blockSize);
		    // a block is a logical entity, without graphical
		    // reprensetation
		    Block block = new Block(Role.BLOCK, rect);
		    ent.add(block);
		}
	    }
	}
	return ent;
    }

    /**
     * A blocking entity is an tile with prop at "true"
     * 
     * for example "blocked" = "true"
     * 
     * @param layer
     * @param prop
     * @return blocking entity
     */
    private Iterable<Body> loadGemEntities(LAYER layer, String prop) {
	Vector<Body> ent = new Vector<Body>();

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		// blocked
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    int blockSize = map.getTileHeight();
		    int xrec = xAxis * blockSize;
		    int yrec = yAxis * blockSize;
		    Rectangle rect = new Rectangle(xrec, yrec, blockSize,
			    blockSize);
		    Image gem = map.getTileImage(xAxis, yAxis, layer.ordinal());
		    Gem block = new Gem(Role.GOLD, rect, gem);
		    ent.add(block);
		}
	    }
	}
	return ent;
    }

    /**
     * A blocking entity is an tile with prop at "true"
     * 
     * for example "blocked" = "true"
     * 
     * @param layer
     * @param prop
     * @return blocking entity
     */
    private Iterable<Body> loadEatGemEntities(LAYER layer, String prop) {
	Vector<Body> ent = new Vector<Body>();

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		// blocked
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    int blockSize = map.getTileHeight();
		    int xrec = xAxis * blockSize;
		    int yrec = yAxis * blockSize;
		    Rectangle rect = new Rectangle(xrec, yrec, blockSize,
			    blockSize);
		    Image gem = map.getTileImage(xAxis, yAxis, layer.ordinal());
		    EatGem block = new EatGem(Role.EATGEM, rect, gem);
		    ent.add(block);
		}
	    }
	}
	return ent;
    }

    /**
     * A blocking entity is an tile with prop at "true"
     * 
     * for example "blocked" = "true"
     * 
     * @param layer
     * @param prop
     * @return blocking entity
     */
    private Iterable<Body> loadGhostEntities(LAYER layer, String prop) {
	Vector<Body> ent = new Vector<Body>();

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		// blocked
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    int blockSize = map.getTileHeight();
		    int xrec = xAxis * blockSize;
		    int yrec = yAxis * blockSize;
		    Rectangle rect = new Rectangle(xrec, yrec, blockSize,
			    blockSize);
		    Ghost ghost = null;
		    try {
			String color = map.getTileProperty(tileID, "red",
				"false");
			Brain b = null;
			if (color.equals("true")) {
			    Vector2f pos = new Vector2f();
			    pos.set(xAxis * 32, yAxis * 32);
			    b = new RedGhostBrain(this, pos);
			    ghost = new Ghost(this, Role.GHOST, rect, b, 0);
			}
			color = map.getTileProperty(tileID, "pink", "false");
			if (color.equals("true")) {
			    Vector2f pos = new Vector2f();
			    pos.set(xAxis * 32, yAxis * 32);
			    b = new PinkGhostBrain(this, pos);
			    ghost = new Ghost(this, Role.GHOST, rect, b, 2);
			}
			color = map.getTileProperty(tileID, "blue", "false");
			if (color.equals("true")) {
			    Vector2f pos = new Vector2f();
			    pos.set(xAxis * 32, yAxis * 32);
			    b = new BlueGhostBrain(this, pos);
			    ghost = new Ghost(this, Role.GHOST, rect, b, 3);
			}
			color = map.getTileProperty(tileID, "orange", "false");
			if (color.equals("true")) {
			    Vector2f pos = new Vector2f();
			    pos.set(xAxis * 32, yAxis * 32);
			    b = new OrangeGhostBrain(this, pos);
			    ghost = new Ghost(this, Role.GHOST, rect, b, 1);
			}
			color = map.getTileProperty(tileID, "green", "false");
			if (color.equals("true")) {
			    Vector2f pos = new Vector2f();
			    pos.set(xAxis * 32, yAxis * 32);
			    b = new GreenGhostBrain(this, pos);
			    ghost = new Ghost(this, Role.GHOST, rect, b, 6);
			}
			color = map.getTileProperty(tileID, "gray", "false");
			if (color.equals("true")) {
			    Vector2f pos = new Vector2f();
			    pos.set(xAxis * 32, yAxis * 32);
			    b = new GrayGhostBrain(this, pos);
			    ghost = new Ghost(this, Role.GHOST, rect, b, 7);
			    // TODO: hack!! baaad hack!
			    ((GrayGhostBrain) ghost.getBrain())
				    .setParent(ghost);
			}

		    } catch (SlickException e) {
			Log.error(e);
		    }
		    ent.add(ghost);
		}
	    }
	}
	return ent;
    }

    private Hashtable<String, Vector2f> loadBluePoint(LAYER layer, String prop) {
	Hashtable<String, Vector2f> positions = new Hashtable<String, Vector2f>();

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		// blocked
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    int blockSize = map.getTileHeight();
		    int xrec = xAxis * blockSize;
		    int yrec = yAxis * blockSize;

		    String name = map.getTileProperty(tileID, "a", "false");
		    if (name.equals("true")) {
			positions.put("a", new Vector2f(xrec, yrec));
		    }
		    name = map.getTileProperty(tileID, "b", "false");
		    if (name.equals("true")) {
			positions.put("b", new Vector2f(xrec, yrec));
		    }
		    name = map.getTileProperty(tileID, "c", "false");
		    if (name.equals("true")) {
			positions.put("c", new Vector2f(xrec, yrec));
		    }
		    name = map.getTileProperty(tileID, "d", "false");
		    if (name.equals("true")) {
			positions.put("d", new Vector2f(xrec, yrec));
		    }

		}
	    }
	}
	return positions;
    }

    private Vector2f loadBaseEntity(LAYER layer, String prop) {
	Vector2f base = new Vector2f(0, 0);

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    base = new Vector2f(xAxis, yAxis);
		    return base;
		}
	    }
	}
	return base;
    }

    public void addToLevel(Level l) {
	// adding collectable entities loaded from map
	Vector<Body> collectable = (Vector<Body>) getCollectableEnt();
	for (Iterator<Body> iterator = collectable.iterator(); iterator
		.hasNext();) {
	    Body bod = iterator.next();
	    bod.addToLevel(l);
	    l.add(bod);
	}
	// adding eatable Gem to the level
	Vector<Body> eatableGem = (Vector<Body>) getEatGemEnt();
	for (Iterator<Body> iterator = eatableGem.iterator(); iterator
		.hasNext();) {
	    Body bod = iterator.next();
	    bod.addToLevel(l);
	    l.add(bod);
	}
	// adding player to level
	l.add(player);
	// adding ghosts entities loaded from map
	Vector<Body> ghosts = (Vector<Body>) getGhostEnt();
	for (Iterator<Body> iterator = ghosts.iterator(); iterator.hasNext();) {
	    Body bod = iterator.next();
	    bod.addToLevel(l);
	    l.add(bod);
	}
    }

    public Role getRole() {
	return Entity.Role.MAP;
    }

    public void onCollision(Entity obstacle) {
	// cannot collide with anyone
    }

    public void removeFromLevel(Level l) {
    }

    /**
     * @return entities that player cannot walk
     */
    public Iterable<Body> getBlockingEntities() {
	return blockingEnt;
    }

    public Shape getPlayerStart(LAYER layer, String prop) {
	Shape player = null;

	for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
	    for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
		int tileID = map.getTileId(xAxis, yAxis, layer.ordinal());
		// blocked
		String value = map.getTileProperty(tileID, prop, "false");
		if ("true".equals(value)) {
		    int blockSize = map.getTileHeight();
		    int xrec = xAxis * blockSize;
		    int yrec = yAxis * blockSize;
		    Rectangle rect = new Rectangle(xrec, yrec, blockSize,
			    blockSize);
		    // a block is a logical entity, without graphical
		    // reprensetation
		    player = rect;
		}
	    }
	}
	return player;
    }

    /**
     * @return the collectableEnt
     */
    public Iterable<Body> getCollectableEnt() {
	return collectableEnt;
    }

    /**
     * @return the ghostEnt
     */
    public Iterable<Body> getGhostEnt() {
	return ghostEnt;
    }

    /**
     * @return the player
     */
    public static Player getPlayer() {
	return player;
    }

    public boolean isToRemove() {
	return false;
    }

    public Path getUpdatedPath(int sx, int sy, int ex, int ey)
	    throws NullPointerException {
	Path path;

	// find any blocked paths
	AStarPathFinder pathfinder = new AStarPathFinder(this, 1000, false);
	Mover dummyMover = new Mover() {
	};
	path = pathfinder.findPath(dummyMover, sx, sy, ex, ey);
	if (path != null) {
	    return path;
	} else {
	    // return null;
	    throw new NullPointerException("cannot find a path");
	}
    }

    public boolean blocked(PathFindingContext contex, int x, int y) {
	if (blocked == null) {
	    blocked = getCollisionMatrix(map, "blocked", "false");
	}
	try {
	    if (blocked[x][y]) {
		return true;
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	    return true;
	}
	return false;
    }

    public float getCost(PathFindingContext contex, int x, int y) {
	return 0;
    }

    public int getHeightInTiles() {
	return map.getHeight();
    }

    public int getWidthInTiles() {
	return map.getWidth();
    }

    public void pathFinderVisited(int x, int y) {

    }

    /**
     * @param map
     * @param key
     *                nome proprietà da valutare
     * @param value
     *                valore della proprietà che rende bloccante quel tile
     * @return matrice di collisione della mappa
     */
    private boolean[][] getCollisionMatrix(TiledMap map, String key,
	    String value) {
	boolean[][] matrix = new boolean[map.getWidth()][map.getHeight()];
	for (int x = 0; x < map.getWidth(); x++) {
	    for (int y = 0; y < map.getHeight(); y++) {
		int tileID = map.getTileId(x, y, 0);
		String temp = map.getTileProperty(tileID, key, value);
		if ("true".equals(temp)) {
		    matrix[x][y] = true;
		}
	    }
	}
	return matrix;
    }

    /**
     * @return the eatGemEnt
     */
    public Iterable<Body> getEatGemEnt() {
	return eatGemEnt;
    }

    /**
     * Load corners of map
     */
    private void initCorners() {
	boolean first = true;
	for (int x = 0; x < map.getWidth(); x++) {
	    for (int y = 0; y < map.getHeight(); y++) {
		boolean block = blocked[x][y];
		if (!block) {
		    if (first) {
			first = false;
			cornerUpLeft.set(x, y);
		    }
		    if (x >= cornerUpRight.x && y > cornerUpRight.y) {
			cornerUpRight.set(x, y);
		    }
		    if (x >= cornerDownLeft.x && y <= cornerDownLeft.y) {
			cornerDownLeft.set(x, y);
		    }
		    if (x >= cornerDownRight.x && y >= cornerDownRight.y) {
			cornerDownRight.set(x, y);
		    }
		}
	    }
	}
    }

    /**
     * @return Return a random corner from map
     */
    public Vector2f getRandomCorner() {

	Vector2f corner = new Vector2f(0, 0);

	Random rnd = new Random();
	int value = rnd.nextInt(3);
	switch (value) {
	case 0:
	    corner.set(cornerUpLeft);
	    break;
	case 1:
	    corner.set(cornerUpRight);
	    break;
	case 2:
	    corner.set(cornerDownLeft);
	    break;
	case 3:
	    corner.set(cornerDownRight);
	    break;
	default:
	    corner.set(cornerUpLeft);
	    break;
	}

	return corner;
    }

    /**
     * @return a random valid (not-blocked) point into map
     * 
     * note: already multiplied for map Size!
     */
    public Vector2f getRandomPoint() {
	Vector2f point = new Vector2f();
	Random rnd = new Random();
	boolean found = false;
	while (!found) {
	    int rndX = rnd.nextInt(map.getWidth() - 1);
	    int rndY = rnd.nextInt(map.getHeight() - 1);

	    if (!blocked[rndX][rndY]) {
		point.x = rndX * 32;
		point.y = rndY * 32;
		found = true;
	    }
	}
	return point;
    }

    /**
     * @return the base
     */
    public Vector2f getBase() {
	return base;
    }

    /**
     * @return the bluePoint
     */
    public Hashtable<String, Vector2f> getBluePoint() {
	return bluePoint;
    }

    /**
     * Return map tile size, assuming is width=height
     * 
     * @return
     */
    public int getTileSize() {
	return map.getTileHeight();
    }

}
