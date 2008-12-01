package it.marte.games.pacman.level;

import it.marte.games.pacman.actors.Ghost;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;
import it.marte.games.pacman.base.Score;
import it.marte.games.pacman.base.Entity.Role;
import it.marte.games.pacman.map.Map;
import it.marte.games.pacman.util.Clock;

import java.util.Iterator;
import java.util.Vector;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.util.Log;

/**
 * A Level of pacman
 * 
 * @author AM
 * @project PacMan
 */
public class SimpleLevel implements Level {

    private Vector<Entity> entities;

    private Map map;

    private boolean levelEnded = false;

    private boolean levelLose = false;

    private AngelCodeFont font;

    private Image box;

    private static Clock clock;

    /**
     * Create simpleLevel for PacMan
     * 
     * @param mapPath
     *                Tiled map definition path
     * @see Level
     */
    public SimpleLevel(String mapPath) {

	try {
	    box = new Image("data/roundBox.png");
	} catch (SlickException e2) {
	    Log.error(e2.getMessage());
	}
	try {
	    font = new AngelCodeFont("data/demo2.fnt", "data/demo2_00.tga");
	} catch (SlickException e1) {
	    Log.error(e1);
	}

	entities = new Vector<Entity>();

	try {
	    map = new Map(mapPath);
	} catch (SlickException e) {
	    Log.error("Map not found : " + mapPath);
	    e.printStackTrace();
	}
	// adding terrain to the level
	add(map);
	// adding other stuff to the level
	map.addToLevel(this);
	// init clock
	clock = new Clock();
    }

    public void add(Entity e) {
	entities.add(e);
    }

    public void clear() {
	for (int i = 0; i < entities.size(); i++) {
	    entities.get(i).removeFromLevel(this);
	    entities.remove(i);
	}
    }

    public void remove(Entity e) {
	entities.remove(e);
    }

    /**
     * Render level to screen
     * 
     */
    public void render(BasicGameState game, Graphics g) {
	for (Entity e : entities) {
	    e.render(game, g);
	}
	// User Interface
	box.draw(0, 0);
	font.drawString(10, 10, "Score: " + Score.getScore());
	font.drawString(10, 40, "Time : " + Clock.getTime());
	box.draw(0, 550);
	font.drawString(10, 560, "Lives : " + Map.getPlayer().getLive());
    }

    /**
     * Update Level Logic
     */
    public void update(GameContainer game, int delta) {
	// update entity
	for (Iterator<Entity> iterator = entities.iterator(); iterator
		.hasNext();) {
	    Entity ent = iterator.next();
	    ent.update(game, delta);
	}
	// there's some entity to remove?
	removeEntities();
	// check victory conditions
	isLevelWinned();
	// check losing conditions
	isLevelLosed();
	// update score
	Score.setScore(Map.getPlayer().getScore());
	// update clock
	clock.update(delta);
	Score.setTime(Clock.getTimer());
    }

    /**
     * @return the levelEnded
     */
    public boolean isLevelWin() {

	return levelEnded;
    }

    /**
     * @return the levelEnded
     */
    public boolean isLevelLose() {
	return levelLose;
    }

    /**
     * Check Victory conditions of current level
     */
    private void isLevelWinned() {
	int gemLeft = 0;
	for (Iterator<Entity> iterator = entities.iterator(); iterator
		.hasNext();) {
	    Entity ent = iterator.next();
	    if (ent.getRole().equals(Entity.Role.GOLD)) {
		gemLeft++;
	    }
	}
	if (gemLeft == 0) {
	    levelEnded = true;
	}
    }

    /**
     * Check losing conditions of current level
     */
    private void isLevelLosed() {
	boolean playerFound = false;
	for (Iterator<Entity> iterator = entities.iterator(); iterator
		.hasNext();) {
	    Entity ent = iterator.next();
	    if (ent.getRole().equals(Entity.Role.PLAYER)) {
		playerFound = true;
	    }
	}
	if (!playerFound) {
	    levelLose = true;
	}
    }

    /**
     * Check if there are entities to remove, and if so, removes from Level!
     * 
     * note: if player eat a particular gem, ghost change their status !
     */
    private void removeEntities() {
	Vector<Entity> removeable = new Vector<Entity>();
	for (Iterator<Entity> iterator = entities.iterator(); iterator
		.hasNext();) {
	    Entity ent = iterator.next();
	    if (ent.isToRemove()) {
		removeable.add(ent);
		if (ent.getRole().equals(Role.EATGEM)) {
		    // appena mangiata una eatgem, lo dico ai fantasmi!
		    alertGhostsEatable();
		}
	    }
	}
	entities.removeAll(removeable);
    }

    /**
     * Alert all ghost that now is time to run!
     */
    public void alertGhostsEatable() {
	for (Iterator<Entity> iterator = entities.iterator(); iterator
		.hasNext();) {
	    Entity ent = iterator.next();
	    if (ent.getRole().equals(Role.GHOST)) {
		Ghost gh = (Ghost) ent;
		gh.setEatable();
	    }
	}

    }

}
