package com.bri64.blockmaze.model;

import java.util.List;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
class GameState {

  private Board curBoard;
  private Point2D curPos;
  private List<Block> possibleMoves;

  public Board getBoard() {
    return curBoard;
  }

  public Point2D getPos() {
    return curPos;
  }

  public List<Block> getMoves() {
    return possibleMoves;
  }

  public GameState(final Board curBoard, final Point2D curPos, final List<Block> possibleMoves) {
    this.curBoard = curBoard;
    this.curPos = curPos;
    this.possibleMoves = possibleMoves;
  }
}
