package day08;

import java.util.ArrayList;
import java.util.List;

public class HandheldGameConsole {
	
	private List<List<Object>> instructions;
	private int instructionPointer, accumulator;
	private Status consoleStatus;
	
	public enum Status {
		RUNNING,
		LOOP,
		LAST_INSTRUCTION_QUIT
	}
	
	private static final String ACC = "acc";
	private static final String NOP = "nop";
	private static final String JMP = "jmp";
	
	public HandheldGameConsole () {
		this.instructions = new ArrayList<>();
		this.instructionPointer = 0;
		this.accumulator = 0;
		this.consoleStatus = Status.RUNNING;
	}
	
	/**
	 * Adds new instruction to the instruction list with execution count = 0. 
	 */
	public void addInstruction(String instructionCode, Integer instructionParameter) {
		this.instructions.add(new ArrayList<>(List.of(instructionCode, instructionParameter, 0)));
	}
	
	/**
	 * Processes instruction from instruction list and jumps to next.
	 * Changes HandheldGameConsole to LAST_INSTRUCTION_QUIT when console tries to load instruction beyond the last one.
	 * Changes HandheldGameConsole to LOOP when console detects, that it is trapped in a loop.
	 */
	private void processNextInstruction() {
		if(this.instructionPointer >= this.instructions.size()) {
			this.consoleStatus = Status.LAST_INSTRUCTION_QUIT;
		} else {
			List<Object> currentInstruction = this.instructions.get(instructionPointer);
			String currentInstructionCode = (String)currentInstruction.get(0);
			Integer currentInstructionParameter = (Integer)currentInstruction.get(1);
			Integer currentInstructionCounter = (Integer)currentInstruction.get(2);
			
			if(currentInstructionCounter > 0) {
				this.consoleStatus = Status.LOOP;
			} else if (currentInstructionCode.equals(ACC)) {
				this.accumulator += currentInstructionParameter;
			} else if (currentInstructionCode.equals(JMP)) {
				this.instructionPointer += currentInstructionParameter - 1;	// instruction pointer always increased by 1 after processing, account for that
			} else if (currentInstructionCode.equals(NOP)) {}

			this.instructionPointer++;
			
			currentInstruction.set(2, currentInstructionCounter+1);
		}
	}
	
	/**
	 * Resets the console to default state - go to first (0th) instruction, clear
	 * instruction counters and set accumulator to 0.
	 */
	public void resetConsole() {
		for(List<Object> currentInstruction : this.instructions) {
			currentInstruction.set(2, 0);
		}
		
		this.instructionPointer = 0;
		this.consoleStatus = Status.RUNNING;
		this.accumulator = 0;
	}
	
	/**
	 * Flips instruction from JMP->NOP and vice versa. 
	 */
	public void flipNopOrJmp(int instructionPosition) {
		List<Object> instructionToBeFlipped = this.instructions.get(instructionPosition);
		if (instructionToBeFlipped.get(0).equals(JMP)) {
			instructionToBeFlipped.set(0, NOP);
		} else {
			instructionToBeFlipped.set(0, JMP);
		}
	}
	
	/**
	 * Returns list<Integer> of positions containing either JMP or NOP instructions.
	 */
	public List<Integer> getNopOrJmpPositions() {
		List<Integer> nopOrJmpPositions = new ArrayList<>();
		
		for(int i = 0; i<this.instructions.size(); i++) {
			List<Object> instruction = this.instructions.get(i);
			String currentInstructionCode = (String)instruction.get(0);
			
			if (currentInstructionCode.equals(JMP) || currentInstructionCode.equals(NOP)) {
				nopOrJmpPositions.add(i);
			}
		}
		return nopOrJmpPositions;
	}
	
	/**
	 * Runs console, while it can.
	 */
	public void runUntilLoopOrQuit() {
		while(this.getStatus()==Status.RUNNING) {
			processNextInstruction();
		}
	}
	
	/**
	 * Get current status of the console.
	 */
	public Status getStatus() {
		return this.consoleStatus;
	}
	
	/**
	 * Returns current accumulator value of the console.
	 */
	public int getAccumulator() {
		return this.accumulator;
	}
}
