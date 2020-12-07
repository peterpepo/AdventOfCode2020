package day07;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day07 implements IDaySolution {
	private static final String SHINY_GOLD = "shiny gold";

	private static final String INPUT_FILE_NAME = "src/Day07/day07_input.txt";
	
	private static final String BAG_CONTENT_PATERN = "(\\d+) (\\w+ \\w+)";
	
	private final Pattern BAG_CONTENT_RE_PATTERN = Pattern.compile(BAG_CONTENT_PATERN);
	
	private Map<String, List<String>> allBags = new HashMap<>();
	
	public int getDayNumber() {
		return 7;
	}
	
	/**
	 * Returns list of (parrent) bags, which encase childBagName.
	 */
	private List<String> getEncasingBags(String childBagName) {
		List<String> encasingBags = new LinkedList<>();
		
		for(Map.Entry<String, List<String>> bagEntry : allBags.entrySet()) {
			if (bagEntry.getValue().contains(childBagName)) {
				encasingBags.add(bagEntry.getKey());
			}
		}
		
		return encasingBags;
	}

	public String[] getSolution() {
		/*
		 * Parse Input
		 */
		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();
		
		for(String puzzleInputLine : puzzleInputLines) {
			// 1) Split on " bags contain"
			String[] bagAndContent = puzzleInputLine.split(" bags contain");
			String parrentBagName = bagAndContent[0];
			String parrentBagContent = bagAndContent[1];

			
			// 2) Find all bags contained in parrent
			List<String> parrentBag = new LinkedList<>();
			Matcher contentReMatcher = BAG_CONTENT_RE_PATTERN.matcher(parrentBagContent);
			
			while (contentReMatcher.find()) {
				int childBagCount = Integer.valueOf(contentReMatcher.group(1));
				String childBagName = contentReMatcher.group(2);
				
				for(int itemCount = 0; itemCount < childBagCount; itemCount++) {
					parrentBag.add(childBagName);
				}
			}
			allBags.put(parrentBagName, parrentBag);

		}
		
		/*
		 * Part One
		 */
		Set<String> seenBags = new HashSet<>();			// Set of bags, which we have already seen
		List<String> bagsToCheck = new LinkedList<>();	// Bags, which encasings we still need to check
		
		bagsToCheck.add(SHINY_GOLD);	// Initialize queue - check all bags, which encase "shiny gold"
		
		while(bagsToCheck.size() > 0) {
			String currentBag = bagsToCheck.remove(0);	// Pop 0th element
			
			if(!seenBags.contains(currentBag)) {
				seenBags.add(currentBag);
				bagsToCheck.addAll(getEncasingBags(currentBag));
			}			
		}
		int partOneOutermostBagCount = seenBags.size() - 1;	// Substract initial SHINY_GOLD, since it is not contained in itself

		
		/*
		 * Part Two
		 */	
		int partTwoBagsInsideShinyGold = 0;

		List<String> bagsToOpen = new LinkedList<>();
		
		bagsToOpen.add(SHINY_GOLD);
		
		while(bagsToOpen.size() > 0) {
			String currentBag = bagsToOpen.remove(0);	// Remove currentBag bag from queue
			partTwoBagsInsideShinyGold++;				// Increase counter of outer bags
				
			bagsToOpen.addAll(allBags.get(currentBag));		// Add all contents inside currentBag
		}
		partTwoBagsInsideShinyGold -= 1;	// Substract one encasing bag, since SHINY_GOLD was already open in the beginning
		
		return new String[] { String.valueOf(partOneOutermostBagCount), String.valueOf(partTwoBagsInsideShinyGold) };
	}

}