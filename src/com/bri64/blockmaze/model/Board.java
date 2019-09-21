package com.bri64.blockmaze.model;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 * A mutable object representing a two-dimensional Board of {@link Block Blocks}
 */
@SuppressWarnings("WeakerAccess")
public class Board {

  /**
   * The width of the Board
   */
  private int width;

  /**
   * The height of the Board
   */
  private int height;

  /**
   * A two-dimensional {@link List list} of {@link Block Blocks}
   */
  private List<List<Block>> board;

  /**
   * Gets the Board's width
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the Board's height
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Creates a Board based on a {@link PuzzleData PuzzleData}
   * @param data the {@link PuzzleData PuzzleData} to build from
   */
  public Board(final PuzzleData data) {
    this(data.getWidth(), data.getHeight());
    for (Point2D p : data.getHoles()) {
      fillBlock(p, BlockState.HOLE);
    }
  }

  /**
   * Creates an empty Board
   * @param width the width
   * @param height the height
   */
  public Board(int width, int height) {
    this.width = width;
    this.height = height;
    this.board = new ArrayList<>();

    for (int i = 0; i < width; i++) {
      List<Block> temp = new ArrayList<>();
      for (int j = 0; j < height; j++) {
        temp.add(new Block(this, BlockState.EMPTY));
      }
      board.add(temp);
    }
  }

  /**
   * Creates a copy of the given Board
   * @param copy the Board to copy from
   */
  public Board(final Board copy) {
    this.width = copy.getWidth();
    this.height = copy.getHeight();
    this.board = new ArrayList<>();

    for (int i = 0; i < width; i++) {
      List<Block> temp = new ArrayList<>();
      for (int j = 0; j < height; j++) {
        temp.add(new Block(this, copy.getBlock(new Point2D(i, j)).getState()));
      }
      board.add(temp);
    }
  }

  /**
   * Gets the {@link Block Block} at the given {@link Point2D Position}
   * @param pos the {@link Point2D Position}
   * @return the {@link Block Block}
   */
  public Block getBlock(Point2D pos) {
    return board.get((int) pos.getX()).get((int) pos.getY());
  }

