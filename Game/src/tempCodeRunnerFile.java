JFrame frame = new JFrame("Rock-Paper-Scissors");
        RockPaperScissors rockPaperScissors = new RockPaperScissors();
        frame.add(rockPaperScissors);
        frame.setSize(400, 400); // Set the size of the window
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);