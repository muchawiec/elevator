package com.iddqd.elevator.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iddqd.elevator.api.Elevator;
import com.iddqd.elevator.api.ElevatorController;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {

	@Autowired
	ElevatorController elevatorController;
	
    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }
    
    @RequestMapping(value = "/elevators", method = RequestMethod.GET) 
    public List<Elevator> listElevators() {
    	return elevatorController.getElevators();
    }
    
    @RequestMapping(value = "/elevators/call/{floor}", method = RequestMethod.GET) 
    public void callElevator(@PathVariable(name="floor") int floor) {
    	elevatorController.requestElevator(floor);
    }
    
    @RequestMapping(value = "/elevators/select/{id}/{floor}", method = RequestMethod.GET) 
    public void selectFloorInElevator(@PathVariable(name="id") int id, @PathVariable(name="floor") int floor) {
    	elevatorController.selectFloorInElevator(id, floor);
    }
    
    @RequestMapping(value = "/firealarm/trigger", method = RequestMethod.GET) 
    public void triggerFireAlarm() {
    	elevatorController.triggerFireAlarm();
    }
    
    @RequestMapping(value = "/firealarm/recall", method = RequestMethod.GET) 
    public void recallFireAlarm() {
    	elevatorController.recallFireAlarm();
    }
}
