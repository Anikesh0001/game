import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RockPaperScissors extends JPanel implements ActionListener {
    private String[] options = {"Rock", "Paper", "Scissors"};
    private JLabel resultLabel;
    private JButton rockButton, paperButton, scissorsButton;

    public RockPaperScissors() {
        setLayout(new FlowLayout());

        resultLabel = new JLabel("Choose your move:");
        add(resultLabel);

        rockButton = new JButton("Rock");
        paperButton = new JButton("Paper");
        scissorsButton = new JButton("Scissors");

        rockButton.addActionListener(this);
        paperButton.addActionListener(this);
        scissorsButton.addActionListener(this);

        add(rockButton);
        add(paperButton);
        add(scissorsButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userChoice = e.getActionCommand();
        String computerChoice = options[new Random().nextInt(options.length)];

        String result = determineWinner(userChoice, computerChoice);
        resultLabel.setText("Computer chose: " + computerChoice + ". " + result);
    }

    private String determineWinner(String user, String computer) {
        if (user.equals(computer)) {
            return "It's a draw!";
        } else if ((user.equals("Rock") && computer.equals("Scissors")) ||
                   (user.equals("Paper") && computer.equals("Rock")) ||
                   (user.equals("Scissors") && computer.equals("Paper"))) {
            return "You win!";
        } else {
            return "You lose!";
        }
    }
}
