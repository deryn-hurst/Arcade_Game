import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener {
    // declare and initialize fields
    // set unchanging values
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // pull device data to set screen width and height
    static final int SCREEN_WIDTH = (int) screenSize.getWidth();
    static final int SCREEN_HEIGHT = (int) screenSize.getHeight();
    static final int UNIT_SIZE = 20; // unit size will ease control of movement
    static final int PADDLE_SIZE = 8; // paddle will bounce ball

    // set game starting values
    static final int DELAY = 5; // controls speed of game
    int ballX; // x coordinate of ball
    int ballY; // y coordinate of ball
    int xOffset = 0; // will be used to implement game physics
    int yOffset = 1; // will be used to implement game physics
    int location = SCREEN_WIDTH / 2; // will be used to move the paddle
    int direction; // determines direction paddle moves
    int points; // will track points when ball hits paddle
    double difficulty; // will increase the ball speed as player points increase

    // set conditions to run game
    boolean running = false; // will change based on game needs
    Timer timer = new Timer(DELAY, this); // used to set game speed

    // ensures leaderboard is only calculated once
    boolean calcLeader = true;

    // use to show leaderboard after game ends
    boolean showLeader = false;

    // use random number to randomize starting conditions
    Random random = new Random();


    GamePanel(){
        startRound();
    }

    // declare methods
    public void startRound(){
        // set screen size and color
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        // set running to true to indicate round is active
        running = true;


        calcLeader = true;
        showLeader = false;

        // reset difficulty
        difficulty = 0;

        // reset points
        points = 0;

        // set paddle to random direction initially
        direction = random.nextInt(3) - 1;

        // set ball to fall at random angle initially
        xOffset = random.nextInt(3) - 1; // randomizes direction between left and right
        yOffset = 1; // sends ball falling straight downward

        // add the ball to start the game
        addBall();

        // start timer with delay
        timer.setDelay(DELAY);
        timer.start();

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // draws game components
    public void draw(Graphics g){
        if (running) {
            showLeader = false;
            // draw grid lines for ease of development
            // will be removed after
        /*
        for(int i = 0; i <= SCREEN_WIDTH / UNIT_SIZE; i++){
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }

        for(int j = 0; j <= SCREEN_HEIGHT / UNIT_SIZE; j++){
            g.drawLine(0, j * UNIT_SIZE, SCREEN_WIDTH, j * UNIT_SIZE);
        }
         */

            // set ball to appear
            g.setColor(Color.white);
            g.fillOval(ballX, ballY, UNIT_SIZE, UNIT_SIZE);

            // draw paddle
            g.setColor(Color.white);
            g.fillRect(location, SCREEN_HEIGHT - (UNIT_SIZE * 5), PADDLE_SIZE * UNIT_SIZE, UNIT_SIZE);

            // draw score
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("" + points, (SCREEN_WIDTH - metrics.stringWidth("" + points)) / 2, g.getFont().getSize() + UNIT_SIZE);
        }
        else {
            if(showLeader) {
                try {
                    leaderboard(g);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else {
                gameOver(g);
            }
        }
    }

    // adds a ball at the beginning of the game
    public void addBall() {
        ballX = SCREEN_WIDTH / 2;
        ballY = 2 * UNIT_SIZE;
    }

    public void move(){
        // ball moves based off of directional offSets
        ballY += yOffset + (int) (yOffset * difficulty); // if y = 1, ball moves down, if y = -1, ball moves up, speed up y movement based on difficulty
        ballX += xOffset; // if x = -1, ball moves to left, x = 1, to right, x = 0, straight up and down

        // dictate direction paddle is moving based on key pressed
        // left key will send paddle left, right will send paddle to the right
        // make sure paddle stays on screen
        if(location >= 0 && location <= SCREEN_WIDTH - (PADDLE_SIZE * UNIT_SIZE)){
            location += direction;
        }
        else {
            if(location < 0){
                location = 0;
                direction = 1;
            }
            else {
                location = SCREEN_WIDTH - (PADDLE_SIZE * UNIT_SIZE);
                direction = -1;
            }
        }
    }

    public void checkCollisions() {
        // if the ball hits the paddle, reverse the direction
        // if the ball falls past the paddle
        if(ballY >= SCREEN_HEIGHT - (UNIT_SIZE * 5) - UNIT_SIZE){
            if(ballX >= location && ballX <= location + (PADDLE_SIZE * UNIT_SIZE)){
                yOffset = -1;
                /* here is where the physics of the game comes in
                depending on where on the paddle the ball hits, change the xOffset
                 */

                xOffset = (ballX - (location + (PADDLE_SIZE * UNIT_SIZE) / 2)) / UNIT_SIZE;
                points++;


            }
            else {
                if(ballY >= SCREEN_HEIGHT - UNIT_SIZE){
                    running = false;
                }
            }
        }

        // if ball hits top, reverse direction
        if(ballY <= 0){
            yOffset = 1;
        }

        // if ball hits sides reverse direction
        if(ballX <= 0){
            xOffset = 1;
        }

        if(ballX >= SCREEN_WIDTH - UNIT_SIZE){
            xOffset = -1;
        }

        // stop timer if game is not running and the leaderboard is not showing
        if(showLeader){
            timer.stop();
        }
    }

    public void increaseDifficulty(){
        // set difficulty to remain proportional to score
        // every 5 points, increase difficulty
        difficulty = (int)(points / 5);
    }


    public void gameOver(Graphics g){
        // set up Game Over Text
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        // display score
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + points, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + points)) / 2, (SCREEN_HEIGHT / 2) + (g.getFont().getSize() + UNIT_SIZE));

        // display restart option when game ends
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 15));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press Down to Show Leaderboard", (SCREEN_WIDTH - metrics3.stringWidth("Press Down to Show Leaderboard")) / 2, SCREEN_HEIGHT - (g.getFont().getSize() + UNIT_SIZE));

    }

    // display top 5 scores
    int [] scores = new int[5];

    public void leaderboard(Graphics g) throws IOException {
        g.setColor(Color.black);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // will be holding leaderboard in file called leaderboard.txt
        String fileName = "C:/Users/Tekhneo!/IdeaProjects/Pong/src/Leaderboard.txt";

        // set up file reader and writer
        File file = new File(fileName);
        Scanner reader = new Scanner(file);


        if(calcLeader) {
            // add each value to list of high scores
            int index = 0;
            try {
                while (index < scores.length && reader.hasNextInt()) {
                    scores[index] = reader.nextInt();
                    index++;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            reader.close();

            // iterate through high scores and determine if score qualifies
            for (int i = 0; i < scores.length; i++) {
                // if score qualifies, update list
                if (points > scores[i]) {
                    for (int j = scores.length - 1; j > i; j--) {
                        scores[j] = scores[j - 1];
                    }
                    scores[i] = points;
                    break;
                }
            }

            calcLeader = false;
        }


        FileWriter fw = new FileWriter(fileName);

        // feed updated list into file
        try{
            for(int s : scores){
                fw.write((s + "\n"));
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

        fw.close();

        // write out leaderboard stats
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("LEADERBOARD", (SCREEN_WIDTH - metrics1.stringWidth("LEADERBOARD"))/2, (SCREEN_HEIGHT / 2) - (g.getFont().getSize() - UNIT_SIZE));

        for(int i = 0; i < scores.length; i++){
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 35));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("" + scores[i], (SCREEN_WIDTH - metrics2.stringWidth("" + scores[i]))/2, (SCREEN_HEIGHT / 2) + (g.getFont().getSize() + UNIT_SIZE) + (2 * i * UNIT_SIZE));
        }

        // display restart option when game ends
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 15));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press Space to Start a New Game", (SCREEN_WIDTH - metrics3.stringWidth("Press Space to Start a New Game")) / 2, SCREEN_HEIGHT - (g.getFont().getSize() + UNIT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkCollisions();
            increaseDifficulty();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            if(running){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT:
                        if(direction < 0){
                            direction--;
                        }
                        else {
                            direction = -1;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(direction > 0){
                            direction++;
                        }
                        else {
                            direction = 1;
                        }
                        break;
                }
            }
            else {
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    startRound();
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    showLeader = true;
                }
            }
        }
    }
}
