package com.bri64.blockmaze.model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class PuzzleData {

  private Dimension2D size;
  private Point2D start;
  private Point2D[] holes;

  public int getWidth() {
    return (int) size.getWidth();
  }

  public int getHeight() {
    return (int) size.getHeight();
  }

  public Point2D getStart() {
    return start;
  }

  public Point2D[] getHoles() {
    return holes;
  }

  public PuzzleData(int width, int height, Point2D start, Point2D[] holes) {
    this.size = new Dimension2D(width, height);
    this.start = start;
    this.holes = holes;
  }

}
