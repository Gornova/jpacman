package it.marte.games.pacman.base;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;

/**
 * Any object which can be written to the screen and animated.
 * 
 * @author Alex Schearer <aschearer@gmail.com>
 */
public interface Animatable {

    public void render(BasicGameState game, Graphics g);

    public void update(GameContainer game, int delta);
}
