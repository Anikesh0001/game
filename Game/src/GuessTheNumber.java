import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuessTheNumber {
    public static void main(String[] args) {
        while (true) {
            playGame();
            int playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
            if (playAgain != JOptionPane.YES_OPTION) {
                break;
            }
        }
    }

    private static void playGame() {
        int range = chooseDifficulty();
        int targetNumber = new Random().nextInt(range) + 1; // Random number within the chosen range
        List<Integer> guessHistory = new ArrayList<>();
        int guess = 0;
        int attempts = 0;

        while (guess != targetNumber) {
            String input = JOptionPane.showInputDialog("Guess a number between 1 and " + range + ":");
            if (input == null) break; // User pressed cancel
            try {
                guess = Integer.parseInt(input);
                attempts++;
                guessHistory.add(guess);
                
                if (guess < targetNumber) {
                    JOptionPane.showMessageDialog(null, "Too low! Try again.");
                } else if (guess > targetNumber) {
                    JOptionPane.showMessageDialog(null, "Too high! Try again.");
                } else {
                    JOptionPane.showMessageDialog(null, "Congratulations! You've guessed the number in " + attempts + " attempts.");
                    showGuessHistory(guessHistory);
                }

                if (attempts > 3) {
                    provideHint(guess, targetNumber);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }
    }

    private static int chooseDifficulty() {
        String[] options = {"Easy (1-10)", "Medium (1-50)", "Hard (1-100)"};
        int choice = JOptionPane.showOptionDialog(null, "Choose a difficulty level:", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (choice) {
            case 0: return 10; // Easy
            case 1: return 50; // Medium
            case 2: return 100; // Hard
            default: return 100; // Default to hard
        }
    }

    private static void showGuessHistory(List<Integer> history) {
        StringBuilder historyMessage = new StringBuilder("Your guesses: ");
        for (int num : history) {
            historyMessage.append(num).append(" ");
        }
        JOptionPane.showMessageDialog(null, historyMessage.toString());
    }

    private static void provideHint(int guess, int target) {
        if (Math.abs(guess - target) <= 10) {
            JOptionPane.showMessageDialog(null, "You're very close!");
        } else if (Math.abs(guess - target) <= 20) {
            JOptionPane.showMessageDialog(null, "You're getting warmer!");
        } else {
            JOptionPane.showMessageDialog(null, "You're quite far off.");
        }
    }
}
