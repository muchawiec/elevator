package com.iddqd.elevator.api.impl;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iddqd.elevator.api.Elevator.Direction;

/**
 * Implementation of thread representing moving elevator.
 * 
 * @author lmucha
 *
 */
public class RunningElevator implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(RunningElevator.class);
	
	private ElevatorImpl elevator;	
	private Integer nextToStop;	
	private int time;
	
	public RunningElevator(ElevatorImpl elevator) {
		this.elevator = elevator;	
		time = 1000;
	}

	@Override
	public void run() {
		while(!elevator.getFloorsToVisit().isEmpty()) { // elevator is running as long as there are floors to visit
			try {
				if (nextToStop == null) {
					closeDoor();
					nextToStop = elevator.getFloorsToVisit().get(0); // get next floor from the list
					elevator.setAddressedFloor(nextToStop);
					
					// if elevator reached top or bottom floors from their list, 
					// change direction and reorder list of floors to visit 
					if (nextToStop < elevator.currentFloor() && elevator.getDirection().equals(Direction.UP)) {
						elevator.setDirection(Direction.DOWN);
						Collections.sort(elevator.getFloorsToVisit(), Collections.reverseOrder());
					} else if (nextToStop > elevator.currentFloor() && elevator.getDirection().equals(Direction.DOWN)) {
						elevator.setDirection(Direction.UP);
						Collections.sort(elevator.getFloorsToVisit());
					}
					
				}
			
				if (nextToStop != elevator.currentFloor()) {
					// if it is not floor where elevator stops
					if (!elevator.isBusy()) {
						elevator.setBusy(true);
						closeDoor();
					}
					move();
				} else {
					// if elevator reaches floor to visit
					if (elevator.isBusy()) {
						openDoor();
						elevator.getFloorsToVisit().remove(nextToStop);
						nextToStop = null;
					}
				}
			} catch (InterruptedException ex) {
				logger.error("Elevator {} got stuck!", elevator.getId());
			}
		}
		elevator.setBusy(false);
		elevator.setDirection(Direction.NONE);
	}

	private void closeDoor() throws InterruptedException {
		logger.debug("Elevator {}: Closing door.", elevator.getId());
		Thread.sleep(2 * time);
	}
	
	private void openDoor() throws InterruptedException {
		logger.debug("Elevator {}: Arrived to floor {}. Opening door", elevator.getId(), elevator.currentFloor());
		Thread.sleep(2 * time);
	}
	
	private void move() throws InterruptedException {
		Thread.sleep(2 * time);
		nextToStop = elevator.getFloorsToVisit().get(0);
		Direction dir = elevator.getDirection();
		int curr = elevator.currentFloor();
		switch (dir) {
			case UP: elevator.setFloor(++curr); logger.debug("Elevator {}: Moving up to {} floor", elevator.getId(), elevator.currentFloor()); break;
			case DOWN: elevator.setFloor(--curr); logger.debug("Elevator {}: Moving down to {} floor", elevator.getId(), elevator.currentFloor()); break;
			case NONE:
		}
	}
	
	public void setTime(int time) {
		this.time = time;
	}
}
