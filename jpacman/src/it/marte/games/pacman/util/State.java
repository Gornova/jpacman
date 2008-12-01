package it.marte.games.pacman.util;

import it.marte.games.pacman.base.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface State {

    public boolean equals(Object o);

    public void enter();

    public void update(GameContainer game, int delta);

    public void render(Graphics g);

    public void onCollision(Entity obstacle);

}