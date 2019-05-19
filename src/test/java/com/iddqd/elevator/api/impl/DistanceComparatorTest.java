package com.iddqd.elevator.api.impl;

import static com.iddqd.elevator.api.Elevator.Direction.DOWN;
import static com.iddqd.elevator.api.Elevator.Direction.UP;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class DistanceComparatorTest {

	private DistanceComparator comparator = new DistanceComparator(5, 10);
	private ElevatorImpl elev1 = new ElevatorImpl(1);
	private ElevatorImpl elev2 = new ElevatorImpl(2);
	
	@Test
	public void testDistanceFromGroundFloor() {
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Distances should be equal", 0, actualResult);
	}
	
	@Test
	public void testFirstCloser() {
		elev1.setFloor(4);
		elev2.setFloor(3);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("First elevator should be closer", -1, actualResult);
	}
	
	@Test
	public void testSecondCloser() {
		elev1.setFloor(1);
		elev2.setFloor(8);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Second elevator should be closer", 1, actualResult);
	}
	
	@Test
	public void testRunningUpInDirection() {
		elev1.setFloor(0);
		elev1.setBusy(true);
		elev1.setDirection(UP);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Both are at the same distance", 0, actualResult);
	}
	
	@Test
	public void testRunningUpInDirectionWithStops() {
		elev1.setFloor(0);
		elev1.setBusy(true);
		elev1.setDirection(UP);
		elev1.getFloorsToVisit().add(2);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Second elevator should be closer", 1, actualResult);
	}
	
	@Test
	public void testRunningUpInDirectionAboveFloor() {
		elev1.setFloor(7);
		elev1.setBusy(true);
		elev1.setDirection(UP);
		elev1.getFloorsToVisit().add(9);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Second elevator should be closer", 2, actualResult);
	}
	
	@Test
	public void testRunningDownInDirection() {
		elev1.setFloor(9);
		elev1.setBusy(true);
		elev1.setDirection(DOWN);
		elev1.getFloorsToVisit().add(0);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("First elevator should be closer", -1, actualResult);
	}
	
	@Test
	public void testRunningDownInDirectionWithStops() {
		elev1.setFloor(9);
		elev1.setBusy(true);
		elev1.setDirection(DOWN);
		elev1.getFloorsToVisit().add(7);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Both are in the same distance", 0, actualResult);
	}
	
	@Test
	public void testRunningDownInDirectionBelowFloor() {
		elev1.setFloor(3);
		elev1.setBusy(true);
		elev1.setDirection(DOWN);
		elev1.getFloorsToVisit().add(1);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Second elevator should be closer", 2, actualResult);
	}
	
	@Test
	public void testStopsCount() {
		elev1.setFloor(1);
		elev1.setBusy(true);
		elev1.setDirection(UP);
		elev1.getFloorsToVisit().add(2);
		elev1.getFloorsToVisit().add(3);
		elev1.getFloorsToVisit().add(4);
		elev2.setFloor(0);
		int actualResult = comparator.compare(elev1, elev2);
		assertEquals("Second elevator should be closer", 2, actualResult);
	}
}
