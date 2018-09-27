package com.bri64.blockmaze.model;

import java.util.List;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class Solution {

  private List<Point2D> moves;

  public Solution(final List<Point2D> moves) {
    this.moves = moves;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String comma = "";
    for (Point2D p : moves) {
      result.append(comma).append("[").append((int) p.getX()).append(",").append((int) p.getY())
          .append("]");
      comma = ",";
    }
    return result.toString();
  }
}
