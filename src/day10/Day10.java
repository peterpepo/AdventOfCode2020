package day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day10 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day10/day10_input.txt";
	
	public int getDayNumber() {
		return 10;
	}

	public String[] getSolution() {
		List<Integer> joltageAdapters = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines().stream().map(num -> Integer.parseInt(num)).collect(Collectors.toList());
		
		/*
		 * Part One
		 * Add power socket joltage 0j and our device max+3j to list of "adapters".
		 * Sort all adapters in ascending order.
		 * For each pair of adapters, calculate difference between them and increase corresponding counter.
		 * Calculate result by multiplying diffCount[1] * diffCount[3]
		 */
		Collections.sort(joltageAdapters);
		
		int[] joltageDiffCount = new int[] {0, 0, 0, 0};	// count of [0j, 1j, 2j, 3j] differences

		joltageAdapters.add(0, Integer.valueOf(0));	// Add initial 0J
		joltageAdapters.add(joltageAdapters.stream().max(Integer::compare).get()+3);	// Add my device (+3J)
		
		for(int idx = 1; idx<joltageAdapters.size(); idx++) {
			int joltageDiff = joltageAdapters.get(idx) - joltageAdapters.get(idx-1);
			joltageDiffCount[joltageDiff] += 1;
		}
		Integer partOneSolution = joltageDiffCount[1] * joltageDiffCount[3];

		
		/*
		 * Part Two
		 * For each adapter, referred as node further on, calculate how many different "ways" can we get there.
		 * For 0th node - 0j - this is beginning of the chain - only one way to get there.
		 * For each subsequent node, this is given by sum of previous nodes (up to (including) 3j difference).
		 * 	Once >3 difference is found, we don't have to continue looking back, since nodes are in increasing order.
		 * Solution is given by result of above calculation for last node (our device).
		 */
		List<Long> waysToNode = new ArrayList<>();
		waysToNode.add(1l);	// 1 way to 0
		
		for(int idx = 1; idx<joltageAdapters.size(); idx++) {
			long waysToThisNode = 0;
			
			int lookBack = 1;
			try {
				while(joltageAdapters.get(idx)-joltageAdapters.get(idx-lookBack)<=3) {
					waysToThisNode += waysToNode.get(idx-lookBack);
					lookBack++;
				}
			} catch (IndexOutOfBoundsException e) {}

			waysToNode.add(waysToThisNode);
		}	
		Long partTwoSolution = waysToNode.get(waysToNode.size()-1);

		return new String[] { String.valueOf(partOneSolution), String.valueOf(partTwoSolution) };
	}

}