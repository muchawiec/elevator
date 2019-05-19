package com.iddqd.elevator.api.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.iddqd.elevator.api.Elevator;

/**
 * This is launcher for threads representing elevators in work.
 * 
 * @author lmucha
 *
 */
@Service
public class ElevatorStarter {

	@Value("${com.iddqd.elevator.time}")
    private int time;
	
	/**
	 * This method launches thread representing moving elevator.
	 * 
	 * @param elev - Model of {@link Elevator} that is working.
	 * @param toFloor - number of floor where elevator is being sent.
	 */
	@Async("asyncExecutor")
	public void runElevator(ElevatorImpl elev, int toFloor) {
		elev.addFloorToVisit(toFloor);
		if (!elev.isBusy()) {
			elev.moveElevator(toFloor);
			RunningElevator running = new RunningElevator(elev);
			running.setTime(time);
			running.run();
		}	
	}
}
