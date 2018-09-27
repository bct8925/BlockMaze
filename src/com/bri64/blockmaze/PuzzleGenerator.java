package com.bri64.blockmaze;

import com.bri64.blockmaze.model.Game;
import com.bri64.blockmaze.model.PuzzleData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.geometry.Point2D;

@SuppressWarnings("WeakerAccess")
public class PuzzleGenerator {
  private int minWidth;
  private int minHeight;
  private int maxWidth;
  private int maxHeight;
  private int minHoles = 1;

  private Random rnd;

  private PuzzleData solution;

  public PuzzleGenerator(int minWidth, int minHeight, int maxWidth, int maxHeight) {
    this.minWidth = minWidth;
    this.minHeight = minHeight;
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;

    this.rnd = new Random();
  }

  public PuzzleData nextGame() {
    PuzzleData solution = null;
    List<SolutionGenerator> threads = new ArrayList<>();
    for (int i = 0; i < 32; i++) {
      SolutionGenerator sg = new SolutionGenerator();
      sg.start();
      threads.add(sg);
    }

    while (solution == null) {
      for (SolutionGenerator thread : threads) {
        if (thread.getResult() != null) {
          solution = thread.getResult();
          break;
        }
      }
    }

    threads.forEach(SolutionGenerator::halt);

    return solution;
  }

  private class SolutionGenerator extends Thread {

    private PuzzleData result = null;
    private boolean shouldHalt = false;

    public PuzzleData getResult() {
      return result;
    }

    public void halt() {
      this.shouldHalt = true;
    }

    @Override
    public void run() {
      int width = (maxWidth == minWidth) ? maxWidth : rnd.nextInt(maxWidth - minWidth) + minWidth;
      int height = (maxHeight == minHeight) ? maxHeight : rnd.nextInt(maxHeight - minHeight) + minHeight;

      int holeAmt = rnd.nextInt((int) Math.sqrt(width * height) - minHoles) + minHoles;
      Set<Point2D> holes = new HashSet<>();
      while (holes.size() < holeAmt + 1) {
        holes.add(new Point2D(rnd.nextInt(width), rnd.nextInt(height)));
      }

      Point2D start = new Point2D(rnd.nextInt(width), rnd.nextInt(height));
      while (holes.contains(start)) {
        start = new Point2D(rnd.nextInt(width), rnd.nextInt(height));
      }

      PuzzleData data = new PuzzleData(width, height, start, holes.toArray(new Point2D[]{}));
      Solver test = new Solver(new Game(data));
      if (!test.isSolvable()) {
        if (shouldHalt) return;
        run();
        return;
      }
      result = data;
    }
  }


}
