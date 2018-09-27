package com.bri64.blockmaze;

import com.bri64.blockmaze.model.Block;
import com.bri64.blockmaze.model.Game;
import com.bri64.blockmaze.model.Solution;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class Solver {

  private Game game;
  private int backtracks;
  private Boolean solvable;

  public int getBacktracks() {
    return backtracks;
  }

  public Solver(final Game game) {
    this.game = game;
    this.backtracks = 0;
    this.solvable = null;
  }

  public void makeMove() {
    List<Block> possibleMoves = game.curMoves();
    if (possibleMoves.size() == 0) {
      backtracks++;
      game.backtrack();
      makeMove();
    } else {
      int choice = new Random(System.nanoTime()).nextInt(possibleMoves.size());
      Point2D nextMove = possibleMoves.get(choice).getPos();
      game.attemptMove(new Point2D(nextMove.getX(), nextMove.getY()));
    }
  }

  public boolean isSolvable() {
    if (solvable == null) {
      solve();
    }
    return solvable;
  }

  public void solve() {
    try {
      while (!game.gameWon()) {
        makeMove();
      }
      solvable = Boolean.TRUE;
    } catch (Exception stackEx) {
      solvable = Boolean.FALSE;
    }
  }

  public void show(int ms) {
    try {
      while (!game.gameWon()) {
        System.out.print("\033[H\033[2J");
        System.out.println(display());
        try {
          Thread.sleep(ms);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        makeMove();
      }
      solvable = Boolean.TRUE;
      System.out.print("\033[H\033[2J");
      System.out.println(display());
    } catch (Exception stackEx) {
      solvable = Boolean.FALSE;
    }
  }

  public List<Solution> solveMulti() {
    List<Solution> solutions = new ArrayList<>();
    try {
      while (true) {
        while (!game.gameWon()) {
          makeMove();
        }
        solvable = Boolean.TRUE;

        solutions.add(game.getSolution());
        game.nextWin();
      }
    } catch (Exception stackEx) {
      if (solutions.size() == 0) {
        solvable = Boolean.FALSE;
      }
    }
    return solutions;
  }

  public void reset() {
    solvable = null;
    backtracks = 0;
    game.reset();
  }

  public String display() {
    return game.display();
  }
}
