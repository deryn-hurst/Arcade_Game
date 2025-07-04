import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        // use class GamePanel to control view of game
        this.add(new GamePanel());
        // set GameFrame appearance and behavior
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
