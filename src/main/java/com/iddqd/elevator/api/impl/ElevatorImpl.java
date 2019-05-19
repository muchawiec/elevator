package com.iddqd.elevator.api.impl;

import static com.iddqd.elevator.api.Elevator.Direction.DOWN;
import static com.iddqd.elevator.api.Elevator.Direction.NONE;
import static com.iddqd.elevator.api.Elevator.Direction.UP;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.iddqd.elevator.api.Elevator;

public class ElevatorImpl implements Elevator {

	private static final Logger logger = LoggerFactory.getLogger(ElevatorImpl.class);
	
	private int id;
	private Direction direction;
	private int addressedFloor;
	private int currentFloor;
	private boolean busy;
	private boolean outOfOrder;
	private List<Integer> floorsToVisit;
	
	public ElevatorImpl(int id) {
		this.id = id;
		busy = false;
		currentFloor = 0;
		addressedFloor = 0;
		direction = NONE;
		floorsToVisit = Lists.newArrayList();
	}
	
	@Override
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public int getAddressedFloor() {
		return addressedFloor;
	}

	public void setAddressedFloor(int floor) {
		addressedFloor = floor;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean isBusy() {
		return busy;
	}
	
	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isOutOfOrder() {
		return outOfOrder;
	}
	
	@Override
	public int currentFloor() {
		return currentFloor;
	}
	
	public void setFloor(int floor) {
		currentFloor = floor;
	}

	@Override
	public void moveElevator(int toFloor) {
		if (toFloor > currentFloor) {
			direction = UP;
		} else if (toFloor < currentFloor) {
			direction = DOWN;
		} else {
			direction = NONE;
		}
		addressedFloor = toFloor;
		busy = true;	
	}
	
	/**
	 * This method allows to assign elevator for visiting specified floor.
	 * 
	 * @param floor - number of floor which this elevator has to visit.
	 */
	public void addFloorToVisit(int floor) {
		if (!floorsToVisit.contains(floor)) {
			floorsToVisit.add(floor);
		}
		if (direction.equals(UP) && floor > currentFloor) {
			Collections.sort(floorsToVisit);
		} else if (direction.equals(DOWN) && floor < currentFloor) {
			Collections.sort(floorsToVisit, Collections.reverseOrder());
		}
		logger.trace("Order of visits: {}", floorsToVisit);
	}
	
	/**
	 * List of floors that this elevator is going to visit.
	 * 
	 * @return List<Integer> list of floors to visit.
	 */
	public List<Integer> getFloorsToVisit() {
		return floorsToVisit;
	}
	
	/**
	 * Triggers fire alarm mode for elevator.
	 */
	public void disable() {
		floorsToVisit = Lists.newArrayList(0);
		direction = DOWN;
		outOfOrder = true;
	}
	
	/**
	 * Allows to use elevator again after fire alarm is cancelled.
	 */
	public void enable() {
		direction = NONE;
		outOfOrder = false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + addressedFloor;
		result = prime * result + (busy ? 1231 : 1237);
		result = prime * result + currentFloor;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((floorsToVisit == null) ? 0 : floorsToVisit.hashCode());
		result = prime * result + id;
		result = prime * result + (outOfOrder ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElevatorImpl other = (ElevatorImpl) obj;
		if (addressedFloor != other.addressedFloor)
			return false;
		if (busy != other.busy)
			return false;
		if (currentFloor != other.currentFloor)
			return false;
		if (direction != other.direction)
			return false;
		if (floorsToVisit == null) {
			if (other.floorsToVisit != null)
				return false;
		} else if (!floorsToVisit.equals(other.floorsToVisit))
			return false;
		if (id != other.id)
			return false;
		if (outOfOrder != other.outOfOrder)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ElevatorImpl [id=" + id + ", direction=" + direction + ", addressedFloor=" + addressedFloor
				+ ", currentFloor=" + currentFloor + ", busy=" + busy + ", outOfOrder=" + outOfOrder
				+ ", floorsToVisit=" + floorsToVisit + "]";
	}

}
