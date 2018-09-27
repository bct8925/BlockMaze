package com.bri64.blockmaze.model;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class Board {

  private int width;
  private int height;
  private List<List<Block>> board;

  public int getWidth() {
    return width;
  }
  public int getHeight() {
    return height;
  }

  public Board(PuzzleData data) {
    this(data.getWidth(), data.getHeight());
    for (Point2D p : data.getHoles()) {
      fillBlock(p, BlockType.HOLE);
    }
  }
  public Board(int width, int height) {
    this.width = width;
    this.height = height;
    this.board = new ArrayList<>();

    for (int i = 0; i < width; i++) {
      List<Block> temp = new ArrayList<>();
      for (int j = 0; j < height; j++) {
        temp.add(new Block(this, BlockType.EMPTY));
      }
      board.add(temp);
    }
  }
  public Board(Board b) {
    this.width = b.getWidth();
    this.height = b.getHeight();
    this.board = new ArrayList<>();

    for (int i = 0; i < width; i++) {
      List<Block> temp = new ArrayList<>();
      for (int j = 0; j < height; j++) {
        temp.add(new Block(this, b.getBlock(new Point2D(i, j)).getType()));
      }
      board.add(temp);
    }
  }

  public Block getBlock(Point2D pos) {
    return board.get((int) pos.getX()).get((int) pos.getY());
  }

  public Point2D getPos(Block block) {
    int x = -1;
    int y = -1;

    boolean found = false;
    outer:
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (getBlock(new Point2D(i, j)).equals(block)) {
          found = true;
          x = i;
          y = j;
          break outer;
        }
      }
    }

    return new Point2D(x, y);
  }

  public void fillBlock(Point2D pos, BlockType type) {
    board.get((int) pos.getX()).set((int) pos.getY(), new Block(this, type));
  }

  public List<Block> openSides(Block block) {
    Point2D curBlock = getPos(block);
    if (curBlock.getX() == -1 || curBlock.getY() == -1) {
      return new ArrayList<>();
    }

    List<Block> open = new ArrayList<>();
    Block cur;

    if (curBlock.getX() + 1 != width) {
      cur = getBlock(new Point2D(curBlock.getX() + 1, curBlock.getY()));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    if (curBlock.getX() - 1 != -1) {
      cur = getBlock(new Point2D(curBlock.getX() - 1, curBlock.getY()));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    if (curBlock.getY() + 1 != height) {
      cur = getBlock(new Point2D(curBlock.getX(), curBlock.getY() + 1));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    if (curBlock.getY() - 1 != -1) {
      cur = getBlock(new Point2D(curBlock.getX(), curBlock.getY() - 1));
      if (!cur.isSolid()) {
        open.add(cur);
      }
    }

    return open;
  }

  private List<Block> allNeighbors(Block start, List<Block> list) {
    if (list == null) {
      list = new ArrayList<>();
    }

    // If not already checked
    if (!list.contains(start)) {
      // Add this block
      list.add(start);

      // For each neighbor
      for (Block b : openSides(start)) {
        // Add it and its neighbors
        if (!list.contains(b)) {
          allNeighbors(b, list);
        }
      }
    }
    return list;
  }

  private Block leastSides() {
    int sides = 4;
    Block least = null;

    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getType() == BlockType.EMPTY) {
          int cur = openSides(b).size();
          if (cur <= sides) {
            sides = cur;
            least = b;
          }
        }
      }
    }
    return least;
  }

  public List<Block> possibleMoves(Block curBlock) {
    List<Block> initial = openSides(curBlock);
    List<Block> result = new ArrayList<>(initial);

    for (Block possible : initial) {
      // Apply move to temp board
      Board copy = new Board(this);
      copy.fillBlock(possible.getPos(), BlockType.FILLED);

      // Sides checks
      List<Block> oneSided = copy.numOneSided();
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

      // Island check
      List<Block> island = allNeighbors(leastSides(), null);
      for (List<Block> l : board) {
        for (Block b : l) {
          if (b.getType() == BlockType.EMPTY && !island.contains(b)) {
            result.remove(possible);
          }
        }
      }
    }

    return result;
  }

  private List<Block> numOneSided() {
    List<Block> oneSided = new ArrayList<>();
    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getType() == BlockType.EMPTY && b.openSides().size() <= 1) {
          oneSided.add(b);
        }
      }
    }
    return oneSided;
  }

  public List<Block> numZeroSided() {
    List<Block> oneSided = new ArrayList<>();
    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getType() == BlockType.EMPTY && b.openSides().size() == 1) {
          oneSided.add(b);
        }
      }
    }
    return oneSided;
  }

  public boolean gameWon() {
    for (List<Block> l : board) {
      for (Block b : l) {
        if (b.getType() == BlockType.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  public String display(Point2D curPos) {
    StringBuilder result = new StringBuilder();
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < (2 * width) + 1; i++) {
        result.append("--");
      }
      result.append("\n");
      for (int i = 0; i < width; i++) {
        if (!curPos.equals(new Point2D(i, j))) {
          result.append("| ").append(getBlock(new Point2D(i, j)).getType()).append(" ");
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

  @Override
  public String toString() {
    return "[Board] " + board.size() + " by " + board.get(0).size();
  }


}
