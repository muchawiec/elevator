package com.iddqd.elevator.api.impl;

import static com.iddqd.elevator.api.Elevator.Direction.DOWN;
import static com.iddqd.elevator.api.Elevator.Direction.UP;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.iddqd.elevator.api.Elevator.Direction;

/**
 * Comparator used for calculation which elevator is closest to requested floor.
 * 
 * @author lmucha
 *
 */
public class DistanceComparator implements Comparator<ElevatorImpl> {

	private int floor;
	private int maxFloor;
	
	public DistanceComparator(int floor, int maxFloor) {
		this.floor = floor;
		this.maxFloor = maxFloor;
	}
	
	@Override
	public int compare(ElevatorImpl o1, ElevatorImpl o2) {
		int d1 = getDistance(o1, floor);
		int d2 = getDistance(o2, floor);
		return d1 - d2;
	}
	
	private int getDistance(ElevatorImpl elevator, int floor) {
		int distance = 2 * maxFloor;
		if (elevator.isBusy()) {
			distance = calculateDistance(elevator, floor);
		} else {
			distance = Math.abs(floor - elevator.currentFloor());
		}
		return distance;
	}
	
	private int calculateDistance(ElevatorImpl elevator, int floor) {
		int distance = 2 * maxFloor;
		if (elevator.getDirection().equals(UP)) {	
			if (floor > elevator.currentFloor()) {
				// Stops also take some time, this factor is also considered in calculations. 
				// Each stop equals one additional distance unit/floor.
				distance = floor - elevator.currentFloor() + getStopsBeforeFloor(elevator, floor);
			} else {
				distance = 2 * Collections.max(elevator.getFloorsToVisit()) - elevator.currentFloor() - floor
						+ getStopsWithDirectionChange(elevator, floor);
			}
		} else if (elevator.getDirection().equals(DOWN)) {
			if (floor < elevator.currentFloor()) {
				distance = elevator.currentFloor() - floor + getStopsBeforeFloor(elevator, floor);
			} else {
				distance = elevator.currentFloor() + floor - 2 * Collections.min(elevator.getFloorsToVisit())
						+ getStopsWithDirectionChange(elevator, floor);
			}
		}
		return distance;
	}
	
	private int getStopsBeforeFloor(ElevatorImpl elev, int floor) {
		int counter = 0;
		Direction dir = elev.getDirection();
		List<Integer> toVisit = elev.getFloorsToVisit();
		if (!toVisit.isEmpty()) {
			if (dir.equals(UP)) {		
				for (Integer stop: toVisit) {
					if (stop < floor) {
						counter++;
					} else {
						break;
					}
				}
			} else if (dir.equals(DOWN)) {
				for (Integer stop: toVisit) {
					if (stop > floor) {
						counter++;
					} else {
						break;
					}
				}
			}
		}
		return counter;
	}
	
	private int getStopsWithDirectionChange(ElevatorImpl elev, int floor) {
		int counter = 0;
		Direction initialDir = elev.getDirection();
		List<Integer> toVisit = elev.getFloorsToVisit();
		if (!toVisit.isEmpty()) {
			if (initialDir.equals(UP)) {
				counter = (int) toVisit.stream().filter(s -> s > floor).count();
			} else if (initialDir.equals(DOWN)) {
				counter = (int) toVisit.stream().filter(s -> s < floor).count();
			}
		}
		return counter;
	}

}
