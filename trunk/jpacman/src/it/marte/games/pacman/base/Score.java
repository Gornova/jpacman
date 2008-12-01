package it.marte.games.pacman.base;

/**
 * A simply class to store score of player
 * 
 * @author AM
 * @project PacMan
 */
public class Score {

    private static int score = 0;

    private static long time = 0;

    /**
     * @return the score
     */
    public static int getScore() {
	return (score * 10); // + getTempBonus();
    }

    public static int getFinalScore() {
	int temp;
	temp = (score * 10) + getTempBonus();
	return temp;
    }

    /**
     * @param score
     *                the score to set
     */
    public static void setScore(int score) {
	Score.score = score;
    }

    /**
     * @return the time
     */
    public static long getTime() {
	return time;
    }

    /**
     * @param time
     *                the time to set
     */
    public static void setTime(long time) {
	Score.time = time;
    }

    private static int getTempBonus() {
	int temporalBonus = 0;

	if (time == 0) {
	    temporalBonus = 0;
	}
	if (time != 0 && time <= 30) {
	    temporalBonus = 10000;
	}
	if (time > 30 && time <= 60) {
	    temporalBonus = 1000;
	}
	if (time > 60 && time <= 90) {
	    temporalBonus = 100;
	}
	if (time >= 90) {
	    temporalBonus = 10;
	}
	return temporalBonus;
    }

}
