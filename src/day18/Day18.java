package day18;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day18 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day18/day18_input.txt";
	
	public int getDayNumber() {
		return 18;
	}

	public String[] getSolution() {
		// Part One configuration
		long partOneSolution = 0;
		Map<String, Integer> partOneOperatorPriorities = new HashMap<>();
		partOneOperatorPriorities.put("+", 1);	// +* are same priority
		partOneOperatorPriorities.put("*", 1);	// +* are same priority
		
		// Part Two configuration
		long partTwoSolution = 0;
		Map<String, Integer> partTwoOperatorPriorities = new HashMap<>();
		partTwoOperatorPriorities.put("+", 1);	// + is higher priority as *
		partTwoOperatorPriorities.put("*", 2);	// * is lowere priority as +

		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			partOneSolution += solveRPN(getRPNShuntingYardAlgorithm(puzzleInputLine, partOneOperatorPriorities));
			partTwoSolution += solveRPN(getRPNShuntingYardAlgorithm(puzzleInputLine, partTwoOperatorPriorities));
		}

		return new String[] { String.valueOf(partOneSolution), String.valueOf(partTwoSolution) };
	}
	
	/**
	 * Returns RPN (Reverse Polish Notation) of given math problem using Shunting Yard Algorithm.
	 * Shunting Yard Explanation: https://en.wikipedia.org/wiki/Shunting-yard_algorithm, https://www.youtube.com/watch?v=Wz85Hiwi5MY 
	 * @param inputString - math problem to convert to RPN.
	 * @param operatorPriorities - Map<String, Integer> of operator priorities (lower 0 = higher priority),
	 * e.g. {"+": 1, "* : 2} => + is higher priority than *
	 * @return List<String> tokenized math problem in RPN notation.
	 */
	private List<String> getRPNShuntingYardAlgorithm(String inputString, Map<String, Integer> operatorPriorities) {
		List<String> output = new LinkedList<>();			// Output queue (FIFO)
		List<String> operatorsStack = new LinkedList<>();	// Operators stack (LIFO)
		
		String PARENTHESIS_OPEN = "(";
		String PARENTHESIS_CLOSE = ")";
		
		String TOKEN_PATTERN = "\\d+|\\+|\\*|\\(|\\)";		// numbers or operators +, *
		Pattern RE_TOKEN_PATTERN = Pattern.compile(TOKEN_PATTERN);
		Matcher tokenMatcher = RE_TOKEN_PATTERN.matcher(inputString);
		
		while(tokenMatcher.find()) {
			String currentToken = tokenMatcher.group(0);
			
			// Check, whether current token is a number / operator / parenthesis
			try {
				// If it is number, add it to output
				Integer.valueOf(currentToken);	// If not a number, NumberFormatException is thrown and next block is executed
				output.add(currentToken);
			} catch (NumberFormatException e) {
				// It is not a number, must be operator or parenthesis
				
				if(operatorPriorities.containsKey(currentToken)) {
					/*
					 * It's and operator, add it to stack, but first pop operators which have higher priority (lower priority index).
					 * Parenthesis are never popped, unless closing parenthesis is found. That is taken care of in next block.
					 */
					while(operatorsStack.size() > 0 &&
							!operatorsStack.get(0).equals(PARENTHESIS_OPEN) &&
							operatorPriorities.get(currentToken) >= operatorPriorities.get(operatorsStack.get(0))) {
						output.add(operatorsStack.remove(0));
					}
					operatorsStack.add(0, currentToken);
					
				} else if (currentToken.equals(PARENTHESIS_OPEN)) {
					// Opening Parenthesis
					operatorsStack.add(0, currentToken);
					
				} else if (currentToken.equals(PARENTHESIS_CLOSE)) {
					/*
					 * Closing parenthesis - pop everything from operator stack and add to output.
					 * Closing parenthesis is not added to stack, unlike opening.
					 * When everything up to opening parentheis is moved to the output, discard opening parenthesis from operator stack
					 */
					while(!operatorsStack.get(0).equals(PARENTHESIS_OPEN)) {
						output.add(operatorsStack.remove(0));
					}
					operatorsStack.remove(0);
				} else {
					System.err.println("Unknown token: " + currentToken);
				}
			}
		}
		
		// LIFO all remaining operators to the output
		while(operatorsStack.size()>0) {
			output.add(operatorsStack.remove(0));
		}
		return output;
	}
	
	/**
	 * Calculates RPN described math problem result.
	 * RPN calculation explanation: https://www.youtube.com/watch?v=7ha78yWRDlE
	 * @param inputTokens - List<String> of tokens - numbers, operators, parenthesses and so on.
	 * @return long result of the math problem.
	 */
	private long solveRPN(List<String> inputTokens) {
		List<Long> numbersStack = new LinkedList<>();	// Stack to hold intermediary calculations and result
		
		// Process all tokens
		while(inputTokens.size()>0) {
			String currentToken = inputTokens.remove(0);
			
			/*
			 * Process a token - if it is number, add it to numberStack.
			 * If this is not a number, NumberFormatException is thrown and processing continues as if the token was operator.
			 */
			try {
				numbersStack.add(0, Long.valueOf(currentToken));
				continue;
			} catch (NumberFormatException e) {
			}
			
			// Pop two operands from number stack
			Long operandOne = numbersStack.remove(0);
			Long operandTwo = numbersStack.remove(0);
			Long operationResult = null;
			
			// Calculate result of two operands and operator
			switch(currentToken) {
			case("+"):
				operationResult = operandOne + operandTwo;
			break;
			case("*"):
				operationResult = operandOne * operandTwo;
			break;
			}
			
			// Add the result to the numbersStack
			numbersStack.add(0, operationResult);
		}
		// Only single number should be left on top of numbersStack and that is the result.
		return numbersStack.get(0);
	}
}