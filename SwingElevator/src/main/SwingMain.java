package main;

import building.Building;
import building.BuildingControllerInterface;
import building.BuildingInterface;
import building.SwingBuildingController;
import building.SwingBuildingView;
import building.SwingBuildingViewInterface;

/**
 * A class that functions as an entry point for a GUI version of building elevator system.
**/
public class SwingMain {
  /**
   * Program entry point for a GUI version of a building.
   *
   * @param args command line arguments
   * */
  public static void main(String[] args) {
    SwingBuildingViewInterface view = new SwingBuildingView(13, 4);
    BuildingInterface model = new Building(13, 4, 5);
    BuildingControllerInterface controller = new SwingBuildingController(view, model);
    controller.start();
  }
}
