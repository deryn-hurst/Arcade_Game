import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    // declare and initialize fields
    // set screen width and height to maximum to fill screen
    // TO DO: adjust to account for rotation on screen
    static final int SCREEN_WIDTH = 1540;
    static final int SCREEN_HEIGHT = 940;

    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;

    // create arrays to hold coordinates for all body parts of snake
    final int [] x = new int[GAME_UNITS];
    final int [] y = new int[GAME_UNITS];

    // set starting values for snake and apple
    int bodyParts = 3;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';

    // set up conditions to run game
    boolean running = false;
    Timer timer;
    Random random;

    // set up GamePanel constructor
    GamePanel () {
        startGame();
    }

    // declare methods
    public void startGame(){
        // use aggregation to set initialized fields in constructor
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        // when game starts, add new apple to the screen
        newApple();
        // set running to true to indicate game is active
        running = true;
        // start the timer with the previously set delay with Game Panel as listener
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running){
            /*
            THIS CODE ADDS GRID LINES
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }
            for (int j = 0; j < SCREEN_HEIGHT / UNIT_SIZE; j++) {
                g.drawLine(0, j * UNIT_SIZE, SCREEN_WIDTH, j * UNIT_SIZE);
            }
             */

            // set apple to appear
            g.setColor(Color.white);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draw snake
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(Color.white);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // draw current score
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("" + applesEaten))/2, g.getFont().getSize() + UNIT_SIZE);
        }
        // once the game is not running, call the game over method
        else {
            gameOver(g);
        }
    }

    // this method will set location of new apple to a random place on the screen
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    // this method will move the snake
    public void move(){
        // this shifts all coordinates in the array
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // dictate direction snake is moving based on key pressed
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApples(){
        //increment points and snake length and set a new apple if apple is encountered
        if((x[0] == appleX) && (y[0] == appleY)){
            applesEaten++;
            bodyParts++;
            newApple();
        }
    }

    // this method will determine if the snake collides with anything
    public void checkCollisions(){
        // end game if head touches body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // end game if head touches left border
        if(x[0] < 0){
            running = false;
        }

        // end game if head touches right border
        if(x[0] > (SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE){
            running = false;
        }

        //end game if head touches top border
        if(y[0] < 0){
            running = false;
        }

        // end game if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        // stop timer if game is not running
        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        // set up Game Over Text
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

        // display score when game ends
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Your Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Your Score: " + applesEaten))/2, (SCREEN_HEIGHT/2) + (g.getFont().getSize() + UNIT_SIZE));

        }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running) {
            move();
            checkApples();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        // this method will allow user to move snake based on key presses
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
