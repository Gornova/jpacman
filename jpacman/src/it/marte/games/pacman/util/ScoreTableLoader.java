package it.marte.games.pacman.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Score table for player's score
 * 
 * @author AM
 * @project PacMan
 */
public class ScoreTableLoader {

    private Properties prop;

    private Hashtable<String, String> table;

    private String filePath;

    /**
     * Load level definitions from a .properties file
     * 
     * @throws FileNotFoundException -
     *                 if file is not found at file path specified
     * @throws IOException -
     *                 if there are some IO errors
     */
    public ScoreTableLoader(String filePath) throws FileNotFoundException,
	    IOException {
	prop = new Properties();
	prop.load(new FileInputStream(filePath));
	this.filePath = filePath;
	table = new Hashtable<String, String>();

	for (Object iterable_element : prop.keySet()) {
	    table.put((String) iterable_element, prop
		    .getProperty((String) iterable_element));
	}
    }

    /**
     * Load Scoretable ordered by points, from major to minus
     */
    public ArrayList<ScoreRecord> loadScoreTable() {
	ArrayList<ScoreRecord> srs = new ArrayList<ScoreRecord>();

	for (String key : table.keySet()) {
	    String value = table.get(key);
	    ScoreRecord sr = new ScoreRecord(key, new Integer(value));
	    srs.add(sr);
	}

	Collections.sort(srs);

	return srs;
    }

    /**
     * Save scoretable to disk, filepath is the same when read scoretable
     * 
     * @param scores
     * @return null if scoretable is null, true if save successfull, false
     *         otherwise
     */
    public boolean saveScoreTable(ArrayList<ScoreRecord> scores) {
	if (scores == null)
	    return false;
	boolean result = true;

	Hashtable<String, String> towrite = new Hashtable<String, String>();

	for (ScoreRecord scoreRecord : scores) {
	    towrite.put(scoreRecord.getName(), scoreRecord.getPoints()
		    .toString());
	}

	prop.putAll(towrite);
	try {
	    prop.store(new FileOutputStream(filePath), null);
	} catch (FileNotFoundException e) {
	    return false;
	} catch (IOException e) {
	    return false;
	}
	return result;
    }

}
