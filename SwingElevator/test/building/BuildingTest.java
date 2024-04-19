package building;

import static org.junit.Assert.assertEquals;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import scanerzus.Request;

/**
 * A Junit test class for the Building class.
 */

public class BuildingTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private Building building1;

  @Before
  public void setUp() {
    building1 = new Building(20, 7, 5);
  }

  /**
   * Test constructor throws exceptions when the number of floors is invalid.
   */
  @Test
  public void testThrowsExceptionFloor() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Number of floors must be between 3 and 30.");
    new Building(2, 7, 5);
  }

  /**
   * Test constructor throws exceptions when the number of elevators is invalid.
   */
  @Test
  public void testThrowsExceptionElevator() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Number of elevators must be between 1 and 10.");
    new Building(20, 0, 5);
  }

  /**
   * Test constructor throws exceptions when the elevator capacity is invalid.
   */
  @Test
  public void testThrowsExceptionCapacity() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Capacity of elevators must be between 3 and 20.");
    new Building(20, 7, 2);
  }

  /**
   * Test building is correctly initialized with the correct number of floors,
   * elevators, and elevator capacity.
   */
  @Test
  public void testInit() {
    assertEquals(20, building1.getNumberOfFloors());
    assertEquals(7, building1.getNumberOfElevators());
    assertEquals(5, building1.getElevatorCapacity());
    assertEquals(ElevatorSystemStatus.outOfService, building1.getStatus());
  }

  /**
   * Test start throws an exception when the building is stopping.
   */
  @Test
  public void testStartThrowsException() {
    exception.expect(IllegalStateException.class);
    exception.expectMessage("Can't start elevator system while it is stopping.");
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    building1.startElevatorSystem();
  }

  /**
   * Test start set status to running and returns true when it's not running or stopping.
   */
  @Test
  public void testStart() {
    assertEquals(true, building1.startElevatorSystem());
    assertEquals(ElevatorSystemStatus.running, building1.getStatus());
  }

  /**
   * Test stop sets status to stopping and takes all elevators out of service.
   */
  @Test
  public void testStop() {
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping, building1.getStatus());
    for (Elevator elevator : building1.getElevators()) {
      assertEquals(false, elevator.isTakingRequests());
    }
    assertEquals(0, building1.getUpRequest().size());
    assertEquals(0, building1.getDownRequest().size());
  }


  /**
   * Test that get elevator reports returns the correct number of elevator reports.
   */
  @Test
  public void testGetElevatorReports() {
    assertEquals(20, building1.getElevatorSystemStatus().getNumFloors());
    assertEquals(7, building1.getElevatorSystemStatus().getNumElevators());
    assertEquals(5, building1.getElevatorSystemStatus().getElevatorCapacity());
    assertEquals(7, building1.getElevatorSystemStatus().getElevatorReports().length);
    assertEquals(0, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, building1.getElevatorSystemStatus().getDownRequests().size());
    assertEquals(ElevatorSystemStatus.outOfService,
        building1.getElevatorSystemStatus().getSystemStatus());
  }

  /**
   * Test that add up request throws IllegalStateException when it's out of service.
   */
  @Test
  public void testAddUpRequest() {
    exception.expect(IllegalStateException.class);
    exception.expectMessage("Elevator system can't accept requests right now.");
    building1.addRequest(new Request(1, 2));
  }

  /**
   * Test that add down request throws IllegalStateException when it's stopping.
   */
  @Test
  public void testAddDownRequest() {
    exception.expect(IllegalStateException.class);
    exception.expectMessage("Elevator system can't accept requests right now.");
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    building1.addRequest(new Request(5, 2));
  }

  /**
   * Test addRequest throws IllegalArgumentException when the request is null.
   */
  @Test
  public void testAddNullRequest() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Request can't be null.");
    building1.startElevatorSystem();
    building1.addRequest(null);
  }

  /**
   * Test addRequest throws IllegalArgumentException when start floor is invalid.
  **/
  @Test
  public void testAddRequestInvalidStartFloor() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Start floor must be between 0 and 19");
    building1.startElevatorSystem();
    building1.addRequest(new Request(-1, 2));
  }

  /**
   * Test addRequest throws IllegalArgumentException when end floor is invalid.
   **/
  @Test
  public void testAddRequestInvalidEndFloor() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("End floor must be between 0 and 19");
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, 20));
  }

  /**
   * Test addRequest throws IllegalArgumentException when start and end floor are the same.
   **/
  @Test
  public void testAddRequestSameStartEndFloor() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Start and end floor can't be the same");
    building1.startElevatorSystem();
    building1.addRequest(new Request(2, 2));
  }

  /**
   * Test addRequest adds the request to the upRequest list when start floor is less than end floor.
   **/
  @Test
  public void testAddRequestUp() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(2, 5));
    assertEquals(1, building1.getUpRequest().size());
  }

  /**
   * Test addRequest adds the request to the downRequest list when start floor is
   * greater than end floor.
   **/
  @Test
  public void testAddRequestDown() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(10, 2));
    assertEquals(1, building1.getDownRequest().size());
    assertEquals(true, building1.addRequest(new Request(10, 2)));
  }

  /**
   * Test stepElevatorSystem distribute requests when it's not stopping.
   */
  @Test
  public void testStepElevatorSystem() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(2, 5));
    building1.stepElevatorSystem();
    assertEquals(0, building1.getUpRequest().size());
    assertEquals(false, building1.getElevators().get(0).isTakingRequests());
  }

  /**
   * Test stepElevatorSystem sets status to out of service when all elevators
   * are on the first floor.
   */
  @Test
  public void testStepElevatorSystemAllOnGround() {
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    building1.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.outOfService, building1.getStatus());
  }

  /**
   * Test that elevator system is still stopping when not all elevators are on the first floor.
   */
  @Test
  public void testStepElevatorSystemNotAllOnGround() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(2, 5));
    for (int i = 0; i < 6; i++) {
      building1.stepElevatorSystem();
    }
    building1.stopElevatorSystem();
    building1.stepElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping, building1.getStatus());
  }

  /**
   * Test downward request is dispatched to elevators on top floor.
  **/
  @Test
  public void testDistributeRequests() {
    building1.startElevatorSystem();
    for (int i = 0; i < 24; i++) {
      building1.stepElevatorSystem();
    }

    building1.addRequest(new Request(19, 5));
    building1.stepElevatorSystem();
    building1.stepElevatorSystem();
    assertEquals(false, building1.getElevators().get(0).isTakingRequests());
  }

}
