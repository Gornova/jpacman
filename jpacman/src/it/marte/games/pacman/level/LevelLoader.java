package it.marte.games.pacman.level;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Load level chain from a properties file
 * 
 * @author AM
 * @project PacMan
 */
public class LevelLoader {

    private Properties prop;

    private Hashtable<String, String> table;

    /**
     * Load level definitions from a .properties file
     * 
     * @throws FileNotFoundException -
     *                 if file is not found at file path specified
     * @throws IOException -
     *                 if there are some IO errors
     */
    public LevelLoader(String filePath) throws FileNotFoundException,
	    IOException {
	prop = new Properties();
	prop.load(new FileInputStream(filePath));
	table = new Hashtable<String, String>();

	for (Object iterable_element : prop.keySet()) {
	    table.put((String) iterable_element, prop
		    .getProperty((String) iterable_element));
	}
    }

    /**
     * Load Hashtable of level definitions. Key is level number,Value is level's
     * map name
     */
    public Hashtable<String, String> getLevelChain() {
	return table;
    }

}
