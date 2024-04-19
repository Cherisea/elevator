package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.List;
import scanerzus.Request;


/**
 * This class represents a building that initializes the elevators and handles the requests.
 */
public class Building implements BuildingInterface {
  private final int numberOfElevators;
  private final int numberOfFloors;
  private final int elevatorCapacity;
  private ElevatorSystemStatus status;
  private final List<Request> upRequest = new ArrayList<>();
  private final List<Request> downRequest = new ArrayList<>();
  private final List<Elevator> elevators = new ArrayList<>();

  /**
   * The constructor for the building.
   *
   * @param numberOfFloors the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity the capacity of the elevators in the building.
   *
   * @throws IllegalArgumentException if the number of floors is less than 3 or greater than 30,
   *         if number of elevators is negative or greater than 10 and if elevator capacity is
   *         less or equal to 3 or greater than 20
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity)
      throws IllegalArgumentException {
    if (numberOfFloors < 3 || numberOfFloors > 30) {
      throw new IllegalArgumentException("Number of floors must be between 3 and 30.");
    }

    if (numberOfElevators <= 0 || numberOfElevators > 10) {
      throw new IllegalArgumentException("Number of elevators must be between 1 and 10.");
    }

    if (elevatorCapacity < 3 || elevatorCapacity > 20) {
      throw new IllegalArgumentException("Capacity of elevators must be between 3 and 20.");
    }

    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    status = ElevatorSystemStatus.outOfService;

    for (int i = 0; i < this.numberOfElevators; i++) {
      elevators.add(new Elevator(this.numberOfFloors, this.elevatorCapacity));
    }
  }


  @Override
  public void stopElevatorSystem() {
    if (status != ElevatorSystemStatus.outOfService && status != ElevatorSystemStatus.stopping) {
      elevators.forEach(Elevator::takeOutOfService);
      status = ElevatorSystemStatus.stopping;
      this.upRequest.clear();
      this.downRequest.clear();
    }
  }

  @Override
  public boolean startElevatorSystem() {
    if (status == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Can't start elevator system while it is stopping.");
    }

    if (status != ElevatorSystemStatus.running) {
      elevators.forEach(Elevator::start);
      status = ElevatorSystemStatus.running;
    }

    return true;
  }

  @Override
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[numberOfElevators];

    for (int i = 0; i < numberOfElevators; i++) {
      elevatorReports[i] = elevators.get(i).getElevatorStatus();
    }

    return new BuildingReport(numberOfFloors, numberOfElevators,
                              elevatorCapacity, elevatorReports, upRequest, downRequest, status);
  }

  @Override
  public boolean addRequest(Request request) throws
      IllegalStateException, IllegalArgumentException {
    // Check if elevator system can accept requests
    if (status == ElevatorSystemStatus.outOfService || status == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator system can't accept requests right now.");
    } else {
      // Check if request is null
      if (request != null) {
        // Check if startFloor is valid
        if (request.getStartFloor() >= 0 && request.getStartFloor() < this.numberOfFloors) {
          // Check if endFloor is valid
          if (request.getEndFloor() >= 0 && request.getEndFloor() < this.numberOfFloors) {
            // Check if start and end floor are the same
            if (request.getStartFloor() != request.getEndFloor()) {
              // Check which one is greater
              if (request.getStartFloor() > request.getEndFloor()) {
                downRequest.add(request);
              } else {
                upRequest.add(request);
              }
            } else {
              throw new IllegalArgumentException("Start and end floor can't be the same");
            }
          } else {
            throw new IllegalArgumentException("End floor must be between 0 and "
              + (this.numberOfFloors - 1));
          }
        } else {
          throw new IllegalArgumentException("Start floor must be between 0 and "
            + (this.numberOfFloors - 1));
        }
      } else {
        throw new IllegalArgumentException("Request can't be null.");
      }
    }

    return true;
  }

  @Override
  public void stepElevatorSystem() {
    if (status != ElevatorSystemStatus.outOfService) {
      if (status != ElevatorSystemStatus.stopping) {
        distributeRequests();
      }

      for (ElevatorInterface elevator : elevators) {
        elevator.step();
      }

      if (status == ElevatorSystemStatus.stopping) {
        boolean allElevatorsOnGroundFloor = true;
        for (ElevatorInterface elevator : elevators) {
          if (elevator.getCurrentFloor() != 0) {
            allElevatorsOnGroundFloor = false;
            break;
          }
        }

        if (allElevatorsOnGroundFloor) {
          status = ElevatorSystemStatus.outOfService;
        }
      }
    }
  }

  private void distributeRequests() {
    if (!upRequest.isEmpty() || !downRequest.isEmpty()) {
      for (ElevatorInterface elevator : elevators) {
        // Check if elevator takes a request
        if (elevator.isTakingRequests()) {
          // Assign upward requests to elevators on the first floor
          if (elevator.getCurrentFloor() == 0) {
            elevator.processRequests(getRequest(upRequest));
          } else if (elevator.getCurrentFloor() == numberOfFloors - 1) {
            // Assign downward requests to elevators on the top floor
            elevator.processRequests(getRequest(downRequest));
          }
        }
      }
    }
  }

  private List<Request> getRequest(List<Request> requests) {
    List<Request> requestToReturn = new ArrayList<>();

    while (!requests.isEmpty() && (requestToReturn.size() < elevatorCapacity)) {
      requestToReturn.add(requests.remove(0));
    }

    return requestToReturn;
  }

  public int getNumberOfElevators() {
    return numberOfElevators;
  }

  public int getNumberOfFloors() {
    return numberOfFloors;
  }

  public int getElevatorCapacity() {
    return elevatorCapacity;
  }

  public ElevatorSystemStatus getStatus() {
    return status;
  }

  public List<Elevator> getElevators() {
    return elevators;
  }

  public List<Request> getUpRequest() {
    return upRequest;
  }

  public List<Request> getDownRequest() {
    return downRequest;
  }


}


