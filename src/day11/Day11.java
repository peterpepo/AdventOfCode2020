package day11;

import java.util.List;
import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day11 implements IDaySolution {
//	private final String INPUT_FILE_NAME = "src/Day11/day11_input_test.txt";
	private final String INPUT_FILE_NAME = "src/Day11/day11_input.txt";
	
	public int getDayNumber() {
		return 11;
	}

	public String[] getSolution() {
		// GameOfSeats instance
		GameOfSeats gosOne = new GameOfSeats(4, 1);		// GOS instance for partOne
		GameOfSeats gosTwo = new GameOfSeats(5, null);	// GOS instance for partTwo
		
		// Load puzzle input to GameOfSeats for partOne and PartTwo
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();
		
		for(Integer posY = 0; posY < puzzleInputLines.size(); posY++) {
			for(Integer posX = 0; posX < puzzleInputLines.get(posY).length(); posX++) {
				char seatVal = puzzleInputLines.get(posY).charAt(posX);
				if(seatVal == 'L') {
					gosOne.setSeat(new Point(posX, posY), GameOfSeats.FREE);
					gosTwo.setSeat(new Point(posX, posY), GameOfSeats.FREE);
				}
				
			}
		}
		
		// Part One
		gosOne.runUntilLoop();
		Integer partOneSolution = gosOne.countOccupiedSeats();
		
		// Part Two
		gosTwo.runUntilLoop();
		Integer partTwoSolution = gosTwo.countOccupiedSeats();

		return new String[] { String.valueOf(partOneSolution), String.valueOf(partTwoSolution) };
	}
}