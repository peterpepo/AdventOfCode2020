package day04;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventOfCode2020.IDaySolution;
import commons.PuzzleInputReader;

public class Day04 implements IDaySolution {
	
	public int getDayNumber() {
		return 4;
	}
	
	private final String INPUT_FILE_NAME = "src/Day04/day04_input.txt";
	
	// All from PassportFieldType enum except OTHER
	private final List<PassportField> REQUIRED_FIELDS = Arrays.asList(PassportField.BYR, PassportField.IYR,
			PassportField.EYR, PassportField.HGT, PassportField.HCL, PassportField.ECL, PassportField.PID);

	private final String FIELD_PATTERN = "(\\S+)(?:\\:)(\\S+)";
	private final Pattern FIELD_RE_PATTERN = Pattern.compile(FIELD_PATTERN);
	
	private enum PassportField {
		BYR("byr", "19[2-9][0-9]|200[0-2]"),
		IYR("iyr", "201[0-9]|2020"),
		EYR("eyr", "202[0-9]|2030"),
		HGT("hgt", "1(?:5[0-9]|[6-8][0-9]|9[0-3])cm|(?:59|6[0-9]|7[0-6])in"),
		HCL("hcl", "#[a-f0-9]{6}"),
		ECL("ecl", "amb|blu|brn|gry|grn|hzl|oth"),
		PID("pid", "[0-9]{9}"),
		OTHER("#OTHER_PLACEHOLDER#", null);	// Any other field, e.g. CID, no validation

		private final String label;
		private final String pattern;

		private PassportField(String label, String pattern) {
			this.label = label;
			this.pattern = pattern;
		}
		
		/**
		 * Returns enum value based on label. If no value is found, OTHER is returned. 
		 */
		public static PassportField fromLabel(String searchedLabel) {
			for (PassportField currentPassportField : PassportField.values()) {
				if (currentPassportField.label.equals(searchedLabel)) {
					return currentPassportField;
				}
			}
			return OTHER;
		}

		/**
		 * Validates inputToCheck against pattern given by field type. If no rule is defined, field is always considered valid. 
		 */
		public boolean isInputValid(String inputToCheck) {
			if (pattern!=null) {
				return matchesPattern(pattern, inputToCheck);
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Checks whether string matches pattern specified.
	 */
	private static boolean matchesPattern(String patternToMatch, String stringToCheck) {
		Pattern checkedPattern = Pattern.compile(patternToMatch);
		Matcher checkedPatternMatcher = checkedPattern.matcher(stringToCheck);

		return checkedPatternMatcher.matches();
	}
	
	/**
	 * Checks, whether passport contains all mandatory fields.
	 */
	private boolean containsAllMandatoryFields(Map<PassportField, String> passportToCheck) {
		return passportToCheck.keySet().containsAll(REQUIRED_FIELDS);
	}
	
	/**
	 * Checks, whether all fields on passports are valid.
	 */
	private boolean areAllFieldsValid(Map<PassportField, String> passportToCheck) {
		boolean passportValid = true;

		for (Map.Entry<PassportField, String> passportFieldNameValue : passportToCheck.entrySet()) {
			PassportField fieldType = passportFieldNameValue.getKey();
			String fieldValue = passportFieldNameValue.getValue();

			if (!fieldType.isInputValid(fieldValue)) {
				passportValid = false;
				break;
			}
		}

		return passportValid;

	}

	public String[] getSolution() {
		int passportsMandatoryFieldsCount = 0;		// partOne counter
		int passportsMandatoryValidFieldsCount = 0;	// partTwo counter

		
		Map<PassportField, String> currentPassport = new HashMap<PassportField, String>();

		List<String> puzzleInputLines = new PuzzleInputReader(INPUT_FILE_NAME).getListOfLines();
		
		for (int idx = 0; idx < puzzleInputLines.size(); idx++) {
			String puzzleInputLine = puzzleInputLines.get(idx);

			// Parse data if row is not empty
			if (puzzleInputLine.length() > 0) {
				Matcher inputLineReMatcher = FIELD_RE_PATTERN.matcher(puzzleInputLine);

				while (inputLineReMatcher.find()) {
					String fieldName = inputLineReMatcher.group(1);
					PassportField fieldType = PassportField.fromLabel(fieldName);
					
					String fieldValue = inputLineReMatcher.group(2);

					currentPassport.put(fieldType, fieldValue);
				}
			}
			
			// Reached end of input or blank line
			if (idx == puzzleInputLines.size() - 1 || puzzleInputLine.length() == 0) {
				
				if(containsAllMandatoryFields(currentPassport)) {
					passportsMandatoryFieldsCount++;
				
					if (areAllFieldsValid(currentPassport)) {
						passportsMandatoryValidFieldsCount++;
					}
				}
				currentPassport.clear();
			}
		}
		
		return new String[] { String.valueOf(passportsMandatoryFieldsCount), String.valueOf(passportsMandatoryValidFieldsCount) };
	}
}