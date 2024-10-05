package play;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakePanel extends JPanel implements ActionListener, Game {
    static final int panelheight = 600;      // panel height for game
    static final int panelwidth = 600;       //panel width for game
    static final int unit_size = 20;        //total cell in row 
    static final int num_of_units = (panelheight * panelwidth) / (unit_size * unit_size);  // total cell in panel with formula
 // hold x and y coordinates for body parts of the snake
    final int x[] = new int[num_of_units];
    final int y[] = new int[num_of_units];

    int snakelength = 3; //default len for snake 
    int foodswallowed; // created variable for food if swallow
    private char direction = 'D'; // Initialize with default direction

    int foodX; // food in horizonal place variable  
    int foodY; // food in vertical  place variable 

    boolean running = false; //default running is not activated
    Random random;  // created random for snake food to move in random
    Timer timer;  // created timer for snake move 

    public SnakePanel() { //here this represent to particular panel design 
        random = new Random();
        this.setSize(panelwidth, panelheight); // total size will be calculated here and give pannel according to the value
        this.setBackground(Color.green);// bg clr 
        this.setFocusable(true); // snake movement
        this.addKeyListener(new MyKey(this)); // Pass reference to SnakePanel 
        playGame(); // calling method
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }
    // game plays here 
    public void playGame() {
        addFood();
        running = true;
		timer = new Timer(150, this);
        timer.start();
    }
    // food will be add in random area.
    public void addFood() {
        foodX = random.nextInt((int) (panelwidth / unit_size)) * unit_size;
        foodY = random.nextInt((int) (panelheight / unit_size)) * unit_size;
    }

    // if game starts need to check if snake hits or eat or bite itself
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint(); //any fault or game over occurs it will reset screen 
    }

    
    public void move() {
        for (int i = snakelength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (direction == 'L') {
            x[0] = x[0] - unit_size;
        } else if (direction == 'R') {
            x[0] = x[0] + unit_size;
        } else if (direction == 'U') {
            y[0] = y[0] - unit_size;
        } else {
            y[0] = y[0] + unit_size;
        }
    }

    @Override
    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.white);//color for gameover
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (panelwidth - metrics.stringWidth("Game Over")) / 2, panelheight / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodswallowed, (panelwidth - metrics.stringWidth("Score: " + foodswallowed)) / 2, graphics.getFont().getSize());
    }

    
    public void checkHit() {
        for (int i = snakelength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] > panelwidth || y[0] < 0 || y[0] > panelheight) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(new Color(214, 00, 00));
            graphics.fillOval(foodX, foodY, unit_size, unit_size);

            graphics.setColor(new Color(255,255,255));//headpart
            graphics.fillRect(x[0], y[0], unit_size, unit_size);

            for (int i = 1; i < snakelength; i++) {
                graphics.setColor(new Color(114,91,91));//snakebody
                graphics.fillRect(x[i], y[i], unit_size, unit_size);
            }
            

            graphics.setColor(Color.red);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodswallowed, (panelwidth - metrics.stringWidth("Score: " + foodswallowed)) / 2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }
    
   
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

   
    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            snakelength++;
            foodswallowed++;
            addFood();
        }
    }
}
