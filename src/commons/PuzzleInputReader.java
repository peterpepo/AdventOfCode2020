package commons;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PuzzleInputReader {

	private List<String> puzzle_input_lines = new ArrayList<>();

	public PuzzleInputReader(String filename) {
		File file_to_read_from = new File(filename);

		FileReader puzzleInputFileReader = null;
		BufferedReader puzzleInputBufferedReader = null;

		try {
			puzzleInputFileReader = new FileReader(file_to_read_from);
			puzzleInputBufferedReader = new BufferedReader(puzzleInputFileReader);

			String current_line;

			while ((current_line = puzzleInputBufferedReader.readLine()) != null) {
				puzzle_input_lines.add(current_line);
			}

		} catch (FileNotFoundException e) {
			System.err.println(String.format("Specified file %s not found.", filename));
		} catch (IOException e) {
			System.err.println(String.format("Error reading from file %s.", filename));
		} finally {
			try {
				puzzleInputBufferedReader.close();
			} catch (Exception e) {
				System.err.println(String.format("Error closing BufferedReader for %s.", filename));
			}
			try {
				puzzleInputFileReader.close();
			} catch (Exception e) {
				System.err.println(String.format("Error closing FileReader for %s.", filename));
			}
		}
	}

	public List<String> getListOfLines() {
		return puzzle_input_lines;
	}

}
