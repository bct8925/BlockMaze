package com.bri64.blockmaze.model;

public enum BlockType {
  EMPTY(" "), FILLED("X"), HOLE("#");

  private String icon;

  BlockType(String icon) {
    this.icon = icon;
  }

  @Override
  public String toString() {
    return icon;
  }
}
