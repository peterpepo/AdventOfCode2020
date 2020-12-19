package day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day19 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day19/day19_input.txt";
	
	private Map<String, String> rules = new HashMap<>();
	private List<String> wordsToCheck = new ArrayList<>();

	public int getDayNumber() {
		return 19;
	}
	
	private String evaluateRule (String ruleId) {
		String result = "";
		
		// Get rule definition from the map of rules
		String thisRule = rules.get(ruleId);
		
		// If not in the map, it is evaluated as literal
		thisRule = thisRule!=null ? thisRule : ruleId;
		
		/*
		 * If + or |, return immediatelly without wrapping in ().
		 * If "a" -> remove quotes
		 * If other, evaluate each token.
		 */
		if (thisRule.equals("|")) {
			result = "|";
		} else if (thisRule.equals("+")) {
			result = "+";
		}
		else if (thisRule.contains("\"")) {
			result = thisRule.replaceAll("\"", "");
		} else {
			// Split current rule into tokens
			String[] thisRuleTokens = thisRule.split(" ");
			
			// Evaluate each of tokens
			for(String thisRuleToken : thisRuleTokens) {
				result += evaluateRule(thisRuleToken);
			}
			result = "(" + result + ")";
		}
		return result;
	}

	public String[] getSolution() {
		// Load input
		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			
			if(puzzleInputLine.contains(":")) {
				String[] numberAndRule = puzzleInputLine.split(": ");
				rules.put(numberAndRule[0], numberAndRule[1]);
			} else if (puzzleInputLine.length() > 0) {
				wordsToCheck.add(puzzleInputLine);
			}
		}
		
		// PartOne
		int partOneSolution = 0;
		String wordPattern = evaluateRule("0");
		wordPattern = "^" + wordPattern + "$";
		Pattern wordREPattern = Pattern.compile(wordPattern);
		
		for(String wordToCheck : wordsToCheck) {
			Matcher wordMatcher = wordREPattern.matcher(wordToCheck);
			if(wordMatcher.find()) {
				partOneSolution += 1;
			}
		}
		
		// PartTwo
		int partTwoSolution = 0;
		/*
		 * Set 8: 42 | 42 8 (e.g. 42 42 42)
		 * This can be translated as: 42+
		 */
		rules.put("8", "42 +");
		/*
		 * Set 11: 42 31 | 42 11 31 (e.g. 42 31 | 42 42 31 31 | 42 42 42 31 31 31)
		 * This mirror / palindrome cannot be translated into regexp. Instead we unpack it to depth of 5 and hope for the best.
		 */
		rules.put("11", "42 31 | 42 42 31 31 | 42 42 42 31 31 31 | 42 42 42 42 31 31 31 31 | 42 42 42 42 42 31 31 31 31 31");
		
		wordPattern = evaluateRule("0");
		wordPattern = "^" + wordPattern + "$";
		wordREPattern = Pattern.compile(wordPattern);
		
		for(String wordToCheck : wordsToCheck) {
			Matcher wordMatcher = wordREPattern.matcher(wordToCheck);
			if(wordMatcher.find()) {
				partTwoSolution += 1;
			}
		}
		return new String[] { String.valueOf(partOneSolution), String.valueOf(partTwoSolution) };
	}
}