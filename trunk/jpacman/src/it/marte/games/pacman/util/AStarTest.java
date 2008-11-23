package it.marte.games.pacman.util;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * A simple pathFinder example
 *  * 
 * @author Gornova81
 * @originalAuthor davida
 * @see http://slick.javaunlimited.net/viewtopic.php?t=841
 */
//TODO: remove this from pacman!
public class AStarTest implements TileBasedMap {

	private int height;
	private int width;
	private int startx;
	private int starty;
	private int endx;
	private int endy;

	private Path path;

	private int currentStep = 0;

	/**
	 * Testing map
	 * 
	 * # = blocking tiles
	 * @ = start
	 * T = target
	 */
	private String[] map = { 
			"  ###                           ",
			"  #@#  ######                   ",
			"  #.####....#                   ",
			"  #.......#.#              ###  ",
			"  ###.#####.######         #.#  ",
			"  #...##.#...#...#         #.#  ",
			"###.#.##.#T###.#########   #.#  ",
			"#...#........#...#.....#   #.#  ",
			"#.########.#.###.###.#######.###",
			"#......#...#.#...#...#...#...#.#",
			"######.#.#####.#####.#.#.#.###.#",
			"     #.....#.....#.....#...#...#",
			"##########.#.#######.#####.#.###",
			"#..........#.............#...#  ",
			"##########.#.#######.###.#.###  ",
			"         #.....# #...#...#...#  ",
			"         ####### #############  " };

	private String[][] mapTiles = null;

	public AStarTest() {
		width = 32;
		height = 17;
		loadMap();
	}

	/**
	 * Load Map
	 */
	private void loadMap() {
		mapTiles = new String[width][height];
		for (int y = 0; y < height; y++) {
			String mapLine = map[y];
			for (int x = 0; x < width; x++) {
				mapTiles[x][y] = mapLine.substring(x, x + 1);
				if ("@".equals(mapTiles[x][y])) {
					startx = x;
					starty = y;
				}
				if ("T".equals(mapTiles[x][y])) {
					endx = x;
					endy = y;
				}

			}
		}
	}


	/**
	 * Calculate path from (sx,sy) to (ex,ey)
	 * 
	 * @param sx - start x of the path
	 * @param sy - start y of the path
	 * @param ex - end x of the path 
	 * @param ey - end y of the path
	 */
	public void updatePath(int sx, int sy, int ex, int ey) throws SlickException {
		// find any blocked paths
		AStarPathFinder pathfinder = new AStarPathFinder(this, 1000, false);
		Mover dummyMover = new Mover() {};
		path = pathfinder.findPath(dummyMover, sx, sy, ex, ey);
		if (path == null) {
			throw new SlickException("cannot find path!");
		}
	}

	/**
	 * Update graphic map based on current step 
	 */
	public void updateMap() {
		Step step = path.getStep(currentStep);
		mapTiles[step.getX()][step.getY()] = "x";
	}

	/**
	 * Print map on screen
	 */
	public void printMap() {
		System.out.println("current step :" + currentStep);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.out.print(mapTiles[x][y]);
			}
			System.out.println("");
		}
	}

	@SuppressWarnings("static-access")
	public static void main(String args[]) {
		AStarTest ast = new AStarTest();
		// program simulation
		while (true) {
			// print map before pathfinding
			ast.printMap();
			// update
			try {
				Thread.currentThread().sleep(1000);//sleep for milliseconds
			} catch (InterruptedException ie) {
			}
			try {
				ast.updatePath(ast.startx, ast.starty,ast.endx, ast.endy);
				ast.updateStep();
				ast.updateMap();
			} catch (SlickException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/**
	 * Update step if is it possibile otherwise, start again
	 */
	private void updateStep() {
		System.out.println("step: " + currentStep + " , lenghtPath: "
				+ path.getLength());
		if (currentStep >= path.getLength() - 1) {
			// target reached
			System.out.println("***************************************");
			System.out.println("*                FOUND !              *");
			System.out.println("***************************************");
			currentStep = 0;
			loadMap();
		} else {
			currentStep++;
		}
	}

	public int getHeightInTiles() {
		return height;
	}

	public int getWidthInTiles() {
		return width;
	}

	public void pathFinderVisited(int x, int y) {
	}

	public class MPoint {
		public int x = 0;
		public int y = 0;

		public MPoint(int px, int py) {
			x = px;
			y = py;
		}
	}

	public boolean blocked(PathFindingContext context, int x, int y) {
		String tile = mapTiles[x][y];
		if (tile.equals("#") || tile.equals(" ")) {
			return true;
		} else {
			return false;
		}
	}

	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		return 0;
	}
}