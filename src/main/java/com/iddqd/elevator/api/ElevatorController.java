package com.iddqd.elevator.api;

import java.util.List;


/**
 * Interface for the Elevator Controller.
 *
 * @author Sven Wesley
 *
 */
public interface ElevatorController {

    /**
     * Request an elevator to the specified floor.
     *
     * @param toFloor
     *            addressed floor as integer.
     * @return The Elevator that is going to the floor, if there is one to move.
     */
    Elevator requestElevator(int toFloor);

    /**
     * Select floor on elevator panel.
     *
     * @param id identifier of elevator.
     * @param toFloor
     *            addressed floor as integer.
     */
    void selectFloorInElevator(int id, int toFloor);
    
    /**
     * A snapshot list of all elevators in the system.
     *
     * @return A List with all {@link Elevator} objects.
     */
    List<Elevator> getElevators();

    /**
     * Telling the controller that the given elevator is free for new
     * operations.
     *
     * @param elevator
     *            the elevator that shall be released.
     */
    void releaseElevator(Elevator elevator);
    
    /**
     * Puts elevator system into fire alarm mode.
     */
    void triggerFireAlarm();
    
    /**
     * Recalls fire alarm, allows elevators to be used again.
     */
    void recallFireAlarm();
    
}
