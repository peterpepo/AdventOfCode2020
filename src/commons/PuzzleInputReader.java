package commons;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PuzzleInputReader {
	
	private List<String> puzzle_input_lines = new ArrayList<>();
	
	public PuzzleInputReader(String filename) {	
		File file_to_read_from = new File(filename);
		
		try {
			BufferedReader puzzle_input_buffered_reader = new BufferedReader(new FileReader(file_to_read_from));
			
			String current_line;
			
			while ((current_line = puzzle_input_buffered_reader.readLine()) != null) {
				puzzle_input_lines.add(current_line);
			}
				
				
		} catch (FileNotFoundException e) {
			System.err.println(String.format("Specified file %s not found.", filename));
		}
		catch (IOException e) {
			System.err.println(String.format("Error reading from file %s.", filename));
		}
	}
	
	public List<String> getListOfLines() {
		return puzzle_input_lines;
	}

}
