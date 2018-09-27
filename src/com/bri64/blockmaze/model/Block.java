package com.bri64.blockmaze.model;

import java.util.List;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class Block {

  private Board board;
  private BlockType type;

  public boolean isSolid() {
    return (type != BlockType.EMPTY);
  }

  public BlockType getType() {
    return type;
  }

  public Block(final Board board, final BlockType type) {
    this.board = board;
    this.type = type;
  }

  public Point2D getPos() {
    return board.getPos(this);
  }

  public List<Block> openSides() {
    return board.openSides(this);
  }

  public boolean isAdjacent(Point2D adj) {
    return (getPos().getX() == adj.getX() || getPos().getY() == adj.getY())
        && getPos().distance(adj) == 1;
  }

}

