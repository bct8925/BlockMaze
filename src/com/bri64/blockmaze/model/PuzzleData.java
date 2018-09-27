package com.bri64.blockmaze.model;

import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class PuzzleData {

  private int width;
  private int height;
  private Point2D start;
  private Point2D[] holes;

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Point2D getStart() {
    return start;
  }

  public Point2D[] getHoles() {
    return holes;
  }

  public PuzzleData(int width, int height, Point2D start, Point2D[] holes) {
    this.width = width;
    this.height = height;
    this.start = start;
    this.holes = holes;
  }

}
