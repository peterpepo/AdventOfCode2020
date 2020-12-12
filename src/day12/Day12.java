package day12;

import java.util.HashMap;
import java.util.Map;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day12 implements IDaySolution {
	private final String INPUT_FILE_NAME = "src/Day12/day12_input.txt";
	
	private final Map<Character, int[]> cardinalDirections = new HashMap<>();
	private final Map<Character, int[]> rotations = new HashMap<>();
	
	public Day12() {
		super();
		cardinalDirections.put('E', new int[] {1, 0});
		cardinalDirections.put('S', new int[] {0, -1});
		cardinalDirections.put('W', new int[] {-1, 0});
		cardinalDirections.put('N', new int[] {0, 1});
		
		rotations.put('R', new int[] {1, -1});
		rotations.put('L', new int[] {-1, 1});
	}

	
	public int getDayNumber() {
		return 12;
	}
	
	private int[] sumArrays(int[] arrayOne, int[] arrayTwo) {
		int[] arraySum = new int[arrayOne.length];
		for(int i=0; i< arrayOne.length; i++) {
			arraySum[i] = arrayOne[i] + arrayTwo[i];
		}
		return arraySum;
	}
	
	private int[] multArrays(int[] arrayOne, int[] arrayTwo) {
		int[] arrayMult = new int[arrayOne.length];
		for(int i=0; i< arrayOne.length; i++) {
			arrayMult[i] = arrayOne[i] * arrayTwo[i];
		}
		return arrayMult;
	}
	
	private int getPartOneSolution() {		
		int[] shipDirection = cardinalDirections.get('E');
		int[] shipPosition = new int[] {0, 0};
		
		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			Character action = puzzleInputLine.charAt(0);
			int times = Integer.valueOf(puzzleInputLine.substring(1));
			
			if(cardinalDirections.containsKey(action)) {
				while(times>0) {
					shipPosition = sumArrays(shipPosition, cardinalDirections.get(action));
					times--;
				}
			} else if (rotations.containsKey(action)) {
				times /= 90;
				while(times > 0) {
					// Reverse [x,y] -> [y,x] and multiply by direction
					shipDirection = multArrays(new int[]{shipDirection[1], shipDirection[0]}, rotations.get(action));
					times--;
				}
			} else if (action=='F') {
				while(times>0) {
					shipPosition = sumArrays(shipPosition, shipDirection);
					times--;
				}
			}
		}
		return Math.abs(shipPosition[0]) + Math.abs(shipPosition[1]); 
	}
	
	private int getPartTwoSolution() {
		int[] waypointPosition = new int[] {10, 1};
		int[] shipPosition = new int[] {0, 0};
		
		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			Character action = puzzleInputLine.charAt(0);
			int times = Integer.valueOf(puzzleInputLine.substring(1));
			
			if(cardinalDirections.containsKey(action)) {
				while(times>0) {
					waypointPosition = sumArrays(waypointPosition, cardinalDirections.get(action));
					times--;
				}
			} else if (rotations.containsKey(action)) {
				times /= 90;
				while(times > 0) {
					waypointPosition = multArrays(new int[]{waypointPosition[1], waypointPosition[0]}, rotations.get(action));
					times--;
				}
			} else if (action=='F') {
				while(times>0) {
					shipPosition = sumArrays(shipPosition, waypointPosition);
					times--;
				}
			}
		}	
		return Math.abs(shipPosition[0]) + Math.abs(shipPosition[1]); 
	}
	
	public String[] getSolution() {
		return new String[] { String.valueOf(getPartOneSolution()), String.valueOf(getPartTwoSolution()) };
	}
}