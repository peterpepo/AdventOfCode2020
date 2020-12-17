package day17;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConwaysCube4D {
	
	private Set<Point4D> liveCells;
	private int[] minMax;
	private boolean trappedInLoop;
	private Integer maxAdjacentCellDistance;
	private Integer minAdjacentToFreeUpSeat;

	public ConwaysCube4D(Integer minAdjacentToFreeUpSeat, Integer maxAdjacentSeatDistance) {
		super();
		this.liveCells = new HashSet<>();
		this.trappedInLoop = false;
		this.minMax = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		this.minAdjacentToFreeUpSeat = minAdjacentToFreeUpSeat;
		this.maxAdjacentCellDistance = maxAdjacentSeatDistance;
	}
	
	public void initLiveCell(Point4D cellPosition) {
		putLiveCell(liveCells, minMax, cellPosition);
	}
	
	private void putLiveCell(Set<Point4D> setToAddTo, int[] minMaxToUpdate, Point4D cellPosition) {
		setToAddTo.add(cellPosition);
		minMaxToUpdate[0] = Math.min(minMaxToUpdate[0], cellPosition.getX());
		minMaxToUpdate[1] = Math.max(minMaxToUpdate[1], cellPosition.getX());
		minMaxToUpdate[2] = Math.min(minMaxToUpdate[2], cellPosition.getY());
		minMaxToUpdate[3] = Math.max(minMaxToUpdate[3], cellPosition.getY());
		minMaxToUpdate[4] = Math.min(minMaxToUpdate[4], cellPosition.getZ());
		minMaxToUpdate[5] = Math.max(minMaxToUpdate[5], cellPosition.getZ());
		minMaxToUpdate[6] = Math.min(minMaxToUpdate[6], cellPosition.getW());
		minMaxToUpdate[7] = Math.max(minMaxToUpdate[7], cellPosition.getW());
	}

	private Set<Point4D> getAdjacentPositions(Point4D originalPosition) {
		Set<int[]> directionsToCheck = new HashSet<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					for (int w = -1; w <= 1; w++) {
						if(x == 0 && y == 0 && z == 0 && w == 0) {
							continue;
						}
						directionsToCheck.add(new int[] {x, y, z, w});	
					}
				}
			}
		}
		
		Set<Point4D> adjacentPositions = new HashSet<>();
		Iterator<int[]> directionsToCheckIterator = directionsToCheck.iterator();
		
		// For each direction find closest seat (regardless of occupation)
		while(directionsToCheckIterator.hasNext()) {
			
			int currentX = originalPosition.getX();
			int currentY = originalPosition.getY();
			int currentZ = originalPosition.getZ();
			int currentW = originalPosition.getW();
			
			int[] currentDirection = directionsToCheckIterator.next();
			directionsToCheckIterator.remove();
			
			while(true) {
				currentX += currentDirection[0];
				currentY += currentDirection[1];
				currentZ += currentDirection[2];
				currentW += currentDirection[3];
				
				Point4D newPosition = new Point4D(currentX, currentY, currentZ, currentW);
							
				// Check if calculated position is on seat plan, and distance to original seat <= maximum distance (1 for partOne, unlimited for partTwo)
				if (maxAdjacentCellDistance!= null &&
						Math.max(
							Math.max(
									Math.max(Math.abs(currentX-originalPosition.getX()),Math.abs(currentY-originalPosition.getY())),
									Math.abs(currentZ-originalPosition.getZ())
							),
							Math.abs(currentW - originalPosition.getW())
						)> maxAdjacentCellDistance) {
					break;
				} else {
					adjacentPositions.add(newPosition);
					break;
				}
			}
		}
		return adjacentPositions;
	}
	
	private void animate() {
		Set<Point4D> newLiveCells = new HashSet<>();
		int[] newMinMax = new int[] {0, 0, 0, 0, 0, 0, 0, 0};

		// 1) Calculate new state
		for(int x = minMax[0] - 1; x <= minMax[1] + 1; x++) {
			for(int y = minMax[2] - 1; y <= minMax[3] + 1; y++) {
				for(int z = minMax[4] - 1; z <= minMax[5] + 1; z++) {
					for(int w = minMax[6] - 1; w <= minMax[7] + 1; w++) {
					
						Point4D currentPosition = new Point4D(x, y, z, w);
						boolean currentIsAlive = liveCells.contains(currentPosition);
						
						int adjacentAlive = 0;
						for(Point4D adjacentCellPosition : getAdjacentPositions(currentPosition)) {
							if(liveCells.contains(adjacentCellPosition)) {
								adjacentAlive += 1;
							}
						}
						
						if (currentIsAlive) {
							if (adjacentAlive == 2 || adjacentAlive == 3) {
								putLiveCell(newLiveCells, newMinMax, currentPosition);
							} else {
								// continue
							}
						} else {
							if (adjacentAlive == 3) {
								putLiveCell(newLiveCells, newMinMax, currentPosition);
							}
						}
					}
				}
			}
		}
		
		// 2) Compare new state with current(previous) - check whether we achieved stabilized state
		trappedInLoop = liveCells.containsAll(newLiveCells) && newLiveCells.containsAll(liveCells);
		
		
		// 3) Set new state as current
		liveCells = newLiveCells;
		minMax = newMinMax;
	}

//	public void runUntilLoop() {
//		while(!trappedInLoop) {
//			animate();
//		}
//	}
	
	public void runNtimes(int times) {
		while(times-- > 0) {
			animate();
		}
	}

	public Integer countLiveCells() {
		return liveCells.size();
	}
}