/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fangora;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.LocationValidatorIntf;
import grid.Grid;
import images.ResourceTools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author diegocantu
 */
class FangoraEnvironment extends Environment implements GridDrawData, LocationValidatorIntf {

    public Point randomPoint() {
        return new Point((int) (Math.random() * grid.getColumns() + 1), ((int) (Math.random() * grid.getRows() + 1)));
    }

    Grid grid;
    private Snake snake;

    private int SLOW_SPEED = 7;
    private int MEDIUM_SPEED = 5;
    private int HIGH_SPEED = 3;

    private int moveDelayLimit = MEDIUM_SPEED;
    private int moveDelayCounter = 0;

    private ArrayList<GridObject> gridObjects;

    public FangoraEnvironment() {
    }

    @Override
    public void initializeEnvironment() {
        this.setBackground(ResourceTools.loadImageFromResource("resources/SnakeImage.jpg").getScaledInstance(1000, 800, Image.SCALE_FAST));
        grid = new Grid(20, 20, 25, 25, new Point(250, 100), Color.BLUE);
        grid.setColor(Color.RED);

        snake = new Snake();
        snake.setDirection(Direction.DOWN);
        snake.setDrawData(this);
        snake.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(3, 1));
        body.add(new Point(3, 2));
        body.add(new Point(2, 2));
        body.add(new Point(2, 4));

        snake.setBody(body);

        gridObjects = new ArrayList<>();
        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(9, 10)));
        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(1, 10)));
        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(2, 3)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(15, 5)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(15, 14)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(11, 2)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(8, 7)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(5, 1)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(3, 11)));

    }

    @Override
    public void timerTaskHandler() {
        if (snake != null) {
            // if counter >= limit then reset counter and move snake
            //else increment counter
            if (moveDelayCounter >= moveDelayLimit) {
                moveDelayCounter = 0;
                snake.move();
            } else {
                moveDelayCounter++;
            }
        }

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            grid.setShowCellCoordinates(!grid.getShowCellCoordinates());
        }
        if ((e.getKeyCode() == KeyEvent.VK_W) && (snake.getDirection() != Direction.DOWN)) {
            snake.setDirection(Direction.UP);
        }
        if ((e.getKeyCode() == KeyEvent.VK_A) && (snake.getDirection() != Direction.RIGHT)) {
            snake.setDirection(Direction.LEFT);
        }
        if ((e.getKeyCode() == KeyEvent.VK_S) && (snake.getDirection() != Direction.UP)) {
            snake.setDirection(Direction.DOWN);
        }
        if ((e.getKeyCode() == KeyEvent.VK_D) && (snake.getDirection() != Direction.LEFT)) {
            snake.setDirection(Direction.RIGHT);
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            snake.togglePause();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            snake.grow(2);
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            AudioPlayer.play("/resources/Tick.wav");
        }
    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {

    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {

    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        if (grid != null) {
            grid.paintComponent(graphics);
        }

        if (snake != null) {
            snake.draw(graphics);
        }

        if (gridObjects != null) {
            for (GridObject gridObject : gridObjects) {
                if (gridObject.getType() == GridObjectType.POISON_BOTTLE) {
                    GraphicsPalette.drawPoisonBottle(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.YELLOW);
                }
                if (gridObject.getType() == GridObjectType.APPLE) {
                    GraphicsPalette.drawApple(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.GREEN);
                }
            }
        }
    }

//<editor-fold defaultstate="collapsed" desc="GridDrawData Interface">
    @Override
    public int getCellHeight() {
        return grid.getCellHeight();
    }

    @Override
    public int getCellWidth() {
        return grid.getCellWidth();
    }

    @Override
    public Point getcellSystemCoordinate(Point cellCoordinate) {
        return grid.getCellSystemCoordinate(cellCoordinate);
    }
   //</editor-fold> 

    //<editor-fold defaultstate="colapsed
    @Override
    public Point validateLocation(Point point) {
        if (point.x >= this.grid.getColumns()) {
            point.x = 0;
        } else if (point.x < 0) {
            point.x = this.grid.getColumns() - 1;
        } else if (point.y >= this.grid.getRows()) {
            point.y = 0;
        } else if (point.y < 0) {
            point.y = this.grid.getRows() - 1;
        }
        // check if the snake hit a gridObject then take the appropiate 
        //action:
        // -Apple- grow the snake by 3
        // -Poison- make sound, kill snake
        //Look at all the locations stored in teh girdObject ArrayList
        //for each compare it to the head location stored 
        //in the "point" parameter

        for (GridObject object : this.gridObjects) {
            if (object.getLocation().equals(point)) {
                System.out.println("HIT = " + object.getType());

                if (object.getType() == GridObjectType.APPLE) {
                    //snake grow by 2
                    snake.grow(2);
                    //move the object to a random location
                    object.setLocation(getRandomPoint());
                } else if (true) {
                    //play yukky sound
                    //decrease score
                    //kill snake
                    //turn snake into different color
                    snake.togglePause();
                }
            }
        }
        return point;
    }

    private Point getRandomPoint() {
        return new Point((int) (grid.getRows()* Math.random()), (int) (grid.getColumns() * Math.random()));
    }
}
