package day05;

import java.util.Comparator;
import java.util.List;
import commons.PuzzleInputReader;

import adventOfCode2020.IDaySolution;

public class Day05 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day05/day05_input.txt";
	
	public int getDayNumber() {
		return 5;
	}
	
	/**
	 * Calculates seatId based on seatCode specified.
	 * Seat code is simple binary number in following pattern (R-ROW, SEAT-S): RRRRRRRSSS
	 * 1) All characters are replaced with corresponding bit -> F/L -> 0, B/R -> 1
	 * 2) Multiplication by 8 - can be represented as bitwise shift left by 3 positions (<<3) - this "appends" three 0 characters at the end
	 * 3) After previous step, last 3 bits are always 0 => we don't need to sum, just "replace" 0 with whatever SSS is
	 * 4) In the end, steps 2) and 3) are performed by not touching the input at all :)
	 * 5) All that is needed is get decimal value from binary value from step 1)
	 */
	private int getSeatId(String seatCode) {
		seatCode = seatCode.replaceAll("F|L", "0").replaceAll("B|R", "1");
		return Integer.parseInt(seatCode, 2);
	}
	
	/**
	 * Compare two seat codes in two steps.
	 * 1) Compare based on ROW (reversed)
	 * 2) Compare based on SEAT
	 */
	private Comparator<String> seatCodeComparator = new Comparator<String>() {
		public int compare(String seatCode1, String seatCode2) {
			
			// B=1, F=0, whereas B<0 alphabetically => output needs to be flipped
			int compareSeatRow = -1 * seatCode1.substring(0, 7).compareTo(seatCode2.substring(0, 7));
			
			if(compareSeatRow != 0) {
				return compareSeatRow;
			} else {
				return seatCode1.substring(7).compareTo(seatCode2.substring(7));
			}
		}
	};

	public String[] getSolution() {
		
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();	// Load puzzle input
		
		/*
		 * Part One
		 * 1) sort lowest to highest
		 * 2) pick last(highest bitwise)
		 * 3) get seatId
		 */
		puzzleInputLines.sort(seatCodeComparator);
		
		int partOneMaxSeatId = getSeatId(puzzleInputLines.get(puzzleInputLines.size()-1));
		
		/*
		 * Part Two
		 * 1) take lowest to highest sorted (bitwise)
		 * 2) loop through
		 * 3) get seat id
		 * 4) compare with previous seatId
		 * 5) if difference > 1, we found our seat
		 */
		Integer partTwoOurSeatId = 0;
		
		Integer previousSeatId = null;
		for (String seatCode : puzzleInputLines) {
			int currentSeatId = getSeatId(seatCode);
			
			if(previousSeatId!=null && currentSeatId - previousSeatId > 1) {
				partTwoOurSeatId = currentSeatId - 1;
				break;
			} else {
				previousSeatId = currentSeatId;
			}
		}
		
		return new String[] { String.valueOf(partOneMaxSeatId), String.valueOf(partTwoOurSeatId) };
	}

}