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
    String result = "";
		String comma = "";
		for (Point2D p : moves) {
			result += comma + "[" + (int) p.getX() + "," + (int) p.getY() + "]"; 
			comma = ",";
		}
		return result;
  }
}
