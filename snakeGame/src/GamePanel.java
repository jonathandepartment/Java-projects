import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private static final long serialVErsionUID = 1L;

    public static final int WIDTH = 500, HEIGHT = 500;

    private Thread thread;

    private boolean running;
    private boolean right = true, left = false, up = false, down = false;
    private BodyPart b;
    private ArrayList<BodyPart> snake;

    private Apple apple;
    private ArrayList<Apple> apples;

    private Random r;

    private int xCoor = 10, yCoor = 10, size = 5;
    private int ticks = 0;

    public GamePanel(){
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        addKeyListener(this);
        snake = new ArrayList<BodyPart>();
        apples = new ArrayList<Apple>();
        r = new Random();

        start();
    }

    public void start(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    public void stop(){
        running = false;
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void tick(){
        if (snake.size() == 0){
            b = new BodyPart(xCoor, yCoor, 10);
            snake.add(b);
        }
        ticks++;
        if (ticks > 500000){
            if (right) xCoor++;
            if (left) xCoor--;
            if (up) yCoor--;
            if (down) yCoor++;

            ticks = 0;
            b = new BodyPart(xCoor, yCoor, 10);
            snake.add(b);

            if (snake.size() > size){
                snake.remove(0);
            }
        }
        if (apples.size() == 0){
            int xCoor = r.nextInt(49);
            int yCoor = r.nextInt(49);

            apple = new Apple(xCoor, yCoor, 10);
            apples.add(apple);
        }
        for (int i = 0; i < apples.size(); i++){
            if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()){
                size++;
                apples.remove(i);
                i++;
            }
        }
        for (int i = 0; i < snake.size();i++){
            if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()){
                if (i != snake.size() - 1){
                    System.out.println("Game Over");
                    stop();
                }
            }
        }
        if (xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49){
            System.out.println("Game Over");
            stop();
        }
    }
    public void paint(Graphics g){
        g.clearRect(0,0,WIDTH,HEIGHT);

        g.setColor(Color.BLACK);
        g.fillRect(0,0, WIDTH,HEIGHT);

        for (int i = 0; i < WIDTH/10;i++){
            g.drawLine(i * 10,0,i * 10, HEIGHT);
        }
        for (int i = 0; i < HEIGHT/10;i++){
            g.drawLine(0, i * 10, HEIGHT, i * 10);
        }
        for (int i = 0; i < snake.size();i++){
            snake.get(i).draw(g);
        }
        for (int i = 0; i < apples.size(); i++){
            apples.get(i).draw(g);
        }
    }

    @Override
    public void run(){
        while(running){
            tick();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if (key == keyEvent.VK_D && !left) {
            right = true;
            up = false;
            down = false;
        }
        if (key == keyEvent.VK_A && !right) {
            left = true;
            up = false;
            down = false;
        }
        if (key == keyEvent.VK_W && !down) {
            up = true;
            left = false;
            right = false;
        }
        if (key == keyEvent.VK_S && !up) {
            down = true;
            left = false;
            right = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
