package adventOfCode2020;

import java.util.ArrayList;
import java.util.List;

import day01.Day01;
import day02.Day02;
import day03.Day03;
import day04.Day04;
import day05.Day05;
import day06.Day06;
import day07.Day07;
import day08.Day08;
import day09.Day09;
import day10.Day10;
import day11.Day11;
import day12.Day12;
import day14.Day14;
import day15.Day15;
import day16.Day16;
import day17.Day17;
import day18.Day18;
import day19.Day19;

public class AdventOfCode2020 {
	private static List<IDaySolution> completedDays = new ArrayList<>();
	
	public AdventOfCode2020() {
		completedDays.add(new Day01());
		completedDays.add(new Day02());
		completedDays.add(new Day03());
		completedDays.add(new Day04());
		completedDays.add(new Day05());
		completedDays.add(new Day06());
		completedDays.add(new Day07());
		completedDays.add(new Day08());
		completedDays.add(new Day09());
		completedDays.add(new Day10());
		completedDays.add(new Day11());
		completedDays.add(new Day12());
		completedDays.add(new Day14());
		completedDays.add(new Day15());
		completedDays.add(new Day16());
		completedDays.add(new Day17());
		completedDays.add(new Day18());
		completedDays.add(new Day19());
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
