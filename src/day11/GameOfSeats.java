package day11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameOfSeats {
	public static final Integer OCCUPIED = 1;
	public static final Integer FREE = 0;
	
	private Map<Point, Integer> seats;
	private boolean trappedInLoop;
	private Integer minX;
	private Integer minY;
	private Integer maxX;
	private Integer maxY;
	private Integer maxAdjacentSeatDistance;
	private Integer minAdjacentToFreeUpSeat;

	public GameOfSeats(Integer minAdjacentToFreeUpSeat, Integer maxAdjacentSeatDistance) {
		super();
		this.seats = new HashMap<Point, Integer>();
		this.trappedInLoop = false;
		this.minX = 0;
		this.minY = 0;
		this.maxX = 0;
		this.maxY = 0;
		this.minAdjacentToFreeUpSeat = minAdjacentToFreeUpSeat;
		this.maxAdjacentSeatDistance = maxAdjacentSeatDistance;
	}

	public void setSeat(Point seatPosition, Integer seatValue) {
		seats.put(seatPosition, seatValue);			
		minX = Math.min(minX, seatPosition.getX());
		minY = Math.min(minY, seatPosition.getY());
		maxX = Math.max(maxX, seatPosition.getX());
		maxY = Math.max(maxY, seatPosition.getY());
	}

	protected Integer getSeat(Point seatPosition) {
		Integer seatValue = seats.get(seatPosition);		
		return seatValue != null ? seatValue : FREE;
	}

	private List<Point> getClosestAdjacentSeatsPositions(Point originalPosition) {
		List<Point> adjacentSeatsPositions = new LinkedList<>();
		
		List<List<Integer>> directionsToCheck = new LinkedList<List<Integer>>(Arrays.asList(
				Arrays.asList(-1, -1),	// <^
				Arrays.asList(-1, 0),	// <
				Arrays.asList(-1, 1),	// <v
				Arrays.asList(0, -1),	// ^
				Arrays.asList(0, 1),	// v
				Arrays.asList(1, -1),	// >^
				Arrays.asList(1, 0),	// >
				Arrays.asList(1, 1)		// >v
				));
		
		// For each direction find closest seat (regardless of occupation)
		while(!directionsToCheck.isEmpty()) {
			
			Integer currentX = originalPosition.getX();
			Integer currentY = originalPosition.getY();
			
			List<Integer> currentDirection = directionsToCheck.remove(0);
			
			while(true) {
				currentX += currentDirection.get(0);
				currentY += currentDirection.get(1);
				
				Point newPosition = new Point(currentX, currentY);
				
				// Check if calculated position is on seat plan, and distance to original seat <= maximum distance (1 for partOne, unlimited for partTwo)
				if (currentX < minX || currentX > maxX ||
						currentY < minY || currentY > maxY ||
						(maxAdjacentSeatDistance!= null && Math.max(Math.abs(currentX-originalPosition.getX()), Math.abs(currentY-originalPosition.getY())) > maxAdjacentSeatDistance)) {
					break;
				} else if (seats.containsKey(newPosition)) {
					adjacentSeatsPositions.add(newPosition);
					break;
				}
			}
		}
		return adjacentSeatsPositions;
	}
	
	private void animate() {
		Map<Point, Integer> newSeats = new HashMap<>();
		
		// 1) Calculate new state
		for(Point seatPosition : seats.keySet()) {
							
			Integer currentSeat = getSeat(seatPosition);
			Integer adjacentOccupied = 0;
			
			// Calculate adjacent occupied
			for(Point closestVisible : getClosestAdjacentSeatsPositions(seatPosition)) {
				adjacentOccupied += getSeat(closestVisible);
			}
			
			// Calculate value for new seat
			if(currentSeat.equals(FREE) && adjacentOccupied.equals(0)) {
				newSeats.put(seatPosition, OCCUPIED);
			} else if (currentSeat.equals(OCCUPIED) && adjacentOccupied >= minAdjacentToFreeUpSeat) {
				newSeats.put(seatPosition, FREE);
			} else {
				newSeats.put(seatPosition, getSeat(seatPosition));
			}
		}
		
		// 2) Compare new state with current(previous) - check whether we achieved stabilized state
		trappedInLoop=true;	// Let's asume we are looping, unless the next for proves us wrong
		
		for(Point seatPosition : seats.keySet()) {
			if(!getSeat(seatPosition).equals(newSeats.get(seatPosition))) {
				trappedInLoop = false;
				break;
			}
		}
		
		// 3) Set new state as current
		seats = newSeats;
	}

	public void runUntilLoop() {
		while(!trappedInLoop) {
			animate();
		}
	}

	public Integer countOccupiedSeats() {
		Integer occupiedSeatsCount = 0;
		
		for(Integer seatValue : seats.values()) {
			occupiedSeatsCount += seatValue;
		}
		return occupiedSeatsCount;
	}
}