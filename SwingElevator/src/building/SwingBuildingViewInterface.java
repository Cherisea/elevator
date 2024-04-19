package building;

import java.util.List;
import javax.swing.JButton;
import scanerzus.Request;

/**
 * An interface that represents GUI version of building view.
**/
public interface SwingBuildingViewInterface {
  /**
   * Update elevator status and step count on step command.
   *
   **/
  void updateView();

  /**
   * Create a popup window asking for user input when restart button is clicked.
   *
   * @return an integer array containing number of floors, number of elevators
   *         and capacity for creating a new building
   **/
  int[] createRestartWin();

  /**
   * Create a popup window to take requested destination floor on button click.
   *
   * @param btn a button that triggers this pop-up
   * @return destination floor user entered
   **/
  int createPopUp(JButton btn);

  /**
   * Reset building step count and elevator step count.
   **/
  void clearView();

  /**
   * Display the request on the view.
   *
   * @param up a list of up requests
   * @param down a list of down requests
   **/
  void displayRequest(List<Request> up, List<Request> down);

  /**
   * Get all buttons used to take user requests.
   *
   * @return all the request buttons in the form of an array
   **/
  JButton[][] getRequests();

  /**
   * Get step button.
   *
   * @return step button
   **/
  JButton getStep();

  /**
   * Get stop button.
   *
   * @return stop button
   **/
  JButton getStop();

  /**
   * Get restart button.
   *
   * @return restart button
   **/
  JButton getRestart();

  /**
   * Get representation of all elevators.
   *
   * @return all elevators in the form of an array
  **/
  JButton[] getElevators();
}
