/**
 * 
 */
package it.marte.games.pacman.util;

/**
 * A single score (name, points)
 * 
 * @author AM
 * @project jpacman
 */
public class ScoreRecord implements Comparable<ScoreRecord> {

    private String name;
    
    private Integer points;

    /**
     * @param name of player
     * @param points of player
     */
    public ScoreRecord(String name, Integer points){
	this.name = name;
	this.points = points;
    }
    
    public int compareTo(ScoreRecord o) {
	ScoreRecord comp = (ScoreRecord)o; 
	if (comp.getPoints() == this.getPoints()){
	    return 0;
	}
	if (comp.getPoints() < this.getPoints()){
	    return -1;
	} else {
	    return 1;
	}
    }



    /**
     * @return the points
     */
    public Integer getPoints() {
        return points;
    }



    /**
     * @param points the points to set
     */
    public void setPoints(Integer points) {
        this.points = points;
    }



    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
