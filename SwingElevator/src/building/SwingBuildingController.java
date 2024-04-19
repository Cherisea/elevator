package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import scanerzus.Request;

/**
 * Controller for a building elevator system designed to work with a view built with Swing.
**/
public class SwingBuildingController implements BuildingControllerInterface {
  private final SwingBuildingViewInterface view;
  private final BuildingInterface model;
  private final JButton[][] requestBtn;
  private final JButton step;
  private final JButton restart;
  private final JButton stop;
  private final int numFloors;

  /**
   * Initializes a controller for a building elevator system.
   *
   * @param view a view component of the building elevator system
   *             that is built with Swing
   * @param model a model component of the building elevator system
  **/
  public SwingBuildingController(SwingBuildingViewInterface view, BuildingInterface model) {
    this.view = view;
    this.model = model;
    this.requestBtn = view.getRequests();
    this.step = view.getStep();
    this.restart = view.getRestart();
    this.stop = view.getStop();
    this.numFloors = model.getNumberOfFloors();
  }

  /**
   * Start the whole elevator system in the building.
  **/
  public void start() {
    model.startElevatorSystem();
    RequestListener requestListener = new RequestListener();
    StatusListener statusListener = new StatusListener();

    step.addActionListener(statusListener);
    restart.addActionListener(statusListener);
    stop.addActionListener(statusListener);

    for (int i = 0; i < numFloors; i++) {
      for (int j = 0; j < 2; j++) {
        requestBtn[i][j].addActionListener(requestListener);
      }
    }
  }

  /**
   * An inner request handler class that processes user's elevator request.
   **/
  private class RequestListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent ae) {
      for (int i = 0; i < numFloors; i++) {
        for (int j = 0; j < 2; j++) {
          if (ae.getSource() == requestBtn[i][j]
              && model.getStatus() == ElevatorSystemStatus.running) {
            int dest = view.createPopUp(requestBtn[i][j]);
            model.addRequest(new Request(i, dest));
            view.displayRequest(model.getUpRequest(), model.getDownRequest());
          }
        }
      }
    }
  }

  /**
   * An inner class for handling user input of building control.
  **/
  private class StatusListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == step) {
        model.stepElevatorSystem();
        update();
        view.updateView();
      } else if (e.getSource() == restart) {
        int[] newArgs = view.createRestartWin();
        restartControl(newArgs[0], newArgs[1], newArgs[2]);
      } else if (e.getSource() == stop) {
        model.stopElevatorSystem();
        view.clearView();
      }
    }
  }


  /**
   * Update the view of the elevator system.
  **/
  private void update() {
    JButton[] elevatorListView = view.getElevators();
    List<Elevator> elevatorList = model.getElevators();

    for (int i = 0; i < elevatorList.size(); i++) {
      int currentFloor = elevatorList.get(i).getCurrentFloor();
      elevatorListView[i].setText("" + currentFloor);
    }
  }

  /**
   * Restart the elevator system with new building parameters.
   *
   * @param newFloor number of floors in the new building
   * @param newElevator number of elevators in the new building
   * @param newCapacity capacity of the elevators in the new building
   * */
  private void restartControl(int newFloor, int newElevator, int newCapacity) {
    BuildingInterface newModel = new Building(newFloor, newElevator, newCapacity);
    SwingBuildingViewInterface newView = new SwingBuildingView(newFloor, newElevator);
    BuildingControllerInterface newController = new SwingBuildingController(newView, newModel);
    newController.start();
  }

}
