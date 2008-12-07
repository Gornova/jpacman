package it.marte.games.pacman.actors;

import it.marte.games.pacman.base.Body;
import it.marte.games.pacman.base.Collider;
import it.marte.games.pacman.base.Entity;
import it.marte.games.pacman.base.Level;
import it.marte.games.pacman.map.Map;
import it.marte.games.pacman.util.SheetUtil;
import it.marte.games.pacman.util.StateManager;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.util.Log;

/**
 * Player-Controlled Pacman
 * 
 * @author AM
 * @project PacMan
 */
public class Player extends Body {

    private Map parent;

    private int score;

    private boolean isToRemove = false;

    private static final float SPEED = 0.15f;

    private SpriteSheet sheet;

    private Animation sprite;

    private enum State {
	NORMAL, POWER, BLINK, DEATH
    }

    private String lastDir;

    private int lives;

    private StateManager manager;

    public Player(Map parent, Shape shape) throws SlickException {
	this.parent = parent;
	shape.setLocation(shape.getX() + 5, shape.getY() + 5);
	this.shape = shape;
	init();
    }

    /**
     * Init graphics of pacman
     */
    private void init() {
	try {
	    // Original orientation of the sprite. It will look right.
	    lastDir = "right";

	    // load sprite sheet
	    sheet = new SpriteSheet("data/pacman.gif", 32, 32);

	    // init states
	    manager = new StateManager();
	    manager.add(new NormalState());
	    manager.add(new PowerState());
	    manager.add(new DeathState());
	    manager.add(new BlinkState());

	    // set initial score for the player
	    score = 0;
	    // set starting life count
	    lives = 3;

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private class NormalState implements it.marte.games.pacman.util.State {

	private Animation up, down, left, right;

	public boolean equals(Object state) {
	    return state == State.NORMAL;
	}

	public void enter() {
	    // load basic animations
	    right = SheetUtil.getAnimationFromSheet(sheet, 0, 0, 2);
	    left = SheetUtil.getAnimationFromSheet(sheet, 1, 0, 2);
	    up = SheetUtil.getAnimationFromSheet(sheet, 2, 0, 2);
	    down = SheetUtil.getAnimationFromSheet(sheet, 3, 0, 2);

	    doNormalAnim();
	}

	public void update(GameContainer game, int delta) {
	    Input input = game.getInput();

	    boolean keyRight = input.isKeyDown(Input.KEY_RIGHT);
	    boolean keyLeft = input.isKeyDown(Input.KEY_LEFT);
	    boolean keyUp = input.isKeyDown(Input.KEY_UP);
	    boolean keyDown = input.isKeyDown(Input.KEY_DOWN);

	    if (keyUp) {
		sprite = getCurrentAnimation(true, false, false, false);
		if (tryMove(getX(), getY() - delta * SPEED)) {
		    sprite.update(delta);
		    float y = getY() - delta * SPEED;
		    shape.setY(y);
		    lastDir = "up";
		}
	    } else {
		if (keyDown) {
		    sprite = getCurrentAnimation(false, true, false, false);
		    if (tryMove(getX(), getY() + delta * SPEED)) {
			sprite.update(delta);
			float y = getY() + delta * SPEED;
			shape.setY(y);
			lastDir = "down";
		    }
		} else {
		    if (keyLeft) {
			sprite = getCurrentAnimation(false, false, false, true);
			if (tryMove(getX() - delta * SPEED, getY())) {
			    sprite.update(delta);
			    float x = getX() - delta * SPEED;
			    shape.setX(x);
			    lastDir = "left";
			}
		    } else {
			if (keyRight) {
			    sprite = getCurrentAnimation(false, false, true,
				    false);
			    if (tryMove(getX() + delta * SPEED, getY())) {
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

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void onCollision(Entity obstacle) {
	    if (obstacle.getRole().equals(Entity.Role.EATGEM)) {
		manager.enter(State.POWER);
		score = score - 50;
	    }
	    if (obstacle.getRole().equals(Entity.Role.GOLD)) {
		score++;
	    }
	    if (obstacle.getRole().equals(Entity.Role.GHOST)) {
		lives--;
		if (lives == 0) {
		    manager.enter(State.DEATH);
		} else {

		    Ghost gh = (Ghost) obstacle;
		    if (gh.getState().equals(Ghost.State.EATABLE)) {
		    } else {
			manager.enter(State.BLINK);
		    }
		}
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
				    .error("Player.doNormalAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

	private Animation getCurrentAnimation(boolean upDir, boolean downDir,
		boolean rightDir, boolean leftDir) {
	    // check last direction of movement
	    if (upDir) {
		return up;
	    }
	    if (downDir) {
		return down;
	    }
	    if (leftDir) {
		return left;
	    }
	    if (rightDir) {
		return right;
	    }
	    return null;
	}

    }

    private class PowerState implements it.marte.games.pacman.util.State {

	private int timer;

	private Animation up, down, left, right;

	public boolean equals(Object state) {
	    return state == State.POWER;
	}

	public void enter() {
	    // load basic animations
	    right = SheetUtil.getAnimationFromSheet(sheet, 0, 0, 2);
	    left = SheetUtil.getAnimationFromSheet(sheet, 1, 0, 2);
	    up = SheetUtil.getAnimationFromSheet(sheet, 2, 0, 2);
	    down = SheetUtil.getAnimationFromSheet(sheet, 3, 0, 2);

	    doNormalAnim();
	    timer = 0;
	}

	public void update(GameContainer game, int delta) {
	    timer = timer + delta;
	    if (timer > 10000) {
		manager.enter(State.NORMAL);
	    }
	    Input input = game.getInput();

	    boolean keyRight = input.isKeyDown(Input.KEY_RIGHT);
	    boolean keyLeft = input.isKeyDown(Input.KEY_LEFT);
	    boolean keyUp = input.isKeyDown(Input.KEY_UP);
	    boolean keyDown = input.isKeyDown(Input.KEY_DOWN);

	    if (keyUp) {
		sprite = getCurrentAnimation(true, false, false, false);
		if (tryMove(getX(), getY() - delta * SPEED)) {
		    sprite.update(delta);
		    float y = getY() - delta * SPEED;
		    shape.setY(y);
		    lastDir = "up";
		}
	    } else {
		if (keyDown) {
		    sprite = getCurrentAnimation(false, true, false, false);
		    if (tryMove(getX(), getY() + delta * SPEED)) {
			sprite.update(delta);
			float y = getY() + delta * SPEED;
			shape.setY(y);
			lastDir = "down";
		    }
		} else {
		    if (keyLeft) {
			sprite = getCurrentAnimation(false, false, false, true);
			if (tryMove(getX() - delta * SPEED, getY())) {
			    sprite.update(delta);
			    float x = getX() - delta * SPEED;
			    shape.setX(x);
			    lastDir = "left";
			}
		    } else {
			if (keyRight) {
			    sprite = getCurrentAnimation(false, false, true,
				    false);
			    if (tryMove(getX() + delta * SPEED, getY())) {
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

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void onCollision(Entity obstacle) {
	    if (obstacle.getRole().equals(Entity.Role.GOLD)) {
		score++;
	    }
	    if (obstacle.getRole().equals(Entity.Role.GHOST)) {
		score = score + 10;
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
				    .error("Player.doNormalAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

	private Animation getCurrentAnimation(boolean upDir, boolean downDir,
		boolean rightDir, boolean leftDir) {
	    // check last direction of movement
	    if (upDir) {
		return up;
	    }
	    if (downDir) {
		return down;
	    }
	    if (leftDir) {
		return left;
	    }
	    if (rightDir) {
		return right;
	    }
	    return null;
	}

    }

    private class DeathState implements it.marte.games.pacman.util.State {

	private Animation deathUp, deathDown, deathLeft, deathRight;

	public boolean equals(Object state) {
	    return state == State.DEATH;
	}

	public void enter() {
	    // load death animation
	    deathRight = SheetUtil.getAnimationFromSheet(sheet, 0, 3, 8);
	    deathLeft = SheetUtil.getAnimationFromSheet(sheet, 1, 3, 8);
	    deathUp = SheetUtil.getAnimationFromSheet(sheet, 2, 3, 8);
	    deathDown = SheetUtil.getAnimationFromSheet(sheet, 3, 3, 8);

	    deathRight.setLooping(false);
	    deathLeft.setLooping(false);
	    deathUp.setLooping(false);
	    deathDown.setLooping(false);

	    // set to do death anim!
	    doDeathAnim();
	}

	public void update(GameContainer game, int delta) {
	    sprite.update(delta);
	    int frameCount = sprite.getFrameCount();
	    int currentFrame = sprite.getFrame();
	    if (currentFrame == frameCount - 1) {
		isToRemove = true;
	    }
	}

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void onCollision(Entity obstacle) {
	}

	/**
	 * Set pacman to do next update death animation, basing on wich is his
	 * last direction
	 */
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
				    .error("Player.doDeathAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

    }

    private class BlinkState implements it.marte.games.pacman.util.State {

	private int blinkTimer;

	private static final int BLINKTIME = 5000;

	private Animation blinkUp, blinkDown, blinkLeft, blinkRight;

	public boolean equals(Object state) {
	    return state == State.BLINK;
	}

	public void enter() {
	    // load blink animation
	    blinkRight = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 2);
	    blinkLeft = SheetUtil.getAnimationFromSheet(sheet, 5, 0, 2);
	    blinkUp = SheetUtil.getAnimationFromSheet(sheet, 6, 0, 2);
	    blinkDown = SheetUtil.getAnimationFromSheet(sheet, 7, 0, 2);

	    blinkRight.setAutoUpdate(true);
	    blinkLeft.setAutoUpdate(true);
	    blinkUp.setAutoUpdate(true);
	    blinkDown.setAutoUpdate(true);

	    doBlinkAnim();

	    // set initial blinking Timer
	    blinkTimer = 0;
	}

	public void update(GameContainer game, int delta) {
	    Input input = game.getInput();

	    boolean keyRight = input.isKeyDown(Input.KEY_RIGHT);
	    boolean keyLeft = input.isKeyDown(Input.KEY_LEFT);
	    boolean keyUp = input.isKeyDown(Input.KEY_UP);
	    boolean keyDown = input.isKeyDown(Input.KEY_DOWN);

	    if (keyUp) {
		sprite = getCurrentAnimation(true, false, false, false);
		if (tryMove(getX(), getY() - delta * SPEED)) {
		    sprite.update(delta);
		    float y = getY() - delta * SPEED;
		    shape.setY(y);
		    lastDir = "up";
		}
	    } else {
		if (keyDown) {
		    sprite = getCurrentAnimation(false, true, false, false);
		    if (tryMove(getX(), getY() + delta * SPEED)) {
			sprite.update(delta);
			float y = getY() + delta * SPEED;
			shape.setY(y);
			lastDir = "down";
		    }
		} else {
		    if (keyLeft) {
			sprite = getCurrentAnimation(false, false, false, true);
			if (tryMove(getX() - delta * SPEED, getY())) {
			    sprite.update(delta);
			    float x = getX() - delta * SPEED;
			    shape.setX(x);
			    lastDir = "left";
			}
		    } else {
			if (keyRight) {
			    sprite = getCurrentAnimation(false, false, true,
				    false);
			    if (tryMove(getX() + delta * SPEED, getY())) {
				sprite.update(delta);
				float x = getX() + delta * SPEED;
				shape.setX(x);
				lastDir = "right";
			    }
			}
		    }
		}
	    }
	    blinkTimer = blinkTimer + delta;
	    if (blinkTimer > BLINKTIME) {
		blinkTimer = 0;
		manager.enter(State.NORMAL);
	    }
	}

	public void render(Graphics g) {
	    sprite.draw(getX(), getY());
	}

	public void onCollision(Entity obstacle) {
	}

	private void doBlinkAnim() {
	    if (lastDir.equalsIgnoreCase("up")) {
		sprite = blinkUp;
	    } else {
		if (lastDir.equalsIgnoreCase("right")) {
		    sprite = blinkRight;
		} else {
		    if (lastDir.equalsIgnoreCase("left")) {
			sprite = blinkLeft;
		    } else {
			if (lastDir.equalsIgnoreCase("down")) {
			    sprite = blinkDown;
			} else {
			    Log
				    .error("Player.doBlinkAnim - impossibile to determine current anim");
			    return;
			}
		    }
		}

	    }
	}

	private Animation getCurrentAnimation(boolean upDir, boolean downDir,
		boolean rightDir, boolean leftDir) {
	    // check last direction of movement
	    if (upDir) {
		return blinkUp;
	    }
	    if (downDir) {
		return blinkDown;
	    }
	    if (leftDir) {
		return blinkLeft;
	    }
	    if (rightDir) {
		return blinkRight;
	    }
	    return null;
	}

    }

    public void addToLevel(Level l) {
	l.add(this);
    }

    public Role getRole() {
	return Entity.Role.PLAYER;
    }

    public void onCollision(Entity obstacle) {
	manager.onCollision(obstacle);
    }

    public void removeFromLevel(Level l) {
	l.remove(this);
    }

    public void render(BasicGameState game, Graphics g) {
	manager.render(g);
    }

    public void update(GameContainer game, int delta) {
	manager.update(game, delta);
    }

    /**
     * Try to move player into new position
     * 
     * @param x
     * @param y
     * @return true if is it possibile, false otherwise
     */
    private boolean tryMove(float x, float y) {
	// REMIND: 25 is value for collision detection!
	Rectangle rect = new Rectangle(x, y, 25, 25);
	DummyBody dummy = new DummyBody(rect);

	if (Collider.testAndReturn(dummy, parent.getBlockingEntities()) == null) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Check if Entity isToRemove from Level
     */
    public boolean isToRemove() {
	return isToRemove;
    }

    /**
     * @return Player score
     */
    public int getScore() {
	return score;
    }

    /**
     * @return the live
     */
    public int getLive() {
	return lives;
    }

    /**
     * @return the lastDir
     */
    public String getLastDir() {
	return lastDir;
    }

}
