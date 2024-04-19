package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import java.util.List;
import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {
  /**
   * This method is used to stop the elevator system.
   */
  void stopElevatorSystem();

  /**
   * This method is used to start the elevator system.
   *
   * @return true if the elevator system is started, false otherwise.
   *
   * @throws IllegalStateException if the elevator system is in the process of stopping.
   */
  boolean startElevatorSystem() throws IllegalStateException;

  /**
   * This method is used to get the status of the elevator system.
   *
   * @return the status of the elevator system.
   */
  BuildingReport getElevatorSystemStatus();

  /**
   * This method is used to add a request to the building.
   *
   * @param request the request to be added.
   *
   * @return true if the request is added, false otherwise.
   * @throws IllegalStateException if the elevator system is out of service or stopping.
   * @throws IllegalArgumentException if either arguments is invalid
   *
  **/
  boolean addRequest(Request request) throws IllegalArgumentException, IllegalStateException;

  /**
   * This method is used to step the elevator system.
   */
  void stepElevatorSystem();

  /**
   * This method is used to get the number of elevators in the building.
   *
   * @return the number of elevators in the building.
   */
  int getNumberOfElevators();

  /**
   * This method is used to get the number of floors in the building.
   *
   * @return the number of floors in the building.
   */
  int getNumberOfFloors();

  /**
   * This method is used to get the capacity of the elevators in the building.
   *
   * @return the capacity of the elevators in the building.
   */
  int getElevatorCapacity();

  /**
   * This method is used to get elevator system status.
   *
   * @return the status of the elevator system.
  **/
  ElevatorSystemStatus getStatus();

  /**
   * This method is used to get the elevators in the building.
   *
   * @return the elevators in the building.
   */
  List<Elevator> getElevators();

  /**
   * This method is used to get the up requests in the building.
   *
   * @return the up requests in the building.
   */
  List<Request> getUpRequest();

  /**
   * This method is used to get the down requests in the building.
   *
   * @return the down requests in the building.
   */
  List<Request> getDownRequest();

}
