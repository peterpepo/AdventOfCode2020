package day09;

import java.util.List;
import java.util.stream.Collectors;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day09 implements IDaySolution {
//	private final String INPUT_FILE_NAME = "src/Day09/day09_input_test.txt";
//	private final static Integer PREAMBLE_LENGTH = 5;
	
	private final String INPUT_FILE_NAME = "src/Day09/day09_input.txt";
	private final static Integer PREAMBLE_LENGTH = 25;
	
	public int getDayNumber() {
		return 9;
	}

	public String[] getSolution() {
		List<Long> xmasMessage = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines().stream().map(num -> Long.parseLong(num)).collect(Collectors.toList());
		
		// Part One
		Integer partOneInvalidNumIdx = null;
		
		// Check every number after PREAMBLE_LENGTH (e.g. for preamble of 25, check from 26 on)
		for(int currentNumIdx = PREAMBLE_LENGTH; currentNumIdx < xmasMessage.size(); currentNumIdx++) {
			boolean sumForCurrentNumFound = false;	// Solution found flag
			Long currentNum = xmasMessage.get(currentNumIdx);
			
			/*
			 * Let the first number index be any in previous <current-PREAMBLE_LENGTH;current - 1)
			 * - since we are summing two numbers, starting with last before current is invalid
			 */
			for(int firstNumIdx = currentNumIdx - PREAMBLE_LENGTH; firstNumIdx < currentNumIdx - 1; firstNumIdx++) {
				
				// Try all from <firstIdx+1; currentNumIdx)
				for(int secondNumIdx = firstNumIdx + 1; secondNumIdx < currentNumIdx; secondNumIdx++) {
					Long firstSecondSum = xmasMessage.get(firstNumIdx) + xmasMessage.get(secondNumIdx);
					
					if(firstSecondSum.equals(currentNum)) {
						sumForCurrentNumFound = true;
						break;
					}
				}
				if(sumForCurrentNumFound) {
					break;
				}
				
			}
			
			if(!sumForCurrentNumFound) {
				partOneInvalidNumIdx = currentNumIdx;
				break;
			}
		}

		// Part Two
		boolean contiguousSetFound = false;
		int contiguousSetStartId = 1, contiguousSetEndIdx = 1;
		
		/*
		 * Let start index of contiguous be any of <0; partOneInvalidNumIdx - 1) -
		 * although set MAY consist just of single number, solution certainly won't be
		 * that single number. When it was, it would has been returned from partOne
		 * instead. (If previous number was smaller than current one - it would have been returned by partOne. Same applies when it was same as current.)
		 * Examples:
		 * preamble = 3; [1, 2, 3, 10, 10] -> the first "10" is marked as invalid, set certainly won't consist only of number 10 before
		 * preamble = 3; [1, 2, 3, 1, 10] -> the ony at index 4 would has been returned, not 10
		 * preamble = 3; [1, 2, 3, 5, 10] -> 10 won't consist of (5) only - single member set, is still might be LAST member (check next for-loop)
		 */
		for(int setStartIdx = 0; setStartIdx < partOneInvalidNumIdx - 1; setStartIdx++) {
			for(int setEndIdx = setStartIdx; setEndIdx < partOneInvalidNumIdx; setEndIdx++) {
				Long contiguousSetSetSum = 0l;
				
				for(int idx = setStartIdx; idx<=setEndIdx; idx++) {
					contiguousSetSetSum += xmasMessage.get(idx);
					
					if(contiguousSetSetSum > xmasMessage.get(partOneInvalidNumIdx)) {
						// In case the sum is already > than sum we are looking for, stop summing
						break;
					} else if (contiguousSetSetSum.equals(xmasMessage.get(partOneInvalidNumIdx))) {
						contiguousSetFound = true;
						contiguousSetStartId = setStartIdx;
						contiguousSetEndIdx = setEndIdx;
						break;
					}
				}
				if(contiguousSetFound) {
					break;
				}
			}
			if(contiguousSetFound) {
				break;
			}
		}
		Long contiguousMin = xmasMessage.subList(contiguousSetStartId, contiguousSetEndIdx).stream().min(Long::compare).get();
		Long contiguousMax = xmasMessage.subList(contiguousSetStartId, contiguousSetEndIdx).stream().max(Long::compare).get();

		return new String[] { String.valueOf(xmasMessage.get(partOneInvalidNumIdx)), String.valueOf(contiguousMin + contiguousMax) };
	}

}