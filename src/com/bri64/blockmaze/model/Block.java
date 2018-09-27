package com.bri64.blockmaze.model;

import java.util.List;
import javafx.geometry.Point2D;

/**
 * An immutable object representing a Block on a {@link Board Board}
 */
@SuppressWarnings("WeakerAccess")
public class Block {

  /**
   * The {@link Board Board} object this Block is on
   */
  private Board board;

  /**
   * The {@link BlockState State} of the Block (EMPTY, FILLED, HOLE)
   */
  private BlockState state;

  /**
   * Determines if a Block should be able to be filled
   * @return a boolean
   */
  public boolean isSolid() {
    return (state != BlockState.EMPTY);
  }

  /**
   * Gets the {@link BlockState State} of the block
   * @return the {@link BlockState State}
   */
  public BlockState getState() {
    return state;
  }

  /**
   * Creates a new Block
   * @param board the {@link Board Board} the Block is on
   * @param state the {@link BlockState State} of the Block
   */
  public Block(final Board board, BlockState state) {
    this.board = board;
    this.state = state;
  }

  /**
   * Gets the {@link Point2D Position} of the Block on the {@link Board Board}
   * @return the {@link Point2D Position}
   */
  public Point2D getPos() {
    return board.getPos(this);
  }

  /**
   * Gets the Blocks that are adjacent and empty
   * @return a {@link List List} of Blocks
   */
  public List<Block> openSides() {
    return board.openSides(this);
  }

  /**
   * Determines if a another Block is adjacent in the cardinal directions
   * @param adj the {@link Point2D Position} of the other Block
   * @return a boolean
   */
  public boolean isAdjacent(Point2D adj) {
    return (getPos().getX() == adj.getX() || getPos().getY() == adj.getY())
        && getPos().distance(adj) == 1;
  }

}

