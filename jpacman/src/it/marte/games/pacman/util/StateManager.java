package it.marte.games.pacman.util;

import it.marte.games.pacman.base.Entity;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * 
 * @author Alex
 */
public class StateManager {
    
    private LinkedList<State> states;
    private State currentState;
    
    public StateManager() {
        states = new LinkedList<State>();
        currentState = null;
    }

    public void add(State s) {
        states.add(s);
        if (currentState == null) {
            currentState = s;
            currentState.enter();
        }
    }
    
    /**
     * Attempts to enter the target state. Fails silently!!
     * 
     * TODO have this fail politely but loudly.
     * @param o
     */
    public void enter(Object o) {
        for (State s : states) {
            if (s.equals(o)) {
                currentState = s;
                currentState.enter();
            }
        }
    }
    
    public void update(GameContainer game, int delta) {
        currentState.update(game, delta);
    }
    
    public void onCollision(Entity obstacle) {
        currentState.onCollision(obstacle);
    }

    public void render(Graphics g) {
        currentState.render(g);
    }

    public Object currentState() {
        return currentState;
    }

}
