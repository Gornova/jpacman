package it.marte.games.pacman.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Trace of time passed in seconds
 * 
 * @author AM
 * @project PacMan
 */
public class Clock {

	private static long timer;

	private static long publicTimer;

	private static SimpleDateFormat sdf;
	
	public Clock(){
		timer = 0;
		publicTimer = 0;
		sdf =  new SimpleDateFormat("mm:ss");
	}
	
	public void update(int delta){
        timer += delta;
        if (timer >= 1000){
        	publicTimer++;
        	timer = 0;
        }
	}
	
	/**
	 * @return time formatted in mm:ss
	 */
	public static String getTime(){
		return sdf.format(new Date(publicTimer*1000));
	}
	
	/**
	 * @return time passed in milliseconds
	 */
	public static long getTimer(){
		return publicTimer;
	}
	
}
