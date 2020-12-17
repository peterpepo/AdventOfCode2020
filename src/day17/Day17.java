package day17;

import java.util.List;
import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day17 implements IDaySolution {
//	private final String INPUT_FILE_NAME = "src/Day17/day17_input_test_part_one.txt";
	private final String INPUT_FILE_NAME = "src/Day17/day17_input.txt";
	
	public int getDayNumber() {
		return 17;
	}

	public String[] getSolution() {

		ConwaysCube CCPartOne = new ConwaysCube(0,1);		//partOne cube
		ConwaysCube4D CCPartTwo = new ConwaysCube4D(0, 1);	//partTwo cube
		
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();
		
		for(int y = 0; y < puzzleInputLines.size(); y++) {
			for(int x = 0; x < puzzleInputLines.get(y).length(); x++) {
				if(puzzleInputLines.get(y).charAt(x) == '#') {
					CCPartOne.initLiveCell(new Point3D(x, y, 0));
					CCPartTwo.initLiveCell(new Point4D(x, y, 0, 0));
				}
			}
		}
		
		// Solve Part One
		CCPartOne.runNtimes(6);
		int partOneSolution = CCPartOne.countLiveCells();
		
		// Solve Part Two
		CCPartTwo.runNtimes(6);
		int CCPartTwoSolution = CCPartTwo.countLiveCells();
		
		return new String[] { String.valueOf(partOneSolution), String.valueOf(CCPartTwoSolution) };
	}

}