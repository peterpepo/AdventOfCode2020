package day06;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day06 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day06/day06_input.txt";
	
	public int getDayNumber() {
		return 6;
	}

	public String[] getSolution() {
		int partOneAnsweredYesCount = 0;
		int partTwoAllAnsweredYesCount = 0;
		
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines(); 

		Set<Character> groupAnsweredYesTo = new HashSet<>();	// partOne temporary
		Set<Character> groupAllAnsweredYesTo = new HashSet<>();	// partTwo temporary
		
		boolean insideGroup = false;	// indicates, whether we are in the middle of the processing of a group
		
		for (int idx = 0; idx < puzzleInputLines.size(); idx++) {
			String puzzleInputLine = puzzleInputLines.get(idx);
			
			// Parse data if row is not empty
			if (puzzleInputLine.length() > 0) {
				List<Character> currentPerson = puzzleInputLine.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
				
				// Part One
				groupAnsweredYesTo.addAll(currentPerson);
				
				/*
				 * Part Two
				 * If not in the middle of a group (new group just starting), consider all questions answered yes by all group members.
				 * Otherwise, keep only those for which group until now and currentPerson answered yes.
				 */
				if(insideGroup == false) {
					groupAllAnsweredYesTo.addAll(currentPerson);
				} else {
					groupAllAnsweredYesTo.retainAll(currentPerson);
				}
				insideGroup = true;
				
			}
			
			// Reached end of group (end of input or blank line)
			if (idx == puzzleInputLines.size() - 1 || puzzleInputLine.length() == 0) {
				// Part One
				partOneAnsweredYesCount += groupAnsweredYesTo.size();
				groupAnsweredYesTo.clear();
				
				// Part Two
				partTwoAllAnsweredYesCount += groupAllAnsweredYesTo.size();
				groupAllAnsweredYesTo.clear();
				insideGroup = false;
			}
		}
		
		return new String[] { String.valueOf(partOneAnsweredYesCount), String.valueOf(partTwoAllAnsweredYesCount) };
	}
}