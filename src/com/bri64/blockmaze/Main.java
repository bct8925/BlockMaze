package com.bri64.blockmaze;

import com.bri64.blockmaze.model.Game;
import com.bri64.blockmaze.model.PuzzleData;
import com.bri64.blockmaze.model.Solution;
import java.util.List;
import javafx.geometry.Point2D;

public class Main {

  public static void main(String[] args) {
    new Main(args);
  }

  private Main(String[] args) {
    // Create a game;
    Game g = new Game(new PuzzleData(8, 7, new Point2D(1, 2), new Point2D[]
        {
            new Point2D(7, 1),
            new Point2D(3, 5),
            new Point2D(4, 5),
            new Point2D(4, 4),
            new Point2D(5, 4)
        }));
    Solver s = new Solver(g);

    // Arguments
    String command = args[0];

    switch (command) {
      case "-show":
        test(s);
        s.show(Integer.parseInt(args[1]));
        break;
      case "-test":
        test(s);

        int reps = 100;
        double time = 0;
        long backtracks = 0;
        for (int i = 0; i < reps; i++) {
          double start = System.nanoTime();
          s.solve();
          double delta = System.nanoTime() - start;

          backtracks += s.getBacktracks();
          time += delta;

          s.reset();
        }
        System.out.println("avg time: " + ((time / 1000000) / reps) + "ms");
        System.out.println("avg backtracks: " + (backtracks / reps));
        break;
      case "-solutions":
        test(s);
        List<Solution> solutions = s.solveMulti();
        for (Solution ss : solutions) {
          System.out.println(ss);
        }
        break;
      default:
        System.out.println("Invalid arguments!");
        break;
    }
  }

  private void test(Solver s) {
    if (!s.isSolvable()) {
      System.out.println("Puzzle not solvable");
      System.exit(1);
    }
    s.reset();
  }
}
