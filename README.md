# Elevator System

This is implementation of elevator system, which is result of provided code challenge. 

It allows to call elevator for specific floor and also allows to select floor from elevator panel. 

If we want to sell our elevator system to US and other countries where regulations require [EFS](https://elevation.fandom.com/wiki/Fire_service_mode_(EFS) we have to also implement fire security operations.

## Assumptions

There are few assumptions that I've made:

* Floors are zero-indexed. Ground floor is floor zero.

* There is only one button for calling elevator to the floor. Elevator with desired direction buttons would be implemented later.

* As there is no specified context and no information where this elevator is operating, I haven't included any smart policies to improve passenger flow. 

## Implementation

Based on provided skeleton and two interfaces:

 * `Elevator` - this is the elevator itself and has a few public methods to take care of. There can be n number of elevators at the same time

 * `ElevatorController` - this is the elevator manager that keeps track of all the elevators running in the elevator shaft. There should be only one ElevatorController
 
Their implementations have been created in `com.iddqd.elevator.api.impl` package.

As I wasn't sure if provided interfaces can be changed, I left all original methods even if I haven't used them. I've extended interface with few useful methods:

* `selectFloorInElevator` - which reflects situation when button is pressed inside elevator and this floor request has to be serviced by this particular elevator.

* `triggerFireAlarm` - initiates phase one of EFS, which means that all elevators are being sent to ground floor and disabled from use.

* `recallFireAlarm` - gets situation back to normal after fire alarm. It is useful for continuing simulation after testing triggering fire alarm without restarting application.

My implementation results with following most important files:

* `DistanceComparator` is a part where calculation which elevator is closest to requested floor.

* `ElevatorControllerImpl` is an implementation of `ElevatorController` interface, responsible for assigning requests to elevators.

* `ElevatorImpl` is an implementation of `Elevator` interface, that holds all model data and elevator status.

* `ElevatorStarter` starts new threads when elevator received new request.

* `RunningElevator` contains elevator in movement behavior.

## Testing

All relevant parts of code has been covered with unit tests.

## How to launch

This is Spring Boot Application, so it can be launched using Maven or Java:

    mvn package
    mvn spring-boot:run

or

    mvn package
    java -jar target/elevator-1.0-SNAPSHOT.jar


## How to play

Number of floors and elevators are defined in application configuration file. There is also parameter called `com.iddqd.elevator.time` which allows to manage speed of simulation. By default it is set to 1000 which reflects one second in the system. I assumed that opening, closing elevator door and moving one floor takes 2 units of time (with default settings it is equal to 2 seconds). 

There are five available endpoints for manipulating Elevator System:

* `GET /rest/v1/elevators` - plain list of all elevators status.

* `GET /rest/v1/elevators/call/{floor}` - invokes one of the elevators to specified floor.

* `GET /rest/v1/elevators/select/{id}/{floor}` - triggers selection of specified floor inside elevator with given id.

* `GET /rest/v1/firealarm/trigger` - triggers fire alarm.

* `GET /rest/v1/firealarm/recall` - cancels fire alarm.

(I'm aware that idempotency of actions above is questionable, but this set up is convenient for testing.)

Actions performed by system are written to logs, that are displayed on stdout by default. Following events are logged:

* Received requests to floor - `Elevator request to floor N received, it will be serviced by elevator X`

* Floors selected in elevator - `Elevator X: Selecting floor N`

* Elevator moving between floors up - `Elevator X: Moving up to N floor`

* Elevator moving between floors down - `Elevator X: Moving down to N floor`

* Elevator opening door when stopping - `Elevator X: Arrived to floor N. Opening door`

* Elevator closing door when leaving floor - `Elevator X: Closing door`

Below is example output of scenario when buttons in two elevators were pressed to floor 10 and 6, and requests to floor 7 and 3 arrived later.
After reaching those floors buttons to ground floor have been pressed.


    Elevator 1: Selecting floor 10
    Elevator 1: Closing door.
    Elevator request to floor 7 received, it will be serviced by elevator 1
    Elevator 1: Moving up to 1 floor
    Elevator 1: Moving up to 2 floor
    Elevator 1: Moving up to 3 floor
    Elevator 2: Selecting floor 6
    Elevator 2: Closing door.
    Elevator 1: Moving up to 4 floor
    Elevator 1: Moving up to 5 floor
    Elevator 2: Moving up to 1 floor
    Elevator request to floor 3 received, it will be serviced by elevator 2
    Elevator 1: Moving up to 6 floor
    Elevator 2: Moving up to 2 floor
    Elevator 1: Moving up to 7 floor
    Elevator 1: Arrived to floor 7. Opening door
    Elevator 2: Moving up to 3 floor
    Elevator 2: Arrived to floor 3. Opening door
    Elevator 1: Closing door.
    Elevator 2: Closing door.
    Elevator 1: Moving up to 8 floor
    Elevator 2: Moving up to 4 floor
    Elevator 1: Moving up to 9 floor
    Elevator 2: Moving up to 5 floor
    Elevator 1: Moving up to 10 floor
    Elevator 1: Arrived to floor 10. Opening door
    Elevator 2: Moving up to 6 floor
    Elevator 2: Arrived to floor 6. Opening door
    Elevator 2: Selecting floor 0
    Elevator 2: Closing door.
    Elevator 1: Selecting floor 0
    Elevator 1: Closing door.
    Elevator 2: Moving down to 5 floor
    Elevator 2: Moving down to 4 floor
    Elevator 1: Moving down to 9 floor
    Elevator 2: Moving down to 3 floor
    Elevator 1: Moving down to 8 floor
    Elevator 2: Moving down to 2 floor
    Elevator 1: Moving down to 7 floor
    Elevator 2: Moving down to 1 floor
    Elevator 1: Moving down to 6 floor
    Elevator 2: Moving down to 0 floor
    Elevator 2: Arrived to floor 0. Opening door
    Elevator 1: Moving down to 5 floor
    Elevator 1: Moving down to 4 floor
    Elevator 1: Moving down to 3 floor
    Elevator 1: Moving down to 2 floor
    Elevator 1: Moving down to 1 floor
    Elevator 1: Moving down to 0 floor
    Elevator 1: Arrived to floor 0. Opening door


  