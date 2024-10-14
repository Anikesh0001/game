import javax.swing.*;

public class App {
    public static void main(String[] args) {
        String[] options = {"Flappy Bird", "Guess the Number", "Tic Tac Toe", "Rock-Paper-Scissors"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose a game to play:",
                "Game Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                startFlappyBird();
                break;
            case 1:
                GuessTheNumber.main(null);
                break;
            case 2:
                startTicTacToe();
                break;
            case 3:
                startRockPaperScissors();
                break;
            default:
                System.exit(0); // Exit if no choice is made
        }
    }

    private static void startFlappyBird() {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }

    private static void startTicTacToe() {
        JFrame frame = new JFrame("Tic Tac Toe");
        TicTacToe ticTacToe = new TicTacToe();
        frame.add(ticTacToe);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    private static void startRockPaperScissors() {
        JFrame frame = new JFrame("Rock-Paper-Scissors");
        RockPaperScissors rockPaperScissors = new RockPaperScissors();
        frame.add(rockPaperScissors);
        frame.setSize(300, 200); // Set the size of the window
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
