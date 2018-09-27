package com.bri64.blockmaze.model;

/**
 * Enumeration representing a {@link Block Block's} state
 */
public enum BlockState {
  EMPTY(" "), FILLED("X"), HOLE("#");

  /**
   * The BlockState's icon as a String
   */
  private String icon;

  /**
   * Sets the BlockState's icon
   * @param icon the icon String
   */
  BlockState(String icon) {
    this.icon = icon;
  }

  /**
   * Returns the BlockState's icon as a String
   * @return the String
   */
  @Override
  public String toString() {
    return icon;
  }
}
