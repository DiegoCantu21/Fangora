/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fangora;

import environment.LocationValidatorIntf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author diegocantu
 */
public class Snake {

    private ArrayList<Point> body = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private GridDrawData drawData;
    private LocationValidatorIntf locationValidator;
    private Boolean paused = false;
    private int growthCounter;

    private final int MAX_OPACITY = 255;
    private final int MIN_OPACITY = 50;

    //grow
    //eat
    //move
    //die
    //draw
    public void grow(int length) {
        growthCounter += length;
    }

    public void draw(Graphics graphics) {
//            for (Point bodySegmentLocation : body) {
//                System.out.println("Location = " + bodySegmentLocation);
//                System.out.println("System Loc = " + drawData.getcellSystemCoordinate(bodySegmentLocation));
//
//                Point topLeft = drawData.getcellSystemCoordinate(bodySegmentLocation);
//                graphics.fillOval(topLeft.x, topLeft.y, drawData.getCellWidth(), drawData.getCellHeight());
//        }
        double opacity = MAX_OPACITY;
        double opacityStepSize = (MAX_OPACITY - MIN_OPACITY) / body.size();
        for (Point bodySegmentLocation : body) {
            Point topLeft = drawData.getcellSystemCoordinate(bodySegmentLocation);
            graphics.setColor(new Color(255, 255, 255, (int) opacity));
            graphics.fillOval(topLeft.x, topLeft.y, drawData.getCellWidth(), drawData.getCellHeight());
            opacity -= opacityStepSize;
        }

    }

    /**
     * @return the body
     */
    public ArrayList<Point> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return the drawData
     */
    public GridDrawData getDrawData() {
        return drawData;
    }

    /**
     * @param drawData the drawData to set
     */
    public void setDrawData(GridDrawData drawData) {
        this.drawData = drawData;
    }

    private final int HEAD_POSITION = 0;

    public void move() {
        if (paused == false) {

            //make the snake move, please!
            Point newHead = (Point) getHead().clone();

            if (direction == Direction.DOWN) {
                newHead.y++;
            } else if (direction == Direction.UP) {
                newHead.y--;
            } else if (direction == Direction.LEFT) {
                newHead.x--;
            } else if (direction == Direction.RIGHT) {
                newHead.x++;
            }

            if (locationValidator != null) {
                body.add(HEAD_POSITION, locationValidator.validateLocation(newHead));
            }

            if (growthCounter <= 0) {
                body.remove(body.size() - 1);
            } else {
                growthCounter--;
            }
        }
    }

    public Point getHead() {
        return body.get(HEAD_POSITION);
    }

    /**
     * @return the locationValidator
     */
    public LocationValidatorIntf getLocationValidator() {
        return locationValidator;
    }

    /**
     * @param locationValidator the locationValidator to set
     */
    public void setLocationValidator(LocationValidatorIntf locationValidator) {
        this.locationValidator = locationValidator;
    }

    /**
     * @return the paused
     */
    public void togglePause() {
        if (paused == true) {
            paused = false;
        } else {
            paused = true;
        }
    }

    /**
     * @return the paused
     */
    public Boolean getPaused() {
        return paused;
    }

    /**
     * @param paused the paused to set
     */
    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    /**
     * @return the growthCounter
     */
    public int getGrowthCounter() {
        return growthCounter;
    }

    /**
     * @param growthCounter the growthCounter to set
     */
    public void setGrowthCounter(int growthCounter) {
        this.growthCounter = growthCounter;
    }

}
