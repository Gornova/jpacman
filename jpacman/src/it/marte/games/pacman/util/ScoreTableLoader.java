package it.marte.games.pacman.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
     * Load Scoretable
     */
    public Hashtable<String, String> loadScoreTable() {
	return table;
    }

    /**
     * Save scoretable to disk, filepath is the same when read scoretable
     * 
     * @param scoretable
     * @return null if scoretable is null, true if save successfull, false otherwise
     */
    public boolean saveScoreTable(Hashtable<String,String> scoretable){
	if (scoretable==null) return false;
	boolean result = true;
	prop.putAll(scoretable);
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
