package com.iddqd.elevator.api.impl;

import static com.iddqd.elevator.api.Elevator.Direction.DOWN;
import static com.iddqd.elevator.api.Elevator.Direction.UP;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class RunningElevatorTest {

	@Spy
	private ElevatorImpl elevator = new ElevatorImpl(1);
	
	@Test
	public void testRunUpAndDown() {
		elevator.setFloor(0);
		elevator.addFloorToVisit(10);
		elevator.addFloorToVisit(0);
		elevator.setDirection(UP);
		elevator.setAddressedFloor(10);
		elevator.setBusy(true);
		RunningElevator running = new RunningElevator(elevator);
		running.setTime(0);
		running.run();
		verify(elevator, times(2)).setFloor(1);
		verify(elevator, times(2)).setFloor(2);
		verify(elevator, times(2)).setFloor(3);
		verify(elevator, times(2)).setFloor(4);
		verify(elevator, times(2)).setFloor(5);
		verify(elevator, times(2)).setFloor(5);
		verify(elevator, times(2)).setFloor(6);
		verify(elevator, times(2)).setFloor(7);
		verify(elevator, times(2)).setFloor(8);
		verify(elevator, times(2)).setFloor(9);
		verify(elevator).setFloor(10);
		verify(elevator, times(2)).setFloor(0);
		Assert.assertFalse(elevator.getFloorsToVisit().contains(10));
		Assert.assertFalse(elevator.getFloorsToVisit().contains(0));
	}
	
	@Test
	public void testRunDownAndUp() {
		elevator.setFloor(10);
		elevator.addFloorToVisit(0);
		elevator.addFloorToVisit(10);
		elevator.setDirection(DOWN);
		elevator.setAddressedFloor(0);
		elevator.setBusy(true);
		RunningElevator running = new RunningElevator(elevator);
		running.setTime(0);
		running.run();
		verify(elevator, times(2)).setFloor(9);
		verify(elevator, times(2)).setFloor(8);
		verify(elevator, times(2)).setFloor(8);
		verify(elevator, times(2)).setFloor(7);
		verify(elevator, times(2)).setFloor(6);
		verify(elevator, times(2)).setFloor(5);
		verify(elevator, times(2)).setFloor(4);
		verify(elevator, times(2)).setFloor(3);
		verify(elevator, times(2)).setFloor(2);
		verify(elevator, times(2)).setFloor(1);
		verify(elevator).setFloor(0);
		verify(elevator, times(2)).setFloor(10);
		
		Assert.assertFalse(elevator.getFloorsToVisit().contains(0));
		Assert.assertFalse(elevator.getFloorsToVisit().contains(10));
	}
	
}
