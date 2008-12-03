package it.marte.games.pacman.actors;

import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Collider;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;
import it.marte.games.pacman.brains.GoToBaseGhostBrain;
import it.marte.games.pacman.brains.RunningGhostBrain;
import it.marte.games.pacman.map.Map;
import it.marte.games.pacman.util.SheetUtil;
import it.marte.games.pacman.util.StateManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * RedGhost, an enemy of pacman
 * 
 * @author AM
 * @project PacMan
 */
public class Ghost extends Body implements it.marte.games.pacman.base.Actor {

    public Animation sprite;

    private static final float SPEED = 0.04f;

    private Brain brain;

    private StateManager manager;

    private SpriteSheet sheet;

    private String lastDir;

    private Map parent;

    private Brain normalBrain;

    private boolean toRemove = false;

    /** Row in SpriteSheet to consider for this ghost * */
    private int rowAnim;

    public enum State {
	NORMAL, WAIT, EATABLE, DEATH;
    }

    public Ghost(Map parent, Shape shape, Brain normalBrain, int rowAnim)
	    throws SlickException {
	this.shape = shape;
	this.parent = parent;
	this.normalBrain = normalBrain;
	this.rowAnim = rowAnim;
	init();
    }

    private void init() {
	try {
	    // load graphic from sheet
	    sheet = new SpriteSheet("data/ghosts.png", 32, 32);

	    // Original orientation of the sprite. It will look right.
	    lastDir = "right";

	    // init states
	    manager = new StateManager();
	    manager.add(new NormalState());
	    manager.add(new WaitState());
	    manager.add(new EatableState());
	    manager.add(new DeathState());

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void addToLevel(Level l) {
	l.add(this);
    }

    public Role getRole() {
	return Entity.Role.GHOST;
    }

    public void onCollision(Entity obstacle) {
	manager.onCollision(obstacle);
    }

    public void removeFromLevel(Level l) {
	l.remove(this);
    }

    /**
     * Render Ghost
     */
    public void render(BasicGameState game, Graphics g) {
	manager.render(g);
	// brain.render(game,g);
    }

    /**
     * Update Ghost logic
     */
    public void update(GameContainer game, int delta) {
	manager.update(game, delta);
    }

    /**
     * check if ghost is to remove from level
     */
    public boolean isToRemove() {
	return toRemove;
    }

    private class NormalState implements it.marte.games.pacman.util.State {

	private Animation up, down, left, right;

	public boolean equals(Object state) {
	    return state == State.NORMAL;
	}

	public void enter() {
	    // load normal animations
	    right = SheetUtil.getAnimationFromSheet(sheet, rowAnim, 0, 7);
	    left = SheetUtil.getAnimationFromSheet(sheet, rowAnim, 0, 7);
	    up = SheetUtil.getAnimationFromSheet(sheet, rowAnim, 0, 7);
	    down = SheetUtil.getAnimationFromSheet(sheet, rowAnim, 0, 7);

	    doNormalAnim();

	    brain = normalBrain;
	    brain.setCurrent(getPosition());
	    brain.init();

	}

	public void onCollision(Entity obstacle) {

	}

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void update(GameContainer game, int delta) {
	    // Check Collision with player
	    Collider.testAndAlert(getBody(), Map.getPlayer());
	    // thinking about next move
	    try {
		brain.update(delta);
		doMovement(brain.getCurrentStep(), delta);
	    } catch (Exception e) {
		manager.enter(State.WAIT);
	    }
	}

	private void doNormalAnim() {
	    if (lastDir.equalsIgnoreCase("up")) {
		sprite = up;
	    } else {
		if (lastDir.equalsIgnoreCase("right")) {
		    sprite = right;
		} else {
		    if (lastDir.equalsIgnoreCase("left")) {
			sprite = left;
		    } else {
			if (lastDir.equalsIgnoreCase("down")) {
			    sprite = down;
			} else {
			    Log
				    .error("Ghost.doNormalAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

	/**
	 * Do movement based on delta and current step
	 * 
	 * @param step
	 * @param delta
	 */
	private void doMovement(Step step, int delta) {
	    // check if reached position of step
	    float cx = shape.getX();
	    float cy = shape.getY();

	    float diffX = cx - step.getX() * 32;
	    float diffY = cy - step.getY() * 32;

	    if (Math.abs(diffX) < 1 && Math.abs(diffY) < 1) {
		// goto next step
		shape.setX(step.getX() * Map.SIZE);
		shape.setY(step.getY() * Map.SIZE);
		// currentStep++;
		brain.goToNextStep(getPosition());
		// Log.debug("current step : "+ shape.getX()+","+shape.getY() +
		// ", next: "+getPosition().toString());
	    } else {
		boolean keyRight = false;
		boolean keyLeft = false;
		boolean keyUp = false;
		boolean keyDown = false;
		// try to find where i must move
		float dx = cx - step.getX() * 32;
		float dy = cy - step.getY() * 32;
		if (dx < 0) {
		    keyRight = true;
		} else {
		    if (dx > 0) {
			keyLeft = true;
		    }
		}
		if (dy < 0) {
		    keyDown = true;
		} else {
		    if (dy > 0) {
			keyUp = true;
		    }
		}
		// do movement
		if (keyUp) {
		    sprite = up;
		    sprite.update(delta);
		    float y = getY() - delta * SPEED;
		    shape.setY(y);
		    lastDir = "up";
		} else {
		    if (keyDown) {
			sprite = down;
			sprite.update(delta);
			float y = getY() + delta * SPEED;
			shape.setY(y);
			lastDir = "down";
		    } else {
			if (keyLeft) {
			    sprite = left;
			    sprite.update(delta);
			    float x = getX() - delta * SPEED;
			    shape.setX(x);
			    lastDir = "left";
			} else {
			    if (keyRight) {
				sprite = right;
				sprite.update(delta);
				float x = getX() + delta * SPEED;
				shape.setX(x);
				lastDir = "right";
			    }
			}
		    }
		}
	    }
	}

    }

    private class WaitState implements it.marte.games.pacman.util.State {

	private int timer;

	public boolean equals(Object state) {
	    return state == State.WAIT;
	}

	public void enter() {
	    timer = 0;
	}

	public void onCollision(Entity obstacle) {

	}

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void update(GameContainer game, int delta) {
	    timer = timer + delta;
	    if (timer > 1000) {

		if (manager.getPreviousState().equals(State.NORMAL)) {
		    manager.enter(State.NORMAL);
		}
		if (manager.getPreviousState().equals(State.EATABLE)) {
		    manager.enter(State.EATABLE);
		}
		manager.enter(State.NORMAL);
	    }
	}

    }

    private class EatableState implements it.marte.games.pacman.util.State {

	private int timer;

	private Animation eatedUp, eatedDown, eatedLeft, eatedRight;

	public boolean equals(Object state) {
	    return state == State.EATABLE;
	}

	public void enter() {
	    // load eated animations
	    eatedRight = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 7);
	    eatedLeft = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 7);
	    eatedUp = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 7);
	    eatedDown = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 7);

	    doEatAnim();

	    try {
		brain = new RunningGhostBrain(parent, getPosition());
	    } catch (SlickException e) {
		Log.error(e);
	    }

	    timer = 0;
	}

	public void onCollision(Entity obstacle) {
	    if (obstacle.getRole().equals(Role.PLAYER)) {
		manager.enter(State.DEATH);
	    }
	}

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void update(GameContainer game, int delta) {
	    timer = timer + delta;
	    if (timer > 10000) {
		manager.enter(State.NORMAL);
	    }

	    // Check Collision with player
	    Collider.testAndAlert(getBody(), Map.getPlayer());
	    // thinking about next move
	    try {
		brain.update(delta);
		doMovement(brain.getCurrentStep(), delta);
	    } catch (Exception e) {
		manager.enter(State.WAIT);
	    }
	}

	private void doEatAnim() {
	    if (lastDir.equalsIgnoreCase("up")) {
		sprite = eatedUp;
	    } else {
		if (lastDir.equalsIgnoreCase("right")) {
		    sprite = eatedRight;
		} else {
		    if (lastDir.equalsIgnoreCase("left")) {
			sprite = eatedLeft;
		    } else {
			if (lastDir.equalsIgnoreCase("down")) {
			    sprite = eatedDown;
			} else {
			    Log
				    .error("Player.doEatAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

	/**
	 * Do movement based on delta and current step
	 * 
	 * @param step
	 * @param delta
	 */
	private void doMovement(Step step, int delta) {
	    // check if reached position of step
	    float cx = shape.getX();
	    float cy = shape.getY();

	    float diffX = cx - step.getX() * 32;
	    float diffY = cy - step.getY() * 32;

	    if (Math.abs(diffX) < 1 && Math.abs(diffY) < 1) {
		// goto next step
		shape.setX(step.getX() * Map.SIZE);
		shape.setY(step.getY() * Map.SIZE);
		// currentStep++;
		brain.goToNextStep(getPosition());
	    } else {
		boolean keyRight = false;
		boolean keyLeft = false;
		boolean keyUp = false;
		boolean keyDown = false;
		// try to find where i must move
		float dx = cx - step.getX() * 32;
		float dy = cy - step.getY() * 32;
		if (dx < 0) {
		    keyRight = true;
		} else {
		    if (dx > 0) {
			keyLeft = true;
		    }
		}
		if (dy < 0) {
		    keyDown = true;
		} else {
		    if (dy > 0) {
			keyUp = true;
		    }
		}
		// do movement
		if (keyUp) {
		    sprite = eatedUp;
		    sprite.update(delta);
		    float y = getY() - delta * SPEED;
		    shape.setY(y);
		    lastDir = "up";
		} else {
		    if (keyDown) {
			sprite = eatedDown;
			sprite.update(delta);
			float y = getY() + delta * SPEED;
			shape.setY(y);
			lastDir = "down";
		    } else {
			if (keyLeft) {
			    sprite = eatedLeft;
			    sprite.update(delta);
			    float x = getX() - delta * SPEED;
			    shape.setX(x);
			    lastDir = "left";
			} else {
			    if (keyRight) {
				sprite = eatedRight;
				sprite.update(delta);
				float x = getX() + delta * SPEED;
				shape.setX(x);
				lastDir = "right";
			    }
			}
		    }
		}
	    }
	}

    }

    private class DeathState implements it.marte.games.pacman.util.State {

	private Animation deathUp, deathDown, deathLeft, deathRight;

	public boolean equals(Object state) {
	    return state == State.DEATH;
	}

	public void enter() {
	    // load eated animations
	    deathRight = SheetUtil.getAnimationFromSheet(sheet, 5, 0, 7);
	    deathLeft = SheetUtil.getAnimationFromSheet(sheet, 5, 0, 7);
	    deathUp = SheetUtil.getAnimationFromSheet(sheet, 5, 0, 7);
	    deathDown = SheetUtil.getAnimationFromSheet(sheet, 5, 0, 7);

	    doDeathAnim();

	    try {
		brain = new GoToBaseGhostBrain(parent, getPosition());
	    } catch (SlickException e) {
		Log.error(e);
	    }
	}

	public void onCollision(Entity obstacle) {
	}

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void update(GameContainer game, int delta) {
	    if (brain.isCannotFindPath()) {
		manager.enter(State.NORMAL);
	    }

	    // thinking about next move
	    try {
		brain.update(delta);
		doMovement(brain.getCurrentStep(), delta);
	    } catch (Exception e) {
		manager.enter(State.WAIT);
	    }
	}

	private void doDeathAnim() {
	    if (lastDir.equalsIgnoreCase("up")) {
		sprite = deathUp;
	    } else {
		if (lastDir.equalsIgnoreCase("right")) {
		    sprite = deathRight;
		} else {
		    if (lastDir.equalsIgnoreCase("left")) {
			sprite = deathLeft;
		    } else {
			if (lastDir.equalsIgnoreCase("down")) {
			    sprite = deathDown;
			} else {
			    Log
				    .error("Ghost.doDeathAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

	/**
	 * Do movement based on delta and current step
	 * 
	 * @param step
	 * @param delta
	 */
	private void doMovement(Step step, int delta) {
	    // check if reached position of step
	    float cx = shape.getX();
	    float cy = shape.getY();

	    float diffX = cx - step.getX() * 32;
	    float diffY = cy - step.getY() * 32;

	    if (Math.abs(diffX) < 1 && Math.abs(diffY) < 1) {
		// goto next step
		shape.setX(step.getX() * Map.SIZE);
		shape.setY(step.getY() * Map.SIZE);
		// currentStep++;
		brain.goToNextStep(getPosition());
	    } else {
		boolean keyRight = false;
		boolean keyLeft = false;
		boolean keyUp = false;
		boolean keyDown = false;
		// try to find where i must move
		float dx = cx - step.getX() * 32;
		float dy = cy - step.getY() * 32;
		if (dx < 0) {
		    keyRight = true;
		} else {
		    if (dx > 0) {
			keyLeft = true;
		    }
		}
		if (dy < 0) {
		    keyDown = true;
		} else {
		    if (dy > 0) {
			keyUp = true;
		    }
		}
		// do movement
		if (keyUp) {
		    sprite = deathUp;
		    sprite.update(delta);
		    float y = getY() - delta * SPEED;
		    shape.setY(y);
		    lastDir = "up";
		} else {
		    if (keyDown) {
			sprite = deathDown;
			sprite.update(delta);
			float y = getY() + delta * SPEED;
			shape.setY(y);
			lastDir = "down";
		    } else {
			if (keyLeft) {
			    sprite = deathLeft;
			    sprite.update(delta);
			    float x = getX() - delta * SPEED;
			    shape.setX(x);
			    lastDir = "left";
			} else {
			    if (keyRight) {
				sprite = deathRight;
				sprite.update(delta);
				float x = getX() + delta * SPEED;
				shape.setX(x);
				lastDir = "right";
			    }
			}
		    }
		}
	    }
	}

    }

    public void setEatable() {
	manager.enter(State.EATABLE);
    }

    private Body getBody() {
	return this;
    }

    public it.marte.games.pacman.util.State getState() {
	return (it.marte.games.pacman.util.State) manager.currentState();
    }

    public void setToRemove() {
	toRemove = true;
    }

}
