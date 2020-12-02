package adventOfCode2020;

import java.util.ArrayList;
import java.util.List;

import day01.Day01;

public class AdventOfCode2020 {
	private static List<IDaySolution> completedDays = new ArrayList<>();
	
	public AdventOfCode2020() {
		completedDays.add(new Day01());
	}
	
	public void printCompletedSolutions() {
		for(IDaySolution completedSolution : completedDays) {
			int dayNumber = completedSolution.getDayNumber();
			
			String[] solution = completedSolution.getSolution();
			String solutionPartOne = solution[0];
			String solutionPartTwo = solution[1];
			
			
			String solution_output_string = String.format("Day %d, partOne: %s, partTwo: %s.", dayNumber, solutionPartOne, solutionPartTwo);
			
			System.out.println(solution_output_string);
		}
	}

	public static void main(String[] args) {
		new AdventOfCode2020().printCompletedSolutions();
	}

}
