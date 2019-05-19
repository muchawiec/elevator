package com.iddqd.elevator.api.impl;

import static com.iddqd.elevator.api.Elevator.Direction.DOWN;
import static com.iddqd.elevator.api.Elevator.Direction.NONE;
import static com.iddqd.elevator.api.Elevator.Direction.UP;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ElevatorImplTest {

	private ElevatorImpl elevator = new ElevatorImpl(1);
	
	@Test
	public void testMoveElevatorHigher() {
		int destFloor = 5;
		elevator.setFloor(3);
		elevator.moveElevator(destFloor);
		assertEquals("Direction should be UP", UP, elevator.getDirection());
		assertEquals("Destination floor should be equal " + destFloor, destFloor, elevator.getAddressedFloor());
		assertEquals("Elevator should be busy", true, elevator.isBusy());
	}
	
	@Test
	public void testMoveElevatorLower() {
		int destFloor = 5;
		elevator.setFloor(8);
		elevator.moveElevator(destFloor);
		assertEquals("Direction should be DOWN", DOWN, elevator.getDirection());
		assertEquals("Destination floor should be equal " + destFloor, destFloor, elevator.getAddressedFloor());
		assertEquals("Elevator should be busy", true, elevator.isBusy());
	}
	
	@Test
	public void testSameFloorMovement() {
		int destFloor = 5;
		elevator.setFloor(5);
		elevator.moveElevator(destFloor);
		assertEquals("Direction should be NONE", NONE, elevator.getDirection());
		assertEquals("Destination floor should be equal " + destFloor, destFloor, elevator.getAddressedFloor());
		assertEquals("Elevator should be busy", true, elevator.isBusy());
	}
	
	@Test
	public void testAddSameFloorToVisit() {
		elevator.setDirection(UP);
		elevator.setFloor(0);
		elevator.addFloorToVisit(5);
		elevator.addFloorToVisit(5);
		List<Integer> floorsToVisit = elevator.getFloorsToVisit();
		assertEquals("There should be only one floor to visit", 1, floorsToVisit.size());
		assertEquals("There should be visit to fifth floor", 5, floorsToVisit.get(0).intValue());
	}

	@Test
	public void testAddVisitBeforeStopWhileGoingUp() {
		elevator.setDirection(UP);
		elevator.setFloor(2);
		elevator.addFloorToVisit(8);
		elevator.addFloorToVisit(5);
		elevator.addFloorToVisit(6);
		List<Integer> floorsToVisit = elevator.getFloorsToVisit();
		assertEquals("There should be three stops", 3, floorsToVisit.size());
		assertEquals("There should be visit to fifth floor", 5, floorsToVisit.get(0).intValue());
		assertEquals("There should be visit to sixth floor", 6, floorsToVisit.get(1).intValue());
		assertEquals("There should be visit to eight floor", 8, floorsToVisit.get(2).intValue());
	}
	
	@Test
	public void testAddVisitBelowCurrentWhileGoingUp() {
		elevator.setDirection(UP);
		elevator.setFloor(5);
		elevator.addFloorToVisit(8);
		elevator.addFloorToVisit(2);
		elevator.addFloorToVisit(4);
		List<Integer> floorsToVisit = elevator.getFloorsToVisit();
		assertEquals("There should be three stops", 3, floorsToVisit.size());
		assertEquals("There should be visit to eight floor", 8, floorsToVisit.get(0).intValue());
		assertEquals("There should be visit to second floor", 2, floorsToVisit.get(1).intValue());
		assertEquals("There should be visit to fourth floor", 4, floorsToVisit.get(2).intValue());
	}
	
	@Test
	public void testAddVisitBeforeStopWhileGoingDown() {
		elevator.setDirection(DOWN);
		elevator.setFloor(9);
		elevator.addFloorToVisit(5);
		elevator.addFloorToVisit(7);
		elevator.addFloorToVisit(2);
		List<Integer> floorsToVisit = elevator.getFloorsToVisit();
		assertEquals("There should be three stops", 3, floorsToVisit.size());
		assertEquals("There should be visit to seventh floor", 7, floorsToVisit.get(0).intValue());
		assertEquals("There should be visit to fifth floor", 5, floorsToVisit.get(1).intValue());
		assertEquals("There should be visit to second floor", 2, floorsToVisit.get(2).intValue());
	}
	
	@Test
	public void testAddVisitAboveStopWhileGoingDown() {
		elevator.setDirection(DOWN);
		elevator.setFloor(6);
		elevator.addFloorToVisit(3);
		elevator.addFloorToVisit(9);
		elevator.addFloorToVisit(7);
		List<Integer> floorsToVisit = elevator.getFloorsToVisit();
		assertEquals("There should be three stops", 3, floorsToVisit.size());
		assertEquals("There should be visit to third floor", 3, floorsToVisit.get(0).intValue());
		assertEquals("There should be visit to ninth floor", 9, floorsToVisit.get(1).intValue());
		assertEquals("There should be visit to seventh floor", 7, floorsToVisit.get(2).intValue());
	}
	
	@Test
	public void testDisableElevator() {
		elevator.setDirection(UP);
		elevator.setFloor(5);
		elevator.addFloorToVisit(7);
		elevator.disable();
		assertEquals("Elevator should head to the ground floor", 0, elevator.getFloorsToVisit().get(0).intValue());
		assertEquals("Elevator should head DOWN", DOWN, elevator.getDirection());
		assertEquals("Elevator should be out of order", true, elevator.isOutOfOrder());
	}
	
	@Test
	public void testEnableElevator() {
		elevator.setDirection(UP);
		elevator.disable();
		elevator.enable();
		assertEquals("Elevator should reset direction", NONE, elevator.getDirection());
		assertEquals("Elevator shouldn't be out of order", false, elevator.isOutOfOrder());
	}
}
