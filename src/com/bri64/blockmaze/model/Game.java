package com.bri64.blockmaze.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import javafx.geometry.Point2D;

public class Game {

  private Board initialBoard;
  private Point2D start;
  private Stack<GameState> stack;

  private Board curBoard() {
    return stack.peek().getBoard();
  }

  private Point2D curPos() {
    return stack.peek().getPos();
  }

  private Block curBlock() {
    return stack.peek().getBoard().getBlock(curPos());
  }

  public List<Block> curMoves() {
    return stack.peek().getMoves();
  }

  public Game(final PuzzleData data) {
    this(new Board(data), data.getStart());
  }

  private Game(final Board initialBoard, final Point2D start) {
    this.initialBoard = initialBoard;
    this.start = start;
    this.initialBoard.fillBlock(start, BlockState.FILLED);
    this.stack = new Stack<>();
    this.stack.push(new GameState(initialBoard, start, possibleMoves(initialBoard, start)));
  }

  private List<Block> possibleMoves(Board board, Point2D pos) {
    return board.possibleMoves(board.getBlock(pos), 2);
  }

  public void attemptMove(Point2D pos) {
    if ((pos.getX() >= 0 && pos.getX() < initialBoard.getWidth())
        && (pos.getY() >= 0 && pos.getY() < initialBoard.getHeight())) {
      if (curBlock().isAdjacent(pos) && !curBoard()
          .getBlock(pos).isSolid()) {
        makeMove(pos);
      }
    }
  }

  private void makeMove(Point2D pos) {
    Block used = curBoard().getBlock(pos);
    curMoves().remove(used);

    Board nextBoard = new Board(curBoard());
    nextBoard.fillBlock(pos, BlockState.FILLED);
    stack.push(new GameState(nextBoard, pos, possibleMoves(nextBoard, pos)));
  }

  public void backtrack() {
    stack.pop();
  }

  public boolean gameWon() {
    return curBoard().gameWon();
  }

  @SuppressWarnings({"unchecked", "InfiniteLoopStatement"})
  public Solution getSolution() {
    Stack<GameState> copy = (Stack) stack.clone();
    List<Point2D> moves = new ArrayList<>();
    try {
      while (true) {
        moves.add(copy.peek().getPos());
        copy.pop();
      }
    } catch (EmptyStackException empty) {
      Collections.reverse(moves);
    }
    return new Solution(moves);
  }

  public void nextWin() {
    if (gameWon()) {
      Point2D lastWin = curPos();
      stack.pop();
      curMoves().remove(curBoard().getBlock(lastWin));
    }
  }

  public void reset() {
    this.stack = new Stack<>();
    this.stack.push(new GameState(initialBoard, start, possibleMoves(initialBoard, start)));
  }

  public String display() {
    return curBoard().display(curPos());
  }
}
