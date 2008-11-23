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
public class Ghost extends Body {

	public Animation sprite, up, down, left, right;

	public Animation eatedUp, eatedDown, eatedLeft, eatedRight;

	private static final float SPEED = 0.03f;

	private RedGhostBrain brain;

	private StateManager manager;	
	
	private enum State{
		NORMAL, WAIT, EATABLE;
	}
	
	public Ghost(Map parent, Shape shape) throws SlickException {
		this.shape = shape;
		brain = new RedGhostBrain(parent,getPosition());
		init();
	}

	private void init() {
		try {
			// load graphic from sheet
			SpriteSheet sheet = new SpriteSheet("data/ghosts.png", 32, 32);
			
			// init states
	        manager = new StateManager();
	        manager.add(new NormalState());
	        manager.add(new WaitState());
	        manager.add(new EatableState());
			
			// load normal animations
			right = SheetUtil.getAnimationFromSheet(sheet, 0, 0, 2);
			left = SheetUtil.getAnimationFromSheet(sheet, 0, 0, 2);
			up = SheetUtil.getAnimationFromSheet(sheet, 0, 0, 2);
			down = SheetUtil.getAnimationFromSheet(sheet, 0, 0, 2);

			// load eated animations
			eatedRight = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 2);
			eatedLeft = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 2);
			eatedUp = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 2);
			eatedDown = SheetUtil.getAnimationFromSheet(sheet, 4, 0, 2);
			
			// Original orientation of the sprite. It will look right.
			sprite = right;

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
		//brain.render(game,g);
	}

	/**
	 * Update Ghost logic
	 */
	public void update(GameContainer game, int delta) {
		manager.update(game, delta);
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
			//currentStep++;
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
				sprite = up;
				sprite.update(delta);
				float y = getY() - delta * SPEED;
				shape.setY(y);
			} else {
				if (keyDown) {
					sprite = down;
					sprite.update(delta);
					float y = getY() + delta * SPEED;
					shape.setY(y);
				} else {
					if (keyLeft) {
						sprite = left;
						sprite.update(delta);
						float x = getX() - delta * SPEED;
						shape.setX(x);
					} else {
						if (keyRight) {
							sprite = right;
							sprite.update(delta);
							float x = getX() + delta * SPEED;
							shape.setX(x);
						}
					}
				}
			}
		}
	}

	/**
	 * check if ghost is to remove from level
	 */
	public boolean isToRemove() {
		return false;
	}

    private class NormalState implements it.marte.games.pacman.util.State {

        public boolean equals(Object state) {
            return state == State.NORMAL;
        }
    	
		public void enter() {
			
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
			} catch (Exception e){
				manager.enter(State.WAIT);
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
			if (timer > 1000){
				manager.enter(State.NORMAL);
			}
		}
    	
    }
    
    private class EatableState implements it.marte.games.pacman.util.State {

        public boolean equals(Object state) {
            return state == State.EATABLE;
        }
    	
		public void enter() {
			Log.info("I'm eatable!");
		}

		public void onCollision(Entity obstacle) {
			
		}

		public void render(Graphics g) {
			sprite.draw(getX(), getY());
		}

		public void update(GameContainer game, int delta) {

		}
    	
    }
    
    public void setEatable(){
    	manager.enter(State.EATABLE);
    }
    
	
    private Body getBody(){
    	return this;
    }
	
}
