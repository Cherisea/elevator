# About
Create a building elevator program that simulates the operation of a real system using a classic MVC design pattern. This project focuses on the view part as other components are either provided or already implemented in previous tasks. 

# Features
- Step: step the system and update the internal state of each elevator.
- Step Count: total number of steps is recorded in the top right corner.
- Restart: build a new simulation by supplying arguments needed to create a new model and view in the subsequent popup window;
- Stop: shut down the whole system. No requests can be issued after stop button is pressed.
- Request an Elevator: press one of the up/down buttons on the right side and enter destination floor in the following popup window. No popups will be generated if the system is stopping or out of service.
- Current Floor of Elevators: current floor of each elevator is displayed beside each elevator icon and updated based on their internal state.
- Requests array: all active up and down requests are displayed in the banner located at the top of frame. 

# How to Run
Double click on the jar file to run the program.

# How to Use the Program
- Press step to run the building system
- Press restart to start a new instance
- Press stop to terminate the system
- Press any up/down button to request an elevator

# Design/Modal Changes
This is the first version of this project.

# Assumptions
- Elevator icons and up/dwon icons are correctly rendered in the final jar file;
- State of elevators are updated in time;
- Initial frame size should not be larger than what the device this program runs on could provide; 

# Limitations
- Location of elevators are not updated in the frame as their current floor updates;
- No feedback is provided after a button is clicked; 

# Citations
- A Visual Guide to Layout Managers (The JavaTM Tutorials > Creating a GUI With JFC/Swing > Laying Out Components Within a Container). (n.d.). Docs.oracle.com. Retrieved April 13, 2024, from https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
- Displaying an image using ImageIcon [Solved] (Swing / AWT / SWT forum at Coderanch). (n.d.). Coderanch.com. Retrieved April 14, 2024, from https://coderanch.com/t/636121/java/Displaying-image-ImageIcon
- getCanonicalPath returns a different path between different platforms. (n.d.). Stack Overflow. Retrieved April 14, 2024, from https://stackoverflow.com/questions/50580303/getcanonicalpath-returns-a-different-path-between-different-platforms
- How to add Icon to JButton in Java? (n.d.). Www.tutorialspoint.com. Retrieved April 13, 2024, from https://www.tutorialspoint.com/how-to-add-icon-to-jbutton-in-java#:~:text=To%20add%20icon%20to%20a
- Java Vertical Layout? (n.d.). Stack Overflow. Retrieved April 13, 2024, from https://stackoverflow.com/questions/8950885/java-vertical-layout
