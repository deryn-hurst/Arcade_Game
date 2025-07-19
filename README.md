# Arcade_Game
3D print files, code, and joystick rigging for creating a full arcade style game and control

<b> GAME FILES </b>

The game files folder contains two types of files:

  <b><i> Three .java files </i></b>
  
    # GameFrame.java
      - sets the frame of the Pong game
      - If you wish to resize, here is where that can be edited.
      By default, the game opens in fullscreen and is not resizable
    # GamePanel.java
      - controls the game appearance and behavior
      - If you wish to change the control keys, speed, or difficulty settings,
      here is where that can be edited. If the control keys are changed in the 
      game code, they must also be changed in the joystick code
    # Pong.java
      - calls GameFrame to start running the game
      
  <b><i> One .txt file </i></b>
    
    Leaderboard.txt 
      - used in GamePanel to hold, update, and retrieve scores
      - to properly display leaderboard, GamePanel code must be updated to the location of
      the Leaderboard.txt files
    

<b> JOYSTICK DESIGN AND MOUNTING FILES </b>

  - These files can be used for setting up the joystick
  - To properly mount electronics, use electronics screws, I sourced mine from an old tablet.
  - The Electronics Mount uses offsets with screw holes that should account for the diameter
    of an electronics screw.
  - The joystick files are provided in whole and by halves.
      * I got best results printing by halves at 40% infill and gluing together
  

<b> JOYSTICK CODE </b>

  - The joystick code directly correlates to the keyboard control associated with the Key Listener in
    GamePanel.java. If anything is changed for key events, the code must also be changed for the Arduino.

<b> SNAKE GAME FILES </b>
  - There are a bonus set of files to run the game Snake. It does not have a leaderboard or 
  restart game capabilities, but they can be updated using the template set in the Pong
  game files.
  - The structure is the same as the Pong files, with GameFrame.java, GamePanel.java, and SnakeGame.java
    files that have the exact same behaviors.
  
