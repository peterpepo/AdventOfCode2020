package adventOfCode2020;

import java.util.ArrayList;
import java.util.List;

import day01.Day01;
import day02.Day02;
import day03.Day03;
import day04.Day04;
import day05.Day05;
import day06.Day06;

public class AdventOfCode2020 {
	private static List<IDaySolution> completedDays = new ArrayList<>();
	
	public AdventOfCode2020() {
		completedDays.add(new Day01());
		completedDays.add(new Day02());
		completedDays.add(new Day03());
		completedDays.add(new Day04());
		completedDays.add(new Day05());
		completedDays.add(new Day06());
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
