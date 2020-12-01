package day01;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import commons.PuzzleInputReader;
import day01.NestedForExclusive.IAction;

public class Day01 {
	private final String INPUT_FILE_NAME = "src/Day01/day01_input.txt";

	private List<Integer> expenses_list = new ArrayList<>();
	private final int MATCHING_EXPENSES_SUM = 2020;

	private void loadPuzzleInput() {
		for (String puzzle_input_line : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			expenses_list.add(Integer.valueOf(puzzle_input_line));
		}
	}

	// Finds first solution and quits.
	private Integer solveGenericUsingStack(int entriesCount) {
		Integer solution = null;

		Stack<List<Integer>> entries_combinations = new Stack<>();
		entries_combinations.push(new ArrayList<Integer>());

		// Repeat until there are any combinations to be checked
		while (entries_combinations.size() > 0) {
			List<Integer> currentCombination = entries_combinations.pop();

			int currentCombinationLength = currentCombination.size();

			// Check whether combination is of proper length. Expand if it is not.
			if (currentCombinationLength == entriesCount) {

				// Get stream of entries based on their indices
				List<Integer> current_entries = currentCombination.stream().map((idx) -> expenses_list.get(idx))
						.collect(Collectors.toList());

				// Get sum of entries
				int entries_sum = current_entries.stream().reduce(0, Integer::sum);

				// Solution found
				if (entries_sum == MATCHING_EXPENSES_SUM) {
					// Get product of current entries and return
					solution = current_entries.stream().reduce(1, (a, b) -> a * b);
					break;
				}

			} else if (currentCombinationLength < entriesCount) {
				int currentCombinationLastIndice = -1;

				if (currentCombination.size() > 0) {
					currentCombinationLastIndice = currentCombination.get(currentCombination.size() - 1);
				}

				// Extend current combination with each subsequent indice
				for (int nextIndice = currentCombinationLastIndice + 1; nextIndice < expenses_list.size() - entriesCount
						+ 1; nextIndice++) {

					List<Integer> newCombination = new ArrayList<>(currentCombination);
					newCombination.add(nextIndice);
					entries_combinations.push(newCombination);
				}
			}

		}
		return solution;

	}

	// Alternative approach - using Recursion. Tries every possible n-tuple, and
	// adds it to list of solutions.
	private String solveGenericUsingRecursion(int entries_count) {

		List<List<Integer>> matchingSumIndicesList = new ArrayList<>(); // List of integer arrays with matching sum

		// Action to be performed inside nested loops
		// Checks whether entries at specified indices sum up to MATCHING_EXPENSES_SUM
		// and if so, adds to list of solutions.
		IAction addEntriesIfMatchingSum = new IAction() {

			@Override
			public void act(int[] indices) {
				int total_sum = 0;

				for (int i = 0; i < indices.length; i++) {
					total_sum += expenses_list.get(indices[i]);
				}

				if (total_sum == MATCHING_EXPENSES_SUM) {
					List<Integer> matching_expenses = new ArrayList<>();

					for (int i = 0; i < indices.length; i++) {
						matching_expenses.add(expenses_list.get(indices[i]));
					}

					matchingSumIndicesList.add(matching_expenses);
				}

			}
		};

		NestedForExclusive nested_for = new NestedForExclusive(0, expenses_list.size() - 1, entries_count,
				addEntriesIfMatchingSum);
		nested_for.nFor();

		List<Integer> listOfProducts = matchingSumIndicesList.stream()
				.map(innerList -> innerList.stream().reduce(1, (a, b) -> a * b)).collect(Collectors.toList());

		String result = listOfProducts.stream().map(num -> num.toString()).collect(Collectors.joining(";"));

		return result;
	}

	public String[] solveUsingStack() {
		if (expenses_list.size() == 0)
			loadPuzzleInput();

		Integer solution_part_one = solveGenericUsingStack(2);
		Integer solution_part_two = solveGenericUsingStack(3);

		return new String[] { solution_part_one.toString(), solution_part_two.toString() };
	}

	public String[] solveUsingRecursion() {
		if (expenses_list.size() == 0)
			loadPuzzleInput();

		String solution_part_one = solveGenericUsingRecursion(2);
		String solution_part_two = solveGenericUsingRecursion(3);

		return new String[] { solution_part_one, solution_part_two };
	}

	public static void main(String[] args) {
		Day01 day01 = new Day01();

		String[] day01SolutionStack = day01.solveUsingStack();
		String[] day01SolutionRecursion = day01.solveUsingRecursion();

		System.out.println(String.format("Day01 using stack, partOne: %s, partTwo: %s", day01SolutionStack));
		System.out.println(String.format("Day01 using recursion, partOne: %s, partTwo: %s", day01SolutionRecursion));

	}

}
