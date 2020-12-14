package day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day14 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day14/day14_input.txt";
	
	private static final String MASK_PATTERN = "(?<=mask = ).\\w+";
	private final Pattern RE_MASK_PATTERN = Pattern.compile(MASK_PATTERN);
	
	private static final String MEM_PATTERN = "mem\\[(\\d+)\\] = (\\d+)";
	private final Pattern RE_MEM_PATTERN = Pattern.compile(MEM_PATTERN);
	
	public int getDayNumber() {
		return 14;
	}

	private long getPartOneSolution() {
		long currentValueMask = 0;
		long currentBitMask = 0;
		Map<Long, Long> memoryRegisters = new HashMap<>();	// Long indexed content of memory - longs
		
		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			Matcher maskMatcher = RE_MASK_PATTERN.matcher(puzzleInputLine);
			Matcher memMatcher = RE_MEM_PATTERN.matcher(puzzleInputLine);
			
			// Try to match mask or memory assignment
			if(maskMatcher.find()) {
				String maskStr = maskMatcher.group(0);
				// Replace 'X' by "anything" - these bits won't be used anyway thanks to mask calculated in next step
				currentValueMask = Long.valueOf(maskStr.replace('X', '0'),2);
				
				// Define mask - determining which bits are overwriting (0/1) and which are not (X)
				currentBitMask = Long.valueOf(maskStr.replace('0', '1').replace('X', '0'),2);
			} else if (memMatcher.find()) {
				long memAddress = Long.valueOf(memMatcher.group(1));
				
				long memValue = Long.valueOf(memMatcher.group(2));
				memValue = (memValue & ~currentBitMask) | (currentValueMask & currentBitMask);
				
				memoryRegisters.put(memAddress, memValue);
			}
		}
		
		// Sum values in memory registers
		return  memoryRegisters.values().stream().reduce(0l, Long::sum);
	}
	
	/**
	 * Returns all bitmasks possible to derive from input bitmask by replacing each X by 0 / 1 using recursion.
	 * E.g. for 1XXX1 returns list of 8 possible values.
	 */
	private List<String> flattenFloatingBits(String bitPattern) {
		List<String> flatPatterns= new ArrayList<>();
		
		if(!bitPattern.contains("X")) {
			flatPatterns.add(bitPattern);
		} else {
			flatPatterns.addAll(flattenFloatingBits(bitPattern.replaceFirst("X", "1")));
			flatPatterns.addAll(flattenFloatingBits(bitPattern.replaceFirst("X", "0")));
		}
		return flatPatterns;
	}
	
	private long getPartTwoSolution() {
		long bitMaskXPositions = 0;
	
		List<String> memoryAddrMasks = new ArrayList<>();	// Current list of address masks (e.g. 1X0X -> holds 1101, 1100, 1001, 1000)
		Map<Long, Long> memoryRegisters = new HashMap<>();	// Long indexed content of memory - longs
		
		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			Matcher maskMatcher = RE_MASK_PATTERN.matcher(puzzleInputLine);
			Matcher memMatcher = RE_MEM_PATTERN.matcher(puzzleInputLine);
			
			// Try to match mask or memory assignment
			if(maskMatcher.find()) {
				String inputMaskStr = maskMatcher.group(0);	// Mask read from puzzle
				
				// Clear and calculate all actual address masks combination by replacing X by either 1 or 0
				memoryAddrMasks.clear();
				memoryAddrMasks.addAll(flattenFloatingBits(inputMaskStr));
				
				// Create bit mask - to mark "replacing" bits (those marked X), all other - 0 / 1 bits are "or-ed" to memory address
				bitMaskXPositions = Long.parseLong(inputMaskStr.replace('1', '0').replace('X', '1'),2);
			} else if (memMatcher.find()) {
				long memAddress = Long.valueOf(memMatcher.group(1));
				long memValue = Long.valueOf(memMatcher.group(2));

				// Apply each of current memory address masks
				for(String memoryAddressMask : memoryAddrMasks) {
					/* 1) Copy original address from puzzle */
					long currentMemAddress = memAddress;
					
					/* 
					 * 2) "OR" 0 / 1 bits from puzzle mask - don't erase any bits
					 * 2a) Long.parseLong(memoryAddressMask, 2) - create long value from string representation
					 * 2b) (Long.parseLong(memoryAddressMask, 2) & ~bitMaskX) - by flipping bitmask we get 1s where in puzzle were 0 / 1.
					 * 	We get 0 where X were. By applying this flipped mask, we "erase" (set 0) to puzzle mask, where X are. In another words
					 * 	we preserve only those values where no X was.
					 * 2c) "OR" this mask to currentAddress -> when there was 0 and mask is 1 -> 1; 1 and 1 -> 1; 1 and 0 -> stays 1.
					 */
					currentMemAddress = currentMemAddress | (Long.parseLong(memoryAddressMask, 2) & ~bitMaskXPositions);
					
					/*
					 * 3) "OR" 0 / 1 bits which are at positions where X was previously in puzzle mask
					 * 3a) (currentMemAddress & ~bitMaskX) - erase bits from memory address on positions where Xs are in puzzle input mask
					 * 3b) (Long.parseLong(memoryAddressMask, 2) & bitMaskX) - keep only those bits of calculated mask, where Xs are in puzzle
					 * 3c) OR bits from 3b) to 3a) => effectively replacing bits at X positions 
					 */
					currentMemAddress = (currentMemAddress & ~bitMaskXPositions) | (Long.parseLong(memoryAddressMask, 2) & bitMaskXPositions);
					
					/* 4) Store calculated value */
					memoryRegisters.put(currentMemAddress, memValue);
				}
			}
		}
		// Sum values in memory registers
		return  memoryRegisters.values().stream().reduce(0l, Long::sum);
	}
	
	public String[] getSolution() {
		return new String[] { String.valueOf(getPartOneSolution()), String.valueOf(getPartTwoSolution()) };
	}
}