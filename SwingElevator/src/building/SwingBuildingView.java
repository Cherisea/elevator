package building;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import scanerzus.Request;

/**
 * View component of an elevator building system built with Swing library.
**/
public class SwingBuildingView implements SwingBuildingViewInterface {
  private final JFrame frame;
  private final JButton[][] requestArr;
  private final JButton[] elevators;
  private final int numFloor;
  private final int numElevator;
  private int stepCnt = 0;
  private JButton step;
  private JButton stop;
  private JButton restart;
  private JLabel stepCountLabel;
  private JPanel elevatorPanel;

  /**
   * Initializes the main component of an elevator system.
   *
   * @param numFloor number of floors in this building
   * @param numElevator number of elevators in the building
  **/
  public SwingBuildingView(int numFloor, int numElevator) {
    frame = new JFrame("Fantastic Building Ltd.");
    frame.setSize(1200, 900);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.decode("#4A171E"));

    this.numFloor = numFloor;
    this.numElevator = numElevator;
    requestArr = new JButton[numFloor][2];
    elevators = new JButton[numElevator];

    addRequestBtn();
    addControl();
    addElevators();

    frame.setVisible(true);
  }

  /**
    * Add a vertical stack of up&down buttons to simulate request.
   **/
  private void addRequestBtn() {
    JPanel requests = new JPanel();
    requests.setLayout(new GridLayout(numFloor, 2));

    ImageIcon up;
    ImageIcon down;
    if (numFloor < 20) {
      up = new ImageIcon("src/main/resources/up-40x40.png");
      down = new ImageIcon("src/main/resources/down-40x40.png");
    } else {
      up = new ImageIcon("src/main/resources/up-20x20.png");
      down = new ImageIcon("src/main/resources/down-20x20.png");
    }

    for (int i = numFloor - 1; i >= 0; i--) {
      JButton upButton = new JButton(String.valueOf(i), up);
      JButton downButton = new JButton(String.valueOf(i), down);

      upButton.setFocusPainted(false);
      downButton.setFocusPainted(false);
      requestArr[i][0] = upButton;
      requestArr[i][1] = downButton;

      requests.add(upButton);
      requests.add(downButton);
    }

    frame.add(requests, BorderLayout.WEST);
  }

  /**
    * Add elevator representation to the building.
   **/
  private void addElevators() {
    elevatorPanel = new JPanel(new BorderLayout());
    JPanel elevatorWrapper = new JPanel();
    ImageIcon eleIcon;

    if (numFloor < 20) {
      eleIcon = new ImageIcon("src/main/resources/elevator-40x40.png");
    } else {
      eleIcon = new ImageIcon("src/main/resources/elevator-20x20.png");
    }

    for (int i = 0; i < numElevator; i++) {
      elevators[i] = new JButton();
      elevators[i].setIcon(eleIcon);
      elevators[i].setText("0");
      elevatorWrapper.add(elevators[i]);
    }

    elevatorPanel.add(elevatorWrapper, BorderLayout.SOUTH);
    frame.add(elevatorPanel, BorderLayout.CENTER);
  }

  /**
    * Add building control panel consisting of three buttons and two label fields.
   **/
  private void addControl() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
             | IllegalAccessException ex) {
      System.out.println("Look and feel not set.");
    }

    JLabel countTitle = new JLabel("Step Count");
    stepCountLabel = new JLabel(String.valueOf(stepCnt), SwingConstants.CENTER);
    countTitle.setFont(new Font("Serif", Font.BOLD, 18));
    stepCountLabel.setFont(new Font("Serif", Font.BOLD, 50));

    JPanel control = new JPanel(new GridLayout(5, 1));
    control.add(countTitle);
    control.add(stepCountLabel);

    JButton[] buttons = new JButton[3];
    buttons[0] = new JButton("STEP");
    buttons[0].setBackground(Color.blue);
    step = buttons[0];

    buttons[1] = new JButton("RESTART");
    buttons[1].setBackground(Color.ORANGE);
    restart = buttons[1];

    buttons[2] = new JButton("STOP");
    buttons[2].setBackground(Color.RED);
    stop = buttons[2];

    for (JButton btn : buttons) {
      btn.setForeground(Color.white);
      btn.setFocusPainted(false);
      control.add(btn);
    }

    frame.add(control, BorderLayout.EAST);
  }

  /**
    * Reset building step count and elevator step count.
   **/
  public void clearView() {
    stepCnt = 0;
    stepCountLabel.setText(String.valueOf(stepCnt));

    for (int i = 0; i < numElevator; i++) {
      elevators[i].setText(String.valueOf(stepCnt));
      elevators[i].setBackground(Color.RED);
      elevators[i].setOpaque(true);
    }
  }

  /**
   * Create a popup window asking for user input when restart button is clicked.
   *
   * @return an integer array containing number of floors, number of elevators
   *         and capacity for creating a new building
   **/
  public int[] createRestartWin() {
    JPanel body = new JPanel();
    JDialog info = new JDialog(frame, "New Building", true);
    info.setLocationRelativeTo(frame);

    JLabel floorLabel = new JLabel("Number of Floors");
    JTextField newFloor = new JTextField(10);
    newFloor.setToolTipText("Enter an integer between 3 and 30.");
    body.add(floorLabel);
    body.add(newFloor);

    JLabel elevatorLabel = new JLabel("Number of Elevators");
    JTextField newElevator = new JTextField(10);
    newElevator.setToolTipText("Enter an integer between 1 and 10.");
    body.add(elevatorLabel);
    body.add(newElevator);

    JLabel capacityLabel = new JLabel("Elevator Capacity");
    JTextField newCapacity = new JTextField(10);
    newCapacity.setToolTipText("Enter an integer between 3 and 20.");
    body.add(capacityLabel);
    body.add(newCapacity);

    JButton submit = new JButton("Submit");
    body.add(submit);
    info.add(body);

    info.setSize(300, 150);

    int[] args = new int[3];
    submit.addActionListener(e -> {
      try {
        args[0] = Integer.parseInt(newFloor.getText());
        args[1] = Integer.parseInt(newElevator.getText());
        args[2] = Integer.parseInt(newCapacity.getText());
        info.dispose();
      } catch (NumberFormatException ex) {
        // Handle invalid input
        JOptionPane.showMessageDialog(info, "Invalid inputs!");
      }
    });

    info.setVisible(true);
    return args;
  }

  /**
   * Create a popup window to take requested destination floor on button click.
   *
   * @param btn a button that triggers this pop-up
   * @return destination floor supplied by the user
   **/
  public int createPopUp(JButton btn) {
    JDialog info = new JDialog(frame, "Request Elevator", true);
    info.setLocationRelativeTo(btn);
    JPanel body = new JPanel();

    JLabel prompt = new JLabel("Enter Destination Floor: ");
    JTextField destFloor = new JTextField(15);
    destFloor.requestFocusInWindow();

    body.add(prompt);
    body.add(destFloor);
    info.add(body);

    info.setSize(300, 100);

    AtomicInteger dest = new AtomicInteger();
    destFloor.addActionListener(e -> {
      try {
        // Close the dialog after successfully parsing the integer
        dest.set(Integer.parseInt(destFloor.getText()));
        info.dispose();
      } catch (NumberFormatException ex) {
        // Handle invalid input
        JOptionPane.showMessageDialog(info, "Please enter a valid integer "
            + "for the destination floor.");
      }
    });

    info.setVisible(true);
    return dest.get();
  }

  @Override
  public void updateView() {
    stepCnt++;
    stepCountLabel.setText(String.valueOf(stepCnt));
  }

  @Override
  public void displayRequest(List<Request> up, List<Request> down) {
    StringBuilder upText = new StringBuilder();
    StringBuilder downText = new StringBuilder();

    if (!down.isEmpty()) {
      downText.append("Down: ");
      for (Request request : down) {
        downText.append(request.getStartFloor()).append(" -> ")
            .append(request.getEndFloor()).append("; ");
      }
    }

    if (!up.isEmpty()) {
      upText.append("Up: ");
      for (Request request : up) {
        upText.append(request.getStartFloor()).append(" -> ")
            .append(request.getEndFloor()).append("; ");
      }
    }

    JPanel displayRequest = new JPanel(new GridLayout(2, 1));
    displayRequest.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 10));
    displayRequest.setBackground(Color.cyan);
    JLabel upLabel = new JLabel();
    JLabel downLabel = new JLabel();

    downLabel.setText(downText.toString());
    upLabel.setText(upText.toString());
    displayRequest.add(downLabel);
    displayRequest.add(upLabel);

    elevatorPanel.add(displayRequest, BorderLayout.NORTH);
    frame.add(elevatorPanel, BorderLayout.CENTER);
  }

  public JButton[][] getRequests() {
    return requestArr;
  }

  public JButton getStep() {
    return step;
  }

  public JButton getStop() {
    return stop;
  }

  public JButton getRestart() {
    return restart;
  }

  public JButton[] getElevators() {
    return elevators;
  }
}
