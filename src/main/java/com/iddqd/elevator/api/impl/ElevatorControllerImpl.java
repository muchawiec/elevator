package com.iddqd.elevator.api.impl;

import static java.text.MessageFormat.format;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.iddqd.elevator.api.Elevator;
import com.iddqd.elevator.api.ElevatorController;
import com.iddqd.elevator.api.exceptions.NoElevatorAvailableException;
import com.iddqd.elevator.api.exceptions.WrongElevatorException;
import com.iddqd.elevator.api.exceptions.WrongFloorException;

@Service
public class ElevatorControllerImpl implements ElevatorController {

	private static final Logger logger = LoggerFactory.getLogger(ElevatorControllerImpl.class);
	
	@Autowired
	private ElevatorStarter starter;
	
	@Value("${com.iddqd.elevator.numberofelevators}")
    private int numberOfElevators;
	
	@Value("${com.iddqd.elevator.numberoffloors}")
    private int numberOfFloors;
	
	private Map<Integer, ElevatorImpl> elevators;
		
	@PostConstruct
	void createElevators() {
		elevators = IntStream.rangeClosed(1, numberOfElevators).mapToObj(i -> new ElevatorImpl(i))
				.collect(Collectors.toMap(ElevatorImpl::getId, Function.identity()));
		logger.debug("{} elevators created", numberOfElevators);
	}
	
	@Override
	public Elevator requestElevator(int toFloor) {
		if (toFloor > numberOfFloors || toFloor < 0) {
			logger.debug("Wrong floor selected");
			throw new WrongFloorException(format("Floor {0} is outside allowed bounds: 0 to {1}", toFloor, numberOfFloors));
		}
		ElevatorImpl elev = selectElevatorForRequest(toFloor);
		starter.runElevator(elev, toFloor);
		logger.debug("Elevator request to floor {} received, it will be serviced by elevator {}", toFloor, elev.getId());
		return elev;
	}
	
	@Override
	public void selectFloorInElevator(int id, int toFloor) {
		logger.debug("Elevator {}: Selecting floor {}", id, toFloor);
		if (toFloor > numberOfFloors || toFloor < 0) {
			logger.debug("Wrong floor selected");
			throw new WrongFloorException(format("Floor {0} is outside allowed bounds: 0 to {1}", toFloor, numberOfFloors));
		}
		if (!elevators.keySet().contains(id)) {
			logger.debug("Wrong elevator selected");
			throw new WrongElevatorException(format("Elevator {0} is not managed by controller, available elevators: {1}", id, elevators.keySet()));
		}
		ElevatorImpl elev = elevators.get(id);
		starter.runElevator(elev, toFloor);
	}
	
	@Override
	public List<Elevator> getElevators() {
		return elevators.values().stream().collect(Collectors.toList());
	}

	@Override
	public void releaseElevator(Elevator elevator) {
		// no-op
		// This method is not fitting into my concept. If it is allowed to remove original methods of
		// interface Elevator, then I would do that here.
	}

	@Override
	public void triggerFireAlarm() {
		logger.debug("Fire alarm triggered!");
		elevators.values().stream().forEach(e -> {e.disable(); starter.runElevator(e, 0);});
	}
	
	@Override
	public void recallFireAlarm() {
		logger.debug("Fire alarm recalled!");
		elevators.values().stream().forEach(e -> e.enable());
	}
	
	private ElevatorImpl selectElevatorForRequest(int floor) {
		return elevators.values().stream().filter(e -> !e.isOutOfOrder())
				.min(new DistanceComparator(floor, numberOfFloors)).orElseThrow(NoElevatorAvailableException::new);
	}
	
	// only for setting up unit tests
	void setParams(int elevators, int floors) {
		numberOfElevators = elevators;
		numberOfFloors = floors;
	}
	
	// only for preparing data for unit tests
	void setElevators(Map<Integer, ElevatorImpl> elevators) {
		this.elevators = elevators;
	}
}
