package day03;

import java.util.List;
import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day03 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day03/day03_input.txt";
	private final char TREE = '#';
	private List<String> areaMap;

	public int getDayNumber() {
		return 3;
	}

	/**
	 * Returns number of trees hit when sliding down from top-left corner given
	 * moveVector on areaMap.
	 * 
	 * @param moveVector in [X;Y] format. X represents X-axis increment, Y
	 *                   represents Y-axis increment (in order to move left X
	 *                   increment is negative number, e.g. -1)
	 * @return number of trees hit when sliding down
	 */
	public int getTreesHitCount(int[] moveVector) {
		int treesHit = 0;

		/*
		 * Initial position on map (top-left) corner. Fact, that puzzle map starts at
		 * [1,1] doesn't really matter. Map width and height - dimensions are important.
		 */
		int posX = 0;
		int posY = 0;

		while (posY < areaMap.size()) {

			// Adjust posX to position on input map (it wraps beyond last X position)
			posX = posX % areaMap.get(0).length();

			// Increment obstacle count, if tree/other obstacle is hit
			if (areaMap.get(posY).charAt(posX) == TREE) { // NOTE inverted [posY;posX] coordinates - this is because
															// file is read by rows - representing Y axis
				treesHit += 1;
			}

			// Move in desired direction
			posX += moveVector[0];
			posY += moveVector[1];
		}

		return treesHit;

	}

	public String[] getSolution() {
		// Load puzzle input
		areaMap = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();

		// Solve partOne
		int[] partOneMoveVector = new int[] { 3, 1 };
		int partOneTreesHitCount = getTreesHitCount(partOneMoveVector);

		// Solve partTwo
		int[][] partTwoMoveVectors = new int[][] { { 1, 1 }, { 3, 1 }, { 5, 1 }, { 7, 1 }, { 1, 2 } };
		long partTwoTreesHitProduct = 1;

		for (int[] moveVector : partTwoMoveVectors) {
			partTwoTreesHitProduct *= getTreesHitCount(moveVector);
		}

		return new String[] { String.valueOf(partOneTreesHitCount), String.valueOf(partTwoTreesHitProduct) };
	}

}