package day08;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;
import day08.HandheldGameConsole.Status;

public class Day08 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day08/day08_input.txt";
	
	private static final String INSTRUCTION_PATTERN = "(\\w+) ((?:\\+|-)\\d+)";
	private final Pattern INSTRUCTION_RE_PATTERN = Pattern.compile(INSTRUCTION_PATTERN);
	
	public int getDayNumber() {
		return 8;
	}

	public String[] getSolution() {
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();
		
		HandheldGameConsole brokenConsole = new HandheldGameConsole();
		
		for(String puzzleInputLine : puzzleInputLines) {
			Matcher instructionREMatcher = INSTRUCTION_RE_PATTERN.matcher(puzzleInputLine);
			
			while(instructionREMatcher.find()) {
				String instructionCode = instructionREMatcher.group(1);
				Integer instructionParameter = Integer.valueOf(instructionREMatcher.group(2));
				
				brokenConsole.addInstruction(instructionCode, instructionParameter);
			}
		}
		
		// Part One - Run console, regardless of quit reason
		brokenConsole.runUntilLoopOrQuit();
		Integer partOneSolution = brokenConsole.getAccumulator();
		
		
		// Part Two - Run console, if it traps in a loop, try switching NOP<>JMP one by one	
		for(Integer nopOrJmpPosition : brokenConsole.getNopOrJmpPositions()) {
			brokenConsole.resetConsole();
			brokenConsole.flipNopOrJmp(nopOrJmpPosition);
			brokenConsole.runUntilLoopOrQuit();
			
			
			if(brokenConsole.getStatus()!=Status.LOOP) {	// Console quit not due to the loop => we have the solution
				break;
			} else {	// Loop detected, swap instruction back
				brokenConsole.flipNopOrJmp(nopOrJmpPosition);
			}
		}
		Integer partTwoSolution = brokenConsole.getAccumulator();
		
		return new String[] { String.valueOf(partOneSolution), String.valueOf(partTwoSolution) };
	}

}