  /**
   * Gets the {@link Point2D Position} of a given {@link Block block}
   * @param block the {@link Block Block}
   * @return the {@link Point2D Position}
   */
  public Point2D getPos(Block block) {
    int x = -1;
    int y = -1;

    outer:
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (getBlock(new Point2D(i, j)).equals(block)) {
          x = i;
          y = j;
          break outer;
        }
      }
    }

    return new Point2D(x, y);
  }

  /**
   * Fills a {@link Block Block} at some {@link Point2D Position}
   * @param pos the {@link Point2D Position} of the {@link Block Block} to fill
   * @param type the {@link BlockState State} of the new {@link Block Block}
   */
  public void fillBlock(Point2D pos, BlockState type) {
    board.get((int) pos.getX()).set((int) pos.getY(), new Block(this, type));
  }

  /**
   * Gets the {@link Block Blocks} that are adjacent and empty
   * @param block the {@link Block Block} to check
   * @return the {@link List List} of {@link Block Blocks}
   */
  public List<Block> openSides(Block block) {
    return openSides(block.getPos());
  }

  /**
   * Gets the {@link Block Blocks} that are adjacent and empty
   * @param pos the {@link Point2D Position} of the {@link Block Block} to check
   * @return a {@link List List} of {@link Block Blocks}
   */
  private List<Block> openSides(Point2D pos) {
    if (pos.getX() == -1 || pos.getY() == -1) {
      return new ArrayList<>();
    }

    List<Block> open = new ArrayList<>();
    Block cur;

    if (pos.getX() + 1 != width) {
      cur = getBlock(new Point2D(pos.getX() + 1, pos.getY()));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    if (pos.getX() - 1 != -1) {
      cur = getBlock(new Point2D(pos.getX() - 1, pos.getY()));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    if (pos.getY() + 1 != height) {
      cur = getBlock(new Point2D(pos.getX(), pos.getY() + 1));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    if (pos.getY() - 1 != -1) {
      cur = getBlock(new Point2D(pos.getX(), pos.getY() - 1));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    return open;
  }

  /**
   * Gets all of a {@link Block Block's} empty neighbors (and their neighbors, etc.)
   * @param start the {@link Block Block} to start from
   * @param list the {@link List List} of already found neighbors (pass "null" to begin)
   * @return the {@link List List} of all neighbors
   */
  private List<Block> allNeighbors(Block start, List<Block> list) {
    if (list == null) {
      list = new ArrayList<>();
    }

    // If not already checked
    if (!list.contains(start)) {
      // Add this block
      list.add(start);

      // For each neighbor
      for (Block b : start.openSides()) {
        // Add it and its neighbors
        if (!list.contains(b)) {
          allNeighbors(b, list);
        }
      }
    }
    return list;
  }

  /**
   * Gets the {@link Block Block} on the Board with the least open sides
   * @return the {@link Block Block}
   */
  private Block leastSides() {
    int sides = 4;
    Block least = null;

    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getState() == BlockState.EMPTY) {
          int cur = b.openSides().size();
          if (cur <= sides) {
            sides = cur;
            least = b;
          }
        }
      }
    }
    return least;
  }

  /**
   * Lists the possible {@link Block Blocks} that are valid moves
   * @param block the start {@link Block Block}
   * @param flags the level of algorithm to use in validation
   * @return the {@link List List} of possible moves
   */
  public List<Block> possibleMoves(Block block, int flags) {
    List<Block> initial = block.openSides();
    List<Block> result = new ArrayList<>(initial);

    for (Block possible : initial) {
      // Apply move to temp board
      Board copy = new Board(this);
      copy.fillBlock(possible.getPos(), BlockState.FILLED);

      // Sides checks
      if (flags > 0) {
        List<Block> oneSided = copy.oneSided();
        if (oneSided.size() > 2) {
          result.remove(possible);
        } else if (oneSided.size() == 2) {
          int found = 0;
          for (Block b : oneSided) {
            if (b.isAdjacent(possible.getPos())) {
              found++;
            }
          }
          if (found != 1) {
            result.remove(possible);
          }
        }
      }

      // Island check
      if (flags > 1) {
        List<Block> island = allNeighbors(leastSides(), null);
        for (List<Block> l : board) {
          for (Block b : l) {
            if (b.getState() == BlockState.EMPTY && !island.contains(b)) {
              result.remove(possible);
            }
          }
        }
      }
    }

    return result;
  }

  /**
   * Lists the zero to one-sided {@link Block Blocks} on the Board
   * @return the {@link List List} of {@link Block Blocks}
   */
  private List<Block> oneSided() {
    List<Block> oneSided = new ArrayList<>();
    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getState() == BlockState.EMPTY && b.openSides().size() <= 1) {
          oneSided.add(b);
        }
      }
    }
    return oneSided;
  }

  /**
   * Determines if the Board has been filled completely
   * @return a boolean
   */
  public boolean gameWon() {
    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getState() == BlockState.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Displays the Board as a {@link String}
   * @param pos the current {@link Point2D Position}
   * @return the {@link String}
   */
  public String display(Point2D pos) {
    StringBuilder result = new StringBuilder();
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < (2 * width) + 1; i++) {
        result.append("--");
      }
      result.append("\n");
      for (int i = 0; i < width; i++) {
        if (!pos.equals(new Point2D(i, j))) {
          result.append("| ").append(getBlock(new Point2D(i, j)).getState()).append(" ");
        } else {
          result.append("| ").append("O").append(" ");
        }
      }
      result.append("|\n");
    }
    for (int i = 0; i < (2 * width) + 1; i++) {
      result.append("--");
    }
    return result.toString();
  }

}
