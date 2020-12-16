package day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day16 implements IDaySolution {
//	private final String INPUT_FILE_NAME = "src/Day16/day16_input_test_part_one.txt";
//	private final String INPUT_FILE_NAME = "src/Day16/day16_input_test_part_two.txt";
	private final String INPUT_FILE_NAME = "src/Day16/day16_input.txt";
	
	
	private static final String RULE_PATTERN = "(.+): (\\d+)-(\\d+) or (\\d+)-(\\d+)";
	private final Pattern RE_RULE_PATTERN = Pattern.compile(RULE_PATTERN);
	
	private static final String TICKET_FIELD = "(?<=^|,)\\d+";
	private final Pattern RE_TICKET_PATTERN = Pattern.compile(TICKET_FIELD);
	
	public int getDayNumber() {
		return 16;
	}

	public String[] getSolution() {
		Map<String, int[]> ticketRules = new HashMap<>();
		List<Integer> myTicket = null;
		List<List<Integer>> otherTickets = new ArrayList<>();
		
		for(String puzzleInputLine : new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines()) {
			Matcher ruleMatcher = RE_RULE_PATTERN.matcher(puzzleInputLine);
			Matcher ticketFieldMatcher = RE_TICKET_PATTERN.matcher(puzzleInputLine);
			
			if(ruleMatcher.find()) {
				ticketRules.put(ruleMatcher.group(1),
						new int[] {Integer.valueOf(ruleMatcher.group(2)),
								Integer.valueOf(ruleMatcher.group(3)),
								Integer.valueOf(ruleMatcher.group(4)),
								Integer.valueOf(ruleMatcher.group(5))});
			} else if(ticketFieldMatcher.find()) {
				List<Integer> currentlyReadTicket = new ArrayList<>();
				ticketFieldMatcher.reset();
				while (ticketFieldMatcher.find()) {
					currentlyReadTicket.add(Integer.valueOf(ticketFieldMatcher.group(0)));
				}
				
				if(myTicket==null) {
					myTicket = currentlyReadTicket;
				} else {
					otherTickets.add(currentlyReadTicket);
				}
			}			
		}
		
		// Part One
		int invalidFieldsSum = 0;
		
		for(List<Integer> otherTicket : otherTickets) {
			for(Integer otherTicketField : otherTicket) {
				boolean fieldValid = false;
				
				for(int[] ticketRule : ticketRules.values()) {
					if((otherTicketField >= ticketRule[0] && otherTicketField <= ticketRule[1]) ||
							(otherTicketField >= ticketRule[2] && otherTicketField <= ticketRule[3])) {
						fieldValid = true;
						break;
					}
				}
				
				if(!fieldValid) {
					invalidFieldsSum += otherTicketField;
				}
				
			}
		}
		
		// Part Two
		Iterator<List<Integer>> otherTicketsIterator = otherTickets.iterator();
		// 1) Get rid of invalid tickets
		while(otherTicketsIterator.hasNext()) {
			List<Integer> otherTicket = otherTicketsIterator.next();
			for(Integer otherTicketField : otherTicket) {
				boolean fieldValid = false;
				
				for(int[] ticketRule : ticketRules.values()) {
					if((otherTicketField >= ticketRule[0] && otherTicketField <= ticketRule[1]) ||
							(otherTicketField >= ticketRule[2] && otherTicketField <= ticketRule[3])) {
						fieldValid = true;
						break;
					}
				}
				
				if(!fieldValid) {
					otherTicketsIterator.remove();
				}
			}
		}
		
		// 2) Let's assume every rule is applicable for each column
		List<List<String>> applicableRules = new ArrayList<>();
		for(int fieldIdx = 0; fieldIdx < otherTickets.get(0).size(); fieldIdx++) {
			applicableRules.add(new ArrayList(ticketRules.keySet()));
		}
		
		// 3) Go through all tickets field by field and remove rules which are not applicable for given position
		// Go through all tickets
		for(List<Integer> otherTicket : otherTickets) {
			// Go through all fields by index
			for(int fieldIdx = 0; fieldIdx < otherTicket.size(); fieldIdx++) {
				Integer fieldValue = otherTicket.get(fieldIdx);
				// Go through all rules for given position
				Iterator<String> ruleIterator = applicableRules.get(fieldIdx).iterator();
				while(ruleIterator.hasNext()) {
					int[] currentRule = ticketRules.get(ruleIterator.next());
					
					// Check whether this value fits interval
					if (!((fieldValue >= currentRule[0] && fieldValue <= currentRule[1])||
							(fieldValue >= currentRule[2] && fieldValue <= currentRule[3]))) {
						ruleIterator.remove();
					}
					
				}
			}
		}
		
		// 4) Find field position, where only one rule is applicable - remove this rule from all other positions. Repeat until options can't be further reduced.
		boolean ruleSetImproved = true;
		List<String> cleanRules = new ArrayList<>();
		
		while(ruleSetImproved) {
			ruleSetImproved = false;
			String ruleToclean = null;

			// Find first with length of 1 (which hasn't been cleaned before)
			for(List<String> applicableRule : applicableRules) {
				if (applicableRule.size() == 1 && !cleanRules.contains(applicableRule.get(0))) {
					ruleToclean = applicableRule.get(0);
					cleanRules.add(ruleToclean);
					break;
				}
			}
			
			// Loop through all rules and remove ruleToClean (skip self)
			for(List<String> currentlyCleanedRule : applicableRules) {
				if (currentlyCleanedRule.size() >= 2 && currentlyCleanedRule.contains(ruleToclean)) {
					currentlyCleanedRule.remove(ruleToclean);
					ruleSetImproved = true;
				}
			}
		}
		
		// 5) Multiply all "departure" value
		long departureProduct = 1;
		
		for(int fieldIdx = 0; fieldIdx < applicableRules.size(); fieldIdx++) {
			if (applicableRules.get(fieldIdx).get(0).startsWith("departure")) {
				departureProduct *= myTicket.get(fieldIdx);
			}
		}
		
		
		return new String[] { String.valueOf(invalidFieldsSum), String.valueOf(departureProduct) };
	}

}