package day15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day15 implements IDaySolution {
	private final String INPUT_FILE_NAME_TEST = "src/Day15/day15_input_test.txt";
	private final String INPUT_FILE_NAME = "src/Day15/day15_input.txt";
	
	public int getDayNumber() {
		return 15;
	}

	private int getPartSolution(String initialCommaSeparatedNumbers, int maxRoundNumber) {
			Map<Integer, int[]> spokenNumbers = new HashMap<>();
			int currentRoundNumber = 1;
			Integer currentNumber = null;
			
			// Load numbers to map
			for(String initialNumber : initialCommaSeparatedNumbers.split(",")) {
				spokenNumbers.put(Integer.valueOf(initialNumber), new int[] {currentRoundNumber, currentRoundNumber});
				currentNumber = Integer.valueOf(initialNumber);
				currentRoundNumber++;
			}
			
			// Init mentions of last numbers as last number(from puzzle input)
			int[] lastNumberMentions = spokenNumbers.get(currentNumber);
			
			while(currentRoundNumber <= maxRoundNumber) {
				/* Calculate number to say in this round (last mention of previous number and one before last) */
				currentNumber = lastNumberMentions[1] - lastNumberMentions[0];
				
				/*
				 * Store last mention of current number. If it has been mentioned "last" before - it becomes
				 * before last value. And new "last" is current round number. If it hasn't been said before,
				 * both last and one before last are initialized to currentRoundNumber - in same way as reading input.
				 */
				int[] currentNumberMentions = spokenNumbers.get(currentNumber);
				if(currentNumberMentions==null) {
					currentNumberMentions = new int[] {currentRoundNumber, currentRoundNumber};
					spokenNumbers.put(currentNumber, new int[] {currentRoundNumber, currentRoundNumber});
				} else {
					currentNumberMentions[0] = currentNumberMentions[1];
					currentNumberMentions[1] = currentRoundNumber;
				}
				
				/* Current number mentions becomes base for next round calculation */
				lastNumberMentions = currentNumberMentions;
				
				/* Increment round number */
				currentRoundNumber++;
			}
		return currentNumber;
	}

	public String[] getSolution() {
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();
		String puzzleInputLine = puzzleInputLines.get(0);
		return new String[] { String.valueOf(getPartSolution(puzzleInputLine, 2020)), String.valueOf(getPartSolution(puzzleInputLine, 30000000)) };
	}
	
	/**
	 * Print solutions for all test inputs.
	 */
	public void printTestSolutions() {
		List<String> puzzleTestInputLines = new PuzzleInputReader(INPUT_FILE_NAME_TEST).getListOfLines();
		for(String puzzleInputLine : puzzleTestInputLines) {
			System.out.println(String.format("[TEST] Day %d, partOne: %s, partTwo: %s.", getDayNumber(), String.valueOf(getPartSolution(puzzleInputLine, 2020)), String.valueOf(getPartSolution(puzzleInputLine, 30000000))));
		}
	}
}