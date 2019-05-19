package com.iddqd.elevator.api.impl;

import static com.iddqd.elevator.api.Elevator.Direction.DOWN;
import static com.iddqd.elevator.api.Elevator.Direction.NONE;
import static com.iddqd.elevator.api.Elevator.Direction.UP;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.iddqd.elevator.api.Elevator;
import com.iddqd.elevator.api.Elevator.Direction;
import com.iddqd.elevator.api.exceptions.NoElevatorAvailableException;
import com.iddqd.elevator.api.exceptions.WrongElevatorException;
import com.iddqd.elevator.api.exceptions.WrongFloorException;

@RunWith(SpringJUnit4ClassRunner.class)
public class ElevatorControllerImplTest {

	@InjectMocks
	private ElevatorControllerImpl controller = new ElevatorControllerImpl();
	
	@Mock
	private ElevatorStarter starter;
	
	@Before
	public void setUp() {
		controller.setParams(2, 10);
		controller.createElevators();
	}
	
	@Test
	public void testInitalStatus() {
		List<Elevator> expectedElevators = Lists.newArrayList(createElevator(1, 0, false, NONE), createElevator(2, 0, false, NONE));
		List<Elevator> actualElevators = controller.getElevators();
		verifyElevatorStatus(expectedElevators, actualElevators);
	}
	
	@Test
	public void testSelectingInNotExistingElevator() {
		try {
			controller.selectFloorInElevator(1000, 5);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue("Wrong exception caught", ex instanceof WrongElevatorException);
		}
	}
	
	@Test
	public void testSelectingNotExistingFloor() {
		try {
			controller.selectFloorInElevator(1, -5);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue("Wrong exception caught", ex instanceof WrongFloorException);
		}
	}
	
	@Test
	public void testSelectingFloorAboveTop() {
		try {
			controller.selectFloorInElevator(1, 15);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue("Wrong exception caught", ex instanceof WrongFloorException);
		}
	}
	
	@Test
	public void testRequestingToNotExistingFloor() {
		try {
			controller.requestElevator(-5);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue("Wrong exception caught", ex instanceof WrongFloorException);
		}
	}
	
	@Test
	public void testRequestingAboveTopFloor() {
		try {
			controller.requestElevator(15);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue("Wrong exception caught", ex instanceof WrongFloorException);
		}
	}
	
	@Test
	public void testRequestingDuringFireAlarm() {
		try {
			controller.triggerFireAlarm();
			controller.requestElevator(5);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue("Wrong exception caught", ex instanceof NoElevatorAvailableException);
		}
	}
	
	@Test
	public void testRequestingEquallyDistant() {
		Elevator elevator = controller.requestElevator(5);
		assertEquals("First elevator should be sent", 1, elevator.getId());
	}
	
	@Test
	public void testRequestingFirstCloser() {
		Map<Integer, ElevatorImpl> elevators = new HashMap<>();
		elevators.put(1, createElevator(1, 2, true, UP));
		elevators.put(2, createElevator(2, 0, false, NONE));
		controller.setElevators(elevators);
		Elevator actualElevator = controller.requestElevator(5);
		assertEquals("First elevator is closer", 1, actualElevator.getId());
	}
	
	@Test
	public void testRequestingSecondCloser() {
		Map<Integer, ElevatorImpl> elevators = new HashMap<>();
		ElevatorImpl one = createElevator(1, 2, true, DOWN);
		one.addFloorToVisit(0);
		elevators.put(1, one);
		elevators.put(2, createElevator(2, 0, false, NONE));
		controller.setElevators(elevators);
		Elevator actualElevator = controller.requestElevator(5);
		assertEquals("Second elevator is closer", 2, actualElevator.getId());
	}
	
	@Test
	public void testRequestingWithStops() {
		Map<Integer, ElevatorImpl> elevators = new HashMap<>();
		ElevatorImpl one = createElevator(1, 0, true, UP);
		one.addFloorToVisit(2);
		one.addFloorToVisit(3);
		elevators.put(1, one);
		ElevatorImpl two = createElevator(2, 0, true, UP);
		two.addFloorToVisit(4);
		elevators.put(2, two);
		controller.setElevators(elevators);
		Elevator actualElevator = controller.requestElevator(5);
		assertEquals("Second elevator is closer", 2, actualElevator.getId());
	}
	
	private ElevatorImpl createElevator(int id, int floor, boolean busy, Direction dir) {
		ElevatorImpl elev = new ElevatorImpl(id);
		elev.setAddressedFloor(floor);
		elev.setBusy(busy);
		elev.setDirection(dir);
		return elev;
	}
	
	private void verifyElevatorStatus(List<Elevator> expected, List<Elevator> actual) {
		assertEquals("List size should be the same", expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			System.out.println("Exp: "+expected.get(i));
			System.out.println("Act: "+expected.get(i));
			Assert.assertEquals("Elevators " + i + " should be equal", expected.get(i), actual.get(i));
		}
	}
}
