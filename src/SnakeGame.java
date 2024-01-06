import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

//created a class SnakeGame
public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    //initialised the tile size, grid size and timer delay in snake movement
    private static final int TILE_SIZE = 25;
    private static final int GRID_SIZE = 25;
    private static final int TIMER_DELAY = 120;

    private LinkedList<Point> snake;
    private Point fruit;
    private int direction;

    private Timer timer;

    //two constructors for initializing the game and UI of window
    public SnakeGame() {
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        fruit = createFruit();
        direction = Direction.RIGHT.getValue();

        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    private void setupUI() {
        addKeyListener(this);
        setFocusable(true);
    }

    //fruit is placed at random location at start of the game
    private Point createFruit() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        return new Point(x, y);
    }

    //defining the directions initially
    private enum Direction {
        UP(0), RIGHT(1), DOWN(2), LEFT(3);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    //snake movement in switch case
    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 0: // up
                newHead.y--;
                break;
            case 1: // right
                newHead.x++;
                break;
            case 2: // down
                newHead.y++;
                break;
            case 3: // left
                newHead.x--;
                break;
        }

        if (newHead.equals(fruit)) {
            snake.addFirst(newHead);
            fruit = createFruit();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }

        checkCollision();
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE || snake.subList(1, snake.size()).contains(head)) {
            handleGameOver();
        }
    }

    private void handleGameOver() {
        timer.stop();
        int choice = JOptionPane.showOptionDialog(this, "Game Over! Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        snake.clear();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        fruit = createFruit();
        direction = Direction.RIGHT.getValue();
        timer.start();
    }

    //interface and colour of the snake and fruit
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the fruit
        g.setColor(Color.RED);
        g.fillRect(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw grid lines
//        g.setColor(Color.BLACK);
//        for (int i = 0; i < GRID_SIZE; i++) {
//            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, GRID_SIZE * TILE_SIZE);
//            g.drawLine(0, i * TILE_SIZE, GRID_SIZE * TILE_SIZE, i * TILE_SIZE);
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN.getValue())
                    direction = Direction.UP.getValue();
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.LEFT.getValue())
                    direction = Direction.RIGHT.getValue();
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP.getValue())
                    direction = Direction.DOWN.getValue();
                break;
            case KeyEvent.VK_LEFT:
                if (direction != Direction.RIGHT.getValue())
                    direction = Direction.LEFT.getValue();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    //main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}
