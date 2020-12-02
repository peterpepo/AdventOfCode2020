package day02;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;


public class Day02 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day02/day02_input.txt";

	private final String INPUT_LINE_PATTERN = "(\\d+)-(\\d+).(\\w)\\:.(\\w+)";
	private final Pattern INPUT_RE_PATTERN = Pattern.compile(INPUT_LINE_PATTERN);

	public int getDayNumber() {
		return 2;
	}

	private static boolean containsCharTimes(String stringToCheck, char searchedCharacter, int minTimes, int maxTimes) {
		/*
		 * 1. Convert string to stream of characters.
		 * 2. Filter characters to those equal to searchedCharacter.
		 * 3. Count them.
		 */
		long searchedCharacterCount = stringToCheck.chars().filter(ch -> ch == searchedCharacter).count();

		return (minTimes <= searchedCharacterCount) && (searchedCharacterCount <= maxTimes);
	}

	/**
	 * Checks whether stringToCheck contains exactly one occurence of searchedCharacter at positions specified by List<Integer> checkedIndexes.
	 * Please note, that while partTwo asks to check at exactly two positions, this method is ready for any number of positions.
	 */
	private static boolean containsCharAtOneOfPositions(String stringToCheck, char searchedCharacter,
			List<Integer> checkedIndexes) {
		
		/*
		 * 1. Create stream of int.
		 * 2. Check whether this idx+1 (incremented by one, since puzzle assumes string starts at position 1 rather than 0) and filter to those.
		 * 3. Map from indexes back to individual characters.
		 * 4. Filter characters to those equal to searchedCharacter.
		 * 5. Count them.
		 */
		long searchedCharacterCount = IntStream.range(0, stringToCheck.length())
				.filter(idx -> checkedIndexes.contains(idx + 1)).mapToObj(stringToCheck::charAt)
				.filter(ch -> ch == searchedCharacter).count();

		return searchedCharacterCount == 1;
	}

	public String[] getSolution() {
		int validPasswordsCountPartOne = 0;
		int validPasswordsCountPartTwo = 0;

		// Loop through list of input lines
		for (String puzzle_input_line : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {

			Matcher inputLineReMatcher = INPUT_RE_PATTERN.matcher(puzzle_input_line);

			// If line matches the pattern, validate password against the rules
			if (inputLineReMatcher.find()) {
				int paramA = Integer.valueOf(inputLineReMatcher.group(1)); // minTimes for partOne, first position to check for partTwo
				int paramB = Integer.valueOf(inputLineReMatcher.group(2)); // maxTimes for partTwo, second position to check for partTwo
				char searchedCharacter = inputLineReMatcher.group(3).charAt(0);
				String stringToCheck = inputLineReMatcher.group(4);

				// Check password validity for partOne
				if (containsCharTimes(stringToCheck, searchedCharacter, paramA, paramB)) {
					validPasswordsCountPartOne += 1;
				}

				// Check password validity for partTwo
				if (containsCharAtOneOfPositions(stringToCheck, searchedCharacter,
						List.of(Integer.valueOf(paramA), Integer.valueOf(paramB)))) {
					validPasswordsCountPartTwo += 1;
				}

			} else {
				System.err.println("Line doesn't match specified pattern.");
			}

		}

		return new String[] { String.valueOf(validPasswordsCountPartOne), String.valueOf(validPasswordsCountPartTwo) };

	}

}
