import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    Image rainyBackgroundImg;
    Image snowyBackgroundImg;
    Image christmasBackgroundImg; 
    Image halloweenBackgroundImg; 
    Image springBackgroundImg; 
    Image dollarSymbolImg; 

    // Bird class
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // DollarSymbol class
    int dollarWidth = 24;
    int dollarHeight = 24;

    class DollarSymbol {
        int x;
        int y;
        int width = dollarWidth;
        int height = dollarHeight;
        Image img;

        DollarSymbol(int x, int y, Image img) {
            this.x = x;
            this.y = y;
            this.img = img;
        }
    }

    // Game logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    ArrayList<DollarSymbol> dollarSymbols; 
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    Timer weatherChangeTimer; 
    boolean gameOver = true;
    boolean paused = false;
    double score = 0;

    // Sound effects
    Clip jumpSound;
    Clip gameOverSound;
    Clip backgroundMusic;
    Clip collectDollarSound; 

    enum Weather {
        CLEAR, RAINY, SNOWY, CHRISTMAS, HALLOWEEN, SPRING
    }

    Weather currentWeather = Weather.CLEAR; 

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        rainyBackgroundImg = new ImageIcon(getClass().getResource("./rainybg.jpg")).getImage();
        snowyBackgroundImg = new ImageIcon(getClass().getResource("./snowybg.jpeg")).getImage();
        christmasBackgroundImg = new ImageIcon(getClass().getResource("./christmas_bg.jpg")).getImage(); 
        halloweenBackgroundImg = new ImageIcon(getClass().getResource("./halloween_bg.jpeg")).getImage(); 
        springBackgroundImg = new ImageIcon(getClass().getResource("./spring_bg.jpg")).getImage(); 
        dollarSymbolImg = new ImageIcon(getClass().getResource("./dollar.png")).getImage(); 

        // Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();
        dollarSymbols = new ArrayList<>(); 

        // Load sound effects
        jumpSound = loadSound("./jump.wav");
        gameOverSound = loadSound("./gameover.wav");
        backgroundMusic = loadSound("./background.wav");
        collectDollarSound = loadSound("./collect_dollar.wav"); 

        // Play background music in a loop
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }

        // Place pipes timer
        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        // Game timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        // Weather change timer
        weatherChangeTimer = new Timer(3000, e -> changeWeather()); 
        weatherChangeTimer.start();
    }

    Clip loadSound(String soundFileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    void playSound(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 3;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);

        // Randomly place dollar symbol between the pipes
        if (random.nextDouble() < 0.5) { 
            int dollarX = topPipe.x + pipeWidth / 2;
            int dollarY = topPipe.y + pipeHeight + openingSpace / 2;
            dollarSymbols.add(new DollarSymbol(dollarX, dollarY, dollarSymbolImg));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw background based on weather
        switch (currentWeather) {
            case CLEAR:
                g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
                break;
            case RAINY:
                g.drawImage(rainyBackgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
                drawRain(g);
                break;
            case SNOWY:
                g.drawImage(snowyBackgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
                drawSnow(g);
                break;
            case CHRISTMAS:
                g.drawImage(christmasBackgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
                break;
            case HALLOWEEN:
                g.drawImage(halloweenBackgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
                break;
            case SPRING:
                g.drawImage(springBackgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
                break;
        }

        // Draw bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // Draw pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Draw dollar symbols
        for (DollarSymbol dollarSymbol : dollarSymbols) {
            g.drawImage(dollarSymbol.img, dollarSymbol.x, dollarSymbol.y, dollarSymbol.width, dollarSymbol.height, null);
        }

        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + (int) score, 10, 35);
        } else if (paused) {
            g.drawString("Paused", boardWidth / 2 - 50, boardHeight / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press SPACE to resume", boardWidth / 2 - 90, boardHeight / 2 + 30);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    void drawRain(Graphics g) {
        g.setColor(new Color(0, 0, 255, 150)); 
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(boardWidth);
            int y = random.nextInt(boardHeight);
            g.fillRect(x, y, 2, 10); 
        }
    }

    void drawSnow(Graphics g) {
        g.setColor(Color.WHITE); 
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(boardWidth);
            int y = random.nextInt(boardHeight);
            g.fillOval(x, y, 5, 5); 
        }
    }

    public void move() {
        if (paused) return;

        // Bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        // Pipes
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
                playSound(gameOverSound);
            }
        }

        // Dollar symbols
        for (int i = 0; i < dollarSymbols.size(); i++) {
            DollarSymbol dollarSymbol = dollarSymbols.get(i);
            dollarSymbol.x += velocityX;

            if (collision(bird, dollarSymbol)) {
                score += 20; 
                playSound(collectDollarSound); 
                dollarSymbols.remove(i); 
                i--; 
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
            playSound(gameOverSound);
        }
    }

    void changeWeather() {
        currentWeather = Weather.values()[random.nextInt(Weather.values().length)];
    }

    boolean collision(Bird bird, Pipe pipe) {
        Rectangle birdRect = new Rectangle(bird.x, bird.y, bird.width, bird.height);
        Rectangle pipeRect = new Rectangle(pipe.x, pipe.y, pipe.width, pipe.height);
        return birdRect.intersects(pipeRect);
    }

    boolean collision(Bird bird, DollarSymbol dollarSymbol) {
        Rectangle birdRect = new Rectangle(bird.x, bird.y, bird.width, bird.height);
        Rectangle dollarRect = new Rectangle(dollarSymbol.x, dollarSymbol.y, dollarSymbol.width, dollarSymbol.height);
        return birdRect.intersects(dollarRect);
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            move();
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (paused) {
                    paused = false; // Resume game
                } else {
                    velocityY = -10;
                    playSound(jumpSound);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                paused = !paused; // Toggle paused state
            }
        }
    }

    void resetGame() {
        pipes.clear();
        dollarSymbols.clear(); 
        bird.y = birdY;
        velocityY = 0;
        score = 0;
        gameOver = false;
        paused = false;
        currentWeather = Weather.CLEAR; 
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
